package org.aimrobot.server4j.web.websocket;

import org.aimrobot.server4j.framework.LRUCache;
import org.aimrobot.server4j.framework.network.Protocol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.BinaryWebSocketHandler;

import java.nio.charset.StandardCharsets;
import java.util.Set;

/**
 * @program: AimRobot-server4j
 * @description:
 * @author: H4rry217
 **/

@Component
public class RobotWebsocketHandler extends BinaryWebSocketHandler {

    private final LRUCache<String, Long> activePlayers = new LRUCache<>(70);

    public Set<String> getRecentPlayers(){
        return this.activePlayers.keySet();
    }

    @Autowired
    private RobotConnectionManager robotConnectionManager;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        if(!robotConnectionManager.tryAdd(session)) session.close(CloseStatus.SESSION_NOT_RELIABLE);
    }

    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) throws Exception {
        byte pkId = message.getPayload().get();

        switch(pkId){
            case Protocol.PACKET_EVENT_CHAT -> {
                int nameLen = message.getPayload().getShort();
                byte[] name = new byte[nameLen];
                message.getPayload().get(name);
                String playerName = new String(name, StandardCharsets.UTF_8);

                int msgLen = message.getPayload().getShort();
                byte[] msg = new byte[msgLen];
                message.getPayload().get(msg);
                String chatMsg = new String(msg, StandardCharsets.UTF_8);

                this.activePlayers.put(playerName, 1L);
                //TODO
            }
            case Protocol.PACKET_EVENT_DEATH -> {
                int kpLen = message.getPayload().getShort();
                byte[] kp = new byte[kpLen];
                message.getPayload().get(kp);
                String killerPlatoon = new String(kp, StandardCharsets.UTF_8);

                int kLen = message.getPayload().getShort();
                byte[] kName = new byte[kLen];
                message.getPayload().get(kName);
                String killerName = new String(kName, StandardCharsets.UTF_8);

                int kbLen = message.getPayload().getShort();
                byte[] kb = new byte[kbLen];
                message.getPayload().get(kb);
                String killBy = new String(kb, StandardCharsets.UTF_8);

                int ppLen = message.getPayload().getShort();
                byte[] pp = new byte[ppLen];
                message.getPayload().get(pp);
                String playerPlatoon = new String(pp, StandardCharsets.UTF_8);

                int pLen = message.getPayload().getShort();
                byte[] pName = new byte[pLen];
                message.getPayload().get(pName);
                String playerName = new String(pName, StandardCharsets.UTF_8);

                this.activePlayers.put(playerName, 1L);
                this.activePlayers.put(killerName, 1L);
                //TODO
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        robotConnectionManager.connectClosed(session);
    }

}
