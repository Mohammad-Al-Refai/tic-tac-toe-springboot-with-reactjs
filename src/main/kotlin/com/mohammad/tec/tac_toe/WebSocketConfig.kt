package com.mohammad.tec.tac_toe

import com.fasterxml.jackson.databind.ObjectMapper
import com.mohammad.tec.tac_toe.repo.GameRepo
import com.mohammad.tec.tac_toe.repo.PlayerRepo
import kotlinx.coroutines.CoroutineDispatcher
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.web.socket.config.annotation.EnableWebSocket
import org.springframework.web.socket.config.annotation.WebSocketConfigurer
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor


@Configuration
@EnableWebSocket
class WebSocketConfig(val objectMapper: ObjectMapper, val gameRepo: GameRepo,val dispatcher:CoroutineDispatcher,val playerRepo: PlayerRepo) : WebSocketConfigurer {
    override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
        registry.addHandler(WebSocketHandler(objectMapper,gameRepo, dispatcher,playerRepo ), "/ws")
            .addInterceptors(HttpSessionHandshakeInterceptor()).setAllowedOrigins("*")
    }
}