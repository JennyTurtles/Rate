package org.sys.rate.service.mail;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.sys.rate.model.Productions;
import org.sys.rate.model.Student;
import org.sys.rate.model.Teacher;
import org.sys.rate.service.admin.StudentService;
import org.sys.rate.service.admin.TeacherService;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class MailToStuService {
    @Resource
    PropertiesService propertiesService;

    @Resource
    private JavaMailSender mailSender;

    @Resource
    StudentService studentService;

    @Resource
    TeacherService teacherService;


    private String from = null;

    public <T extends Productions> void sendStuMail(String state, T production) {
        if (state.equals("commit")) {
            return;
        }

        Student student = studentService.getById(Math.toIntExact(production.getStudentID()));
        Teacher teacher = teacherService.getById(student.getTutorID());

        String subject = "", content = "";
        String to = student.getEmail();

        StringBuilder contentBuilder = new StringBuilder();
        contentBuilder.append("亲爱的同学：<br>")
                .append("您好！<br>")
                .append("<b>您的").append(production.getName());

        switch (state) {
            case "tea_pass":
                subject = "恭喜你，你的论文已经被导师通过！";
                contentBuilder.append("已经被").append(teacher.getName()).append("老师通过！</b><br>");
                break;
            case "tea_reject":
                subject = "你的论文已经被导师驳回！";
                contentBuilder.append("已经被").append(teacher.getName()).append("老师驳回！</b><br>");
                break;
            case "adm_pass":
                subject = "恭喜你，你的论文已经被管理员通过！";
                contentBuilder.append("已经被管理员通过！</b><br>");
                break;
            case "adm_reject":
                subject = "你的论文已经被管理员驳回！";
                contentBuilder.append("已经被管理员驳回！</b><br>");
                break;
            default:
                throw new IllegalArgumentException("未知状态！");
        }
        contentBuilder.append("登录<a href=\"https://localhost:8080/#/Teacher/Login\" target=\"_blank\">教学系统</a>可以进行详情查看！<br><br>")
                .append("本邮件由东华大学计算机学院教学系统自动发出，如有疑问，请联系<a href=\"mailto:rateAdmin@126.com?\">管理员</a>！");
        content = contentBuilder.toString();

        // 设置邮件主题
        subject = subject!=null?subject:"东华大学计算机学院教学系统邮件";

        try {
            getFrom();
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(this.from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);
            mailSender.send(message);
            System.out.println("邮件已发送给学生 ");
        } catch (MessagingException e) {
            System.out.println("邮件发送给学生失败");
        }
    }

    private void getFrom(){
        if (this.from == null) {
            String username = propertiesService.getUsername();
            if (username == null) {
                throw new NullPointerException("from is null");
            }
            this.from = username;
        }
    }
}
