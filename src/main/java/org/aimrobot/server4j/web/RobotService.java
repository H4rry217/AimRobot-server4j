package org.aimrobot.server4j.web;

import org.aimrobot.server4j.framework.config.SettingConfig;
import org.aimrobot.server4j.framework.network.packet.*;
import org.aimrobot.server4j.web.websocket.RobotConnectionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * @program: AimRobot-server4j
 * @description:
 * @author: H4rry217
 **/

@Service
@ShellComponent
public class RobotService {

    @Autowired
    private SettingConfig settingConfig;

    @Autowired
    private RobotConnectionManager robotConnectionManager;

    @ShellMethod(key = "getToken", value = "get current token")
    public String getToken(){
        return settingConfig.getToken();
    }

    @ShellMethod(key = "getExistsServerIds", value = "get the currently exists server")
    public Set<String> getExistsServerIds(){
        return robotConnectionManager.getServerIds();
    }

    @ShellMethod(key = "banPlayer", value = "ban player")
    public void banPlayer(String serverId, String name, String reason){
        BanPlayerByNamePacket pk = new BanPlayerByNamePacket();
        pk.playerName = name;
        pk.reason = reason;

        robotConnectionManager.sendPacket(serverId, pk);
    }

    @ShellMethod(key = "sendChat", value = "send chat")
    public void sendChat(String serverId, String message){
        SendChatPacket pk = new SendChatPacket();
        pk.message = message;

        robotConnectionManager.sendPacket(serverId, pk);
    }

    @ShellMethod(key = "exceCommand", value = "execute command")
    public void execCommand(String serverId, String command){
        CommandPacket pk = new CommandPacket();
        pk.command = command;

        robotConnectionManager.sendPacket(serverId, pk);
    }

    public void runTask(String serverId, String task){
        RunTaskPacket pk = new RunTaskPacket();
        pk.runTask = task;

        robotConnectionManager.sendPacket(serverId, pk);
    }


}
