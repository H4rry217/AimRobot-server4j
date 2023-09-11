package org.aimrobot.server4j.framework.network.packet;

import org.aimrobot.server4j.framework.network.DataPacket;
import org.aimrobot.server4j.framework.network.Protocol;

/**
 * @program: AimRobot-server4j
 * @description:
 * @author: H4rry217
 **/

public class BanPlayerPacket extends DataPacket {

    public long playerId;
    public String reason;

    @Override
    public byte getPacketId() {
        return Protocol.PACKET_BAN;
    }

    @Override
    public void decode() {

    }

    @Override
    public void encode() {
        this.reset();

        this.putByte(this.getPacketId());
        this.putLong(this.playerId);

        this.putString(this.reason);

        this.encode = true;
    }
}
