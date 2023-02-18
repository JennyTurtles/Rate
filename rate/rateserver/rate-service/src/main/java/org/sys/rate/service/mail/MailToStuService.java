package org.sys.rate.service.mail;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.sys.rate.model.Paper;
import org.sys.rate.model.Student;
import org.sys.rate.model.Teacher;
import org.sys.rate.service.admin.PaperService;
import org.sys.rate.service.admin.StudentService;
import org.sys.rate.service.admin.TeacherService;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class MailToStuService {
    @Value("${spring.mail.username}")
    private String from;

    @Resource
    private JavaMailSender mailSender;

    @Resource
    StudentService studentService;

    @Resource
    TeacherService teacherService;

    @Resource
    PaperService paperService;

    // createMimeMessage可以展示超文本信息
    @GetMapping("multi")
    public void sendStuMail(String state, Long ID) throws MessagingException {
        if(state.equals("commit"))
                return;
        System.out.println("------------开始给相关的学生发送邮件！------------");

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message,true);

        Paper paper = paperService.getById((int)(long)ID);
        Student student = studentService.getById((int)(long)paper.getStudentID());
        Teacher teacher = teacherService.getById(student.getTutorID());

        String subject = "", content = "";
        String to=student.getEmail();

        switch (state){
            case "tea_pass":subject="恭喜你，你的论文已经被导师通过！";
                            content="亲爱的同学：<br>"+
                                    "您好！<br>"+
                                    "<b>您的"+paper.getName()+"已经被"+teacher.getName()+"老师通过！</b><br>"+
                                    "登录<a href=\"https://localhost:8080/#/Teacher/Login\" target=\"_blank\">教学系统</a>可以进行详情查看！"+"<br><br>"+
                                    "本邮件由东华大学计算机学院教学系统自动发出，如有疑问，请联系<a href=\"mailto:rateAdmin@126.com?\">管理员</a>！";
                            break;
            case "tea_reject":subject="你的论文已经被导师驳回！";
                            content="亲爱的同学：<br>"+
                                    "您好！<br>"+
                                    "<b>您的"+paper.getName()+"已经被"+teacher.getName()+"老师驳回！</b><br>"+
                                    "登录<a href=\"https://localhost:8080/#/Teacher/Login\" target=\"_blank\">教学系统</a>可以进行详情查看！"+"<br><br>"+
                                    "本邮件由东华大学计算机学院教学系统自动发出，如有疑问，请联系<a href=\"mailto:rateAdmin@126.com?\">管理员</a>！";
                            break;
            case "adm_pass":subject="恭喜你，你的论文已经被管理员通过！";
                            content="亲爱的同学：<br>"+
                                    "您好！<br>"+
                                    "<b>您的"+paper.getName()+"已经被管理员通过！</b><br>"+
                                    "登录<a href=\"https://localhost:8080/#/Teacher/Login\" target=\"_blank\">教学系统</a>可以进行详情查看！"+"<br><br>"+
                                    "本邮件由东华大学计算机学院教学系统自动发出，如有疑问，请联系<a href=\"mailto:rateAdmin@126.com?\">管理员</a>！";
                            break;
            case "adm_reject":subject="你的论文已经被管理员驳回！";
                            content="亲爱的同学：<br>"+
                                    "您好！<br>"+
                                    "登录<a href=\"https://localhost:8080/#/Teacher/Login\" target=\"_blank\">教学系统</a>可以进行详情查看！"+"<br><br>"+
                                    "本邮件由东华大学计算机学院教学系统自动发出，如有疑问，请联系<a href=\"mailto:rateAdmin@126.com?\">管理员</a>！";
                            break;
            default:System.out.println("未知状态！");
        }

        helper.setFrom(from);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(content,true);
//        helper.addAttachment("教师审核学生项目——邮件回复模板.txt",new File("upload/教师审核学生项目——邮件回复模板.txt"));
//        helper.addAttachment(paper.getName()+"",new File("upload/"+filePath));


        mailSender.send(message);
        System.out.println("------------给学生的邮件已经成功发送------------");
    }
}
