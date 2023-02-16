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
                "第一作者："+student.getName()+"<br>"+
                "提交时间："+timeStr1+"<br>"+
                "证明材料："+"请查看邮件附件"+"<br><br>"+
                "<b>您可以登录教学<a href=\"https://localhost:8080/#/Teacher/Login\" target=\"_blank\">系统</a>进行审核，也可以直接回复" +
                        "本邮件完成审核。</b><br>"+
                "如果回复本邮件，方式如下"+
                "(1) 若审核<b>通过</b>该成果，请在邮件中仅保留以下两行并<a href=\"mailto:ratemail@126.com?subject=InstructorReply\">回复</a>。<br>"+
                "论文编号："+paper.getID()+"<br>"+
                "审核结果："+"通过"+"<br>"+

                "(2) 若<b>驳回</b>该论文，请在邮件中仅保留以下三行并<a href=\"mailto:ratemail@126.com?subject=InstructorReply\">回复</a>。<br>"+
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
        Paper paper = paperService.getById((int)(long)ID);
        Student student = null;
        Teacher teacher = null;

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
            case "sendNoEditMail":subject="禁止修改该论文状态，论文编号："+ID;
                                  String temp = "";
                                  switch (paper.getState()){
                                      case "tea_pass":temp="导师通过";break;
                                      case "tea_reject":temp="导师驳回";break;
                                      case "admin_pass":temp="管理员通过";break;
                                      case "admin_reject":temp="管理员驳回";break;
                                      default:System.out.println("paper的状态不对！请检查数据库！");
                                  }
                                  content="亲爱的用户：<br>"+
                                          "您好！<br>"+
                                          "<b>"+student.getName()+"的"+paper.getName()+"的状态已经修改为“"+temp+"”，禁止通过回信修改论文状态，论文编号："+ID+"</b><br>"+
                                          "若要修改论文状态，请登录系统(http://localhost:8080/#/Teacher/Login)或者通过邮件联系管理员。<br><br>"+
                                          "如有其他需求或建议，请联系管理员rateAdmin@126.com<br>"+
                                          "祝好，<br>"+"ratemail团队";
                                  break;
            case "sendRemarkMail":subject="修改论文不通过状态失败！论文编号："+ID;
                                  content="亲爱的用户：<br>"+
                                          "您好！<br>"+
                                          "<b>您若是想驳回"+student.getName()+"的"+paper.getName()+"，需要在回信中添加不通过的理由！</b><br>"+
                                          "请在邮件的正文中添加下面内容，完善不通过理由，回复至ratemail@126.com:<br>"+
                                          "论文编号："+paper.getID()+"<br>"+
                                          "是否通过：不通过<br>"+
                                          "不通过理由：<br><br>"+
                                          "如有其他需求或建议，请联系管理员rateAdmin@126.com<br>"+
                                          "祝好，<br>"+"ratemail团队";
                                  break;
            case "sendRejtSuccessMail":subject=student.getName()+"的论文已经成功被导师驳回！论文编号："+ID;
                                       content="亲爱的用户：<br>"+
                                               "您好！<br>"+
                                               student.getName()+"的论文已经成功被导师驳回！论文编号："+ID+"<br>"+
                                               "请登录系统(http://localhost:8080/#/Teacher/Login)查看详情！"+"<br><br>"+
                                               "如有其他需求或建议，请联系管理员rateAdmin@126.com<br>"+
                                               "祝好，<br>"+"ratemail团队";
                                       break;
            case "sendPassSuccessMail":subject=student.getName()+"的论文已经成功被导师通过！论文编号："+ID;
                                       content="亲爱的用户：<br>"+
                                               "您好！<br>"+
                                               student.getName()+"的论文已经成功被导师通过！论文编号："+ID+"<br>"+
                                               "请登录系统(http://localhost:8080/#/Teacher/Login)查看详情！"+"<br><br>"+
                                               "如有其他需求或建议，请联系管理员rateAdmin@126.com<br>"+
                                               "祝好，<br>"+"ratemail团队";
                                       break;
            case "sendPIDErrMail":subject="修改论文状态失败！请检查论文编号:"+ID+"是否正确！";
                                  content="亲爱的用户：<br>"+
                                          "您好！<br>"+
                                          "<b>该邮箱对应的数据库中不存在编号为"+ID+"的论文</b>，请检查论文编号是否正确！"+"<br>"+
                                          "请登录系统(http://localhost:8080/#/Teacher/Login)查看待审核论文的详情！"+"<br><br>"+
                                          "如有其他需求或建议，请联系管理员rateAdmin@126.com<br>"+
                                          "祝好，<br>"+"ratemail团队";
                                  break;
            case "sendEmailErrMail":subject="修改论文状态失败！请检查邮箱地址是否正确！论文编号:"+ID;
                                    content="亲爱的用户：<br>"+
                                            "您好！<br>"+
                                            "回信审核编号为<b>"+ID+"</b>的论文时，请使用注册时的<b>"+teacher.getEmail()+"</b>邮箱，按照格式重新发送给ratemail@126.com。"+"<br>"+
                                            "请登录系统(http://localhost:8080/#/Teacher/Login)查看待审核论文的详情！"+"<br><br>"+
                                            "如有其他需求或建议，请联系管理员rateAdmin@126.com<br>"+
                                            "祝好，<br>"+"ratemail团队";
                                    break;
            case "sendPIDEmailMail":subject="修改论文状态失败！请检查邮箱地址和论文编号是否正确！论文编号:"+ID;
                                    content="亲爱的用户：<br>"+
                                            "您好！<br>"+
                                            "回信审核编号为<b>"+ID+"</b>的论文时，<b>请检查是否使用注册时的邮箱，以及论文编号是否复制错误。</b><br>" +
                                            "之后可以按照格式重新发送给ratemail@126.com。"+"<br>"+
                                            "或者直接登录系统(http://localhost:8080/#/Teacher/Login)查看待审核论文的详情！"+"<br><br>"+
                                            "如有其他需求或建议，请联系管理员rateAdmin@126.com<br>"+
                                            "祝好，<br>"+"ratemail团队";
                                    break;
            case "sendErrMail":subject="请注册或登录后使用专家打分系统！";
                               content="亲爱的用户：<br>"+
                                       "您好！<br>"+
                                       "请注册后使用本系统，或者检查邮箱的收件箱和垃圾邮件按照格式回复！"+"<br><br>"+
                                       "如有其他需求或建议，请联系管理员rateAdmin@126.com<br>"+
                                       "祝好，<br>"+"ratemail团队";
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
