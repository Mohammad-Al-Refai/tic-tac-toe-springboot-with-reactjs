package com.mohammad.tec.tac_toe.models

import org.springframework.web.socket.WebSocketSession
import java.util.UUID


data class PlayerConnected(
    override var action: ActionResponse = ActionResponse.CONNECTED,
    var clientId: UUID? = null,
) : IGameResponse

data class GameError(
    override var action: ActionResponse = ActionResponse.ERROR,
    var errorMessage: String = ""
) : IGameResponse

data class GameCreated(
    override var action: ActionResponse = ActionResponse.GAME_CREATED,
    var gameId: UUID
) : IGameResponse

data class JoinedGame(
    override var action: ActionResponse = ActionResponse.JOINED_GAME,
    var gameId: UUID,
    var playerId1: UUID?,
    var playerId2: UUID?,
    var turn: CellState,
    val cell1: CellState,
    val cell2: CellState,
    val cell3: CellState,
    val cell4: CellState,
    val cell5: CellState,
    val cell6: CellState,
    val cell7: CellState,
    val cell8: CellState,
    val cell9: CellState,
) : IGameResponse

data class NewPlayerJoinedGame(
    override var action: ActionResponse = ActionResponse.NEW_PLAYER_JOINED,
    var gameId: UUID,
    var playerId: UUID,
    var playerName:String
) : IGameResponse
data class PlayerQuietGame(
    override var action: ActionResponse = ActionResponse.PLAYER_QUIET,
    var gameId: UUID,
    var playerId: UUID,
    var playerName:String
) : IGameResponse

data class UpdateGame(
    override var action: ActionResponse = ActionResponse.UPDATE_GAME,
    var gameId: UUID,
    var playerIdTurn: UUID,
    val turn: CellState,
    val cell1: CellState,
    val cell2: CellState,
    val cell3: CellState,
    val cell4: CellState,
    val cell5: CellState,
    val cell6: CellState,
    val cell7: CellState,
    val cell8: CellState,
    val cell9: CellState,
) : IGameResponse

data class WinGame(
    override var action: ActionResponse = ActionResponse.WIN,
    var gameId: UUID,
    var winner: CellState,
    var playerId: UUID
) : IGameResponse
