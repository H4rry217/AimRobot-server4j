package org.aimrobot.server4j.framework;

import org.aimrobot.server4j.framework.config.SettingConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

/**
 * @program: AimRobot-server4j
 * @description:
 * @author: H4rry217
 **/
@Component
@Order(-1)
public class TokenGenerateRunner implements ApplicationRunner {

    private final static Logger log = LoggerFactory.getLogger(TokenGenerateRunner.class);

    @Autowired
    private SettingConfig settingConfig;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if(settingConfig.getToken().isBlank()){
            String chrs = "QWERTYUIOPASDFGHJKLZXCVBNMqwertyuiopasdfghjklzxcvbnm1234567890";

            StringBuilder randomString = new StringBuilder();
            SecureRandom random = new SecureRandom();

            for (int i = 0; i < 10; i++) {
                int index = random.nextInt(chrs.length());
                randomString.append(chrs.charAt(index));
            }

            log.warn("empty token in setting yml!!! regenerate new token ({}) ", randomString);

            settingConfig.setToken(randomString.toString());
        }
    }

}
