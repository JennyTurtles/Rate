package org.sys.rate.service.mail;

import com.sun.mail.imap.IMAPStore;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.formula.functions.T;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.sys.rate.model.*;
import org.sys.rate.service.admin.*;

import javax.annotation.Resource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMultipart;
import javax.mail.search.FlagTerm;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
public class ReceiveMails {
    @Resource
    MailService mailService;

    @Resource
    MailToTeacherService mailToTeacherService;

    @Resource
    PaperService paperService;

    @Resource
    StudentService studentService;

    @Resource
    TeacherService teacherService;

    @Resource
    OperationService operationService;


    private String from = null;
    private String password = null;
    private String host = null;
    private final String[] patterns = {"成果类型", "成果编号", "审核结果", "审核理由"};
    private final String[] phrases = {"成果类型：", "成果编号：", "审核结果：", "审核理由："};
    private final String[] allTypes = {"学术论文", "授权专利", "科研获奖", "科研项目", "制定标准", "决策咨询", "学术专著和教材", "制造或设计的产品", "学科竞赛"};
    private final String[] allResult = {"通过", "驳回"};
    private final String REASON_PROMPT_FIXED = "(请填写理由)";


    public void readMails() throws Exception {
        handleNullPointerException();
        if (StringUtils.isBlank(this.from) || StringUtils.isBlank(this.password)) {
            log.error("账号或密码不能为空！");
            return;
        }

        // 准备连接服务器的会话信息
        Properties props = new Properties();
        props.setProperty("mail.store.protocol", "imap");
        props.setProperty("mail.imap.host", this.host);
        props.setProperty("mail.imap.port", "143");
        props.setProperty("mail.imap.auth.login.disable", "true");

        HashMap<String, String> IAM = new HashMap<>(4);
        IAM.put("name", "rate");
        IAM.put("version", "1.0.0");
        IAM.put("vendor", "rate");
        IAM.put("support-email", "testmail@test.com");

        Session session = Session.getInstance(props);
        IMAPStore store = null;
        try {
            store = (IMAPStore) session.getStore("imap");
            store.connect(this.from, this.password);
            store.id(IAM);

            Folder folder = store.getFolder("INBOX");
            folder.open(Folder.READ_WRITE);

            Message[] unreadMessages = folder.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));
            unreadMessages = getFirstNMessages(unreadMessages, 200);

