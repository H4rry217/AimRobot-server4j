package org.aimrobot.server4j.framework.network;

import lombok.Getter;
import lombok.Setter;

/**
 * @program: AimRobot-server4j
 * @description:
 * @author: H4rry217
 **/
@Getter
@Setter
public abstract class DataPacket extends BinaryStream implements Cloneable{

    public abstract byte getPacketId();

    public abstract void decode();

    public abstract void encode();

    public boolean encode;

    @Override
    public DataPacket clone() {
        try {
            return (DataPacket) super.clone();
        } catch (CloneNotSupportedException e) {
            throw null;
        }
    }
}