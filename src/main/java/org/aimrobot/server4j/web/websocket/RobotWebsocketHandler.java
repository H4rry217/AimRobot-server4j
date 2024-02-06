package org.aimrobot.server4j.web.websocket;

import org.aimrobot.server4j.framework.network.DataPacket;
import org.aimrobot.server4j.framework.network.Protocol;
import org.aimrobot.server4j.framework.network.packet.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
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
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) throws Exception {
        byte pkId = message.getPayload().get();

        DataPacket pk = DataPacket.getPacket(pkId);
        pk.setBuffer(message.getPayload().array());
        pk.setOffset(1);

        pk.decode();

        switch(pkId){
            case Protocol.PACKET_EVENT_CHAT -> {
                var serverContext = robotConnectionManager.getServerContext(robotConnectionManager.getServerId(session));
                serverContext.updateActivePlayer(((ChatEventPacket)pk).playerChatEvent.speaker);
                serverContext.updateChat(((ChatEventPacket)pk).playerChatEvent);
            }
            case Protocol.PACKET_EVENT_DEATH -> {
                var serverContext = robotConnectionManager.getServerContext(robotConnectionManager.getServerId(session));
                serverContext.updateActivePlayer(((DeathEventPacket)pk).playerDeathEvent.playerName);
                serverContext.updateActivePlayer(((DeathEventPacket)pk).playerDeathEvent.killerName);
            }
            case Protocol.PACKET_SCREENSHOT -> {
                var serverId = robotConnectionManager.getServerId(session);
                robotConnectionManager.getServerContext(serverId).updateScreenshot(((ScreenshotPacket)pk).timestamp, ((ScreenshotPacket)pk).image);
            }
            case Protocol.PACKET_WINDOWINFO -> {
                var serverId = robotConnectionManager.getServerId(session);
                robotConnectionManager.getServerContext(serverId).updateWindowInfo((InfoUpdatePacket) pk);
            }
            case Protocol.PACKET_BAN_BY_NAME -> {
                var serverId = robotConnectionManager.getServerId(session);
                robotConnectionManager.getServerContext(serverId).updateBan((BanPlayerByNamePacket) pk);
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        robotConnectionManager.connectClosed(session);
    }

}