            parseMessage(unreadMessages);
        } catch (MessagingException e) {
            log.error("连接邮件服务器失败！", e);
            throw e;
        } finally {
            if (store != null && store.isConnected()) {
                try {
                    store.close();
                } catch (MessagingException e) {
                    // 处理异常
                    log.error("无法关闭收件箱！");
                }
            }
        }


    }

    private void handleNullPointerException() {
        this.from = mailService.getEmailAddress();
        this.password = mailService.getIMAPVerifyCode();
        this.host = mailService.getIMAPHost();

        if (this.from == null) {
            throw new NullPointerException("from is null");
        }

        if (this.password == null) {
            throw new NullPointerException("password is null");
        }

        if (this.host == null) {
            throw new NullPointerException("host is null");
        }
    }

    public static Message[] getFirstNMessages(Message[] messages, int n) {
        if (messages.length > n) {
            Message[] newMessageArray = new Message[n];
            System.arraycopy(messages, 0, newMessageArray, 0, n);
            return newMessageArray;
        } else {
            return messages;
        }
    }

    public void parseMessage(Message[] messages) throws MessagingException, IOException {
        if (messages.length < 1) {
            return;
        }
        // 开始解析未读邮件
        for (Message message : messages) {
            String subject = message.getSubject();
            String senderEmail = InternetAddress.parse(message.getFrom()[0].toString())[0].getAddress();
            Date sendDate = message.getSentDate();

            // ***特殊情况处理，可能还需要加上163和qq邮箱，因为对系统邮箱只会无意义的回信
            if (senderEmail.equals("Postmaster@126.com")) {
                continue;
            }

            String content = getTextFromMessage(message);
            message.setFlag(Flags.Flag.DELETED, true);
            content = clearFormat(content);
            String originalMessage = getOriginalMessage(subject, senderEmail, sendDate, content);

            // 1.计算关键词出现次数以及位置
            int numLines = sumLinesNum(content);
            if (numLines != 3 && numLines != 4) {
                mailToTeacherService.sendTeaFeedbackMail(senderEmail, "errLines", originalMessage);
                continue;
            }
            if (sendEmailIfKeywordSumWrong(content, numLines, senderEmail, originalMessage)) {
                // 计算关键词出现的位置
                if (!startsWithPhrase(content)) {
                    mailToTeacherService.sendTeaFeedbackMail(senderEmail, "getWrongStart", originalMessage);
                    continue;
                }
            } else {
                continue;
            }
            // 2.获取"正确顺序"的每行内容，前面保证了四行或者三行内容在每行开头出现，并且只出现了一次
            Map<String, String> lines = extractLines(content, numLines);
            // 3.判断每行的内容是否符合要求
            if (!checkLineValue(lines, numLines, senderEmail, originalMessage)) {
                continue;
            }
            // 4.通过泛型来解决重复修改论文状态等问题
            int ID = Integer.parseInt(lines.get(this.phrases[1]));
            //  Java's switch statement does not support using Chinese characters as case labels
            if (lines.get(this.phrases[0]).equals(this.allTypes[0])) {
                // 0.检查ID是否正确
                if (paperService.getById(ID) == null) {
                    mailToTeacherService.sendTeaFeedbackMail(senderEmail, "errID", originalMessage);
                    continue;
                }
                Paper paper = paperService.getById(ID);
                String state = lines.get(this.phrases[2]).equals("通过") ? "tea_pass" : "tea_reject";
                // 1.重复修改论文状态
                if (!checkProductionState(paper)) {
                    mailToTeacherService.sendTeaFeedbackMail(paper, "论文", senderEmail, "dupEditState", originalMessage);
                    continue;
                }
                // 2.教师的邮件和发件人邮件是否匹配
                if (getTutorEmail(paper) == null || getTutorEmail(paper).length() == 0 || !getTutorEmail(paper).equals(senderEmail)) {
                    mailToTeacherService.sendTeaFeedbackMail(paper, "论文", senderEmail, "IDNotMatchTutorEmail", originalMessage);
                    continue;
                }
                // 3.修改成果的操作历史
                if (!editOperation(paper, lines.get(this.phrases[3]), state, "学术论文")) {
                    log.error("修改论文操作历史失败，成果类型：" + lines.get(this.phrases[0]) + "，成果编号：" + lines.get(this.phrases[1]));
                    continue;
                }
                // 4.修改成果的状态
                if (paperService.editState(state, Long.valueOf(ID)) == 0) {
                    log.error("修改论文状态失败!");
                    continue;
                }
                // 5.若成功，将修改后的状态发给老师&&学生
                String editStateSuccess = state.equals("tea_pass") ? "editPassSuccess" : "editRejectSuccess";
                mailToTeacherService.sendTeaFeedbackMail(paper, "论文", senderEmail, editStateSuccess, originalMessage);


            }
            // 在这里添加其他的成果类型

        }
    }

    private String getTextFromMessage(Message message) throws MessagingException, IOException {
        String result = "";
        if (message.isMimeType("text/plain")) {
            result = message.getContent().toString();
        } else if (message.isMimeType("multipart/*")) {
            MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
            result = getTextFromMimeMultipart(mimeMultipart);
        }
        return result;
    }

    private String getTextFromMimeMultipart(MimeMultipart mimeMultipart) throws MessagingException, IOException {
        String result = "";
        int count = mimeMultipart.getCount();
        for (int i = 0; i < count; i++) {
            BodyPart bodyPart = mimeMultipart.getBodyPart(i);
            if (bodyPart.isMimeType("text/plain")) {
                result = result + "\n" + bodyPart.getContent();
                break; // without break same text appears twice in my tests
            } else if (bodyPart.isMimeType("text/html")) {
                String html = (String) bodyPart.getContent();
                result = result + "\n";
            } else if (bodyPart.getContent() instanceof MimeMultipart) {
                result = result + getTextFromMimeMultipart((MimeMultipart) bodyPart.getContent());
            }
        }
        return result;
    }

    public <T extends Production> boolean editOperation(T production, String remark, String state, String type) {
        Student student = studentService.getById(production.getStudentId());
        Teacher teacher = teacherService.getById(student.getTutorID());

        String operationName = state.equals("tea_pass") ? "经邮件回复，教师审核通过" : "经邮件回复，教师驳回";
        Date date = new Date();
        // 修改论文操作状态
        Operation operation = new Operation();
        operation.setOperatorRole("teacher");
        operation.setOperatorId(teacher.getId());
        operation.setOperatorName(teacher.getName());
        operation.setProdType(type);
        operation.setProdId(production.getId());
        operation.setTime((Timestamp) date);
        operation.setOperationName(operationName);
        operation.setState(state);
        operation.setRemark(remark);

        return operationService.insertOper(operation) != 0 ? true : false;
    }

    private <T extends Production> String getTutorEmail(T production) {
        Student student = studentService.getById(production.getStudentId().intValue());
        if (student == null) {
            return null;
        } else {
            Teacher teacher = teacherService.getById(student.getTutorID());
            if (teacher == null) {
                return null;
            } else {
                return teacher.getEmail();
            }
        }
    }

    public <T extends Production> boolean checkProductionState(T production) {
        return production.getState().equals("commit");
    }

    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean checkLineValue(Map<String, String> lines, int numLines, String senderEmail, String originalMessage) {

        // judge the line1
        if (!Arrays.asList(this.allTypes).contains(lines.get(this.phrases[0]))) {
            mailToTeacherService.sendTeaFeedbackMail(senderEmail, "wrongOrLeakType", originalMessage);
            return false;
        }

        // judge the line2
        if (lines.get(this.phrases[1]).isEmpty() || !isNumeric(lines.get(this.phrases[1]))) {
            mailToTeacherService.sendTeaFeedbackMail(senderEmail, "notDigitProductionID", originalMessage);
            return false;
        }

        // extra conditions, judge the senderEmail, or this can be judge in the mailToTeacherService, cause use <T> is not so good
        // and you may see if ID and type is correct, it's hardly to say the email is a spam email
        // and I found if u check the tutor email later, it can avoid other mistakes that will occur in the other 2 lines

        // judge the line3
        if (!Arrays.asList(this.allResult).contains(lines.get(this.phrases[2]))) {
            mailToTeacherService.sendTeaFeedbackMail(senderEmail, "wrongProductionResult", originalMessage);
            return false;
        }

        // extra judge
        if (lines.get(this.phrases[2]).equals("驳回") && numLines == 3) {
            mailToTeacherService.sendTeaFeedbackMail(senderEmail, "wrongNumKeyWords", originalMessage);
        }

        // judge the line4
        if (numLines == 4) {
            // clear the reason
            if (lines.get(this.phrases[3]).startsWith(this.REASON_PROMPT_FIXED)) {
                lines.put(this.phrases[3], lines.get(this.phrases[3]).substring(this.REASON_PROMPT_FIXED.length()));
            }
            if (lines.get(this.phrases[3]).isEmpty() || lines.get(this.phrases[3]).length() == 0) {
                mailToTeacherService.sendTeaFeedbackMail(senderEmail, "lackRejectReason", originalMessage);
                return false;
            }
        }
        return true;
    }

    public Map<String, String> extractLines(String content, int numLines) {
        assert numLines <= 4;
        content += '\n';
        // The HashMap implementation of the Map interface allows null keys and values.
        // the polymorphism feature of Java
        Map<String, String> lines = new HashMap<>(numLines);

        for (int i = 0; i < numLines; i++) {
            int start = content.indexOf(this.phrases[i]) + this.phrases[i].length();
            int end = content.indexOf("\n", start);
            String value = content.substring(start, end);
            lines.put(this.phrases[i], value);
        }
        if (numLines == 3) {
            lines.put(this.phrases[3], "");
        }

        return lines;
    }

    public boolean startsWithPhrase(String str) {
        String[] sentences = str.split("\\n");
        for (String sentence : sentences) {
            boolean startsWithPhrase = false;
            for (String phrase : this.phrases) {
                if (sentence.startsWith(phrase)) {
                    startsWithPhrase = true;
                    break;
                }
            }
            if (!startsWithPhrase) {
                return false;
            }
        }
        return true;
    }

    private boolean sendEmailIfKeywordSumWrong(String content, int numLines, String senderEmail, String originalMessage) {
        switch (countOccurrences(content, numLines)) {
            case 0:
                return true;
            case 1:
                mailToTeacherService.sendTeaFeedbackMail(senderEmail, "wrongNumKeyWords", originalMessage);
                return false;
            case -1:
                mailToTeacherService.sendTeaFeedbackMail(senderEmail, "spam", originalMessage);
                return false;
            default:
                log.error("countOccurrences(content, numLines) got wrong num!");
                return false;
        }
    }

    public int countOccurrences(String input, int linesNum) {
        // 记录计数器
        Map<String, Integer> counts = new HashMap<>(4);
        for (String pattern : this.patterns) {
            counts.put(pattern, 0);
        }

        // 匹配模式字符串并计数
        Matcher matcher = Pattern.compile(String.join("|", this.patterns)).matcher(input);
        while (matcher.find()) {
            counts.put(matcher.group(), counts.get(matcher.group()) + 1);
        }

        int numMatches = 0;
        if (linesNum == 3) {
            counts.remove(this.patterns[3]);
        }
        for (int count : counts.values()) {
            if (count == 1) {
                numMatches++;
            }
            if (count != 1) {
                return 1;
            }
        }

        if (numMatches == linesNum) {
            return 0;
        } else if (numMatches == 0) {
            return -1;
        } else {
            return 1;
        }
    }

    private String getOriginalMessage(String subject, String senderEmail, Date sendDate, String content) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String sendTime = formatter.format(sendDate);
        String senderName = senderEmail.substring(0, senderEmail.indexOf("@"));
        // rateMail特指本系统使用的管理员邮箱，之后会实时更改！！！
        String rateMail = "ratemail@126.com";
        String rateMailName = rateMail.substring(0, rateMail.indexOf("@"));
        String replyContent = "------------------ 原始邮件 ------------------<br>" +
                String.format("<p style=\"background-color:#efefef;\">发件人: \"%s\" <%s>;\n发送时间: %s;\n收件人: \"%s\"<%s>;\n主题: %s\n</p>", senderName, senderEmail, sendTime, rateMailName, rateMail, subject) +
                content;
        return replyContent.replaceAll("\n", "<br>");
    }

    private int sumLinesNum(String content) {
        String[] lines = content.split("\\n");
        return lines.length;
    }

    private String clearFormat(String content) {
        return content.trim().replaceAll("\r", "").replaceAll("\n+", "\n");
    }


    public boolean editOperation(Paper production, String remark, String state, String type) {
        Student student = studentService.getById(Math.toIntExact(production.getStudentID()));
        Teacher teacher = teacherService.getById(student.getTutorID());

        String operationName = state.equals("tea_pass") ? "经邮件回复，教师审核通过" : "经邮件回复，教师驳回";
        Date date = new Date();
        // 修改论文操作状态
        Operation operation = new Operation();
        operation.setOperatorRole("teacher");
        operation.setOperatorId(teacher.getId());
        operation.setOperatorName(teacher.getName());
        operation.setProdType(type);
        operation.setProdId(Math.toIntExact(production.getID()));
        operation.setTime((Timestamp) date);
        operation.setOperationName(operationName);
        operation.setState(state);
        operation.setRemark(remark);

        return operationService.insertOper(operation) != 0 ? true : false;
    }

    private String getTutorEmail(Paper production) {
        Student student = studentService.getById(production.getStudentID().intValue());
        if (student == null) {
            return null;
        } else {
            Teacher teacher = teacherService.getById(student.getTutorID());
            if (teacher == null) {
                return null;
            } else {
                return teacher.getEmail();
            }
        }
    }

    public boolean checkProductionState(Paper production) {
        return production.getState().equals("commit");
    }

}
