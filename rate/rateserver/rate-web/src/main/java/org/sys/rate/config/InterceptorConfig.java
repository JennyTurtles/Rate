package org.sys.rate.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.sys.rate.config.interceptor.jwtInterceptor;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(newjwtInterceptor())
                .addPathPatterns("/**")//拦截所有请求，判断token是否合法决定是否需要登录
                .excludePathPatterns("/doLogin", "/register", "**/export", "**/import","/getPublicKey","/paper/basic/download");    // 拦截所有请求， 决定判断token是否合法来决定是否需要登录
    }
    @Bean
    public jwtInterceptor newjwtInterceptor(){
        return new jwtInterceptor();
    }

}
