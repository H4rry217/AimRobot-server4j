package org.aimrobot.server4j.framework.network.packet;

import org.aimrobot.server4j.framework.network.DataPacket;
import org.aimrobot.server4j.framework.network.Protocol;

/**
 * @program: AimRobot-server4j
 * @description:
 * @author: H4rry217
 **/

public class DeathEventPacket extends DataPacket {

    public PlayerDeathEvent playerDeathEvent;

    @Override
    public byte getPacketId() {
        return Protocol.PACKET_EVENT_DEATH;
    }

    @Override
    public void decode() {
        this.playerDeathEvent = new PlayerDeathEvent();
        this.playerDeathEvent.killerPlatoon = this.getString();
        this.playerDeathEvent.killerName = this.getString();
        this.playerDeathEvent.killerBy = this.getString();
        this.playerDeathEvent.playerPlatoon = this.getString();
        this.playerDeathEvent.playerName = this.getString();
    }

    @Override
    public void encode() {

    }

    public static class PlayerDeathEvent{
        public String killerPlatoon;
        public String killerName;

        public String killerBy;

        public String playerPlatoon;
        public String playerName;
    }

}
