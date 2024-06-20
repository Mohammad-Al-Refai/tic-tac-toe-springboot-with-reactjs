package com.mohammad.tec.tac_toe

import com.fasterxml.jackson.databind.ObjectMapper
import com.mohammad.tec.tac_toe.models.*
import com.mohammad.tec.tac_toe.repo.GameRepo
import com.mohammad.tec.tac_toe.repo.PlayerRepo
import com.mohammad.tec.tac_toe.utils.dtoToByteArray
import com.mohammad.tec.tac_toe.utils.messageToWsCommand
import kotlinx.coroutines.*
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler
import reactor.kotlin.core.publisher.toMono
import java.util.UUID
import kotlin.coroutines.CoroutineContext


class WebSocketHandler(
    private val objectMapper: ObjectMapper, private val gameRepo: GameRepo,
    private val dispatcher: CoroutineDispatcher, private val playerRepo: PlayerRepo
) : TextWebSocketHandler(), CoroutineScope {
    private val activePlayers = mutableMapOf<UUID, WebSocketSession>()
    private val activeSessionsIps = mutableSetOf<String>()
    override val coroutineContext: CoroutineContext = dispatcher

    override fun afterConnectionEstablished(session: WebSocketSession) {
        val ip = getIpAddress(session)
        if (activeSessionsIps.add(ip)) {
            launch {
                val player = playerRepo.findByIpAddress(ip)
                if (player == null) {
                    createPlayer(session)
                } else {
                    setPlayerActive(session)
                }
            }.start()
        }
    }

    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        val messageText = message.payload
        val command = messageToWsCommand(messageText, objectMapper)
        if (command == null) {
            sendError(session, "Invalid payload")
            return
        }

        when (command.action) {
            ActionType.CREATE_GAME -> createGame(session, command)
            ActionType.JOIN_GAME -> joinGame(session, command)
            ActionType.UPDATE_GAME -> updateGame(session, command)
        }
    }

    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        setPlayerInactive(session)
        activeSessionsIps.remove(getIpAddress(session))
    }

    private fun sendError(session: WebSocketSession, message: String) {
        val response = GameError(action = ActionResponse.ERROR, errorMessage = message)
        session.sendMessage(
            TextMessage(
                dtoToByteArray(
                    response,
                    objectMapper
                )
            )
        )
    }

    private fun sendMessage(session: WebSocketSession, commandResponse: IGameResponse) {
        if (!session.isOpen) {
            return
        }
        session.sendMessage(TextMessage(dtoToByteArray(commandResponse, objectMapper)))
    }

    private fun createGame(session: WebSocketSession, command: WsCommand) {
        if (command.clientId == null) {
            sendError(session, "clientId required")
            return
        }
        launch {
            val isPlayerNotExist = activePlayers[command.clientId] == null
            if (isPlayerNotExist) {
                sendError(session, "Player is unknown")
                return@launch
            }
            val game = gameRepo.save(
                Games(
                    adminId = command.clientId,
                )
            )
            if (game.id == null) {
                sendError(session, "Unable to create Game")
                return@launch
            }
            val response = GameCreated(gameId = game.id, action = ActionResponse.GAME_CREATED)
            sendMessage(session, response)
        }.start()
    }

    private suspend fun createPlayer(session: WebSocketSession) {
        val clientIp = getIpAddress(session)
        val player = playerRepo.save(Players(name = "Player", ipAddress = clientIp, isActive = true))
        val response = PlayerConnected(clientId = player.id)
        if (player.id == null) {
            sendError(session, "Unable to create player")
            return
        }
        activePlayers[player.id] = session
        sendMessage(session, response)
    }

    private fun joinGame(session: WebSocketSession, command: WsCommand) {
        if (command.clientId == null) {
            sendError(session, "clientId required")
            return
        }
        launch {
            val isPlayerExist = playerRepo.existsById(command.clientId)
            if (!isPlayerExist) {
                sendError(session, "Player with id ${command.clientId} doesn't exist")
                return@launch
            }
            if (command.gameId == null) {
                sendError(session, "gameId required")
                return@launch
            }
            val game = gameRepo.findById(command.gameId)
            if (game?.id == null) {
                sendError(session, "Game not found")
                return@launch
            }
            if (game.playerId1 != null && game.playerId2 != null) {
                sendError(session, "Game is full")
                return@launch
            }
            if (game.playerId1 == null) {
                joinPlayer1(game = game, clientId = command.clientId, session = session)
                return@launch
            }
            if (game.playerId2 == null) {
                joinPlayer2(game = game, clientId = command.clientId, session = session)
                return@launch
            }
        }.start()
    }

    private fun updateGame(session: WebSocketSession, command: WsCommand) {
        if (command.gameId == null) {
            sendError(session, "gameId required")
            return
        }
        if (command.clientId == null) {
            sendError(session, "clientId required")
            return
        }
        launch {
            val game = gameRepo.findById(command.gameId)
            if (game == null) {
                sendError(session, "Game not found")
                return@launch
            }
            if (game.playerId1 == null || game.playerId2 == null) {
                sendError(session, "Game is not ready yet")
                return@launch
            }
            val player1 = playerRepo.findById(game.playerId1!!)
            val player2 = playerRepo.findById(game.playerId2!!)
            if (player1 == null || player2 == null) {
                sendError(session, "Game is not ready yet")
                return@launch
            }
            val player1Session = activePlayers[player1.id]
            val player2Session = activePlayers[player2.id]
            if (player1Session == null || player2Session == null) {
                sendError(session, "Game is not ready yet, missing player")
                return@launch
            }
            if (command.clientId != game.playerIdTurn) {
                sendError(session, "it's not your turn yet")
                return@launch
            }
            when (command.cellIndex) {
                CellIndex.Cell1 -> game.apply {
                    if (game.cell1 != CellState.NONE) {
                        sendError(session, "Cell1 is already filled")
                        return@launch
                    }
                    cell1 = game.currentCellType
                }

                CellIndex.Cell2 -> game.apply {
                    if (game.cell2 != CellState.NONE) {
                        sendError(session, "Cell2 is already filled")
                        return@launch
                    }
                    cell2 = game.currentCellType
                }

                CellIndex.Cell3 -> game.apply {
                    if (game.cell3 != CellState.NONE) {
                        sendError(session, "Cell3 is already filled")
                        return@launch
                    }
                    cell3 = game.currentCellType
                }

                CellIndex.Cell4 -> game.apply {
                    if (game.cell4 != CellState.NONE) {
                        sendError(session, "Cell4 is already filled")
                        return@launch
                    }
                    cell4 = game.currentCellType
                }

                CellIndex.Cell5 -> game.apply {
                    if (game.cell5 != CellState.NONE) {
                        sendError(session, "Cell5 is already filled")
                        return@launch
                    }
                    cell5 = game.currentCellType
                }

                CellIndex.Cell6 -> game.apply {
                    if (game.cell6 != CellState.NONE) {
                        sendError(session, "Cell6 is already filled")
                        return@launch
                    }
                    cell6 = game.currentCellType
                }

                CellIndex.Cell7 -> game.apply {
                    if (game.cell7 != CellState.NONE) {
                        sendError(session, "Cell7 is already filled")
                        return@launch
                    }
                    cell7 = game.currentCellType
                }

                CellIndex.Cell8 -> game.apply {
                    if (game.cell8 != CellState.NONE) {
                        sendError(session, "Cell8 is already filled")
                        return@launch
                    }
                    cell8 = game.currentCellType
                }

                CellIndex.Cell9 -> game.apply {
                    if (game.cell9 != CellState.NONE) {
                        sendError(session, "Cell9 is already filled")
                        return@launch
                    }
                    cell9 = game.currentCellType
                }

                else -> {}
            }
            game.apply {
                currentCellType = getCellTurn(currentCellType)
                playerIdTurn = getPlayerTurn(game)
            }
            if (game.playerIdTurn == null) {
                sendError(session, "No body start yet")
                return@launch
            }
            updateGameToAssignedPlayers(session, game, player1, player2, game.playerIdTurn!!)
        }.start()
    }

    private fun setPlayerInactive(session: WebSocketSession) {
        launch {
            val player = playerRepo.findByIpAddress(getIpAddress(session)) ?: return@launch
            player.isActive = false
            playerRepo.save(player)
            activePlayers.remove(player.id)
        }
    }

    private fun setPlayerActive(session: WebSocketSession) {
        launch {
            val player = playerRepo.findByIpAddress(getIpAddress(session)) ?: return@launch
            player.isActive = true
            playerRepo.save(player)
            activePlayers[player.id!!] = session
            sendMessage(session, PlayerConnected(clientId = player.id))
        }
    }

    private fun getIpAddress(session: WebSocketSession): String {
        return session.remoteAddress?.address.toString()
    }

    private fun updateGameToAssignedPlayers(
        currentSession: WebSocketSession,
        game: Games,
        player1: Players,
        player2: Players,
        playerIdTurn: UUID
    ) {
        launch {
            println(game)
//            val savedGame = gameRepo.save(game)
            if (game.id == null) {
                sendError(currentSession, "Unable to update game")
                return@launch
            }
            val player1Session = activePlayers[player1.id]
            val player2Session = activePlayers[player2.id]
            if (player1Session == null || player2Session == null) {
                sendError(currentSession, "Game is not ready yet, missing player")
                return@launch
            }

            val isWin = checkWin(game)
            //Has issue in currentCellType doesn't have the correct value
            println("--------IsWIN $isWin")

            if (isWin && game.currentCellType == CellState.X && game.playerIdTurn == game.playerId1) {
                val winResponse = WinGame(gameId = game.id, playerId = game.playerId1!!, winner = game.currentCellType)
                sendMessage(player1Session, winResponse)
                sendMessage(player2Session, winResponse)
            }
            if (isWin && game.currentCellType == CellState.O && game.playerIdTurn == game.playerId1) {
                val winResponse = WinGame(gameId = game.id, playerId = game.playerId1!!, winner = game.currentCellType)
                sendMessage(player1Session, winResponse)
                sendMessage(player2Session, winResponse)
            }
            if (isWin && game.currentCellType == CellState.X && game.playerIdTurn == game.playerId2) {
                val winResponse = WinGame(gameId = game.id, playerId = game.playerId2!!, winner = game.currentCellType)
                sendMessage(player1Session, winResponse)
                sendMessage(player2Session, winResponse)
            }
            if (isWin && game.currentCellType == CellState.O && game.playerIdTurn == game.playerId2) {
                val winResponse = WinGame(gameId = game.id, playerId = game.playerId2!!, winner = game.currentCellType)
                sendMessage(player1Session, winResponse)
                sendMessage(player2Session, winResponse)
            }
            val savedGame = gameRepo.save(game)
            val gameResponse = UpdateGame(
                gameId = savedGame.id!!,
                cell1 = savedGame.cell1,
                cell2 = savedGame.cell2,
                cell3 = savedGame.cell3,
                cell4 = savedGame.cell4,
                cell5 = savedGame.cell5,
                cell6 = savedGame.cell6,
                cell7 = savedGame.cell7,
                cell8 = savedGame.cell8,
                cell9 = savedGame.cell9,
                playerIdTurn = savedGame.playerIdTurn!!,
                turn = savedGame.currentCellType
            )
            sendMessage(player1Session, gameResponse)
            sendMessage(player2Session, gameResponse)
        }
    }

    private fun getCellTurn(currentCellType: CellState): CellState {
        return if (currentCellType == CellState.X) {
            CellState.O
        } else {
            CellState.X
        }
    }

    private suspend fun joinPlayer1(game: Games, clientId: UUID, session: WebSocketSession) {
        game.apply {
            playerId1 = clientId
            playerIdTurn = clientId
        }
        gameRepo.save(game)
        sendMessage(
            session,
            JoinedGame(
                gameId = game.id!!,
                playerId1 = game.playerId1,
                playerId2 = game.playerId2,
                cell1 = game.cell1,
                cell2 = game.cell2,
                cell3 = game.cell3,
                cell4 = game.cell4,
                cell5 = game.cell5,
                cell6 = game.cell6,
                cell7 = game.cell7,
                cell8 = game.cell8,
                cell9 = game.cell9
            )
        )
    }

    private suspend fun joinPlayer2(game: Games, clientId: UUID, session: WebSocketSession) {
        game.apply {
            playerId2 = clientId
        }
        gameRepo.save(game)
        sendMessage(
            session,
            JoinedGame(
                gameId = game.id!!,
                playerId1 = game.playerId1,
                playerId2 = game.playerId2,
                cell1 = game.cell1,
                cell2 = game.cell2,
                cell3 = game.cell3,
                cell4 = game.cell4,
                cell5 = game.cell5,
                cell6 = game.cell6,
                cell7 = game.cell7,
                cell8 = game.cell8,
                cell9 = game.cell9
            )
        )
    }

    private fun getPlayerTurn(game: Games): UUID? {
        if (game.playerId1 == game.playerIdTurn) {
            return game.playerId2
        }
        if (game.playerId2 == game.playerIdTurn) {
            return game.playerId1
        }
        return null
    }

    private fun checkWin(game: Games): Boolean {
        return (game.cell1 == game.currentCellType && game.cell2 == game.currentCellType && game.cell3 == game.currentCellType) ||
                (game.cell4 == game.currentCellType && game.cell5 == game.currentCellType && game.cell6 == game.currentCellType) ||
                (game.cell7 == game.currentCellType && game.cell8 == game.currentCellType && game.cell9 == game.currentCellType) ||
                (game.cell1 == game.currentCellType && game.cell4 == game.currentCellType && game.cell7 == game.currentCellType) ||
                (game.cell2 == game.currentCellType && game.cell5 == game.currentCellType && game.cell8 == game.currentCellType) ||
                (game.cell3 == game.currentCellType && game.cell6 == game.currentCellType && game.cell9 == game.currentCellType) ||
                (game.cell1 == game.currentCellType && game.cell5 == game.currentCellType && game.cell9 == game.currentCellType) ||
                (game.cell3 == game.currentCellType && game.cell5 == game.currentCellType && game.cell7 == game.currentCellType)
    }
}