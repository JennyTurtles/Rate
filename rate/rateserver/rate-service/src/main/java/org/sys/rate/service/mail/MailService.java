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
@Slf4j
public class MailService {

    @Resource
    MailMapper mailMapper;

    public Mail getMail(){
        return mailMapper.getMail();
    }


    public boolean updateMail(Mail mail) {
        return mailMapper.updateMail(mail) == 1;
    }


}
