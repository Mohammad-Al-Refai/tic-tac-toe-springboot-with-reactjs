package com.mohammad.tec.tac_toe.models

import java.util.UUID


enum class CellIndex{
    Cell1,
    Cell2,
    Cell3,
    Cell4,
    Cell5,
    Cell6,
    Cell7,
    Cell8,
    Cell9
}
enum class ActionType{
    CREATE_GAME,
    JOIN_GAME,
    UPDATE_GAME,
    GET_AVAILABLE_GAMES
}

data class WsCommand (
    val action: ActionType,
    val gameId:UUID?,
    val clientId:UUID?,
    val cellIndex: CellIndex?,
)