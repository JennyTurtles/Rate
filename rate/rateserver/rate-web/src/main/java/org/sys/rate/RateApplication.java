package org.sys.rate;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.http.HttpStatus;


import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableCaching
@MapperScan(basePackages = "org.sys.rate.mapper")
@EnableScheduling
@EnableTransactionManagement(proxyTargetClass = true)
public class RateApplication {

    public static void main(String[] args) {
        SpringApplication.run(RateApplication.class, args);
    }

    @Bean
    public WebServerFactoryCustomizer<ConfigurableWebServerFactory> webServerFactoryCustomizer(){
        return factory -> {
            ErrorPage error404Page = new ErrorPage(HttpStatus.NOT_FOUND, "/index.html");
            factory.addErrorPages(error404Page);
        };
    }

//application.properties配置：
// === tomcat (https)START ===
//  server.port= 443
//  server.ssl.key-store=classpath:dhu.jks
//  server.ssl.key-store-password=DhuXxb@06272023
//  server.ssl.key-store-type=JKS
//  server.ssl.enabled-protocols=TLSv1,TLSv1.1,TLSv1.2
//  server.ssl.ciphers=TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA256,TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA,TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA384,TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA,TLS_ECDHE_RSA_WITH_RC4_128_SHA,TLS_RSA_WITH_AES_128_CBC_SHA256,TLS_RSA_WITH_AES_128_CBC_SHA,TLS_RSA_WITH_AES_256_CBC_SHA256,TLS_RSA_WITH_AES_256_CBC_SHA,SSL_RSA_WITH_RC4_128_SHA
// === tomcat (https)END ===

    /* === 同时拥有http并重定向至https端口上: START === */
     @Bean
     public TomcatServletWebServerFactory tomcatServletWebServerFactory() {
         TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory() {
             @Override
             protected void postProcessContext(Context context) {
                 SecurityConstraint securityConstraint = new SecurityConstraint();
                 securityConstraint.setUserConstraint("CONFIDENTIAL");
                 SecurityCollection securityCollection = new SecurityCollection();
                 securityCollection.addPattern("/*");
                 securityConstraint.addCollection(securityCollection);
                 context.addConstraint(securityConstraint);
             }
         };
         factory.addAdditionalTomcatConnectors(httpConnector());
         return factory;
     }

     @Bean
     public Connector httpConnector() {
         Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
         connector.setScheme("http");
         //Connector监听的http的端口号
         connector.setPort(80);
         connector.setSecure(false);
         //监听到http的端口号后转向到的https的端口号
         connector.setRedirectPort(443);
         return connector;
     }
    /* === 同时拥有http并重定向至https端口上: END === */
}
