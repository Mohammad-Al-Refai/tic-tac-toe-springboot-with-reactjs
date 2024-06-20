package com.mohammad.tec.tac_toe.utils

import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.databind.ObjectMapper
import com.mohammad.tec.tac_toe.models.IGameResponse
import com.mohammad.tec.tac_toe.models.WsCommand
import java.io.IOException

fun messageToWsCommand(message:String,objectMapper:ObjectMapper):WsCommand?{
    return try {
        val jsonObject = objectMapper.readValue(message, WsCommand::class.java)
        jsonObject

    }catch (e:JsonParseException){
        null
    }
}
fun dtoToByteArray(dto: IGameResponse, objectMapper:ObjectMapper): ByteArray {
    return  try {
        objectMapper.writeValueAsBytes(dto)
    }catch (e:IOException){
        byteArrayOf()
    }
}