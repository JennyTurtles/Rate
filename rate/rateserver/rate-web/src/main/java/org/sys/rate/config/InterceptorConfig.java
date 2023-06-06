package org.sys.rate.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.sys.rate.config.interceptor.jwtInterceptor;

import javax.annotation.Resource;

@Configuration
public class InterceptorConfig extends WebMvcConfigurationSupport {

    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new jwtInterceptor())
                        .addPathPatterns("/**")//拦截所有请求，判断token是否合法决定是否需要登录
                        .excludePathPatterns("/doLogin", "/registerUser", "**/export", "**/import","/getPublicKey","/paper/basic/download","**/exportPDF");    // 拦截所有请求， 决定判断token是否合法来决定是否需要登录
    }
}

