package org.aimrobot.server4j.framework;

import lombok.Getter;
import org.aimrobot.server4j.framework.network.packet.BanPlayerByNamePacket;
import org.aimrobot.server4j.framework.network.packet.ChatEventPacket;
import org.aimrobot.server4j.framework.network.packet.InfoUpdatePacket;
import org.aimrobot.server4j.framework.network.packet.ScreenshotPacket;
import org.aimrobot.server4j.web.websocket.RobotConnectionManager;
import org.springframework.web.socket.WebSocketSession;

import java.awt.*;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @program: AimRobot-server4j
 * @description:
 * @author: H4rry217
 **/

public final class ServerContext {

    @Getter
    private WebSocketSession session;

    private final LRUCache<String, Long> activePlayers = new LRUCache<>(80);
    private final Queue<BanRecord> banHistories = new ArrayDeque<>(1000);
    private final Queue<ChatRecord> chatHistories = new ArrayDeque<>(1000);

    private final LRUCache<Long, Image> screenshotCache = new LRUCache<>(5);

    private final RobotConnectionManager robotConnectionManager;

    @Getter
    private String serverId;

    @Getter
    private InfoUpdatePacket lastUpdateInfo;

    public ServerContext(RobotConnectionManager robotConnectionManager){
        this.robotConnectionManager = robotConnectionManager;
    }

    public void setConnectSession(WebSocketSession session){
        this.session = session;
        this.serverId = robotConnectionManager.getServerId(session);
    }

    public List<BanRecord> findBanHistories(String name){
        return this.banHistories.stream().filter(banHistory -> banHistory.name.equalsIgnoreCase(name)).collect(Collectors.toList());
    }

    public List<BanRecord> getRecentBan(int size){
        return this.banHistories.stream().limit(size).collect(Collectors.toList());
    }

    public List<ChatRecord> getRecentChat(int size){
        var list = this.chatHistories.stream().toList();
        return list.subList(Math.max(0, list.size() - size), list.size());
    }

    public void updateScreenshot(Long time, Image image){
        this.screenshotCache.put(time, image);
    }

    public void updateChat(ChatEventPacket.PlayerChatEvent event){
        ChatRecord chatRecord = new ChatRecord();
        chatRecord.chatEvent = event;
        chatRecord.time = System.currentTimeMillis();
        this.chatHistories.offer(chatRecord);

        updateActivePlayer(event.speaker);
    }

    public void updateBan(BanPlayerByNamePacket banPlayerByNamePacket){
        BanRecord banRecord = new BanRecord();
        banRecord.name = banPlayerByNamePacket.playerName;
        banRecord.reason = banPlayerByNamePacket.reason;
        banRecord.time = System.currentTimeMillis();

        this.banHistories.offer(banRecord);
    }

    public Set<String> getRecentPlayers(){
        return this.activePlayers.keySet();
    }

    public void updateActivePlayer(String name){
        this.activePlayers.put(name, 1L);
    }

    public long screenshot(){
        ScreenshotPacket screenshotPacket = new ScreenshotPacket();
        screenshotPacket.timestamp = System.currentTimeMillis();
        robotConnectionManager.sendPacket(this.serverId, screenshotPacket);

        return screenshotPacket.timestamp;
    }

    public Image getScreenshot(long timestamp){
        ScreenshotPacket screenshotPacket = new ScreenshotPacket();
        screenshotPacket.timestamp = System.currentTimeMillis();
        robotConnectionManager.sendPacket(this.serverId, screenshotPacket);

        return this.screenshotCache.get(timestamp);
    }

    public void updateWindowInfo(InfoUpdatePacket pk){
        this.lastUpdateInfo = pk;
    }

    public static class BanRecord{
        public long time;
        public String name;
        public String reason;
    }

    public static class ChatRecord{
        public long time;
        public ChatEventPacket.PlayerChatEvent chatEvent;
    }

}
