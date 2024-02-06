package org.aimrobot.server4j.framework.network;

import lombok.Getter;
import lombok.Setter;
import org.aimrobot.server4j.framework.network.packet.*;

import java.lang.reflect.InvocationTargetException;

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

    private static Class<? extends DataPacket>[] PACKET_POOL = new Class[256];

    static {
        PACKET_POOL[Protocol.PACKET_EVENT_CHAT] = ChatEventPacket.class;
        PACKET_POOL[Protocol.PACKET_EVENT_DEATH] = DeathEventPacket.class;
        PACKET_POOL[Protocol.PACKET_SCREENSHOT] = ScreenshotPacket.class;
        PACKET_POOL[Protocol.PACKET_WINDOWINFO] = InfoUpdatePacket.class;
        PACKET_POOL[Protocol.PACKET_BAN_BY_NAME] = BanPlayerByNamePacket.class;
    }

    public static DataPacket getPacket(byte pkId) {
        try {
            return PACKET_POOL[pkId].getDeclaredConstructor().newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}