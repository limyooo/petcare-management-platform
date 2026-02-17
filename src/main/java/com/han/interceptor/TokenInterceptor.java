package com.han.interceptor;

import com.han.utils.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
@Slf4j
@Component
public class TokenInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //获取请求路径
        String requestURI = request.getRequestURI();
        //判断请求路径是否是登录接口
        if (requestURI.startsWith("/api/login")) {
            log.info("登录接口,放行");
            return true;//放行
        }
        //获取请求头中的令牌
        String token = request.getHeader("token");
        //判断令牌是否为空
        if (token == null) {
            log.info("令牌为空,401");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
        //令牌不为空,判断令牌是否正确
        try {
            JwtUtils.parseJwt(token);
        }catch (Exception e){
            log.info("令牌错误,401");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
        log.info("令牌正确,放行");
        return true;
    }
}
