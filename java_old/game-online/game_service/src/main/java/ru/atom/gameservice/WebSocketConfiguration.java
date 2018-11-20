package ru.atom.gameservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.HandshakeInterceptor;

@Configuration
@EnableWebSocket
public class WebSocketConfiguration implements WebSocketConfigurer {

    private final HandshakeInterceptor handshakeInterceptor;
    private final WebSocketHandler eventHandler;

    @Autowired
    public WebSocketConfiguration(HandshakeInterceptor handshakeInterceptor, WebSocketHandler eventHandler) {
        this.handshakeInterceptor = handshakeInterceptor;
        this.eventHandler = eventHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(eventHandler, "/game/connect")
                .setAllowedOrigins("*")
                .addInterceptors(handshakeInterceptor);
    }

}