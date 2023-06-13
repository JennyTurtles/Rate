//package org.sys.rate.service.mail;
//
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang.StringUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Service;
//
//import javax.activation.DataHandler;
//import javax.activation.FileDataSource;
//import javax.annotation.Resource;
//import javax.mail.*;
//import javax.mail.internet.InternetAddress;
//import javax.mail.internet.MimeBodyPart;
//import javax.mail.internet.MimeMessage;
//import javax.mail.internet.MimeMultipart;
//import java.io.File;
//import java.util.Properties;
//import java.util.concurrent.CompletableFuture;
//
//@Slf4j
//@Service
//public class SendMails {
//    @Resource
//    MailService mailService;
//
//    private String from = null;
//    private String password = null;
//    private String sendHost = null;
//
//    private void handleNullPointerException() {
//        this.from = mailService.getEmailAddress();
//        this.password = mailService.getIMAPVerifyCode();
//        this.sendHost = mailService.getSMTPHost();
//
//        if (this.from == null) {
//            throw new NullPointerException("from is null");
//        }
//
//        if (this.password == null) {
//            throw new NullPointerException("password is null");
//        }
//
//        if (this.sendHost == null) {
//            throw new NullPointerException("sendHost is null");
//        }
//    }
//
//    public void sendMailAsync(final String to, final String subject, final String content, final String fileName, final File attachment) {
//        if (StringUtils.isEmpty(to) || StringUtils.isEmpty(subject) || StringUtils.isEmpty(content) || StringUtils.isEmpty(fileName) || attachment == null) {
//            throw new IllegalArgumentException("One or more parameters required for sending email is empty or null.");
//        }
//
//        handleNullPointerException();
//
//        CompletableFuture.runAsync(() -> {
//            Properties props = new Properties();
//            props.setProperty("mail.host", this.sendHost);
//            props.setProperty("mail.transport.protocol", "SMTP");
//            props.setProperty("mail.smtp.auth", "true");
//            Authenticator authenticator = new Authenticator() {
//                @Override
//                public PasswordAuthentication getPasswordAuthentication() {
//                    return new PasswordAuthentication(from, password);
//                }
//            };
//
//            Session session = Session.getInstance(props, authenticator);
//            MimeMessage message = new MimeMessage(session);
//            try {
//                message.setFrom(new InternetAddress(from));
//                message.setRecipients(Message.RecipientType.TO, to);
//                message.setSubject(subject);
//                Multipart multipart = new MimeMultipart();
//                MimeBodyPart messageBodyPart = new MimeBodyPart();
//                messageBodyPart.setContent(content,"text/html;charset=utf-8");
//                multipart.addBodyPart(messageBodyPart);
//
//                MimeBodyPart filePart = new MimeBodyPart();
//                FileDataSource fileDataSource = new FileDataSource(attachment);
//                filePart.setDataHandler(new DataHandler(fileDataSource));
//                filePart.setFileName(attachment.getName());
//                multipart.addBodyPart(filePart);
//
//                message.setContent(multipart);
//
//                Transport.send(message);
//                log.info("Email sent to {}", to);
//            } catch (MessagingException e) {
//                e.printStackTrace();
//                log.error("Failed to send email: {}", e.getMessage(), e);
//            }
//        });
//    }
//
//    public void sendMailAsync(final String to, final String subject, final String content) {
//        if (StringUtils.isEmpty(to) || StringUtils.isEmpty(subject) || StringUtils.isEmpty(content)) {
//            throw new IllegalArgumentException("One or more parameters required for sending email is empty or null.");
//        }
//        handleNullPointerException();
//
//        CompletableFuture.runAsync(() -> {
//            Properties props = new Properties();
//            props.setProperty("mail.host", this.sendHost);
//            props.setProperty("mail.transport.protocol", "SMTP");
//            props.setProperty("mail.smtp.auth", "true");
//            Authenticator authenticator = new Authenticator() {
//                @Override
//                public PasswordAuthentication getPasswordAuthentication() {
//                    return new PasswordAuthentication(from, password);
//                }
//            };
//
//            Session session = Session.getInstance(props, authenticator);
//            MimeMessage message = new MimeMessage(session);
//            try {
//                message.setFrom(new InternetAddress(from));
//                message.setRecipients(Message.RecipientType.TO, to);
//                message.setSubject(subject);
//                message.setContent(content, "text/html;charset=utf-8");
//                Transport.send(message);
//                log.info("Email sent to {}", to);
//            } catch (MessagingException e) {
//                e.printStackTrace();
//                log.error("Failed to send email: {}", e.getMessage(), e);
//            }
//        });
//    }
//}
