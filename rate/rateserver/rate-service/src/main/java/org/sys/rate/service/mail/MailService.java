package org.sys.rate.service.mail;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.sys.rate.mapper.MailMapper;
import org.sys.rate.model.Mail;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import java.util.Map;
import java.util.Properties;

@Service
@Transactional
@Slf4j
public class MailService {
    @Resource
    private JdbcTemplate jdbcTemplate;
    @Resource
    MailMapper mailMapper;

    private String IMAPHost;
    private String emailAddress;
    private String IMAPVerifyCode;
    private String SMTPHost;

    private Map<String, Object> cachedMail;

    public String getSMTPHost() {
        return SMTPHost;
    }

    public void setSMTPHost(String SMTPHost) {
        this.SMTPHost = SMTPHost;
    }

    public void setIMAPHost(String IMAPHost) {
        this.IMAPHost = IMAPHost;
    }

    public String getIMAPHost() {
        return IMAPHost;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getEmailAddress() {
        if (cachedMail != null && cachedMail.get("emailAddress") != null) {
            return (String) cachedMail.get("emailAddress");
        } else {
            return null;
        }
    }

    public void setIMAPVerifyCode(String IMAPVerifyCode) {
        this.IMAPVerifyCode = IMAPVerifyCode;
    }

    public String getIMAPVerifyCode() {
        if (cachedMail != null && cachedMail.get("IMAPVerifyCode") != null) {
            return (String) cachedMail.get("IMAPVerifyCode");
        } else {
            return null;
        }
    }

    public void setMail() {
        String sql = "SELECT emailAddress, IMAPVerifyCode, IMAPHost, SMTPHost FROM mail WHERE id =0";
        cachedMail = jdbcTemplate.queryForMap(sql);

        setEmailAddress((String) cachedMail.get("emailAddress"));
        setIMAPVerifyCode((String) cachedMail.get("IMAPVerifyCode"));
        setIMAPHost((String) cachedMail.get("IMAPHost"));
        setSMTPHost((String) cachedMail.get("SMTPHost"));
    }

    public boolean updateMail(Mail mail) throws MessagingException {
        boolean result = false;
        if(mailMapper.updateMail(mail)==1){
            result = true;
        }
        return result;
    }

    public boolean checkMail(Mail mail) throws MessagingException {
        Properties props = new Properties();
        props.setProperty("mail.smtp.host", mail.getSMTPHost());
        props.setProperty("mail.imap.host", mail.getIMAPHost());
        props.setProperty("mail.smtp.auth", "true");
        Session session = Session.getDefaultInstance(props);

        Transport transport = session.getTransport("smtp");
        transport.connect(mail.getSMTPHost(), mail.getEmailAddress(), mail.getIMAPVerifyCode());

        Store store = session.getStore("imap");
        store.connect(mail.getIMAPHost(), mail.getEmailAddress(), mail.getIMAPVerifyCode());

        try{
            if(transport.isConnected() && store.isConnected()){
                return true;
            }
        }finally {
            transport.close();
            store.close();
        }
        return false;
    }

    public MailService() {

    }
}
