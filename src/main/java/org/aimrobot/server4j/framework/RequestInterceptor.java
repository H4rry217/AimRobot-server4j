package org.aimrobot.server4j.framework;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.aimrobot.server4j.framework.config.SettingConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.NamedThreadLocal;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: AimRobot-server4j
 * @description:
 * @author: H4rry217
 **/

public class RequestInterceptor implements HandlerInterceptor {
    private static final Logger log = LoggerFactory.getLogger(RequestInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        loggerPreHandle(request, response, handler);

        if(handler instanceof HandlerMethod){
            if(!tokenCheck(request, response, handler)){

                HEADER_THREAD_LOCAL.remove();
                START_TIME_THREAD_LOCAL.remove();

                return false;
            }
        }

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex){
        loggerAfterCompletion(request, response, handler, ex);
    }
    
    /*************************************************************/

    private static final ThreadLocal<Long> START_TIME_THREAD_LOCAL = new NamedThreadLocal<Long>("ThreadLocal");
    private static final ThreadLocal<Map<String, String>> HEADER_THREAD_LOCAL = new NamedThreadLocal<Map<String, String>>("ThreadLocal");

    public void loggerPreHandle(HttpServletRequest request, HttpServletResponse response, Object handler){
        long curTime = System.currentTimeMillis();
        log.info(
                "{} *************** Start {}     From IP {}",
                curTime,
                request.getRequestURI(),
                getIpAddrFromHeader(request)
        );


        Map<String, String> headerMap = new HashMap<>();

        Enumeration<String> names = request.getHeaderNames();
        while(names.hasMoreElements()){
            String headerName = names.nextElement();
            headerMap.put(headerName, request.getHeader(headerName));
        }

        HEADER_THREAD_LOCAL.set(headerMap);
        START_TIME_THREAD_LOCAL.set(curTime);
    }

    public void loggerAfterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex){
        long startTime = START_TIME_THREAD_LOCAL.get();


        StringBuilder header = new StringBuilder();


        HEADER_THREAD_LOCAL.get().forEach((name, val) -> {
            header.append(name).append(": ").append(val).append(" ");
        });

        log.info("{} Headers            : {}", startTime, header);
        log.info("{} Client IP          : {}", startTime, getIpAddrFromHeader(request));


        log.info(
                "{} *************** End   {}     Spendtime(Sec) {}",
                startTime,
                request.getRequestURI(),
                ((System.currentTimeMillis() - startTime) / 1000F)
        );

        HEADER_THREAD_LOCAL.remove();
        START_TIME_THREAD_LOCAL.remove();
    }

    private static final String LOCAL_IP = "127.0.0.1";

    public static String getIpAddrFromHeader(HttpServletRequest request) {
        String ip = request.getHeader("X-real-ip");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("x-forwarded-for");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        return "0:0:0:0:0:0:0:1".equals(ip)? LOCAL_IP: ip;
    }

    private static boolean tokenCheck(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        HandlerMethod handlerMethod = (HandlerMethod)handler;
        Method method = handlerMethod.getMethod();

        AccessToken requireToken = null;
        if(method.isAnnotationPresent(AccessToken.class)){
            requireToken = method.getAnnotation(AccessToken.class);
        }else if(method.getDeclaringClass().isAnnotationPresent(AccessToken.class)){
            requireToken = method.getDeclaringClass().getAnnotation(AccessToken.class);
        }

        String token = HEADER_THREAD_LOCAL.get().get("access-token");
        if(requireToken != null &&
                (token == null || !token.equals(SpringUtils.getBean(SettingConfig.class).getToken()))){

            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            outWrite(response, "{\"statusCode\": 114514,\"msg\": \"Token Verify Error\",\"ok\": false}");

            return false;
        }

        return true;
    }

    protected static void outWrite(HttpServletResponse response, String content) throws IOException {
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json;charset=utf-8");

        PrintWriter out = response.getWriter();
        out.write(content);
        out.flush();
        out.close();
    }

}
