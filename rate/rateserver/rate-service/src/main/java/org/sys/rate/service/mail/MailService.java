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
import org.sys.rate.service.admin.PublicationService;
import org.sys.rate.service.admin.StudentService;
import org.sys.rate.service.admin.TeacherService;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class MailService {
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

    @Resource
    PublicationService publicationService;

    // createMimeMessage可以展示超文本信息
    @GetMapping("multi")
    public void sendMail(Paper paper, String uploadFileName) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message,true);

        Student student = studentService.getById((int)(long)paper.getStudentID());
        Teacher teacher=teacherService.getById(student.getTutorID());
        String to=teacher.getEmail();
//            String subject="【请阅】论文送审前查重-"+paper.getStudentID();  // 加入【】很容易被识别为垃圾邮件
        String subject="请在教学系统中审核"+student.getName()+"的论文成果，编号"+paper.getID();
        String timeStr1= LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String pubName = publicationService.selectPublicationById(paper.getPublicationID()).getName();


        String content=
                "尊敬的"+teacher.getName()+"老师：<br>"+
                "您好！<br>"+
                "<b>您的学生"+student.getName()+"已经在系统中提交成果申报。</b><br>" +
                "论文标题："+paper.getName()+"。<br>"+
                "发表期刊："+pubName+"<br>" +
                "出版年月："+paper.getYear()+"-"+paper.getMonth()+"<br>"+
                "作者列表："+paper.getAuthor()+"<br>"+
                "提交时间："+timeStr1+"<br>"+
                "证明材料："+"请查看邮件附件"+"<br><br>"+
                "<b>您可以登录<a href=\"https://localhost:8080/#/Teacher/Login\" target=\"_blank\">教学系统</a>进行审核，也可以直接回复" +
                        "本邮件完成审核。</b><br>"+
                "如果回复本邮件，方式如下：<br>"+
                "(1) 若审核<b>通过</b>该成果，请在邮件中仅保留以下两行并回复。<br>"+
                "论文编号："+paper.getID()+"<br>"+
                "审核结果："+"通过"+"<br>"+

                "(2) 若<b>驳回</b>该论文，请在邮件中仅保留以下三行并回复。<br>"+
                "论文编号："+paper.getID()+"<br>"+
                "审核结果："+"不通过"+"<br>"+
                "驳回理由："+"<span style=\"color:red;\">(请填写理由)"+"</span><br><br>"+
//                "&emsp;&emsp;<span class=\"bold-dot\">•</span>&nbsp;直接在附件中下载“"+paper.getName()+"”，“审阅通过回复模板”或者“审阅拒绝回复模板”，<br>" +
//                "&emsp;&emsp;<span class=\"bold-dot\">•</span>&nbsp;按照回复模板在正文中添加相关信息给ratemail@126发送邮件。<br>" +
//                "&emsp;&emsp;<span class=\"bold-dot\">•</span>&nbsp;若格式错误，系统会自动回复有关格式错误信息的邮件；" +
//                "若修改论文状态成功，系统会自动回复修改论文状态成功的邮件。<br><br>"+
                "本邮件由系统自动发出，如有疑问，请联系<a href=\"mailto:rateAdmin@126.com?\">管理员</a>！";
        // 这里要不要加上如果不符合要求，那么交给管理员审阅尼？


        helper.setFrom(from);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(content,true);
//        helper.addAttachment("审阅拒绝回复模板.txt",new File("upload/审阅拒绝回复模板.txt"));
//        helper.addAttachment("审阅拒绝拒绝模板.txt",new File("upload/审阅拒绝拒绝模板.txt"));
        helper.addAttachment(uploadFileName+"",new File("upload/"+uploadFileName));
