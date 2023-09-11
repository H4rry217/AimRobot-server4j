package org.aimrobot.server4j.web.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.BinaryWebSocketHandler;

/**
 * @program: AimRobot-server4j
 * @description:
 * @author: H4rry217
 **/

@Component
public class RobotWebsocketHandler extends BinaryWebSocketHandler {

    @Autowired
    private RobotConnectionManager robotConnectionManager;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        if(!robotConnectionManager.tryAdd(session)) session.close(CloseStatus.SESSION_NOT_RELIABLE);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message){
//        webSocketManager.terminalReceiveContent(
//                webSocketManager.getTerminalOperator(session.getId()).getServerId(),
//                message.getPayload()
//        );
        System.out.println(1);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        robotConnectionManager.connectClosed(session);
    }

}
