package org.aimrobot.server4j.framework.network.packet;

import org.aimrobot.server4j.framework.network.DataPacket;
import org.aimrobot.server4j.framework.network.Protocol;

/**
 * @program: AimRobot-server4j
 * @description:
 * @author: H4rry217
 **/

public class SendChatPacket extends DataPacket {

    public String message;

    @Override
    public byte getPacketId() {
        return Protocol.PACKET_SEND_CHAT;
    }

    @Override
    public void decode() {

    }

    @Override
    public void encode() {
        this.reset();

        this.putByte(this.getPacketId());
        this.putString(this.message);

        this.encode = true;
    }

}
