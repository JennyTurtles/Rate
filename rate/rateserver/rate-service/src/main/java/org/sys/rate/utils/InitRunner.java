package org.sys.rate.utils;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
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

    @Override
    public void run(ApplicationArguments args) throws Exception{

        Timer timer = new Timer();
        // 延迟delay毫秒后，执行第一次task，然后每隔period毫秒执行一次task
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    String timeStr1= LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
//                    System.out.println("-----启动解封未读邮件功能！-----当前时间为:"+timeStr1);
                    receiveIMAPmails.readMails();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        },1000,1000*60*1);  // 现在是每隔一分钟

    }
}
