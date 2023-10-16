package org.sys.rate.service.mail;

import com.github.pagehelper.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.sys.rate.model.*;

import javax.annotation.Resource;
import java.sql.Timestamp;

@Slf4j
@Service
public class MailToStuService {
    @Resource
    private SendMails sendMails;

    @Resource
    private SendMailContentService sendMailContentService;

    @Resource
    private EmailErrorLogService emailErrorLogService;

    @Resource
    private MailService mailService;


    public <T extends Production> void sendStuMail(String state, T production, Paper paper, String type) {
        if (state.equals("commit")) {
            return;
        }

        SendMailContent sendMailContent;
        if (paper != null) {
            sendMailContent = sendMailContentService.getSendMailContent(Math.toIntExact(paper.getStudentID()));
        } else {
            sendMailContent = sendMailContentService.getSendMailContent(Math.toIntExact(production.getStudentId()));
        }
        if (sendMailContent == null) {
            return;
        }

        Mail mail = mailService.handleNullPointerException();
        if (mail == null) {
            return;
        }

        if (StringUtil.isEmpty(sendMailContent.getStudentEmail())) {
            EmailErrorLog emailErrorLog = new EmailErrorLog();
            emailErrorLog.setErrorType("给学生发件错误");
            emailErrorLog.setErrorDescription("学生邮箱地址为空。系统邮箱：" + mail.getEmailAddress() + "。邮件内容：" + sendMailContent.toString());
            emailErrorLog.setTimestamp(new Timestamp(System.currentTimeMillis()));
            emailErrorLogService.addEmailErrorLog(emailErrorLog);
            return;
        }


        String subject = "", content = "";

        StringBuilder contentBuilder = new StringBuilder();
        contentBuilder.append("亲爱的同学：<br>")
                .append("您好！<br>")
                .append("<b>您的").append(paper == null ? production.getName() : paper.getName());

        switch (state) {
            case "tea_pass":
                subject = "恭喜你，你的" + type + "已经被导师通过！";
                contentBuilder.append("已经被").append(sendMailContent.getTeacherName()).append("老师通过！</b><br>");
                break;
            case "tea_reject":
                subject = "你的" + type + "已经被导师驳回！";
                contentBuilder.append("已经被").append(sendMailContent.getTeacherName()).append("老师驳回！</b><br>");
                break;
            case "adm_pass":
                subject = "恭喜你，你的" + type + "已经被管理员通过！";
                contentBuilder.append("已经被管理员通过！</b><br>");
                break;
            case "adm_reject":
                subject = "你的" + type + "已经被管理员驳回！";
                contentBuilder.append("已经被管理员驳回！</b><br>");
                break;
            default:
                break;
        }
        contentBuilder.append("登录<a href=\"http://106.15.36.190:8081/#/Student/Login\" target=\"_blank\">教学系统</a>可以进行详情查看！<br><br>")
                .append("本邮件由东华大学计算机学院教学系统自动发出，如有疑问，请联系管理员</a>！");
        content = contentBuilder.toString();

        // 设置邮件主题
        subject = subject != null ? subject : "东华大学计算机学院教学系统邮件";
        sendMails.sendMailAsync(sendMailContent.getStudentEmail(), subject, content, null);
    }


}
