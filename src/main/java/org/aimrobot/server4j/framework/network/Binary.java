package org.aimrobot.server4j.framework.network;

/**
 * @program: minenvoy
 * @description:
 * @author: H4rry217
 **/

public class Binary {

    public static long readLong(byte[] bytes){
        return ((long)(bytes[0]) << 56) +
                ((long)(bytes[1] & 0xff) << 48) +
                ((long)(bytes[2] & 0xff) << 40) +
                ((long)(bytes[3] & 0xff) << 32) +
                ((long)(bytes[4] & 0xff) << 24) +
                ((bytes[5] & 0xff) << 16) +
                ((bytes[6] & 0xff) << 8) +
                ((bytes[7] & 0xff));
    }

    public static byte[] writeLong(long v){
        return new byte[]{
                (byte) (v >>> 56),
                (byte) (v >>> 48),
                (byte) (v >>> 40),
                (byte) (v >>> 32),
                (byte) (v >>> 24),
                (byte) (v >>> 16),
                (byte) (v >>> 8),
                (byte) v,
        };
    }

    public static int readShort(byte[] bytes) {
        return ((bytes[0] & 0xff) << 8) + (bytes[1] & 0xff);
    }

    public static byte[] writeShort(int s) {
        return new byte[]{(byte)(s >>> 8 & 0xff), (byte)(s & 0xff)};
    }

}
