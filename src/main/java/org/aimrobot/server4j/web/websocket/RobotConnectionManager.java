package org.aimrobot.server4j.web.websocket;

import org.aimrobot.server4j.framework.ServerContext;
import org.aimrobot.server4j.framework.config.SettingConfig;
import org.aimrobot.server4j.framework.network.DataPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * @program: AimRobot-server4j
 * @description:
 * @author: H4rry217
 **/

@Component
public class RobotConnectionManager {

    @Autowired
    private SettingConfig settingConfig;

    private final Map<WebSocketSession, String> sessionMap = new HashMap<>();

    private final Map<String, ServerContext> serverContexts = new HashMap<>();

    private static final Logger logger = LoggerFactory.getLogger(RobotConnectionManager.class);
    private static final String PARAMS_ACCESS_TOKEN = "token";
    private static final String PARAMS_SERVER_ID = "serverId";

    public String getServerId(WebSocketSession webSocketSession){
        return this.sessionMap.get(webSocketSession);
    }

    public ServerContext getServerContext(String serverId){
        return this.serverContexts.get(serverId);
    }

    public synchronized boolean tryAdd(WebSocketSession session){
        Map<String, String> params = getQueryMap(Objects.requireNonNull(session.getUri()).toString());

        String ip;
        if(session.getHandshakeHeaders().containsKey("x-forwarded-for")){
            ip = Objects.requireNonNull(session.getHandshakeHeaders().get("x-forwarded-for")).get(0);
        }else{
            ip = Objects.requireNonNull(session.getRemoteAddress()).toString();
        }

        logger.info("{} websocket try connect", ip);

        if(params.containsKey(PARAMS_ACCESS_TOKEN) && params.containsKey(PARAMS_SERVER_ID)){
            String paramToken = params.get(PARAMS_ACCESS_TOKEN);
            String paramServerId = params.get(PARAMS_SERVER_ID);

            logger.info("{} use token: {} and serverId: {} to connect the server ...", ip, paramToken, paramServerId);

            if(this.settingConfig.getToken().equals(paramToken)) {
                if(this.sessionMap.size() < this.settingConfig.getMaxConnection()){

                    if(!this.serverContexts.containsKey(paramServerId) && this.serverContexts.size() < settingConfig.getMaxConnection()){
                        this.serverContexts.put(paramServerId, new ServerContext(this));
                    }

                    if (this.serverContexts.containsKey(paramServerId)) {
                        if(this.serverContexts.get(paramServerId).getSession() == null){
                            logger.info("serverId {} connect success", paramServerId);
                            this.sessionMap.put(session, paramServerId);

                            ServerContext serverContext = this.serverContexts.get(paramServerId);
                            serverContext.setConnectSession(session);

                            return true;
                        }else{
                            logger.error("there has already been other connection");
                        }

                    }else{
                        logger.error("max connect count {} excess!", settingConfig.getMaxConnection());
                    }
                }else{
                    logger.error("max connect count {} excess!", settingConfig.getMaxConnection());
                }

            }else{
                logger.error("token verify error!");
            }
        }else{
            logger.error("websocket params is required!");
        }

        return false;
    }

    public synchronized void connectClosed(WebSocketSession session){
        Map<String, String> params = getQueryMap(Objects.requireNonNull(session.getUri()).toString());

        if(params.containsKey(PARAMS_SERVER_ID)){
            String serverId = params.get(PARAMS_SERVER_ID);


            WebSocketSession sessionFromCache = this.serverContexts.get(serverId).getSession();
            if(session.equals(sessionFromCache)){
                this.sessionMap.remove(sessionFromCache);
                this.serverContexts.get(serverId).setConnectSession(null);
                logger.info("robot connection ({}) closed", serverId);
            }
        }

    }

    public void sendPacket(String serverId, DataPacket pk){
        if(!pk.isEncode()) pk.encode();

        this.serverContexts.computeIfPresent(serverId, (k, v) -> {
            try {
                v.getSession().sendMessage(new BinaryMessage(pk.getBuffer()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return v;
        });
    }

    public Set<String> getServerIds(){
        return this.serverContexts.keySet();
    }

    private static Map<String, String> getQueryMap(String url) {
        int urlsp = url.lastIndexOf("?");

        if(urlsp != -1) url = url.substring(urlsp + 1);

        String[] params = url.split("&");
        Map<String, String> map = new HashMap<String, String>();

        for (String param : params) {
            map.put(param.split("=")[0], param.split("=")[1]);
        }

        return map;
    }

}
