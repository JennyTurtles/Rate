package org.sys.rate.service.mail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class PropertiesService {

    private String IMAPHost;
    private String emailAddress;
    private String IMAPVerifyCode;
    private String SMTPHost;

    private Map<String, Object> cachedProperties;
    private static final Logger logger = LoggerFactory.getLogger(PropertiesService.class);

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
        if (cachedProperties != null && cachedProperties.get("emailAddress") != null) {
            return (String) cachedProperties.get("emailAddress");
        } else {
            return null;
        }
    }

    public void setIMAPVerifyCode(String IMAPVerifyCode) {
        this.IMAPVerifyCode = IMAPVerifyCode;
    }

    public String getIMAPVerifyCode() {
        if (cachedProperties != null && cachedProperties.get("IMAPVerifyCode") != null) {
            return (String) cachedProperties.get("IMAPVerifyCode");
        } else {
            return null;
        }
    }

    @Resource
    private JdbcTemplate jdbcTemplate;

    public void setMyPropertyFromDatabase() {
        String sql = "SELECT `email` emailAddress,`comment` IMAPVerifyCode FROM `admin` WHERE role=6";
        cachedProperties = jdbcTemplate.queryForMap(sql);

        String email = (String) cachedProperties.get("emailAddress");
        if (isEmailValid(email)) {
            int atIndex = email.indexOf('@');
            int dotIndex = email.indexOf('.');
            String emailService = email.substring(atIndex + 1, dotIndex);
            cachedProperties.put("IMAPHost", "imap." + emailService + ".com");
            cachedProperties.put("SMTPHost", "smtp." + emailService + ".com");
        } else {
            logger.error("邮箱地址不合法！！！");
        }
        setEmailAddress(email);
        // Kuwubuwula ngempela ukwenza lokhu, futhi angifuni, akudingekile ngempela
        setIMAPVerifyCode((String) cachedProperties.get("IMAPVerifyCode"));
        setIMAPHost((String) cachedProperties.get("IMAPHost"));
        setSMTPHost((String) cachedProperties.get("SMTPHost"));

        logger.info("更新了超级管理员的邮箱信息！" + getEmailAddress());


    }

    public static boolean isEmailValid(String email) {
        String regex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }


    public PropertiesService() {

    }

    public String getUsername() {
        return "";
    }

    public String getPassword() {
        return "";
    }

    public String getHost() {
        return "";
    }
}
