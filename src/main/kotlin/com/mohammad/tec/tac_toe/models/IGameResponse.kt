package com.mohammad.tec.tac_toe.models
enum class ActionResponse {
    NONE,
    ERROR,
    CONNECTED,
    GAME_CREATED,
    JOINED_GAME,
    UPDATE_GAME
}

interface IGameResponse {
    val action: ActionResponse
}