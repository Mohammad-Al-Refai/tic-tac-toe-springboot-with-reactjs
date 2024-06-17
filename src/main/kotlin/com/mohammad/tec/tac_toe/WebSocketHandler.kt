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
import java.util.*
import kotlin.coroutines.CoroutineContext


class WebSocketHandler(
    private val objectMapper: ObjectMapper, private val gameRepo: GameRepo,
    private val dispatcher: CoroutineDispatcher, private val playerRepo: PlayerRepo
) : TextWebSocketHandler(), CoroutineScope {

    override val coroutineContext: CoroutineContext = dispatcher

    override fun afterConnectionEstablished(session: WebSocketSession) {
        launch {
            val player = playerRepo.findByIpAddress(getIpAddress(session))
            if (player == null) {
                createPlayer(session)
            } else {
                setPlayerActive(session)
                sendMessage(session, PlayerConnected(clientId = player.id))
            }
        }.start()

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
            else -> {}
        }
    }

    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        setPlayerInactive(session)
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
        session.sendMessage(TextMessage(dtoToByteArray(commandResponse, objectMapper)))
    }

    private fun createGame(session: WebSocketSession, command: WsCommand) {
        if (command.clientId == null) {
            sendError(session, "clientId required")
            return
        }
        launch {
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
        val response = PlayerConnected(action = ActionResponse.CONNECTED, clientId = player.id)
        if (player.id == null) {
            sendError(session, "Unable to create player")
            return
        }
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
            if (game.playerId1 == null) {
                game.apply {
                    playerId1 = command.clientId
                }
            }
            if (game.playerId2 == null) {
                game.apply {
                    playerId2 = command.clientId
                }
            }
            if (game.playerId1 != null && game.playerId2 != null) {
                sendError(session, "Cannot join game")
                return@launch
            }
            gameRepo.save(game)
            sendMessage(
                session,
                JoinedGame(
                    gameId = game.id,
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
        }.start()
    }

    private fun updateGame(session: WebSocketSession) {

    }

    private fun setPlayerInactive(session: WebSocketSession) {
        launch {
            val player = playerRepo.findByIpAddress(getIpAddress(session)) ?: return@launch
            player.isActive = false
            playerRepo.save(player)
        }
    }

    private fun setPlayerActive(session: WebSocketSession) {
        launch {
            val player = playerRepo.findByIpAddress(getIpAddress(session)) ?: return@launch
            player.isActive = true
            playerRepo.save(player)
        }
    }

    private fun getIpAddress(session: WebSocketSession): String {
        return session.remoteAddress?.address.toString()
    }
}