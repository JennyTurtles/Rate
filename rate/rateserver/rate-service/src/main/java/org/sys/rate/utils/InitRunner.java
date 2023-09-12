//package org.sys.rate.utils;
//
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//import org.sys.rate.service.mail.ReceiveMails;
//
//import javax.annotation.Resource;
//import java.util.concurrent.TimeUnit;
//
//@Component
//public class InitRunner  {
//
//    @Resource
//    public ReceiveMails receiveMails;
//
//
////    60000 2
//    @Scheduled(fixedRateString = "20000")
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
