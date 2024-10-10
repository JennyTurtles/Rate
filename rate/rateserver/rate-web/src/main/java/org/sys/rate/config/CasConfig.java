package org.sys.rate.config;

import org.jasig.cas.client.authentication.AuthenticationFilter;
import org.jasig.cas.client.session.SingleSignOutFilter;
import org.jasig.cas.client.session.SingleSignOutHttpSessionListener;
import org.jasig.cas.client.util.AssertionThreadLocalFilter;
import org.jasig.cas.client.util.HttpServletRequestWrapperFilter;
import org.jasig.cas.client.validation.Cas20ProxyReceivingTicketValidationFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class CasConfig {
    /**
     * cas服务端地址
     */
    // private String  casServerLoginUrl="";
    private final String  casServerLoginUrl="https://cas.dhu.edu.cn/authserver/login";
    /**lo
     * 当前应用地址
     */
    //@Value("${env.serverName}")
    private final String serverName="https://up.dhu.edu.cn";




    /**
     * 该监听器用于实现单点登出功能，session失效监听器
     */
    @Bean
    public ServletListenerRegistrationBean<SingleSignOutHttpSessionListener> singleSignOutHttpSessionListener() {
        ServletListenerRegistrationBean<SingleSignOutHttpSessionListener> listener = new ServletListenerRegistrationBean<>();
        listener.setEnabled(true);
        listener.setListener(new SingleSignOutHttpSessionListener());
        listener.setOrder(1);
        return listener;
    }

    /**
     * 该过滤器用于实现单点登出功能，单点退出配置，一定要放在其他filter之前
     *  当调用当前应用的/logout时，该拉截器将会重定向到cas服务端的/logout请求
     */
    @Bean
    public FilterRegistrationBean logOutFilter() {
        FilterRegistrationBean filterRegistration = new FilterRegistrationBean();//new SecurityContextLogoutHandler()
        LogoutFilter logoutFilter = new LogoutFilter(casServerLoginUrl + "/logout?service=" + serverName,new SecurityContextLogoutHandler());
        filterRegistration.setFilter(logoutFilter);
        filterRegistration.setEnabled(true);
        filterRegistration.addUrlPatterns("/cas/casLogout");
        filterRegistration.addInitParameter("casServerUrlPrefix", casServerLoginUrl);
        filterRegistration.addInitParameter("serverName", serverName);
        filterRegistration.setOrder(2);
        return filterRegistration;
    }


    /**
     * 该过滤器用于实现单点登出功能，当一个系统登出时，cas服务端会通知，各个应
     * 用进行进行退出操作，该过滤器就是用来接收cas回调的请求，如果是前后端分离
     * 应用，需要重写SingleSignOutFilter过滤器，按自已的业务规则去处理
     */
    @Bean
    public FilterRegistrationBean singleSignOutFilter() {
        FilterRegistrationBean filterRegistration = new FilterRegistrationBean();
        filterRegistration.setFilter(new SingleSignOutFilter());
        filterRegistration.setEnabled(true);
        filterRegistration.addUrlPatterns("/*");
        filterRegistration.addInitParameter("casServerUrlPrefix", casServerLoginUrl);
        filterRegistration.addInitParameter("serverName", serverName);
        filterRegistration.setOrder(3);
        return filterRegistration;
    }

    /**
     * 该过滤器负责单点登录功能，用户登录的认证工作
     * @return
     */
    @Bean
    public FilterRegistrationBean authenticationFilterRegistrationBean() {
//        FilterRegistrationBean authenticationFilter = new FilterRegistrationBean();
//        authenticationFilter.setFilter(new AuthenticationFilter ());
//        Map<String, String> initParameters = new HashMap<String, String>();
//        initParameters.put("casServerLoginUrl", casServerLoginUrl);
//        initParameters.put("ignorePattern", "^/(home)?$");
//        initParameters.put("serverName",serverName);
//        authenticationFilter.setInitParameters(initParameters);
//        authenticationFilter.setOrder(4);
//        List<String> urlPatterns = new ArrayList<String>();
//        urlPatterns.add("/cas/casLogin");
//        authenticationFilter.setUrlPatterns(urlPatterns);
//        return authenticationFilter;
        FilterRegistrationBean<AuthenticationFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new AuthenticationFilter());
        registrationBean.setOrder(4);
        registrationBean.setEnabled(true);

        Map<String, String> initParameters = new HashMap<>();
        initParameters.put("casServerLoginUrl", "https://cas.dhu.edu.cn/authserver/login");
        initParameters.put("serverName", "https://up.dhu.edu.cn");
        // 忽略静态资源
        initParameters.put("ignorePattern", "^/(static|css|js|images)/.*$|^/$");

        registrationBean.setInitParameters(initParameters);

        List<String> urlPatterns = new ArrayList<>();
        urlPatterns.add("/cas/casLogin");
        registrationBean.setUrlPatterns(urlPatterns);

        return registrationBean;
    }

    /**
     * 该过滤器用于单点登录功能，负责对Ticket的校验工作
     * @return
     */
    @Bean
    public FilterRegistrationBean ValidationFilterRegistrationBean(){
        FilterRegistrationBean authenticationFilter = new FilterRegistrationBean();
        authenticationFilter.setOrder(5);
        authenticationFilter.setFilter(new Cas20ProxyReceivingTicketValidationFilter());

        Map<String, String> initParameters = new HashMap<>();
        initParameters.put("casServerUrlPrefix", casServerLoginUrl);
        initParameters.put("serverName", serverName);
        authenticationFilter.setInitParameters(initParameters);

        List<String> urlPatterns = new ArrayList<String>();
        urlPatterns.add("/cas/casLogin");
        authenticationFilter.setUrlPatterns(urlPatterns);
        return authenticationFilter;
    }


    /**
     * 该过滤器用于单点登录功能 ，对HttpServletRequest请求包装， 可通过HttpServletRequest的getRemoteUser()方法获得登录用户的登录名
     * @return
     */

    @Bean
    public FilterRegistrationBean casHttpServletRequestWrapperFilter(){
        FilterRegistrationBean authenticationFilter = new FilterRegistrationBean();
        authenticationFilter.setFilter(new HttpServletRequestWrapperFilter());
        authenticationFilter.setOrder(6);
        List<String> urlPatterns = new ArrayList<String>();
        urlPatterns.add("/cas/casLogin");
        authenticationFilter.setUrlPatterns(urlPatterns);
        return authenticationFilter;
    }

    /**
     * 该过滤器使得可以通过org.jasig.cas.client.util.AssertionHolder来获取用户的登录名。
     比如AssertionHolder.getAssertion().getPrincipal().getName()。
     这个类把Assertion信息放在ThreadLocal变量中，这样应用程序不在web层也能够获取到当前登录信息
     * @return
     */
    @Bean
    public FilterRegistrationBean casAssertionThreadLocalFilter(){
        FilterRegistrationBean authenticationFilter = new FilterRegistrationBean();
        authenticationFilter.setFilter(new AssertionThreadLocalFilter());
        authenticationFilter.setOrder(7);
        List<String> urlPatterns = new ArrayList<>();
        urlPatterns.add("/cas/casLogin");
        authenticationFilter.setUrlPatterns(urlPatterns);
        return authenticationFilter;
    }
}
