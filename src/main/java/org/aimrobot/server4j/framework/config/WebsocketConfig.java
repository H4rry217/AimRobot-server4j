package org.aimrobot.server4j.framework.config;

import org.aimrobot.server4j.web.websocket.RobotWebsocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * @program: AimRobot-server4j
 * @description:
 * @author: H4rry217
 **/

@Configuration
@EnableWebSocket
public class WebsocketConfig implements WebSocketConfigurer {

    @Autowired
    private RobotWebsocketHandler robotWebsocketHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {
        webSocketHandlerRegistry
                .addHandler(robotWebsocketHandler, "/ws/robot")
                .setAllowedOrigins("*");
    }

}
