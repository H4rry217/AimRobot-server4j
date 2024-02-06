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
    public static byte PACKET_COMMAND = 0x06;

    public static byte PACKET_CONNECTION_CLOSE = 0x10;


    public static byte PACKET_EVENT_DEATH = 0x0a;
    public static byte PACKET_EVENT_CHAT = 0x0b;
    public static byte PACKET_SCREENSHOT = 0x0c;

    public static byte PACKET_WINDOWINFO = 0x0d;
    public static byte PACKET_RUNTASK = 0x0e;
}
