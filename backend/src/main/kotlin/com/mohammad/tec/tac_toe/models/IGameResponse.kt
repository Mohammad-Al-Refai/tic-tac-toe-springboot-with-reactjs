package com.mohammad.tec.tac_toe.models

enum class ActionResponse {
    NONE,
    ERROR,
    CONNECTED,
    GAME_CREATED,
    JOINED_GAME,
    NEW_PLAYER_JOINED,
    UPDATE_GAME,
    PLAYER_QUIET,
    WIN
}

interface IGameResponse {
    val action: ActionResponse
}