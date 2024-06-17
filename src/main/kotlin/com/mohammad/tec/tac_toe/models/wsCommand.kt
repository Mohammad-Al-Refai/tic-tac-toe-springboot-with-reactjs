package com.mohammad.tec.tac_toe.models

import java.util.UUID

enum class ActionType{
    CREATE_GAME,
    JOIN_GAME,
    UPDATE_GAME,
}

data class WsCommand (
    val action: ActionType,
    val gameId:UUID?,
    val clientId:UUID?,
    val cell1:CellState?,
    val cell2:CellState?,
    val cell3:CellState?,
    val cell4:CellState?,
    val cell5:CellState?,
    val cell6:CellState?,
    val cell7:CellState?,
    val cell8:CellState?,
    val cell9:CellState?,
)