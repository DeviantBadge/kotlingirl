package com.kotlingirl.gameservice.socket

import com.kotlingirl.serverconfiguration.GameServiceConstants.WEB_SOCKET_PATH
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.web.socket.config.annotation.EnableWebSocket
import org.springframework.web.socket.config.annotation.WebSocketConfigurer
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry

@Configuration
@EnableWebSocket
class WebSocketConfiguration : WebSocketConfigurer {
    @Autowired
    private lateinit var handler: SockEventHandler

    override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
        registry.addHandler(handler, WEB_SOCKET_PATH)
                .setAllowedOrigins("*")
                .withSockJS()
    }
}