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
import org.sys.rate.model.SendMailContent;

import javax.annotation.Resource;

/**
 * @Description:
 * @param: $
 * @return: $
 */
@Service
public class SendMailContentService {

    @Resource
    private SendMailContentMapper sendMailContentMapper;

    public SendMailContent getSendMailContent(Integer studentId){
        try {
            SendMailContent sendMailContent = sendMailContentMapper.getSendMailContent(studentId);
            return sendMailContent;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public SendMailContent getSendMailContentPaper(int paperId) {
        try {
            SendMailContent sendMailContent = sendMailContentMapper.getSendMailContenPaper(paperId);
            return sendMailContent;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
