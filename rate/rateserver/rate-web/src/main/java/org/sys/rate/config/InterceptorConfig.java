package org.sys.rate.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.sys.rate.config.interceptor.JwtInterceptor;

import javax.annotation.Resource;

@Configuration
public class InterceptorConfig extends WebMvcConfigurationSupport {
    @Resource
    private JwtInterceptor jwtInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/**")//拦截所有请求，判断token是否合法决定是否需要登录
                .excludePathPatterns("/doLogin", "/registerUser/**", "**/export", "/institution/basic/searchByName", "/teacher/basic/getTeaByIdNumber",
                        "/undergraduateM/basic/downloadSign","/system/teacher/downloadSign",
                        "**/import","/getPublicKey","/paper/basic/download","**/exportPDF","/graduatestudentM/basic/exportGraduate","/paperComment/basic/exportPDF","/undergraduateM/basic/exportUnderGraduate","/doctorM/basic/exportDoctor");    // 拦截所有请求， 决定判断token是否合法来决定是否需要登录
    }

    @Override
    protected void addViewControllers(ViewControllerRegistry registry) {
        // TODO Auto-generated method stub
        // 注册访问 /login 转向 page-login.html 页面
        registry.addViewController("/login").setViewName("page-login.html");
        super.addViewControllers(registry);
    }

    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        // TODO Auto-generated method stub
        registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
        super.addResourceHandlers(registry);
    }
}

