package org.aimrobot.server4j.framework.network.packet;

import org.aimrobot.server4j.framework.network.DataPacket;
import org.aimrobot.server4j.framework.network.Protocol;

/**
 * @program: AimRobot-server4j
 * @description:
 * @author: H4rry217
 **/

public class ChatEventPacket extends DataPacket {

    public PlayerChatEvent playerChatEvent;

    @Override
    public byte getPacketId() {
        return Protocol.PACKET_EVENT_CHAT;
    }

    @Override
    public void decode() {
        this.playerChatEvent = new PlayerChatEvent();
        this.playerChatEvent.speaker = this.getString();
        this.playerChatEvent.message = this.getString();
    }

    @Override
    public void encode() {

    }

    public static class PlayerChatEvent{
        public String speaker;
        public String message;
    }

}