//        helper.addAttachment(paper.getName()+"",new File("upload/"+filePath));


        mailSender.send(message);
        System.out.println("邮件已经成功发送");
    }

    @GetMapping("multi")
    public void sendFeedbackMail(String to, Long ID, String mailState) throws MessagingException{
        Paper paper = null;
        Student student = null;
        Teacher teacher = null;

        if(ID!=null){
            paper = paperService.getById((int)(long)ID);
        }

        if(paper!=null) {
            student = studentService.getById((int) (long) paper.getStudentID());
            teacher = teacherService.getById(student.getTutorID());
        }

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message,true);

        String subject="";
        String content="";


        // 这里要不要加上如果不符合要求，那么交给管理员审阅尼？
        switch (mailState){
            case "sendNoEditMail":subject="成果申报无需重复审核，论文编号："+ID;
                                  String temp = "";
                                  switch (paper.getState()){
                                      case "tea_pass":temp="导师通过";break;
                                      case "tea_reject":temp="导师驳回";break;
                                      case "adm_pass":temp="管理员通过";break;
                                      case "adm_reject":temp="管理员驳回";break;
                                      default:System.out.println("paper的状态不对！请检查数据库！");
                                  }
                                  content="亲爱的用户：<br>"+
                                          "您好！<br>"+
                                          ""+student.getName()+"的"+paper.getName()+"的状态目前状态是：<span style=\"color:red;\"><b>"+temp+"</b></span><br>"+
                                          "该成果申报的当前状态不需要您审核。如需修改您的审核结果，请联系<a href=\"mailto:rateAdmin@126.com?\">管理员</a>。<br><br>"+
                                          "本邮件由系统自动发出，如有疑问，请联系<a href=\"mailto:rateAdmin@126.com?\">管理员</a>！";
                                  break;
            case "sendRemarkMail":subject="修改论文状态失败！论文编号："+ID;
                                  content="亲爱的用户：<br>"+
                                          "您好！<br>"+
                                          "您若是想驳回"+student.getName()+"的"+paper.getName()+"，<b>需要在回信中填写驳回理由！</b><br>"+
                                          "请在邮件的正文中添加下面内容，完善不通过理由，回复至本邮箱。<br>"+
                                          "论文编号："+paper.getID()+"<br>"+
                                          "审核结果："+"不通过"+"<br>"+
                                          "驳回理由："+"<span style=\"color:red;\">(请填写理由)"+"</span><br><br>"+
                                          "本邮件由系统自动发出，如有疑问，请联系<a href=\"mailto:rateAdmin@126.com?\">管理员</a>！";
                                  break;
            case "sendRejtSuccessMail":subject=student.getName()+"的论文已经成功被导师驳回！论文编号："+ID;
                                       content="亲爱的用户：<br>"+
                                               "您好！<br>"+
                                               student.getName()+"的论文已经成功被"+teacher.getName()+"驳回！论文编号："+ID+"<br>"+
                                               "登录<a href=\"https://localhost:8080/#/Teacher/Login\" target=\"_blank\">教学系统</a>可以进行详情查看！"+"<br><br>"+
                                               "本邮件由系统自动发出，如有疑问，请联系<a href=\"mailto:rateAdmin@126.com?\">管理员</a>！";
                                       break;
            case "sendPassSuccessMail":subject=student.getName()+"的论文已经成功被"+teacher.getName()+"通过！论文编号："+ID;
                                       content="亲爱的用户：<br>"+
                                               "您好！<br>"+
                                               student.getName()+"的论文已经成功被导师通过！论文编号："+ID+"<br>"+
                                               "登录<a href=\"https://localhost:8080/#/Teacher/Login\" target=\"_blank\">教学系统</a>可以进行详情查看！"+"<br><br>"+
                                               "本邮件由系统自动发出，如有疑问，请联系<a href=\"mailto:rateAdmin@126.com?\">管理员</a>！";
                                       break;
            case "sendPIDErrMail":subject="修改论文状态失败！请检查论文编号:"+ID+"是否正确！";
                                  content="亲爱的用户：<br>"+
                                          "您好！<br>"+
                                          "<b>该邮箱对应的数据库中不存在编号为"+ID+"的论文</b>，请检查论文编号是否正确！"+"<br>"+
                                          "登录<a href=\"https://localhost:8080/#/Teacher/Login\" target=\"_blank\">教学系统</a>可以进行详情查看！"+"<br><br>"+
                                          "本邮件由系统自动发出，如有疑问，请联系<a href=\"mailto:rateAdmin@126.com?\">管理员</a>！";
                                  break;
            case "sendEmailErrMail":subject="修改论文状态失败！请检查邮箱地址是否正确！论文编号:"+ID;
                                    content="亲爱的用户：<br>"+
                                            "您好！<br>"+
                                            "您若是想回信审核"+student.getName()+"的"+paper.getName()+"，<b>需要使用系统注册时的邮箱：<b>"+teacher.getEmail()+"</b><br>"+
                                            "登录<a href=\"https://localhost:8080/#/Teacher/Login\" target=\"_blank\">教学系统</a>可以进行详情查看！"+"<br><br>"+
                                            "本邮件由系统自动发出，如有疑问，请联系<a href=\"mailto:rateAdmin@126.com?\">管理员</a>！";
                                    break;
            case "sendPIDEmailMail":subject="修改论文状态失败！请检查邮箱地址和论文编号是否正确！论文编号:"+ID;
                                    content="亲爱的用户：<br>"+
                                            "您好！<br>"+
                                            "回信审核论文编号为<b>"+ID+"</b>的论文时，<b>请检查是否为系统注册时的邮箱，以及论文编号是否有误。</b><br>" +
                                            "登录<a href=\"https://localhost:8080/#/Teacher/Login\" target=\"_blank\">教学系统</a>可以进行详情查看！"+"<br><br>"+
                                            "本邮件由系统自动发出，如有疑问，请联系<a href=\"mailto:rateAdmin@126.com?\">管理员</a>！";
                                    break;
            case "sendErrMail":subject="修改论文状态失败！邮箱地址未登记！";
                               content="亲爱的用户：<br>"+
                                       "您好！<br>"+
                                       "请很高兴收到您的邮件，但很遗憾您的邮箱地址未在系统中登记，请使用您在教学系统中登记的邮箱发邮件给<a href=\"mailto:rateAdmin@126.com?\">我</a>，谢谢！"+"<br><br>"+
                                       "教学系统机器人";
                               break;
            default:System.out.println("导师回信出现其他错误，请检查邮箱！");
        }

        helper.setFrom(from);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(content,true);



        mailSender.send(message);
        System.out.println("反馈邮件已经成功发送");
    }


}
