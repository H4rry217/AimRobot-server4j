package org.aimrobot.server4j.web;

import org.aimrobot.server4j.framework.AccessToken;
import org.aimrobot.server4j.framework.RequestResult;
import org.aimrobot.server4j.framework.ServerContext;
import org.aimrobot.server4j.web.websocket.RobotConnectionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
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
@CrossOrigin
public class RobotController {

    @Autowired
    private RobotService robotService;

    @Autowired
    private RobotConnectionManager robotConnectionManager;

    @RequestMapping(value = "/banPlayer")
    @AccessToken
    public RequestResult banPlayer(@RequestBody Map<String, String> params){
        String serverId = getServerIdOrDefault(params);

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
        String serverId = getServerIdOrDefault(params);

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
        String serverId = getServerIdOrDefault(params);

        if(serverId != null) {
            robotService.execCommand(serverId, params.get("command"));
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
        return new RequestResult().putData(robotService.getExistsServerIds());
    }

    @RequestMapping(value = "/getServerInfos")
    public RequestResult getServerInfos(){
        var ids = robotService.getExistsServerIds();
        Map<String, Map<String, Object>> data = new HashMap<>();

        for (String id : ids) {
            var context = robotConnectionManager.getServerContext(id);
            var pk = context.getLastUpdateInfo();
            Map<String, Object> serverData = new HashMap<>();

            if(pk != null){
                serverData.put("timestamp", pk.timestamp);
                serverData.put("state", pk.state);
                serverData.put("errorCount", pk.errorCount);
                serverData.put("runTask", pk.task);
            }

            serverData.put("alive", context.getSession() != null);

            data.put(id, serverData);
        }

        return new RequestResult().putData(data);
    }

    @RequestMapping(value = "/getRecentActivePlayers")
    public RequestResult getRecentActivePlayers(@RequestBody Map<String, String> params){
        String serverId = getServerIdOrDefault(params, false);

        if(serverId != null){
            var set = robotConnectionManager.getServerContext(serverId).getRecentPlayers();
            return new RequestResult().putData(set).withOriginMsg("共返回 " +set.size()+ " 名最近活跃玩家");
        }else{
            return new RequestResult().withOriginMsg("找不到相应的远程服务器");
        }

    }
    @RequestMapping(value = "/getScreenshot")
    @AccessToken
    public RequestResult getScreenshot(@RequestBody Map<String, String> params) throws IOException, InterruptedException {
        String serverId = getServerIdOrDefault(params);

        String base64Image = null;

        if(serverId != null) {
            var time = robotConnectionManager.getServerContext(serverId).screenshot();
            Thread.sleep(10000);

            Image image = robotConnectionManager.getServerContext(serverId).getScreenshot(time);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            if(image != null){
                ImageIO.write((RenderedImage) image, "png", byteArrayOutputStream);
                byte[] imageBytes = byteArrayOutputStream.toByteArray();

                base64Image = Base64.getEncoder().encodeToString(imageBytes);
            }
        }

        return new RequestResult().putData(base64Image).withOriginMsg(
                (serverId == null?
                        "找不到相应的远程服务器":
                        "获取 "+serverId+" 服务器截图"
                )
        );
    }

    @RequestMapping(value = "/runTask")
    @AccessToken
    public RequestResult runTask(@RequestBody Map<String, String> params){
        String serverId = getServerIdOrDefault(params);

        if(serverId != null) {
            robotService.runTask(serverId, params.get("task"));
        }

        return new RequestResult().putData(serverId).withOriginMsg(
                (serverId == null?
                        "找不到相应的远程服务器":
                        "在 "+serverId+" 服务器执行任务"
                )
        );
    }

    @RequestMapping(value = "/chatHistory")
    @AccessToken
    public RequestResult chatHistory(@RequestBody Map<String, String> params){
        String serverId = getServerIdOrDefault(params, false);

        List<ServerContext.ChatRecord> data = null;
        if(serverId != null) {
            data = robotConnectionManager.getServerContext(serverId).getRecentChat(50);
        }

        return new RequestResult().putData(data).withOriginMsg(
                (serverId == null?
                        "找不到相应的远程服务器":
                        "在 "+serverId+" 查询记录"
                )
        );
    }

    @RequestMapping(value = "/banHistory")
    @AccessToken
    public RequestResult banHistory(@RequestBody Map<String, String> params){
        String serverId = getServerIdOrDefault(params, false);

        List<ServerContext.BanRecord> data = null;
        if(serverId != null) {
            if(params.getOrDefault("playerName", null) != null){
                data = robotConnectionManager.getServerContext(serverId).findBanHistories(params.get("playerName"));
            }else{
                data = robotConnectionManager.getServerContext(serverId).getRecentBan(50);
            }

        }

        return new RequestResult().putData(data).withOriginMsg(
                (serverId == null?
                        "找不到相应的远程服务器":
                        "在 "+serverId+" 查询记录"
                )
        );
    }

    private String getServerIdOrDefault(Map<String, String> params){
        return this.getServerIdOrDefault(params, true);
    }
    private String getServerIdOrDefault(Map<String, String> params, boolean alive){
        Set<String> set = robotService.getExistsServerIds();
        String serverId = null;

        if(params.get("serverId") != null && set.contains(params.get("serverId"))){
            serverId = params.get("serverId");
        }else if(set.size() == 1){
            //使用默认服务器
            for (String sId: set){
                if(!alive || robotConnectionManager.getServerContext(sId).getSession() != null){
                    serverId = sId;
                    break;
                }
            }
        }

        return serverId;
    }


}
