package org.aimrobot.server4j.framework.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @program: AimRobot-server4j
 * @description:
 * @author: H4rry217
 **/

@EnableConfigurationProperties
@Configuration
@Getter
@Setter
public class SettingConfig {

    @Value("${robot-websocket.token}")
    private String token;

    @Value("${arl.max-limit}")
    private Integer maxConnection;

}
