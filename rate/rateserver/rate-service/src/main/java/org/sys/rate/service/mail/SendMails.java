package org.sys.rate.service.mail;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.sys.rate.model.EmailErrorLog;
import org.sys.rate.model.Mail;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.annotation.Resource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Timestamp;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class SendMails {
    @Resource
    private MailService mailService;
    @Resource
    private EmailErrorLogService emailErrorLogService;


    public void sendMailAsync(final String to, final String subject, final String content, final File attachment) {
        Mail mail = mailService.handleNullPointerException();

        if (StringUtils.isEmpty(to)) {
            EmailErrorLog emailErrorLog = new EmailErrorLog();
            emailErrorLog.setErrorDescription("接收人的邮箱地址为空");
            emailErrorLog.setSenderEmail(mail.getEmailAddress() + "。发件内容是：" + content);
            emailErrorLog.setTimestamp(new Timestamp(System.currentTimeMillis()));

            emailErrorLogService.addEmailErrorLog(emailErrorLog);
            return;
        }

        CompletableFuture.runAsync(() -> {
            try {
                sendMailInternal(to, subject, content, mail, attachment);
            } catch (MessagingException e) {
                EmailErrorLog emailErrorLog = new EmailErrorLog();
                emailErrorLog.setErrorType("发件错误");
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                e.printStackTrace(pw);
                String errorDetails = sw.toString();
                emailErrorLog.setErrorDescription(errorDetails + "。收件人：" + to + "。发件内容：" + content);
                emailErrorLog.setTimestamp(new Timestamp(System.currentTimeMillis()));

                emailErrorLogService.addEmailErrorLog(emailErrorLog);
            }
        });
    }


    private void sendMailInternal(String to, String subject, String content, Mail mail, File attachment) throws MessagingException {

        Properties props = new Properties();
        props.setProperty("mail.host", mail.getSMTPHost());
        props.setProperty("mail.transport.protocol", "SMTP");
        props.setProperty("mail.smtp.auth", "true");
        props.setProperty("mail.smtp.ssl.enable", "true");
        props.setProperty("mail.smtp.timeout", "25000");
//        props.setProperty("mail.smtp.port", "465");
//        props.setProperty("mail.smtp.socketFactory.port", "465");
        props.setProperty("mail.smtp.port", "25");
        props.setProperty("mail.smtp.socketFactory.port", "25");
        props.setProperty("mail.smtp.socketFactory.fallback", "false");
        props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

        Authenticator authenticator = new Authenticator() {
            @Override
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(mail.getEmailAddress(), mail.getIMAPVerifyCode());
            }
        };

        Session session = Session.getInstance(props, authenticator);
        MimeMessage message = new MimeMessage(session);

        message.setFrom(new InternetAddress(mail.getEmailAddress()));
        message.setRecipients(Message.RecipientType.TO, to);
        message.setSubject(subject);

        Multipart multipart = new MimeMultipart();
        MimeBodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setContent(content, "text/html;charset=utf-8");
        multipart.addBodyPart(messageBodyPart);

        if (attachment != null && attachment.exists()) {
            MimeBodyPart filePart = new MimeBodyPart();
            FileDataSource fileDataSource = new FileDataSource(attachment);
            filePart.setDataHandler(new DataHandler(fileDataSource));
            filePart.setFileName(attachment.getName());
            multipart.addBodyPart(filePart);
        }

        message.setContent(multipart);

        Transport.send(message);

    }


}
