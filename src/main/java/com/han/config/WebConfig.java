package com.han.config;

import com.han.interceptor.DemoInterceptor;
import com.han.interceptor.TokenInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
public class WebConfig implements WebMvcConfigurer {
    /*注册拦截器*/
    @Autowired
    private TokenInterceptor tokenInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tokenInterceptor )
                .addPathPatterns("/**")//拦截所有请求
                .excludePathPatterns("/api/login")//登录接口不拦截
        ;

    }
}
