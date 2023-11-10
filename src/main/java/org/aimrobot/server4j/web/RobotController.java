package org.aimrobot.server4j.web;

import org.aimrobot.server4j.framework.AccessToken;
import org.aimrobot.server4j.framework.RequestResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Set;

/**
 * @program: AimRobot-server4j
 * @description:
 * @author: H4rry217
 **/


@RestController
@RequestMapping(value = "/robot")
@ShellComponent
public class RobotController {

    @Autowired
    private RobotService robotService;

    @RequestMapping(value = "/banPlayer")
    @AccessToken
    public RequestResult banPlayer(@RequestBody Map<String, String> params){
        Set<String> set = robotService.getConnectServerIds();
        String serverId = null;

        if(params.get("serverId") != null && set.contains(params.get("serverId"))){
            serverId = params.get("serverId");
        }else if(set.size() == 1){
            //使用默认服务器
            for (String sId: set){
                serverId = sId;
                break;
            }
        }

        if(serverId != null){
            robotService.banPlayer(serverId, params.get("playerName"), params.get("reason"));
        }

        return new RequestResult().putData(serverId).withOriginMsg(
                (serverId == null?
                        "找不到相应的远程服务器":
                        "将在 "+serverId+" 服务器屏蔽玩家 "+params.get("playerName")
                )
        );
    }

    @RequestMapping(value = "/sendChat")
    @AccessToken
    public RequestResult sendChat(@RequestBody Map<String, String> params){
        Set<String> set = robotService.getConnectServerIds();
        String serverId = null;

        if(params.get("serverId") != null && set.contains(params.get("serverId"))){
            serverId = params.get("serverId");
        }else if(set.size() == 1){
            //使用默认服务器
            for (String sId: set){
                serverId = sId;
                break;
            }
        }

        if(serverId != null) {
            robotService.sendChat(serverId, params.get("message"));
        }

        return new RequestResult().putData(serverId).withOriginMsg(
                (serverId == null?
                        "找不到相应的远程服务器":
                        "发送管理员大喇叭至 "+serverId+" 服务器"
                )
        );
    }

    @RequestMapping(value = "/exceCommand")
    @AccessToken
    public RequestResult exceCommand(@RequestBody Map<String, String> params){
        Set<String> set = robotService.getConnectServerIds();
        String serverId = null;

        if(params.get("serverId") != null && set.contains(params.get("serverId"))){
            serverId = params.get("serverId");
        }else if(set.size() == 1){
            //使用默认服务器
            for (String sId: set){
                serverId = sId;
                break;
            }
        }

        if(serverId != null) {
            robotService.exceCommand(serverId, params.get("command"));
        }

        return new RequestResult().putData(serverId).withOriginMsg(
                (serverId == null?
                        "找不到相应的远程服务器":
                        "发送指令至 "+serverId+" 服务器"
                )
        );
    }

    @RequestMapping(value = "/getServerIds")
    public RequestResult getServerIds(){
        return new RequestResult().putData(robotService.getConnectServerIds());
    }

}
