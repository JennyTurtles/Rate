package org.sys.rate.config;/**
 * ClassName: DruidConfig
 * Package: org.sys.rate.config
 * Description:
 *
 * @Author ZYK
 * @Create 2023/11/21 12:16
 * @Version 1.0
 */

import com.alibaba.druid.support.http.StatViewServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description:
 * @param: $
 * @return: $
 */
@Configuration
public class DruidConfig {

    @Bean
    public ServletRegistrationBean druidServlet() {
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean();
        servletRegistrationBean.setServlet(new StatViewServlet());
        servletRegistrationBean.setEnabled(false);
        return servletRegistrationBean;
    }

}
