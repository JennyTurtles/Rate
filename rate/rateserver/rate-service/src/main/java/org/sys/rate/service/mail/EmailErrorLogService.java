package org.sys.rate.service.mail;
/**
 * ClassName: EmailErrorLogService
 * Package: org.sys.rate.service.mail
 * Description:
 *
 * @Author ZYK
 * @Create 2023/9/18 15:28
 * @Version 1.0
 */

import org.springframework.stereotype.Service;
import org.sys.rate.mapper.EmailErrorLogMapper;
import org.sys.rate.model.EmailErrorLog;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description:
 * @param: $
 * @return: $
 */
@Service
public class EmailErrorLogService {
    @Resource
    private EmailErrorLogMapper emailErrorLogMapper;

    public void addEmailErrorLog(EmailErrorLog emailErrorLog) {
        emailErrorLogMapper.insert(emailErrorLog);
    }

    public void deleteEmailErrorLog(Long id) {
        emailErrorLogMapper.delete(id);
    }

    public void deleteMultiEmailErrorLog(List<EmailErrorLog> data) {
        emailErrorLogMapper.deleteMulti(data);
    }

    public EmailErrorLog getEmailErrorLogById(Long id) {
        return emailErrorLogMapper.getById(id);
    }

    public List<EmailErrorLog> getAllEmailErrorLogs() {
        return emailErrorLogMapper.getAll();
    }
}
