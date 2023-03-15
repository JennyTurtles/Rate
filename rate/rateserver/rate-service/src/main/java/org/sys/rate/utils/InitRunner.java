package org.sys.rate.utils;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.sys.rate.service.mail.PropertiesService;
import org.sys.rate.service.mail.ReceiveIMAPmails;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Timer;
import java.util.TimerTask;


/**
 * 在项目运行时就运行receiveIMAPmails.readMails()的方法，定时启动
 */
@Component
@Order(1)
public class InitRunner implements ApplicationRunner {

    @Resource
    public ReceiveIMAPmails receiveIMAPmails;

    @Value("${spring.mail.username}")
    private String from;

    @Resource
    PropertiesService propertiesService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Timer timer = new Timer();
        // 延迟delay毫秒后，执行第一次task，然后每隔period毫秒执行一次task
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    String timeStr1 = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
//                    System.out.println("-----启动解封未读邮件功能！-----当前时间为:"+timeStr1);
                    updateProperties();
                    //receiveIMAPmails.readMails();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 1000, 1000 * 60 * 1);  // 现在是每隔一分钟

    }

    @Resource
    private JdbcTemplate jdbcTemplate;

//    @Scheduled(initialDelay = 0, fixedRate = 60000)
    public void updateProperties() throws ConfigurationException {
        propertiesService.setMyPropertyFromDatabase();
//        System.out.println(propertiesService.getUsername());
//        Properties props = new Properties();
        try {
//            FileInputStream in = new FileInputStream("rate/rateserver/rate-web/src/main/resources/application.properties");
//            props.load(in);
//            in.close();
//
//            // 修改属性值
//            props.setProperty("spring.mail.username", propertiesService.getUsername());
//            props.setProperty("spring.mail.password", propertiesService.getPassword());
//            props.setProperty("spring.mail.host", propertiesService.getSendHost());
////            props.setProperty("spring.mail.username", propertiesService.getUsername());
//            FileOutputStream out = new FileOutputStream("rate/rateserver/rate-web/src/main/resources/application.properties");
//            props.store(out, null);
//            out.close();
            PropertiesConfiguration config = new PropertiesConfiguration("rate/rateserver/rate-web/src/main/resources/application.properties");
            config.setProperty("spring.mail.username", propertiesService.getUsername());
            config.setProperty("spring.mail.password", propertiesService.getPassword());
            config.setProperty("spring.mail.host", propertiesService.getSendHost());

            config.save();

//            System.out.println("配置文件更新成功");
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }

    }


}
