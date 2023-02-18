package org.sys.rate.service.mail;

import com.sun.mail.imap.IMAPStore;
import org.springframework.stereotype.Service;
import org.sys.rate.model.Paper;
import org.sys.rate.model.PaperOper;
import org.sys.rate.model.Student;
import org.sys.rate.model.Teacher;
import org.sys.rate.service.admin.PaperOperService;
import org.sys.rate.service.admin.PaperService;
import org.sys.rate.service.admin.StudentService;
import org.sys.rate.service.admin.TeacherService;

import javax.annotation.Resource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ReceiveIMAPmails {
    @Resource
    PaperService paperService;

    @Resource
    StudentService studentService;

    @Resource
    TeacherService teacherService;

    @Resource
    PaperOperService paperOperService;

    @Resource
    MailService mailService;


    /**
     * 完成邮件读取功能
     */
    public void readMails() throws Exception {
        // 准备连接服务器的会话信息
        Properties props = new Properties();
        props.setProperty("mail.store.protocol", "imap");
        props.setProperty("mail.imap.host", "imap.126.com");
        // IMAP收件服务器地址：imap.126.com 安全类型：SSL 端口号：993;若安全类型选择“无”，则需将端口号修改为 143
        props.setProperty("mail.imap.port", "143");
        props.setProperty("mail.imap.auth.login.disable", "true");

        // 解决A3 NO SELECT UNSAFE LOGIN
        HashMap IAM = new HashMap();
        // 带上IMAP ID信息，由key和value组成，例如name，version，vendor，support-email等。
        // 其中value随便写
        IAM.put("name", "myname");
        IAM.put("version", "1.0.0");
        IAM.put("vendor", "myclients");
        IAM.put("supoort-email", "testmail@test.com");

        // 创建Session实例对象
        Session session = Session.getInstance(props);
        // 创建IMAP协议的Store对象
//        Store store = session.getStore("imap");
        IMAPStore store = (IMAPStore) session.getStore("imap");
        // 连接邮箱服务器
        store.connect("ratemail@126.com", "IOLMDKJXAQJQUAMN");
        store.id(IAM);


        // 获得收件箱
        Folder folder = store.getFolder("INBOX");

        // 以读写的方式打开收件箱
        folder.open(Folder.READ_WRITE);

        // 获取收件箱的·未读·邮件列表
        Message[] messages = folder.getMessages(folder.getMessageCount() - folder.getUnreadMessageCount() + 1, folder.getMessageCount());

        // 解析邮件
        parseMessage(messages);

//        // 打印不同状态的邮件数量
//        System.out.println("收件箱中共有"+messages.length+"封邮件！");
//        System.out.println("收件箱中共有"+folder.getUnreadMessageCount()+"封未读邮件！");
//        System.out.println("收件箱中共有"+folder.getNewMessageCount()+"封新邮件！");
//        System.out.println("收件箱中共有"+folder.getDeletedMessageCount()+"封已经删除邮件！");
//


        folder.close(false);    // 关闭邮件夹对象；若删除邮件则及时更新Folder,打开.
        // If you just close the folder, you can't do anything more to the folder, but you can still operate on the store.
        store.close();             // 关闭连接对象
        System.out.println("-----解封未读邮件成功！-----");

    }

    /**
     * 获取邮件的内容
     *
     * @param message
     * @return
     * @throws MessagingException
     * @throws IOException
     */
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

    private String getTextFromMimeMultipart(
            MimeMultipart mimeMultipart) throws MessagingException, IOException {
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

    /**
     * 安全检查，反馈邮件
     *
     * @param ID
     * @param remark
     * @param state
     * @param from
     * @throws MessagingException
     */
    public boolean SafeCheck(Long ID, String remark, String state, String from) throws MessagingException {
        Paper paper = null;
        Student student;
        Teacher teacher;

        if (ID != null) {
            paper = paperService.getById((int) (long) ID);
        }


        // 若paper_state != commit，则禁止修改
        if (paper != null && !paper.getState().equals("commit")) {
            mailService.sendFeedbackMail(from, ID, "sendNoEditMail");
            return false;
        }

        if (teacherService.getByEmail(from) != null) {
            if (paper != null) {
                student = studentService.getById((int) (long) paper.getStudentID());
                teacher = teacherService.getById(student.getTutorID());
                if (from.equals(teacher.getEmail())) {
                    // 邮箱地址和PaperID匹配
                    // 若不通过需要添加remark
                    if (state.equals("tea_reject")) {
                        if (remark.isEmpty() || remark.equals("(请填写理由)")) {
                            mailService.sendFeedbackMail(from, ID, "sendRemarkMail");
                        } else {
                            mailService.sendFeedbackMail(from, ID, "sendRejtSuccessMail");
                            return true;
                        }
                    } else {
                        mailService.sendFeedbackMail(from, ID, "sendPassSuccessMail");
                        return true;
                    }
                } else {
                    // 邮箱地址正确，paperID在数据库中存在，但是邮箱地址和PaperID不匹配
                    mailService.sendFeedbackMail(from, ID, "sendPIDErrMail");
                }
            } else {
                // 邮箱地址正确，但是paperID在数据库中不存在；
                mailService.sendFeedbackMail(from, ID, "sendPIDErrMail");
            }
        } else {
            // 邮箱地址在数据库中不存在，但是paperID在正文中出现！
            if (ID != null) {
                if (paper != null) {
                    // paperID在数据库中存在
                    student = studentService.getById((int) (long) paper.getStudentID());
                    teacher = teacherService.getById(student.getTutorID());
                    mailService.sendFeedbackMail(from, ID, "sendEmailErrMail");
                } else {
                    // paperID在数据库中不存在
                    mailService.sendFeedbackMail(from, ID, "sendPIDEmailMail");
                }
            } else {
                // 若数据库中没有该地址，且paperID也不存在
                mailService.sendFeedbackMail(from, ID, "sendErrMail");
            }
        }
        return false;

    }

    public void editPaperOperation(Long ID, String remark, String state) {
        Paper paper = paperService.getById((int) (long) ID);
        Student student = studentService.getById((int) (long) paper.getStudentID());
        Teacher teacher = teacherService.getById(student.getTutorID());

        String operation = state.equals("tea_pass") ? "经邮件回复，教师审核通过" : "经邮件回复，教师驳回";
        Date date = new Date();
        Timestamp ts = new Timestamp(date.getTime());
        // 修改论文操作状态
        PaperOper paperOper = new PaperOper();
        paperOper.setOperatorRole("teacher");
        paperOper.setOperatorName(teacher.getName());
        paperOper.setPaperID(ID);
        paperOper.setPaperName(paper.getName());
        paperOper.setPubID(paper.getPublicationID());
        paperOper.setPubName(paper.getPubName());
        paperOper.setTime(ts);
        paperOper.setOperation(operation);
        paperOper.setState(state);
        paperOper.setRemark(remark);


        paperOperService.insertPaperOper(paperOper);
        System.out.println("---------------论文状态修改成功！--------------------");
    }


    /**
     * 返回sub在str中出现的次数
     *
     * @param str
     * @param sub
     * @return
     */
    public static int numOccurrences(String str, String sub) {
        int count = 0;
        int index = 0;
        while ((index = str.indexOf(sub, index)) != -1) {
            count++;
            index += sub.length();
        }
        return count;
    }


    /**
     * 判断字符串是否含有非数字的字符，若全是则输出true
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }


    /**
     * 解析邮件的内容。
     *
     * @param messages
     * @throws MessagingException
     */
    public void parseMessage(Message... messages) throws MessagingException, IOException {
        Paper paper = null;
        Student student;
        Teacher teacher;

        if (messages == null || messages.length < 1) {
            System.out.println("-----暂时没有其他未读的邮件！-----");
//            throw new MessagingException("暂时还没有其他要解析的邮件!");
            return;
        }
        // 开始解析未读邮件
        for (Message message : messages) {
            String subject = message.getSubject();
            InternetAddress address = (InternetAddress) message.getFrom()[0];
            String from = address.getAddress();
            String content = getTextFromMessage(message);
            // delete read messages
            message.setFlag(Flags.Flag.DELETED, true);


            System.out.println("邮件的主题是：" + subject + "\t发件人的地址是：" + from + "\n邮件正文：" + content + "\t");

            Long ID = null;
            String type = "", remark = "", state = "";
            String[] allTypes = {"论文", "专利", "奖励", "科研项目", "学术专著和教材", "制造或设计的产品"};
            boolean flag = false;


            // 0.检测是否为垃圾邮件
            if ((numOccurrences(content, "成果类型：") == 0)
                    && (numOccurrences(content, "成果编号：") == 0)
                    && (numOccurrences(content, "审核结果：") == 0)) {
                //判断为垃圾邮件
                mailService.sendFeedbackMail(from, ID, "junkMail");
                return;
            } else if ((numOccurrences(content, "成果类型：") > 1)
                    || (numOccurrences(content, "成果编号：") > 1)
                    || (numOccurrences(content, "审核理由：") > 1)) {
                //判断为垃圾邮件
                mailService.sendFeedbackMail(from, ID, "duplicateVarMail");
                return;
            } else {
                //判断不为垃圾邮件
                // 0.0 修剪content,去除首尾空行
//                content = content.replaceFirst("\n\n", "\n");
                content = content.trim();
                content = content.replace("\r","");
                String[] lines = content.split("\n");
                String line1 = lines[0], line2 = lines[1], line3 = lines[2], line4 = "";

                // 保障基本上四句话都要大于5个字符
                if (lines.length > 3) {
                    line4 = lines[3];
                    if(line1.length()<5 || line2.length()<5 || line3.length()<5 || line4.length()<5){
                        mailService.sendFeedbackMail(from, ID, "less5Mail");
                        return;
                    }
                }else{
                    if(line1.length()<5 || line2.length()<5 || line3.length()<5){
                        mailService.sendFeedbackMail(from, ID, "less5Mail");
                        return;
                    }
                }

                for (int i = 3; i < lines.length; i++) {
                    remark += lines[i] + "\n";
                }


                if (remark.length() > 5)
                    remark = remark.substring(5);
                if (line4.length() >= 12 && line4.substring(5, 12).equals("(请填写理由)")) {
                    remark = remark.substring(7);
                }
                remark.trim();
                // 0.1 首先提取成果编号
                // 第二行其余内容是否为纯数字？
                if (isNumeric(line2.substring(5)) && !line2.substring(5).equals("")) {
                    ID = Long.parseLong(line2.substring(5));
                } else {
                    mailService.sendFeedbackMail(from, ID, "IDNotDigitErrMail");
                    return;
                }
                // 0.2 判断ID是否在数据库中
                paper = paperService.getById((int) (long) ID);
                if (paper != null) {
                    // 是否和邮箱地址匹配？
                    // ID 在数据库中
                    student = studentService.getById((int) (long) paper.getStudentID());
                    teacher = teacherService.getById(student.getTutorID());
                    if (!from.equals(teacher.getEmail())) {
                        if (teacherService.getByEmail(from) != null) {
                            mailService.sendFeedbackMail(from, ID, "matchErrMail");
                        } else {
                            mailService.sendFeedbackMail(from, ID, "emailErrMail");
                        }
                        return;
                    }
                } else {
                    if (teacherService.getByEmail(from) != null) {
                        mailService.sendFeedbackMail(from, ID, "IDNotExistErrMail");
                    } else {
                        mailService.sendFeedbackMail(from, ID, "registerMail");
                    }
                    return;
                }
                // 0.3 再判断是否有成果编号字样
                if (!line2.substring(0, 5).equals("成果编号：")) {
                    mailService.sendFeedbackMail(from, ID, "lackMail_2");
                    return;
                }

                // 1.2 判断第一行
                if (line1.substring(0, 5).equals("成果类型：")) {
                    type = line1.substring(5);
                    for (String b : allTypes) {
                        if (type.equals(b)) {
                            flag = true;
                            break;
                        }
                    }
                    if (!flag) {
                        mailService.sendFeedbackMail(from, ID, "typeErrMail");
                        return;
                    }
                } else {
                    mailService.sendFeedbackMail(from, ID, "lackMail_1");
                    return;
                }

                // 3.1 判断第三行
                if (line3.substring(0, 5).equals("审核结果：")) {
                    state = line3.substring(5);
                    if (state.equals("通过") || state.equals("驳回")) {
                        if (!paper.getState().equals("commit")) {
                            mailService.sendFeedbackMail(from, ID, "dupStateErrMail");
                            return;
                        }
                    } else {
                        mailService.sendFeedbackMail(from, ID, "stateErrMail");
                        return;
                    }
                } else {
                    mailService.sendFeedbackMail(from, ID, "lackMail_3");
                    return;
                }

                // 4.1 判断第四行
                if (state.equals("通过")) {
                    mailService.sendFeedbackMail(from, ID, "sendPassMail");
                } else {
                    if (line4.substring(0, 5).equals("审核理由：")) {
                        if (remark.length() > 1) {
                            mailService.sendFeedbackMail(from, ID, "sendRejectMail");
                        } else {
                            mailService.sendFeedbackMail(from, ID, "lackMail_reason");
                            return;
                        }
                    } else {
                        mailService.sendFeedbackMail(from, ID, "lackMail_4");
                        return;
                    }
                }

                // 5. 无错误
                state = state.equals("通过") ? "tea_pass" : "tea_reject";
                // 修改论文操作的状态
                editPaperOperation(ID, remark, state);
                // 修改论文的状态
                paperService.edit_state(state, ID);
                System.out.println("·····修改论文状态成功！·····");


            }


// 安全检查
//            if (SafeCheck(ID, remark, state, from)) {
//                // 修改论文操作的状态
//                editPaperOperation(ID, remark, state);
//                // 修改论文的状态
//                paperService.edit_state(state, ID);
//                System.out.println("·····修改论文状态成功！·····");
//            } else {
//                System.out.println("·····修改论文状态失败！·····");
//            }


        }
    }
}


// 正则化
// 正则化查找目标字符串
//String flag_type="", flag_PaperID="", flag_Pass="", flag_remark="";
//    String pattern0="(成果类型：)(.*?)(\n)";
//    String pattern1="(成果编号：)(.*?)(\n)";
//    String pattern2="(审核结果：)(.*?)(通过)";
//    String pattern3="(审核理由：)(.*?)(\n\n)";
//    Pattern pattern11 = Pattern.compile(pattern1,1);
//    Pattern pattern22 = Pattern.compile(pattern2,1);
//    Pattern pattern33 = Pattern.compile(pattern3, Pattern.DOTALL);
//    Matcher matcher11 = pattern11.matcher(content);
//    Matcher matcher22 = pattern22.matcher(content);
//    Matcher matcher33 = pattern33.matcher(content);
//
//            if (matcher11.find()){
//                    flag_PaperID= matcher11.group(2);
//                    }
//                    if (matcher22.find()){
//                    flag_Pass= matcher22.group(2)+"通过";
//                    }
//                    if(flag_Pass.equals("不通过")) {
//                    if (matcher33.find()) {
//                    flag_remark = matcher33.group(2);
//                    }
//                    }
//
//
//
//                    Long ID = null;
//                    String state="";
//                    if(flag_PaperID!="") {
//                    ID = Long.parseLong(flag_PaperID);
//                    state = flag_Pass.equals("通过") ? "tea_pass" : "tea_reject";
//                    }
//                    String remark = flag_remark;