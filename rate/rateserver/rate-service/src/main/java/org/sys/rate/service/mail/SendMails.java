package org.sys.rate.service.mail;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
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
import java.util.Properties;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class SendMails {
    @Resource
    private MailService mailService;


    private Mail handleNullPointerException() {
        Mail mail = mailService.getMail();

        if (mail.getEmailAddress() == null) {
            throw new NullPointerException("EmailAddress is null");
        }

        if (mail.getIMAPVerifyCode() == null) {
            throw new NullPointerException("IMAPVerifyCode is null");
        }

        if (mail.getSMTPHost() == null) {
            throw new NullPointerException("SMTPHost is null");
        }
        return mail;
    }

    public void sendMailAsync(final String to, final String subject, final String content, final File attachment) {
        validateParameters(to, subject, content);
        Mail mail = handleNullPointerException();

        CompletableFuture.runAsync(() -> {
            try {
                sendMailInternal(to, subject, content, mail, attachment);
            } catch (MessagingException e) {
                log.error("Failed to send email: {}", e.getMessage(), e);
                throw new RuntimeException("Failed to send email", e);
            }
        });
    }


    private void sendMailInternal(String to, String subject, String content, Mail mail, File attachment) throws MessagingException {
        Properties props = new Properties();
        props.setProperty("mail.host", mail.getSMTPHost());
        props.setProperty("mail.transport.protocol", "SMTP");
        props.setProperty("mail.smtp.auth", "true");
        props.setProperty("mail.smtp.ssl.enable",  "true");
//        props.setProperty("mail.debug", "true");//启用调试
        props.setProperty("mail.smtp.timeout", "25000");//设置链接超时
        props.setProperty("mail.smtp.port", "465");//设置端口
        props.setProperty("mail.smtp.socketFactory.port", "465");//设置ssl端口
        props.setProperty("mail.smtp.socketFactory.fallback", "false");
        props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
//        props.put("mail.smtp.starttls.enable", "true");

        Authenticator authenticator = new Authenticator() {
            @Override
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(mail.getEmailAddress(), mail.getIMAPVerifyCode());
            }
        };

        Session session = Session.getInstance(props, authenticator);
//        session.setDebug(true);
        MimeMessage message = new MimeMessage(session);

        message.setFrom(new InternetAddress(mail.getEmailAddress()));
        message.setRecipients(Message.RecipientType.TO, to);
        message.setSubject(subject);

        Multipart multipart = new MimeMultipart();
        MimeBodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setContent(content, "text/html;charset=utf-8");
        multipart.addBodyPart(messageBodyPart);

        if (attachment != null) {
            MimeBodyPart filePart = new MimeBodyPart();
            FileDataSource fileDataSource = new FileDataSource(attachment);
            filePart.setDataHandler(new DataHandler(fileDataSource));
            filePart.setFileName(attachment.getName());
            multipart.addBodyPart(filePart);
        }

        message.setContent(multipart);

        Transport.send(message);
        log.info("Email sent to {}", to);
    }

    private void validateParameters(String to, String subject, String content) {
        if (StringUtils.isEmpty(to) || StringUtils.isEmpty(subject) || StringUtils.isEmpty(content)) {
            throw new IllegalArgumentException("One or more parameters required for sending email is empty or null.");
        }
    }

}
