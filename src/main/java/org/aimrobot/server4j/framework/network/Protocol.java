package org.aimrobot.server4j.framework.network;

/**
 * @program: AimRobot-server4j
 * @description:
 * @author: H4rry217
 **/

public interface Protocol {

    public static byte PACKET_BAN = 0x01;
    public static byte PACKET_UNBAN = 0x02;
    public static byte PACKET_SEND_CHAT = 0x03;
    public static byte PACKET_PLAYER_LIST = 0x04;
    public static byte PACKET_BAN_BY_NAME = 0x05;

    public static byte PACKET_CONNECTION_CLOSE = 0x10;


}
