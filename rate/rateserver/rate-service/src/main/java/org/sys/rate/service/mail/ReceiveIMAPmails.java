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
     *  完成邮件读取功能
     * */
    public void readMails() throws Exception{
        // 准备连接服务器的会话信息
        Properties props = new Properties();
        props.setProperty("mail.store.protocol", "imap");
        props.setProperty("mail.imap.host", "imap.126.com");
        // IMAP收件服务器地址：imap.126.com 安全类型：SSL 端口号：993;若安全类型选择“无”，则需将端口号修改为 143
        props.setProperty("mail.imap.port", "143");
        props.setProperty("mail.imap.auth.login.disable","true");

        // 解决A3 NO SELECT UNSAFE LOGIN
        HashMap IAM = new HashMap();
        // 带上IMAP ID信息，由key和value组成，例如name，version，vendor，support-email等。
        // 其中value随便写
        IAM.put("name","myname");
        IAM.put("version","1.0.0");
        IAM.put("vendor","myclients");
        IAM.put("supoort-email","testmail@test.com");

        // 创建Session实例对象
        Session session = Session.getInstance(props);
        // 创建IMAP协议的Store对象
//        Store store = session.getStore("imap");
        IMAPStore store = (IMAPStore) session.getStore("imap");
        // 连接邮箱服务器
        store.connect("ratemail@126.com","IOLMDKJXAQJQUAMN");
        store.id(IAM);


        // 获得收件箱
        Folder folder = store.getFolder("INBOX");

        // 以读写的方式打开收件箱
        folder.open(Folder.READ_WRITE);

        // 获取收件箱的·未读·邮件列表
        Message[] messages = folder.getMessages(1,folder.getMessageCount());

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
            MimeMultipart mimeMultipart)  throws MessagingException, IOException{
        String result = "";
        int count = mimeMultipart.getCount();
        for (int i = 0; i < count; i++) {
            BodyPart bodyPart = mimeMultipart.getBodyPart(i);
            if (bodyPart.isMimeType("text/plain")) {
                result = result + "\n" + bodyPart.getContent();
                break; // without break same text appears twice in my tests
            } else if (bodyPart.isMimeType("text/html")) {
                String html = (String) bodyPart.getContent();
                result = result + "\n" ;
            } else if (bodyPart.getContent() instanceof MimeMultipart){
                result = result + getTextFromMimeMultipart((MimeMultipart)bodyPart.getContent());
            }
        }
        return result;
    }

    /**
     * 安全检查，反馈邮件
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
        
        if(ID!=null){
            paper = paperService.getById((int)(long)ID);
        }


        // 若paper_state != commit，则禁止修改
        if(paper!=null && !paper.getState().equals("commit")){
            mailService.sendFeedbackMail(from, ID, "sendNoEditMail");
            return false;
        }

        if(teacherService.getByEmail(from)!=null){
            if(paper!=null){
                student = studentService.getById((int)(long)paper.getStudentID());
                teacher = teacherService.getById(student.getTutorID());
                if(from.equals(teacher.getEmail())) {
                    // 邮箱地址和PaperID匹配
                    // 若不通过需要添加remark
                    if (state.equals("tea_reject")) {
                        if (remark.isEmpty()||remark.equals("(请填写理由)")) {
                            mailService.sendFeedbackMail(from, ID, "sendRemarkMail");
                        } else {
                            mailService.sendFeedbackMail(from, ID, "sendRejtSuccessMail");
                            return true;
                        }
                    }else {
                        mailService.sendFeedbackMail(from, ID, "sendPassSuccessMail");
                        return true;
                    }
                }else{
                    // 邮箱地址正确，paperID在数据库中存在，但是邮箱地址和PaperID不匹配
                    mailService.sendFeedbackMail(from, ID, "sendPIDErrMail");
                }
            }else{
                // 邮箱地址正确，但是paperID在数据库中不存在；
                mailService.sendFeedbackMail(from, ID, "sendPIDErrMail");
            }
        }else{
            // 邮箱地址在数据库中不存在，但是paperID在正文中出现！
            if(ID!=null){
                if(paper!=null){
                    // paperID在数据库中存在
                    student = studentService.getById((int)(long)paper.getStudentID());
                    teacher = teacherService.getById(student.getTutorID());
                    mailService.sendFeedbackMail(from, ID, "sendEmailErrMail");
                }else{
                    // paperID在数据库中不存在
                    mailService.sendFeedbackMail(from, ID, "sendPIDEmailMail");
                }
            }else{
                // 若数据库中没有该地址，且paperID也不存在
                mailService.sendFeedbackMail(from, ID, "sendErrMail");
            }
        }
        return false;

    }

    public void editPaperOperation(Long ID, String remark, String state){
        Paper paper = paperService.getById((int)(long)ID);
        Student student = studentService.getById((int)(long)paper.getStudentID());
        Teacher teacher = teacherService.getById(student.getTutorID());

        String operation = state.equals("tea_pass")?"经邮件回复，教师审核通过":"经邮件回复，教师驳回";
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
     * 解析邮件的内容。
     * @param messages
     * @throws MessagingException
     */
    public void parseMessage(Message ...messages) throws MessagingException, IOException {
        if (messages == null || messages.length < 1) {
            System.out.println("-----暂时没有其他未读的邮件！-----");
//            throw new MessagingException("暂时还没有其他要解析的邮件!");
            return;
        }
        // 开始解析未读邮件
        for(Message message:messages){
            String subject = message.getSubject();
            InternetAddress address = (InternetAddress) message.getFrom()[0];
            String from = address.getAddress();
            String content=getTextFromMessage(message);
            // delete read messages
            message.setFlag(Flags.Flag.DELETED,true);


            if(subject!=null)
                System.out.println("邮件的主题是："+subject+"\t发件人的地址是："+from+"\n邮件正文：" + content+"\t");

            // 正则化查找目标字符串
            String flag_PaperID="", flag_Pass="", flag_remark="";
            String pattern1="(论文编号：)(.*?)(\\D)";
            String pattern2="(审核结果：)(.*?)(通过)";
            String pattern3="(驳回理由：)(.*?)(本邮件由)";
            Pattern pattern11 = Pattern.compile(pattern1);
            Pattern pattern22 = Pattern.compile(pattern2);
            Pattern pattern33 = Pattern.compile(pattern3, Pattern.DOTALL);
            Matcher matcher11 = pattern11.matcher(content);
            Matcher matcher22 = pattern22.matcher(content);
            Matcher matcher33 = pattern33.matcher(content);

            while (matcher11.find()){
                flag_PaperID= matcher11.group(2);
            }
            while (matcher22.find()){
                flag_Pass= matcher22.group(2)+"通过";
            }
            while (matcher33.find()){
                flag_remark= matcher33.group(2);
            }



            Long ID = null;
            String state="";
            if(flag_PaperID!="") {
                ID = Long.parseLong(flag_PaperID);
                state = flag_Pass.equals("通过") ? "tea_pass" : "tea_reject";
            }
            String remark = flag_remark;



            // 进行安全检查！！！
            // 1. 检查paperID和收件人的邮箱地址
            // 1.1 若收件人的邮箱地址存在，则检查正文格式
            // 1.1.1 若正文格式正确，则不发送邮件
            // 1.1.2 若正文格式不正确，则发送“格式错误”
            // 1.2 若收件人的邮箱地址不成功，则检查paperID
            // 1.2.1 若paperID不存在，则发送”非合法用户，请检查邮箱和正文格式“
            // 1.2.2 若paperID存在，则发送“请用以下“邮箱”发送回信”


            // 安全检查
            if(SafeCheck(ID, remark, state, from)) {
                // 修改论文操作的状态
                editPaperOperation(ID, remark, state);
                // 修改论文的状态
                paperService.edit_state(state,ID);
                System.out.println("·····修改论文状态成功！·····");
            }else{
                System.out.println("·····修改论文状态失败！·····");
            }

        }
    }
}
