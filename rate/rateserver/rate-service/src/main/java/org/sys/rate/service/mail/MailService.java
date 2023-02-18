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
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        Student student = studentService.getById((int) (long) paper.getStudentID());
        Teacher teacher = teacherService.getById(student.getTutorID());
        String to = teacher.getEmail();
//            String subject="【请阅】论文送审前查重-"+paper.getStudentID();  // 加入【】很容易被识别为垃圾邮件
        String subject = "请在教学系统中审核" + student.getName() + "的论文成果，成果编号：" + paper.getID();
        String timeStr1 = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String pubName = publicationService.selectPublicationById(paper.getPublicationID()).getName();
        String type = "论文";


        String content =
                "尊敬的" + teacher.getName() + "老师：<br>" +
                        "您好！<br>" +
                        "<b>您的学生" + student.getName() + "已经在系统中提交成果申报。</b><br>" +
                        "论文标题：" + paper.getName() + "。<br>" +
                        "发表期刊：" + pubName + "<br>" +
                        "出版年月：" + paper.getYear() + "-" + paper.getMonth() + "<br>" +
                        "作者列表：" + paper.getAuthor() + "<br>" +
                        "提交时间：" + timeStr1 + "<br>" +
                        "证明材料：" + "请查看邮件附件" + "<br><br>" +
                        "<b>您可以登录<a href=\"https://localhost:8080/#/Teacher/Login\" target=\"_blank\">教学系统</a>进行审核，也可以直接回复" +
                        "本邮件完成审核。</b><br>" +
                        "如果回复本邮件，方式如下：<br>" +
                        "(1) 若审核<b>通过</b>该成果，请在邮件中<span style=\"color:red;\">仅保留</span>以下三行并回复。<br>" +
                        "成果类型：" + type + "<br>" +
                        "成果编号：" + paper.getID() + "<br>" +
                        "审核结果：" + "通过" + "<br>" +
                        "(2) 若<b>驳回</b>该论文，请在邮件中<span style=\"color:red;\">仅保留</span>以下四行并回复。<br>" +
                        "成果类型：" + type + "<br>" +
                        "成果编号：" + paper.getID() + "<br>" +
                        "审核结果：" + "驳回" + "<br>" +
                        "审核理由：" + "<span style=\"color:red;\">(请填写理由)" + "</span><br><br>" +
//                "&emsp;&emsp;<span class=\"bold-dot\">•</span>&nbsp;直接在附件中下载“"+paper.getName()+"”，“审阅通过回复模板”或者“审阅拒绝回复模板”，<br>" +
//                "&emsp;&emsp;<span class=\"bold-dot\">•</span>&nbsp;按照回复模板在正文中添加相关信息给ratemail@126发送邮件。<br>" +
//                "&emsp;&emsp;<span class=\"bold-dot\">•</span>&nbsp;若格式错误，系统会自动回复有关格式错误信息的邮件；" +
//                "若修改论文状态成功，系统会自动回复修改论文状态成功的邮件。<br><br>"+
                        "本邮件由东华大学计算机学院教学系统自动发出，如有疑问，请联系<a href=\"mailto:rateAdmin@126.com?\">管理员</a>！";
        // 这里要不要加上如果不符合要求，那么交给管理员审阅尼？


        helper.setFrom(from);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(content, true);
//        helper.addAttachment("审阅拒绝回复模板.txt",new File("upload/审阅拒绝回复模板.txt"));
//        helper.addAttachment("审阅拒绝拒绝模板.txt",new File("upload/审阅拒绝拒绝模板.txt"));
        helper.addAttachment(uploadFileName + "", new File("upload/" + uploadFileName));
