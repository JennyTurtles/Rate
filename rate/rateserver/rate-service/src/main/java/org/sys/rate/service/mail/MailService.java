package org.sys.rate.service.mail;

import com.github.pagehelper.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.sys.rate.mapper.MailMapper;
import org.sys.rate.model.EmailErrorLog;
import org.sys.rate.model.Mail;

import javax.annotation.Resource;
import java.sql.Timestamp;

@Service
@Slf4j
public class MailService {

    @Resource
    MailMapper mailMapper;

    @Resource
    private EmailErrorLogService emailErrorLogService;

    public Mail getMail() {
        return mailMapper.getMail();
    }


    public boolean updateMail(Mail mail) {
        return mailMapper.updateMail(mail) == 1;
    }

    public Mail handleNullPointerException() {
        Mail mail = this.getMail();

        if (StringUtil.isEmpty(mail.getEmailAddress())) {
            logAndHandleError("发件人的邮箱地址为空", mail.getEmailAddress());
            return null;
        }

        if (StringUtil.isEmpty(mail.getIMAPVerifyCode())) {
            logAndHandleError("发件人的IMAP验证码为空", mail.getEmailAddress());
            return null;
        }

        if (StringUtil.isEmpty(mail.getSMTPHost())) {
            logAndHandleError("发件人的SMTP的host为空", mail.getEmailAddress());
            return null;
        }

        return mail;
    }

    private void logAndHandleError(String errorDescription, String senderEmail) {
        EmailErrorLog emailErrorLog = new EmailErrorLog();
        emailErrorLog.setErrorType("发件人邮箱信息错误");
        emailErrorLog.setErrorDescription(errorDescription + "。发件人邮箱地址为：" + senderEmail);
        emailErrorLog.setTimestamp(new Timestamp(System.currentTimeMillis()));
        emailErrorLogService.addEmailErrorLog(emailErrorLog);
    }


}
