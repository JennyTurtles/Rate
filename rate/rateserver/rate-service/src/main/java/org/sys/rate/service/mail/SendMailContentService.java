package org.sys.rate.service.mail;/**
 * ClassName: SendMailContentService
 * Package: org.sys.rate.service.mail
 * Description:
 *
 * @Author ZYK
 * @Create 2023/9/11 9:49
 * @Version 1.0
 */

import org.springframework.stereotype.Service;
import org.sys.rate.mapper.SendMailContentMapper;
import org.sys.rate.model.EmailErrorLog;
import org.sys.rate.model.SendMailContent;

import javax.annotation.Resource;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Timestamp;

/**
 * @Description:
 * @param: $
 * @return: $
 */
@Service
public class SendMailContentService {

    @Resource
    private SendMailContentMapper sendMailContentMapper;

    @Resource
    private EmailErrorLogService emailErrorLogService;

    public SendMailContent getSendMailContent(Integer studentId) {
        try {
            SendMailContent sendMailContent = sendMailContentMapper.getSendMailContent(studentId);
            return sendMailContent;
        } catch (Exception e) {
            EmailErrorLog emailErrorLog = new EmailErrorLog();
            emailErrorLog.setErrorType("发件错误");
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            String errorDetails = sw.toString();
            emailErrorLog.setErrorDescription(errorDetails);
            emailErrorLog.setSenderEmail("");
            emailErrorLog.setRecipientEmail("");
            emailErrorLog.setSubject("");
            emailErrorLog.setBody("");
            emailErrorLog.setTimestamp(new Timestamp(System.currentTimeMillis()));

            emailErrorLogService.addEmailErrorLog(emailErrorLog);

            return null;
        }
    }



}
