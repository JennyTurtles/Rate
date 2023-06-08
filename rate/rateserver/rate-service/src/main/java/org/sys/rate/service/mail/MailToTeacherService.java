//package org.sys.rate.service.mail;
//
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Service;
//import org.sys.rate.model.Production;
//import org.sys.rate.model.Student;
//import org.sys.rate.model.Teacher;
//import org.sys.rate.service.admin.PublicationService;
//import org.sys.rate.service.admin.StudentService;
//import org.sys.rate.service.admin.TeacherService;
//
//import javax.annotation.Resource;
//import javax.mail.MessagingException;
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//@Service
//public class MailToTeacherService {
//
//    @Resource
//    MailService propertiesService;
//
//    @Resource
//    StudentService studentService;
//
//    @Resource
//    TeacherService teacherService;
//
//    @Resource
//    PublicationService publicationService;
//
//    @Resource
//    SendMails sendMails;
//
//    private static final Logger logger = LoggerFactory.getLogger(MailToTeacherService.class);
//    private String from = null;
//    private String password = null;
//    private String sendHost = null;
//
//    private final String greetingToUser = "亲爱的用户：<br>";
//    private final String greetingToTeacher = "尊敬的老师：<br>";
//    private final String hello = "您好！<br>";
//    private final String reason = "错误原因：";
//    private final String solution = "解决方法：";
//    private final String formatMessage = "请使用标题为<b>请在教学系统中审核XX的论文成果，成果编号：XX</b>的邮件要求的回信格式。<br><br>";
//    private final String systemMessage = "本邮件由东华大学计算机学院教学系统自动发出，如有疑问，请联系<a href=\"mailto:rateAdmin@126.com?\">管理员</a>！<br><br>";
//
//
//    private void handleNullPointerException() {
//        this.from = propertiesService.getEmailAddress();
//        this.password = propertiesService.getIMAPVerifyCode();
//        this.sendHost = propertiesService.getSMTPHost();
//
//        if (this.from == null) {
//            throw new NullPointerException("from is null");
//        }
//
//        if (this.password == null) {
//            throw new NullPointerException("password is null");
//        }
//
//        if (this.sendHost == null) {
//            throw new NullPointerException("sendHost is null");
//        }
//    }
//
//
//    // when production is send, send the information to the tutor
//    public <T extends Production> void sendTeaCheckMail(T production, String type, String uploadFileName) throws FileNotFoundException {
//        try {
//            File file = new File("upload/" + uploadFileName);
//            if (!file.exists()) {
//                throw new FileNotFoundException("File not found: " + uploadFileName);
//            }
//
//            Student student = studentService.getById((int) (long) production.getStudentID());
//            Teacher teacher = teacherService.getById(student.getTutorID());
//            String to = teacher.getEmail();
//            String subject = "请在教学系统中审核" + student.getName() + "的论文成果，成果编号：" + production.getID();
//            String upLoadTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
//            String pubName = publicationService.selectPublicationById(production.getPublicationID()).getName();
//
//            StringBuilder contentBuilder = new StringBuilder();
//            contentBuilder.append("尊敬的").append(teacher.getName()).append("老师：<br>");
//            contentBuilder.append("您好！<br>");
//            contentBuilder.append("<b>您的学生").append(student.getName()).append("已经在系统中提交成果申报。</b><br>");
//            contentBuilder.append("论文标题：").append(production.getName()).append("。<br>");
//            contentBuilder.append("发表期刊：").append(pubName).append("<br>");
//            contentBuilder.append("出版年月：").append(production.getYear()).append("-").append(production.getMonth()).append("<br>");
//            contentBuilder.append("作者列表：").append(production.getAuthor()).append("<br>");
//            contentBuilder.append("提交时间：").append(upLoadTime).append("<br>");
//            contentBuilder.append("证明材料：请查看邮件附件<br><br>");
//            contentBuilder.append("<b>您可以登录<a href=\"https://localhost:8080/#/Teacher/Login\" target=\"_blank\">教学系统</a>进行审核，也可以直接回复本邮件完成审核。</b><br>");
//            contentBuilder.append("如果回复本邮件，方式如下：<br>");
//            contentBuilder.append("(1) 若审核<b>通过</b>该成果，请在邮件中<span style=\"color:red;\">仅保留</span>以下三行并回复。<br>");
//            contentBuilder.append("成果类型：").append(type).append("<br>");
//            contentBuilder.append("成果编号：").append(production.getID()).append("<br>");
//            contentBuilder.append("审核结果：").append("通过").append("<br>");
//            contentBuilder.append("(2) 若<b>驳回</b>该论文，请在邮件中<span style=\"color:red;\">仅保留</span>以下四行并回复。<br>");
//            contentBuilder.append("成果类型：").append(type).append("<br>");
//            contentBuilder.append("成果编号：").append(production.getID()).append("<br>");
//            contentBuilder.append("审核结果：").append("驳回").append("<br>");
//            contentBuilder.append("审核理由：<span style=\"color:red;\">(请填写理由)</span><br><br>");
//            contentBuilder.append(this.systemMessage);
//
//            String content = contentBuilder.toString();
//
//            sendMails.sendMailAsync(to, subject, content, uploadFileName, file);
//        } catch (Exception e) {
//            // 处理发送异常的情况
//            logger.error("Exception occurred during sending email: " + e.getMessage(), e);
//        }
//
//    }
//
//    // Handle error messages that don't require entity classes
//    public void sendTeaFeedbackMail(String to, String mailState, String originalMessage) {
//        String subject = getFeedbackMailSubject(originalMessage);
//        String content = getFeedbackMailContent(originalMessage, mailState);
//        sendMails.sendMailAsync(to, subject, content);
//    }
//
//    private String getFeedbackMailContent(String originalMessage, String mailState) {
//        StringBuilder contentBuilder = new StringBuilder();
//        switch (mailState) {
//            case "errLines":
//                contentBuilder.append(this.greetingToUser)
//                        .append(this.hello)
//                        .append(this.reason).append("您的回信正文行数不是3或4行。<br>")
//                        .append(this.solution).append("请仅在邮件中保留要求的内容，多余的内容请删除，缺少的内容请添加。<br>");
//                break;
//            case "wrongNumKeyWords":
//                contentBuilder.append(this.greetingToUser)
//                        .append(this.hello)
//                        .append(this.reason).append("您的回信中可能缺少或多余了<b>成果类型、成果编号、审核结果、审核理由</b>的关键词。<br>")
//                        .append(this.solution).append("请添加相应缺失的关键词或者删除多余的关键词。<br>");
//                break;
//            case "spam":
//                contentBuilder.append(this.greetingToUser)
//                        .append(this.hello)
//                        .append("很高兴收到您的邮件，但很遗憾教学系统暂不支持该功能。<br>")
//                        .append("我们会尽最大努力不断完善，满足您的需求。很抱歉给您造成的不便。<br>")
//                        .append("请使用您在教学系统中登记的邮箱发邮件给<a href=\"mailto:rateAdmin@126.com?\">我</a>，谢谢！<br><br>");
//                break;
//            case "getWrongStart":
//                contentBuilder.append(this.greetingToUser)
//                        .append(this.hello)
//                        .append(this.reason).append("您的回信中行首缺少<b>成果类型：、成果编号：、审核结果：、审核理由：</b>的关键词。<br>")
//                        .append(this.solution).append("请在行首正确的添加<b>成果类型：、成果编号：、审核结果：、审核理由：</b>的关键词。<br>");
//                break;
//            case "wrongOrLeakType":
//                contentBuilder.append(this.greetingToTeacher)
//                        .append(this.hello)
//                        .append(this.reason).append("您所填写的成果类型不为<b>论文、专利、科研获奖、科研项目、学术专著和教材、制造或设计的产品</b>中的任何一项。<br>")
//                        .append(this.solution).append("请在<b>论文、专利、获奖、项目、专著和教材</b>选择一项，修改原邮件中的成果类型。<br>");
//                break;
//            case "notDigitProductionID":
//                contentBuilder.append(this.greetingToTeacher)
//                        .append(this.hello)
//                        .append(this.reason).append("您的成果编号可能未添加，或者成果编号中有英文或者中文等非数字的字符存在。<br>")
//                        .append(this.solution).append("请检查成果编号是否正确。<br>");
//                break;
//            case "wrongProductionResult":
//                contentBuilder.append(this.greetingToTeacher)
//                        .append(this.hello)
//                        .append(this.reason).append("您所填写的审核结果不为<b>通过、驳回</b>中的任何一项。<br>")
//                        .append(this.solution).append("请在<b>通过、驳回</b>选择一项，修改原邮件中的审核结果。<br>");
//                break;
//            case "lackRejectReason":
//                contentBuilder.append(this.greetingToTeacher)
//                        .append(this.hello)
//                        .append(this.reason).append("回信第四行未补充审核理由：<span style=\"color:red;\">(请填写理由)</span>。<br>")
//                        .append(this.solution).append("请在回信的第四行的<span style=\"color:red;\">(请填写理由)</span>中补充审核理由。<br>");
//                break;
//            case "errID":
//                contentBuilder.append(this.greetingToTeacher)
//                        .append(this.hello)
//                        .append(this.reason).append("您的成果编号在数据库中查询失败。<br>")
//                        .append(this.solution).append("请检查成果编号是否正确。<br>");
//                break;
//            default:
//                logger.error("检测到未知状态的邮件！可能是使用了错误的重载函数！");
//        }
//
//        contentBuilder.append(this.formatMessage)
//                .append(this.systemMessage)
//                .append(originalMessage);
//
//        return contentBuilder.toString();
//    }
//
//    private String getFeedbackMailSubject(String originalMessage) {
//        String subject = "";
//        Pattern pattern = Pattern.compile("主题: (.*?)<br>");
//        Matcher matcher = pattern.matcher(originalMessage);
//        if (matcher.find()) {
//            subject = matcher.group(1);
//        }
//        if (subject.trim().isEmpty()) {
//            return "Re:";
//        } else {
//            return "Re: " + subject;
//        }
//    }
//
//    // Handle error messages that require entity classes
//    public <T extends Production> void sendTeaFeedbackMail(T production, String type, String to, String mailState, String originalMessage) throws MessagingException {
//        String correctFormat = getCorrectFormat(production.getID(), type);
//        String infoProduction = getInfoProduction(production);
//        String subject = getFeedbackMailSubject(originalMessage);
//        if (mailState.equals("editPassSuccess") || mailState.equals("editRejectSuccess")) {
//            subject = "教学系统审核论文成果：论文编号" + production.getID() + "审核成功！";
//        }
//        String content = getFeedbackMailContent(production, correctFormat, type, infoProduction, originalMessage, mailState);
//        sendMails.sendMailAsync(to, subject, content);
//    }
//
//    private <T extends Production> String getFeedbackMailContent(T production, String correctFormat, String type, String infoProduction, String originalMessage, String mailState) {
//        StringBuilder contentBuilder = new StringBuilder();
//        switch (mailState) {
//            case "dupEditState":
//                contentBuilder.append(this.greetingToTeacher)
//                        .append(this.hello)
//                        .append(this.reason).append("您所审核的论文编号：").append(production.getID())
//                        .append("目前状态为<b>").append(getProductionCurrentState(production.getState())).append("</b>，因此不需要再次修改其状态。<br>");
//                break;
//            case "IDNotMatchTutorEmail":
//                Student student = studentService.getById((int) (long) production.getStudentID());
//                Teacher teacher = teacherService.getById(student.getTutorID());
//                contentBuilder.append(this.greetingToTeacher)
//                        .append(this.hello)
//                        .append(this.reason).append("邮箱地址错误，或者成果编号错误。<br>")
//                        .append(this.solution).append("请检查成果编号是否正确。<br>");
//                break;
//            case "editPassSuccess":
//                contentBuilder.append(this.greetingToTeacher)
//                        .append(this.hello)
//                        .append("您已经通过回信，成功修改成果编号：").append(production.getID()).append("，成果类型：").append(type)
//                        .append("的状态为：<span style=\\\"color:red;\\\"><b>导师通过</b></span>。<br>");
//                break;
//            case "editRejectSuccess":
//                contentBuilder.append(this.greetingToTeacher)
//                        .append(this.hello)
//                        .append("您已经通过回信，成功修改成果编号：").append(production.getID()).append("，成果类型：").append(type)
//                        .append("的状态为：<span style=\\\"color:red;\\\"><b>导师拒绝</b></span>。<br>");
//                break;
//            default:
//                logger.error("检测到未知状态的邮件！可能是使用了错误的重载函数！");
//        }
//
//        if (mailState.equals("editPassSuccess") || mailState.equals("editRejectSuccess")) {
//            contentBuilder.append(infoProduction)
//                    .append(this.systemMessage);
//
//        } else {
//            contentBuilder.append(infoProduction)
//                    .append(correctFormat)
//                    .append(this.formatMessage)
//                    .append(this.systemMessage)
//                    .append(originalMessage);
//        }
//        return contentBuilder.toString();
//    }
//
//    private String getProductionCurrentState(String state) {
//        String temp = "";
//        switch (state) {
//            case "tea_pass":
//                temp = "导师通过";
//                break;
//            case "tea_reject":
//                temp = "导师驳回";
//                break;
//            case "adm_pass":
//                temp = "管理员通过";
//                break;
//            case "adm_reject":
//                temp = "管理员驳回";
//                break;
//            default:
//                logger.error("该成果目前状态非法！请检查数据库！");
//        }
//        return temp;
//    }
//
//    private <T extends Production> String getInfoProduction(T production) {
//        String pubName = publicationService.selectPublicationById(production.getPublicationID()).getName();
//        StringBuilder infoProduction = new StringBuilder();
//        infoProduction.append("下面是论文的一些基本信息：<br>")
//                .append("论文标题：").append(production.getName()).append("。<br>")
//                .append("发表期刊：").append(pubName).append("<br>")
//                .append("出版年月：").append(production.getYear()).append("-").append(production.getMonth())
//                .append("<br>").append("作者列表：").append(production.getAuthor()).append("<br><br>");
//        return infoProduction.toString();
//    }
//
//    private String getCorrectFormat(Long ID, String type) {
//        StringBuilder correctFormat = new StringBuilder();
//        correctFormat.append("下面是邮件回复的正确格式：<br>")
//                .append("(1) 若审核<b>通过</b>该成果，请在邮件中<b><span style=\"color:red;\">仅保留</span>以下三行并回复</b>。<br>")
//                .append("成果类型：").append(type).append("<br>")
//                .append("成果编号：").append(ID).append("<br>")
//                .append("审核结果：通过<br>")
//                .append("(2) 若<b>驳回</b>该论文，请在邮件中<b><span style=\"color:red;\">仅保留</span>以下四行并回复</b>。<br>")
//                .append("成果类型：").append(type).append("<br>")
//                .append("成果编号：").append(ID).append("<br>")
//                .append("审核结果：驳回<br>")
//                .append("审核理由：<span style=\"color:red;\">(请填写理由)</span><br><br>");
//        return correctFormat.toString();
//    }
//
//
//}
