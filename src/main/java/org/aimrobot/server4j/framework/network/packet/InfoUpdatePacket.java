package org.aimrobot.server4j.framework.network.packet;

import org.aimrobot.server4j.framework.network.DataPacket;
import org.aimrobot.server4j.framework.network.Protocol;

/**
 * @program: AimRobot-server4j
 * @description:
 * @author: H4rry217
 **/

public class InfoUpdatePacket extends DataPacket {

    public String state;
    public String task;
    public int errorCount;

    public long timestamp;

    @Override
    public byte getPacketId() {
        return Protocol.PACKET_WINDOWINFO;
    }

    @Override
    public void decode() {
        this.timestamp = this.getLong();
        this.task = this.getString();
        this.state = this.getString();
        this.errorCount = this.getShort();
    }

    @Override
    public void encode() {

    }

}
