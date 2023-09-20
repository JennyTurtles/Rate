package org.sys.rate.service.mail;


import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.sys.rate.model.*;
import org.sys.rate.service.admin.StudentService;
import org.sys.rate.service.admin.TeacherService;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
public class MailToTeacherService {

    @Resource
    private StudentService studentService;

    @Resource
    private TeacherService teacherService;

    @Resource
    private SendMails sendMails;

    @Resource
    private SendMailContentService sendMailContentService;

    @Resource
    private ProductionService productionService;

    @Resource
    private EmailErrorLogService emailErrorLogService;

    @Resource
    private MailService mailService;

    private static final Map<String, Integer> typeMap = new HashMap<>();

    static {
        typeMap.put("学术论文", 1);
        typeMap.put("授权专利", 2);
        typeMap.put("科研获奖", 3);
        typeMap.put("横向科研项目", 4);
        typeMap.put("纵向科研项目", 10);
        typeMap.put("制定标准", 5);
        typeMap.put("决策咨询", 6);
        typeMap.put("学术专著和教材", 7);
        typeMap.put("产品应用", 8);
        typeMap.put("学科竞赛", 9);
    }

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月");

    private final String greetingToUser = "亲爱的用户：<br>";
    private final String greetingToTeacher = "尊敬的老师：<br>";
    private final String hello = "您好！<br>";
    private final String reason = "错误原因：";
    private final String solution = "解决方法：";
    private final String formatMessage = "请参照以上解决方法，重新回复邮件。<br><br>";
    private final String systemMessage = "本邮件由东华大学计算机学院教学系统自动发出，如有疑问，请联系管理员</a>！<br><br>";

