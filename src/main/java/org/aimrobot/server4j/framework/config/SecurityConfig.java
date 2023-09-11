package org.aimrobot.server4j.framework.config;

import org.aimrobot.server4j.framework.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @program: AimRobot-server4j
 * @description:
 * @author: H4rry217
 **/

@Configuration
public class SecurityConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requestInterceptor())
                .addPathPatterns("/**");
    }

    @Bean
    public RequestInterceptor requestInterceptor(){
        return new RequestInterceptor();
    }

}