//        helper.addAttachment(paper.getName()+"",new File("upload/"+filePath));


        mailSender.send(message);
        System.out.println("邮件已经成功发送");
    }

    @GetMapping("multi")
    public void sendFeedbackMail(String to, Long ID, String mailState) throws MessagingException {
        String type = "论文"; // 后续可以考虑根据ID判断是否为论文
        Paper paper = null;
        Student student = null;
        Teacher teacher = null;
        String pubName = "", correctFormat = "", infoPaper = "";

        if (ID != null) {
            paper = paperService.getById((int) (long) ID);
            if (paper != null) {
                student = studentService.getById((int) (long) paper.getStudentID());
                teacher = teacherService.getById(student.getTutorID());
                pubName = publicationService.selectPublicationById(paper.getPublicationID()).getName();

                correctFormat = "下面是邮件回复的正确格式：<br>" +
                        "(1) 若审核<b>通过</b>该成果，请在邮件中<b><span style=\"color:red;\">仅保留</span>以下三行并回复</b>。<br>" +
                        "成果类型：" + type + "<br>" +
                        "成果编号：" + paper.getID() + "<br>" +
                        "审核结果：" + "通过" + "<br>" +
                        "(2) 若<b>驳回</b>该论文，请在邮件中<b><span style=\"color:red;\">仅保留</span>以下四行并回复</b>。<br>" +
                        "成果类型：" + type + "<br>" +
                        "成果编号：" + paper.getID() + "<br>" +
                        "审核结果：" + "驳回" + "<br>" +
                        "审核理由：" + "<span style=\"color:red;\">(请填写理由)" + "</span><br><br>";

                infoPaper = "下面是论文的一些基本信息：<br>" +
                        "论文标题：" + paper.getName() + "。<br>" +
                        "发表期刊：" + pubName + "<br>" +
                        "出版年月：" + paper.getYear() + "-" + paper.getMonth() + "<br>" +
                        "作者列表：" + paper.getAuthor() + "<br><br>";
            }


        }


        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        String subject = "";
        String content = "";
        assert paper != null;


        switch (mailState) {
            case "duplicateVarMail":
                subject = "教学系统：回信请删除多余的内容";
                content = "亲爱的用户：<br>" +
                        "您好！<br>" +
                        "<b>错误原因：您的回信中可能出现了多次“成果类型”、“成果编号”、“审核结果”的字样。<br>" +
                        "解决方法：请仅在邮件中保留要求的内容，多余的内容请删除。</b><br>" +
                        "请使用标题为<b>请在教学系统中审核XX的论文成果，成果编号：XX</b>的邮件要求的回信格式。<br><br>" +
                        "本邮件由东华大学计算机学院教学系统自动发出，如有疑问，请联系<a href=\"mailto:rateAdmin@126.com?\">管理员</a>！";
                break;
            case "junkMail":
                subject = "教学系统：暂不支持该功能";
                content = "亲爱的用户：<br>" +
                        "您好！<br>" +
                        "很高兴收到您的邮件，但很遗憾教学系统暂不支持该功能。<br>" +
                        "我们会尽最大努力不断完善，满足您的需求。很抱歉给您造成的不便。<br>" +
                        "请使用您在教学系统中登记的邮箱发邮件给<a href=\"mailto:rateAdmin@126.com?\">我</a>，谢谢！" + "<br><br>" +
                        "本邮件由东华大学计算机学院教学系统自动发出，如有疑问，请联系<a href=\"mailto:rateAdmin@126.com?\">管理员</a>！";
                break;
            case "less5Mail":
                subject = "教学系统：缺少内容";
                content = "亲爱的用户：<br>" +
                        "您好！<br>" +
                        "<b>错误原因：您的回信中可能缺少了<b>成果类型、成果编号、审核结果、审核理由</b>的字样。<br>" +
                        "解决方法：请添加相应缺失的文字。</b><br>" +
                        "请使用标题为<b>请在教学系统中审核XX的论文成果，成果编号：XX</b>的邮件要求的回信格式。<br><br>" +
                        "本邮件由东华大学计算机学院教学系统自动发出，如有疑问，请联系<a href=\"mailto:rateAdmin@126.com?\">管理员</a>！";
                break;
            case "IDNotExistErrMail":
            case "registerMail":
            case "matchErrMail":
                subject = "教学系统审核论文成果：成果编号错误";
                content = "亲爱的用户：<br>" +
                        "您好！<br>" +
                        "<b>错误原因：您的成果编号在数据库中查询失败。<br>" +
                        "解决方法：请检查成果编号是否正确。</b><br>" +
                        "请仔细查看标题为<b>请在教学系统中审核XX的论文成果，成果编号：XX</b>的邮件。<br><br>" +
                        "本邮件由东华大学计算机学院教学系统自动发出，如有疑问，请联系<a href=\"mailto:rateAdmin@126.com?\">管理员</a>！";
                break;
            case "emailErrMail":
                assert teacher != null;
                subject = "教学系统审核论文成果：论文编号" + paper.getID() + "审核失败";
                content = "尊敬的老师：<br>" +
                        "您好！<br>" +
                        "<b>错误原因：邮箱地址错误地使用了" + from + "。</b><br>" +
                        "<b>解决方法：请使用<span style=\"color:red;\">" + teacher.getEmail() + "</span>回复至本邮箱。</b><br><br>" +
                        infoPaper +
                        correctFormat +
                        "本邮件由东华大学计算机学院教学系统自动发出，如有疑问，请联系<a href=\"mailto:rateAdmin@126.com?\">管理员</a>！";
                break;
//            case "IDNotExistErrMail":
//                subject = "教学系统审核论文成果：论文编号" + ID + "审核失败";
//                content = "尊敬的老师：<br>" +
//                        "您好！<br>" +
//                        "<b>您在审核编号为" + ID + "的论文时，" + ID + "在数据库中查询失败。</b><br>" +
//                        "错误原因：" + ID + "错误。<br>" +
//                        "请仔细查看标题为<b>请在教学系统中审核XX的论文成果，成果编号：XX</b>的邮件。<br>" +
//                        "<b>请仔细核对成果编号</b>后回信至本邮箱。<br><br>" +
//                        "本邮件由系统自动发出，如有疑问，请联系<a href=\"mailto:rateAdmin@126.com?\">管理员</a>！";
//                break;
//            case "registerMail":
//                subject = "教学系统审核论文成果：论文编号" + ID + "审核失败";
//                content = "尊敬的老师：<br>" +
//                        "您好！<br>" +
//                        "<b>您在审核编号为" + ID + "的论文时，" + ID + "在数据库中查询失败。</b><br>" +
//                        "错误原因：1." + ID + "错误；2.邮箱地址" + from + "没有在系统中登记。<br>" +
//                        "请仔细查看标题为<b>请在教学系统中审核XX的论文成果，成果编号：XX</b>的邮件。<br>" +
//                        "<b>请仔细核对成果编号和邮箱地址后</b>后回信至本邮箱。<br>" +
//                        "本邮件由系统自动发出，如有疑问，请联系<a href=\"mailto:rateAdmin@126.com?\">管理员</a>！";
//                break;
            case "IDNotDigitErrMail":
                subject = "教学系统审核论文成果：成果编号错误";
                content = "亲爱的用户：<br>" +
                        "您好！<br>" +
                        "错误原因：您的成果编号可能未添加，或者成果编号中有英文或者中文等非数字的字符存在。<br>" +
                        "解决方法：请检查成果编号是否正确。<br>" +
                        "请仔细查看标题为<b>请在教学系统中审核XX的论文成果，成果编号：XX</b>的邮件。<br><br>" +
                        "本邮件由东华大学计算机学院教学系统自动发出，如有疑问，请联系<a href=\"mailto:rateAdmin@126.com?\">管理员</a>！";
                break;
            case "lackMail_1":
                subject = "教学系统审核论文成果：论文编号" + paper.getID() + "审核失败";
                content = "尊敬的老师：<br>" +
                        "您好！<br>" +
                        "错误原因：回信第一行缺少<b>成果类型：</b><br>" +
                        "解决方法：请在回信的第一行的开头添加<b>成果类型：</b>。<br><br>" +
                        infoPaper +
                        correctFormat +
                        "本邮件由东华大学计算机学院教学系统自动发出，如有疑问，请联系<a href=\"mailto:rateAdmin@126.com?\">管理员</a>！";
                break;
            case "lackMail_2":
                subject = "教学系统审核论文成果：论文编号" + paper.getID() + "审核失败";
                content = "尊敬的老师：<br>" +
                        "您好！<br>" +
                        "错误原因：回信第二行缺少<b>成果编号：</b><br>" +
                        "解决方法：请在回信的第二行的开头添加<b>成果编号：</b>。<br><br>" +
                        infoPaper +
                        correctFormat +
                        "本邮件由东华大学计算机学院教学系统自动发出，如有疑问，请联系<a href=\"mailto:rateAdmin@126.com?\">管理员</a>！";
                break;
            case "lackMail_3":
                subject = "教学系统审核论文成果：论文编号" + paper.getID() + "审核失败";
                content = "尊敬的老师：<br>" +
                        "您好！<br>" +
                        "错误原因：回信第三行缺少<b>审核结果：</b><br>" +
                        "解决方法：请在回信的第三行的开头添加<b>审核结果：</b>。<br><br>" +
                        infoPaper +
                        correctFormat +
                        "本邮件由东华大学计算机学院教学系统自动发出，如有疑问，请联系<a href=\"mailto:rateAdmin@126.com?\">管理员</a>！";
                break;
            case "lackMail_4":
                subject = "教学系统审核论文成果：论文编号" + paper.getID() + "审核失败";
                content = "尊敬的老师：<br>" +
                        "您好！<br>" +
                        "错误原因：回信第四行缺少<b>审核理由：</b><br>" +
                        "解决方法：请在回信的第四行的开头添加<b>审核理由：</b>。<br><br>" +
                        infoPaper +
                        correctFormat +
                        "本邮件由东华大学计算机学院教学系统自动发出，如有疑问，请联系<a href=\"mailto:rateAdmin@126.com?\">管理员</a>！";
                break;
            case "lackMail_reason":
                subject = "教学系统审核论文成果：论文编号" + paper.getID() + "审核失败";
                content = "尊敬的老师：<br>" +
                        "您好！<br>" +
                        "错误原因：回信第四行未补充审核理由：" + "<span style=\"color:red;\">(请填写理由)" + "</span>" + "<br>" +
                        "解决方法：请在回信的第四行的" + "<span style=\"color:red;\">(请填写理由)" + "</span>" + "中补充审核理由。<br><br>" +
                        infoPaper +
                        correctFormat +
                        "本邮件由东华大学计算机学院教学系统自动发出，如有疑问，请联系<a href=\"mailto:rateAdmin@126.com?\">管理员</a>！";
                break;
            case "typeErrMail":
                subject = "教学系统审核论文成果：论文编号" + paper.getID() + "审核失败";
                content = "尊敬的老师：<br>" +
                        "您好！<br>" +
                        "错误原因：您所填写的成果类型不为<b>论文、专利、获奖、项目、专著和教材</b>中的任何一项。<br>" +
                        "解决方法：请在<b>论文、专利、获奖、项目、专著和教材</b>选择一项，修改原邮件中的成果类型。<br>" +
                        infoPaper +
                        correctFormat +
                        "本邮件由东华大学计算机学院教学系统自动发出，如有疑问，请联系<a href=\"mailto:rateAdmin@126.com?\">管理员</a>！";
                break;
            case "dupStateErrMail":
                subject = "教学系统审核论文成果：成果申报无需重复审核";
                String temp = "";
                switch (paper.getState()) {
                    case "tea_pass":
                        temp = "导师通过";
                        break;
                    case "tea_reject":
                        temp = "导师驳回";
                        break;
                    case "adm_pass":
                        temp = "管理员通过";
                        break;
                    case "adm_reject":
                        temp = "管理员驳回";
                        break;
                    default:
                        System.out.println("paper的状态不对！请检查数据库！");
                }
                content = "尊敬的老师：<br>" +
                        "您好！<br>" +
                        "错误原因：您所审核的论文编号" + paper.getID() + "目前状态为<b>" + temp + "</b>，因此不需要再次修改其状态。<br>" +
                        infoPaper +
                        "本邮件由东华大学计算机学院教学系统自动发出，如有疑问，请联系<a href=\"mailto:rateAdmin@126.com?\">管理员</a>！";
                break;
            case "stateErrMail":
                subject = "教学系统审核论文成果：论文编号" + paper.getID() + "审核失败";
                content = "尊敬的老师：<br>" +
                        "您好！<br>" +
                        "错误原因：您所填写的审核结果不为<b>通过、驳回</b>中的任何一项。<br>" +
                        "解决方法：请在<b>通过、驳回</b>选择一项，修改原邮件中的审核结果。<br>" +
                        infoPaper +
                        correctFormat +
                        "本邮件由东华大学计算机学院教学系统自动发出，如有疑问，请联系<a href=\"mailto:rateAdmin@126.com?\">管理员</a>！";
                break;
            case "sendRejectMail":
                subject = "教学系统审核论文成果：论文编号" + paper.getID() + "审核成功！";
                content = "尊敬的老师：<br>" +
                        "您好！<br>" +
                        "您已经通过回信，成功修改成果编号为" + ID + "的论文的状态为：<span style=\"color:red;\"><b>导师驳回</b></span>。<br>" +
                        infoPaper +
                        "本邮件由东华大学计算机学院教学系统自动发出，如有疑问，请联系<a href=\"mailto:rateAdmin@126.com?\">管理员</a>！";
                break;
            case "sendPassMail":
                subject = "教学系统审核论文成果：论文编号" + paper.getID() + "审核成功！";
                content = "尊敬的老师：<br>" +
                        "您好！<br>" +
                        "您已经通过回信，成功修改成果编号为" + ID + "的论文的状态为：<span style=\"color:red;\"><b>导师通过</b></span>。<br>" +
                        infoPaper +
                        "本邮件由东华大学计算机学院教学系统自动发出，如有疑问，请联系<a href=\"mailto:rateAdmin@126.com?\">管理员</a>！";
                break;
            default:
                System.out.println("导师回信出现其他错误，请检查邮箱！");
        }

        helper.setFrom(from);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(content, true);


        mailSender.send(message);
        System.out.println("反馈邮件已经成功发送");
    }


}