    // 当提交或者修改成功，会调用下面的代码
    public <T extends Production> void sendTeaCheckMail(T production, String type, String addOrUpdate) throws FileNotFoundException {
        File file = new File(production.getUrl());
        String isFileEmpty = "";

        if (!file.exists()) {
            isFileEmpty = "无，可以驳回后请学生重新上传";
        }
        SendMailContent sendMailContent = sendMailContentService.getSendMailContent(production.getStudentId());
        if (sendMailContent == null) {
            return;
        }

        Mail mail = mailService.handleNullPointerException();

        if (StrUtil.isEmpty(sendMailContent.getTeacherEmail())) {
            EmailErrorLog emailErrorLog = new EmailErrorLog();
            emailErrorLog.setErrorType("发件错误");
            emailErrorLog.setErrorDescription("对应的导师没有邮箱地址");
            emailErrorLog.setSenderEmail(mail.getEmailAddress());
            emailErrorLog.setRecipientEmail(sendMailContent.getTeacherEmail());
            emailErrorLog.setSubject("");
            emailErrorLog.setBody("");
            emailErrorLog.setTimestamp(new Timestamp(System.currentTimeMillis()));
            emailErrorLogService.addEmailErrorLog(emailErrorLog);
            return;
        }

        String subject = "请在教学系统中审核" + sendMailContent.getStudentName() + "的" + type + "成果，成果编号：" + production.getId();

        StringBuilder contentBuilder = new StringBuilder();
        contentBuilder.append("尊敬的").append(sendMailContent.getTeacherName()).append("老师：<br>");
        contentBuilder.append("您好！<br>");
        contentBuilder.append("<b>您的学生").append(sendMailContent.getStudentName()).append("已经在系统中" + addOrUpdate + "成果申报。</b><br>");
        contentBuilder.append("成果标题：").append(production.getName()).append("<br>");
        try {

            switch (typeMap.get(type)) {
                case 2:
                    contentBuilder.append("状态年月：").append(sdf.format(production.getDate())).append("<br>");
                    contentBuilder.append("参与人：").append(production.getAuthor()).append("<br>");
                    break;
                case 3:
                case 9:
                    contentBuilder.append("获奖年月：").append(sdf.format(production.getDate())).append("<br>");
                    contentBuilder.append("获奖人：").append(production.getAuthor()).append("<br>");
                    break;
                case 4:
                case 10:
                    contentBuilder.append("立项时间：").append(sdf.format(production.getStartDate())).append("<br>");
                    contentBuilder.append("参与人：").append(production.getAuthor()).append("<br>");
                    break;
                case 5:
                case 6:
                    contentBuilder.append("制定年月：").append(sdf.format(production.getDate())).append("<br>");
                    contentBuilder.append("制定人：").append(production.getAuthor()).append("<br>");
                    break;
                case 7:
                    contentBuilder.append("完成年月：").append(sdf.format(production.getDate())).append("<br>");
                    contentBuilder.append("完成人：").append(production.getAuthor()).append("<br>");
                    contentBuilder.append("出版社：").append(production.getPublisher()).append("<br>");
                    break;
                case 8:
                    contentBuilder.append("完成年月：").append(sdf.format(production.getDate())).append("<br>");
                    contentBuilder.append("完成人：").append(production.getAuthor()).append("<br>");
                    break;

                default:
                    // 默认情况
                    break;
            }
            contentBuilder.append("提交时间：").append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))).append("<br>");
            contentBuilder.append("证明材料：").append(StringUtil.isEmpty(isFileEmpty) ? "请查看邮件附件" : isFileEmpty).append("<br><br>");
            contentBuilder.append("<b>您可以登录<a href=\"http://106.15.36.190:8081/#/Teacher/Login\" target=\"_blank\">教学系统</a>进行审核，也可以直接回复本邮件完成审核。</b><br>");
            contentBuilder.append("如果回复本邮件，方式如下：<br>");
            contentBuilder.append("(1) 若审核<b>通过</b>该成果，请在邮件中<span style=\"color:red;\">仅保留</span>以下三行并回复。<br>");
            contentBuilder.append("成果类型：").append(type).append("<br>");
            contentBuilder.append("成果编号：").append(production.getId()).append("<br>");
            contentBuilder.append("审核结果：").append("通过").append("<br>");
            contentBuilder.append("(2) 若<b>驳回</b>该成果，请在邮件中<span style=\"color:red;\">仅保留</span>以下四行并回复。<br>");
            contentBuilder.append("成果类型：").append(type).append("<br>");
            contentBuilder.append("成果编号：").append(production.getId()).append("<br>");
            contentBuilder.append("审核结果：").append("驳回").append("<br>");
            contentBuilder.append("审核理由：<span style=\"color:red;\">(请填写理由)</span><br><br>");
            contentBuilder.append(this.systemMessage);
            sendMails.sendMailAsync(sendMailContent.getTeacherEmail(), subject, contentBuilder.toString(), file);
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
            emailErrorLog.setBody(contentBuilder.toString() + "\n" + production.toString() + type + addOrUpdate);
            emailErrorLog.setTimestamp(new Timestamp(System.currentTimeMillis()));

            emailErrorLogService.addEmailErrorLog(emailErrorLog);
        }


    }

    // !导师回信审核有较为普通的错误，会经过这个代码
    public void sendTeaFeedbackMail(String to, String mailState, String originalMessage) {
        String subject = getFeedbackMailSubject(originalMessage);
        String content = getFeedbackMailContent(originalMessage, mailState);
        sendMails.sendMailAsync(to, subject, content, null);
    }

    private String getFeedbackMailContent(String originalMessage, String mailState) {
        StringBuilder contentBuilder = new StringBuilder();
        switch (mailState) {
            case "errLines":
                contentBuilder.append(this.greetingToUser)
                        .append(this.hello)
                        .append(this.reason).append("您的回信正文行数不是3或4行。<br>")
                        .append(this.solution).append("请仅在邮件中保留要求的内容，多余的内容请删除，缺少的内容请添加。<br>");
                break;
            case "wrongNumKeyWords":
                contentBuilder.append(this.greetingToUser)
                        .append(this.hello)
                        .append(this.reason).append("您的回信中可能缺少或多余了<b>成果类型、成果编号、审核结果、审核理由</b>的关键词。<br>")
                        .append(this.solution).append("请添加相应缺失的关键词或者删除多余的关键词。<br>");
                break;
            case "spam":
                contentBuilder.append(this.greetingToUser)
                        .append(this.hello)
                        .append("很高兴收到您的邮件，但很遗憾教学系统暂不支持该功能。<br>")
                        .append("我们会尽最大努力不断完善，满足您的需求。很抱歉给您造成的不便。<br>")
                        .append("请使用您在教学系统中登记的邮箱发邮件给<a href=\"mailto:rateAdmin@126.com?\">我</a>，谢谢！<br><br>");
                break;
            case "getWrongStart":
                contentBuilder.append(this.greetingToUser)
                        .append(this.hello)
                        .append(this.reason).append("您的回信中行首缺少<b>成果类型：、成果编号：、审核结果：、审核理由：</b>的关键词。<br>")
                        .append(this.solution).append("请在行首正确的添加<b>成果类型：、成果编号：、审核结果：、审核理由：</b>的关键词。<br>");
                break;
            case "wrongOrLeakType":
                contentBuilder.append(this.greetingToTeacher)
                        .append(this.hello)
                        .append(this.reason).append("您所填写的成果类型不为<b>学术论文、授权专利、科研获奖、纵向科研项目、横向科研项目、制定标准、决策咨询、学术专著和教材、产品应用、学科竞赛</b>中的任何一项。<br>")
                        .append(this.solution).append("请在<b>学术论文、授权专利、科研获奖、纵向科研项目、横向科研项目、制定标准、决策咨询、学术专著和教材、产品应用、学科竞赛</b>选择一项，修改原邮件中的成果类型。<br>");
                break;
            case "notDigitProductionID":
                contentBuilder.append(this.greetingToTeacher)
                        .append(this.hello)
                        .append(this.reason).append("您的成果编号可能未添加，或者成果编号中有英文或者中文等非数字的字符存在。<br>")
                        .append(this.solution).append("请检查成果编号是否正确。<br>");
                break;
            case "wrongProductionResult":
                contentBuilder.append(this.greetingToTeacher)
                        .append(this.hello)
                        .append(this.reason).append("您所填写的审核结果不为<b>通过、驳回</b>中的任何一项。<br>")
                        .append(this.solution).append("请在<b>通过、驳回</b>选择一项，修改原邮件中的审核结果。<br>");
                break;
            case "lackRejectReason":
                contentBuilder.append(this.greetingToTeacher)
                        .append(this.hello)
                        .append(this.reason).append("回信第四行未补充审核理由：<span style=\"color:red;\">(请填写理由)</span>。<br>")
                        .append(this.solution).append("请在回信的第四行的<span style=\"color:red;\">(请填写理由)</span>中补充审核理由。<br>");
                break;
            case "errID":
                contentBuilder.append(this.greetingToTeacher)
                        .append(this.hello)
                        .append(this.reason).append("您的成果编号在数据库中查询失败。<br>")
                        .append(this.solution).append("请检查成果编号是否正确。<br>");
                break;
            default:
                break;
        }

        contentBuilder.append(this.formatMessage)
                .append(this.systemMessage)
                .append(originalMessage);

        return contentBuilder.toString();
    }

    private String getFeedbackMailSubject(String originalMessage) {
        String subject = "";
        Pattern pattern = Pattern.compile("主题: (.*?)<br>");
        Matcher matcher = pattern.matcher(originalMessage);
        if (matcher.find()) {
            subject = matcher.group(1);
        }
        if (subject.trim().isEmpty()) {
            return "Re:";
        } else {
            return "Re: " + subject;
        }
    }

    // !导师回信审核有关系到id、email等实体类的错误 or 回信审核成功，会经过这个代码
    public <T extends Production> void sendTeaFeedbackMail(T production, String type, String to, String mailState, String originalMessage) {
        String correctFormat = getCorrectFormat(production.getId(), type);
//        String infoProduction = getInfoProduction(production, type);
        String infoProduction = "";
        String subject = getFeedbackMailSubject(originalMessage);
        if (mailState.equals("editPassSuccess") || mailState.equals("editRejectSuccess")) {
            subject = "教学系统审核" + type + "成果：" + type + "编号" + production.getId() + "审核成功！";
        }
        String content = getFeedbackMailContent(production, correctFormat, type, infoProduction, originalMessage, mailState);
        sendMails.sendMailAsync(to, subject, content, null);
    }

    private <T extends Production> String getFeedbackMailContent(T production, String correctFormat, String type, String infoProduction, String originalMessage, String mailState) {
        StringBuilder contentBuilder = new StringBuilder();

        switch (mailState) {
            case "dupEditState":
                contentBuilder.append(this.greetingToTeacher)
                        .append(this.hello)
                        .append(this.reason).append("您所审核的" + type + "编号：").append(production.getId())
                        .append("目前状态为<b>").append(getProductionCurrentState(production.getState())).append("</b>，因此不需要再次修改其状态。<br>");
                break;
            case "IDNotMatchTutorEmail":
                Student student = studentService.getById((int) (long) production.getStudentId());
                Teacher teacher = teacherService.getById(student.getTutorID());
                contentBuilder.append(this.greetingToTeacher)
                        .append(this.hello)
                        .append(this.reason).append("成果编号错误，或者邮箱地址错误。<br>")
                        .append(this.solution).append("请检查成果编号和邮箱地址是否正确。<br>");
                break;
            case "editPassSuccess":
                contentBuilder.append(this.greetingToTeacher)
                        .append(this.hello)
                        .append("您已经通过回信，成功修改成果编号：").append(production.getId()).append("，成果类型：").append(type)
                        .append("的状态为：<span style=\\\"color:red;\\\"><b>导师通过</b></span>。<br>");
                break;
            case "editRejectSuccess":
                contentBuilder.append(this.greetingToTeacher)
                        .append(this.hello)
                        .append("您已经通过回信，成功修改成果编号：").append(production.getId()).append("，成果类型：").append(type)
                        .append("的状态为：<span style=\\\"color:red;\\\"><b>导师拒绝</b></span>。<br>");
                break;
            default:
                break;
        }

        if (mailState.equals("editPassSuccess") || mailState.equals("editRejectSuccess") || mailState.equals("dupEditState")) {
            contentBuilder.append(infoProduction)
                    .append(this.systemMessage);

        } else {
            contentBuilder.append(infoProduction)
                    .append(correctFormat)
                    .append(this.formatMessage)
                    .append(this.systemMessage)
                    .append(originalMessage);
        }
        return contentBuilder.toString();
    }

    private String getProductionCurrentState(String state) {
        String temp = "";
        switch (state) {
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
                break;
        }
        return temp;
    }

    private <T extends Production> String getInfoProduction(T production, String type) {
        StringBuilder infoProduction = new StringBuilder();

        if ("纵向科研项目".equals(type)) {
            infoProduction.append("下面是原成果的一些基本信息：<br>")
                    .append("成果标题：").append(production.getName()).append("。<br>")
                    .append("立项年月：").append(sdf.format(production.getStartDate())).append("<br>")
                    .append("作者列表：").append(production.getAuthor()).append("<br><br>");
        } else {
            infoProduction.append("下面是原成果的一些基本信息：<br>")
                    .append("成果标题：").append(production.getName()).append("。<br>")
                    .append("发表年月：").append(sdf.format(production.getDate())).append("<br>")
                    .append("作者列表：").append(production.getAuthor()).append("<br><br>");
        }
        return infoProduction.toString();
    }

    private String getCorrectFormat(Integer ID, String type) {
        StringBuilder correctFormat = new StringBuilder();
        correctFormat.append("下面是邮件回复的正确格式：<br>")
                .append("(1) 若审核<b>通过</b>该成果，请在邮件中<b><span style=\"color:red;\">仅保留</span>以下三行并回复</b>。<br>")
                .append("成果类型：").append(type).append("<br>")
                .append("成果编号：").append(ID).append("<br>")
                .append("审核结果：通过<br>")
                .append("(2) 若<b>驳回</b>该成果，请在邮件中<b><span style=\"color:red;\">仅保留</span>以下四行并回复</b>。<br>")
                .append("成果类型：").append(type).append("<br>")
                .append("成果编号：").append(ID).append("<br>")
                .append("审核结果：驳回<br>")
                .append("审核理由：<span style=\"color:red;\">(请填写理由)</span><br><br>");
        return correctFormat.toString();
    }

    // !下面是paper相关的邮件代码

    public void sendTeaCheckMail(Paper production, String type, String addOrUpdate) {
        try {
            File file = new File(production.getUrl());
            String isFileEmpty = "";

            if (!file.exists()) {
                isFileEmpty = "无，可以驳回后请学生重新上传";
            }

            SendMailContent sendMailContent = sendMailContentService.getSendMailContent(Math.toIntExact(production.getStudentID()));
            if (sendMailContent == null) {
                return;
            }

            Mail mail = mailService.handleNullPointerException();

            if (StrUtil.isEmpty(sendMailContent.getTeacherEmail())) {
                EmailErrorLog emailErrorLog = new EmailErrorLog();
                emailErrorLog.setErrorType("发件错误");
                emailErrorLog.setErrorDescription("对应的导师没有邮箱地址");
                emailErrorLog.setSenderEmail(mail.getEmailAddress());
                emailErrorLog.setRecipientEmail(sendMailContent.getTeacherEmail());
                emailErrorLog.setSubject("");
                emailErrorLog.setBody("");
                emailErrorLog.setTimestamp(new Timestamp(System.currentTimeMillis()));
                emailErrorLogService.addEmailErrorLog(emailErrorLog);
                return;
            }

            String pubName = productionService.getPublicationName(production.getPublicationID());

            String subject = "请在教学系统中审核" + sendMailContent.getStudentName() + "的学术论文成果，成果编号：" + production.getID();

            StringBuilder contentBuilder = new StringBuilder();
            contentBuilder.append("尊敬的").append(sendMailContent.getTeacherName()).append("老师：<br>");
            contentBuilder.append("您好！<br>");
            contentBuilder.append("<b>您的学生").append(sendMailContent.getStudentName()).append("已经在系统中" + addOrUpdate + "成果申报。</b><br>");
            contentBuilder.append("成果标题：").append(production.getName()).append("<br>");
            contentBuilder.append("发表期刊：").append(pubName).append("<br>");
            contentBuilder.append("发表年月：").append(production.getYear()).append("年").append(production.getMonth()).append("月<br>");
            contentBuilder.append("作者列表：").append(production.getAuthor()).append("<br>");
            contentBuilder.append("提交时间：").append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))).append("<br>");
            contentBuilder.append("证明材料：").append(StringUtil.isEmpty(isFileEmpty) ? "请查看邮件附件" : isFileEmpty).append("<br><br>");
            contentBuilder.append("<b>您可以登录<a href=\"http://106.15.36.190:8081/#/Teacher/Login\" target=\"_blank\">教学系统</a>进行审核，也可以直接回复本邮件完成审核。</b><br>");
            contentBuilder.append("如果回复本邮件，方式如下：<br>");
            contentBuilder.append("(1) 若审核<b>通过</b>该成果，请在邮件中<span style=\"color:red;\">仅保留</span>以下三行并回复。<br>");
            contentBuilder.append("成果类型：").append(type).append("<br>");
            contentBuilder.append("成果编号：").append(production.getID()).append("<br>");
            contentBuilder.append("审核结果：").append("通过").append("<br>");
            contentBuilder.append("(2) 若<b>驳回</b>该论文，请在邮件中<span style=\"color:red;\">仅保留</span>以下四行并回复。<br>");
            contentBuilder.append("成果类型：").append(type).append("<br>");
            contentBuilder.append("成果编号：").append(production.getID()).append("<br>");
            contentBuilder.append("审核结果：").append("驳回").append("<br>");
            contentBuilder.append("审核理由：<span style=\"color:red;\">(请填写理由)</span><br><br>");
            contentBuilder.append(this.systemMessage);

            sendMails.sendMailAsync(sendMailContent.getTeacherEmail(), subject, contentBuilder.toString(), file);
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
        }

    }

    public void sendTeaFeedbackMail(Paper production, String type, String to, String mailState, String originalMessage) {
        String correctFormat = getCorrectFormat(Math.toIntExact(production.getID()), type);
//        String infoProduction = getInfoProduction(production, type);
        String infoProduction = "";
        String subject = getFeedbackMailSubject(originalMessage);
        if (mailState.equals("editPassSuccess") || mailState.equals("editRejectSuccess")) {
            subject = "教学系统审核学术论文成果：学术论文编号" + production.getID() + "审核成功！";
        }
        String content = getFeedbackMailContent(production, correctFormat, type, infoProduction, originalMessage, mailState);
        sendMails.sendMailAsync(to, subject, content, null);
    }

    private String getFeedbackMailContent(Paper production, String correctFormat, String type, String infoProduction, String originalMessage, String mailState) {
        StringBuilder contentBuilder = new StringBuilder();
        switch (mailState) {
            case "dupEditState":
                contentBuilder.append(this.greetingToTeacher)
                        .append(this.hello)
                        .append(this.reason).append("您所审核的论文编号：").append(production.getID())
                        .append("目前状态为<b>").append(getProductionCurrentState(production.getState())).append("</b>，因此不需要再次修改其状态。<br>");
                break;
            case "IDNotMatchTutorEmail":
                Student student = studentService.getById((int) (long) production.getStudentID());
                Teacher teacher = teacherService.getById(student.getTutorID());
                contentBuilder.append(this.greetingToTeacher)
                        .append(this.hello)
                        .append(this.reason).append("成果编号，或者邮箱地址错误错误。<br>")
                        .append(this.solution).append("请检查成果编号和邮箱地址是否正确。<br>");
                break;
            case "editPassSuccess":
                contentBuilder.append(this.greetingToTeacher)
                        .append(this.hello)
                        .append("您已经通过回信，成功修改成果编号：").append(production.getID()).append("，成果类型：").append(type)
                        .append("的状态为：<span style=\\\"color:red;\\\"><b>导师通过</b></span>。<br>");
                break;
            case "editRejectSuccess":
                contentBuilder.append(this.greetingToTeacher)
                        .append(this.hello)
                        .append("您已经通过回信，成功修改成果编号：").append(production.getID()).append("，成果类型：").append(type)
                        .append("的状态为：<span style=\\\"color:red;\\\"><b>导师拒绝</b></span>。<br>");
                break;
            default:
                break;
        }

        if (mailState.equals("editPassSuccess") || mailState.equals("editRejectSuccess") || mailState.equals("dupEditState")) {
            contentBuilder.append(infoProduction)
                    .append(this.systemMessage);

        } else {
            contentBuilder.append(infoProduction)
                    .append(correctFormat)
                    .append(this.formatMessage)
                    .append(this.systemMessage)
                    .append(originalMessage);
        }
        return contentBuilder.toString();
    }

    private String getInfoProduction(Paper production, String type) {
        StringBuilder infoProduction = new StringBuilder();
        infoProduction.append("下面是原成果的一些基本信息：<br>")
                .append("成果标题：").append(production.getName()).append("。<br>")
                .append("发表年月：").append(production.getYear()).append("年").append(production.getMonth()).append("月<br>")
                .append("作者列表：").append(production.getAuthor()).append("<br><br>");
        return infoProduction.toString();
    }
}
