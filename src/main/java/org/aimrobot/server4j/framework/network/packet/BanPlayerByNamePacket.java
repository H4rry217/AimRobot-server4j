package org.aimrobot.server4j.framework.network.packet;

import org.aimrobot.server4j.framework.network.DataPacket;
import org.aimrobot.server4j.framework.network.Protocol;

/**
 * @program: AimRobot-server4j
 * @description:
 * @author: H4rry217
 **/

public class BanPlayerByNamePacket extends DataPacket {

    public String playerName;
    public String reason;

    @Override
    public byte getPacketId() {
        return Protocol.PACKET_BAN_BY_NAME;
    }

    @Override
    public void decode() {
        this.playerName = this.getString();
        this.reason = this.getString();
    }

    @Override
    public void encode() {
        this.reset();

        this.putByte(this.getPacketId());
        this.putString(this.playerName);
        this.putString(this.reason);

        this.encode = true;
    }
}
