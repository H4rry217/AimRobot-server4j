package org.aimrobot.server4j.framework.network.packet;

import org.aimrobot.server4j.framework.network.DataPacket;
import org.aimrobot.server4j.framework.network.Protocol;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @program: AimRobot-server4j
 * @description:
 * @author: H4rry217
 **/

public class ScreenshotPacket extends DataPacket {

    public Image image;
    public long timestamp;

    @Override
    public byte getPacketId() {
        return Protocol.PACKET_SCREENSHOT;
    }

    @Override
    public void decode() {
        this.timestamp = this.getLong();

        int imageLen = this.getInt();
        InputStream is = new ByteArrayInputStream(this.get(imageLen));
        try {
            this.image = ImageIO.read(is);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void encode() {
        this.reset();

        this.putByte(this.getPacketId());
        this.putLong(this.timestamp);

        this.encode = true;
    }
}
