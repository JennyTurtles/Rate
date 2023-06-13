//package org.sys.rate.utils;
//
//import org.springframework.boot.ApplicationArguments;
//import org.springframework.boot.ApplicationRunner;
//import org.springframework.core.annotation.Order;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//import org.sys.rate.service.mail.MailService;
//import org.sys.rate.service.mail.ReceiveMails;
//
//import javax.annotation.Resource;
//import java.util.concurrent.TimeUnit;
//
//@Component
//@Order(1)
//public class InitRunner implements ApplicationRunner {
//
//    @Resource
//    public ReceiveMails receiveMails;
//
//    @Resource
//    MailService mailService;
//
//    @Override
//    public void run(ApplicationArguments args) throws Exception {
//        mailService.setMail();
//    }
//
//    @Scheduled(fixedRateString = "120000")
//    public void readMails() {
//        try {
//            TimeUnit.SECONDS.sleep(2);
//            receiveMails.readMails();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//}
