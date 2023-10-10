package org.sys.rate.utils;


import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.sys.rate.model.*;
import org.sys.rate.service.admin.PaperCommentService;
import org.sys.rate.service.admin.StudentService;
import org.sys.rate.service.admin.TeacherService;
import org.sys.rate.service.mail.EmailErrorLogService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.StandardOpenOption;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@Slf4j
@Service
public class ExportPDF {
    @Resource
    private StudentService studentService;

    @Resource
    private TeacherService teacherService;

    @Resource
    private PaperCommentService paperCommentService;

    @Autowired
    private EmailErrorLogService emailErrorLogService;

    private final static int PRESUMROWS = 17;
    private final static int NEXTPLANROWS = 21;
    private final static int ONEROWMAXCOUNT = 35;
//    private final static String DEST = "src/main/resources/exportFiles/";
//    private final String FONT_PATH_Song = "rate/rateserver/rate-web/src/main/resources/static/template/song.ttf";
//    private final String TEMPLATE_PATH10 = "rate/rateserver/rate-web/src/main/resources/static/template/template_10.pdf";
//    private final String TEMPLATE_PATH20 = "rate/rateserver/rate-web/src/main/resources/static/template/template_20.pdf";

    private final static String DEST = "D:/software/rate/upload/template/exportFiles/";
    private final String FONT_PATH_Song = "D:/software/rate/upload/template/song.ttf";
    private final String TEMPLATE_PATH10 = "D:/software/rate/upload/template/template_10.pdf";
    private final String TEMPLATE_PATH20 = "D:/software/rate/upload/template/template_20.pdf";


    private boolean checkIfNecessaryFilesAndDirectoriesExist() {
        boolean directoryResult = checkIfFileExists(DEST, "导出PDF，目录不存在", "目录 " + DEST + " 不存在！");
        boolean fontResult = checkIfFileExists(FONT_PATH_Song, "导出PDF，模版文件不存在", "字体文件 " + FONT_PATH_Song + " 不存在");
        boolean template10Result = checkIfFileExists(TEMPLATE_PATH10, "导出PDF，模版文件不存在", "模版文件 " + TEMPLATE_PATH10 + " 不存在！！！");
        boolean template20Result = checkIfFileExists(TEMPLATE_PATH20, "导出PDF，模版文件不存在", "模版文件 " + TEMPLATE_PATH20 + " 不存在！！！");

        return directoryResult && fontResult && template10Result && template20Result;
    }

    private boolean checkIfFileExists(String filePath, String errorType, String errorDescription) {
        File file = new File(filePath);
        if (!file.exists()) {
            EmailErrorLog emailErrorLog = new EmailErrorLog();
            emailErrorLog.setErrorType(errorType);
            emailErrorLog.setErrorDescription(errorDescription);
            emailErrorLog.setTimestamp(new Timestamp(System.currentTimeMillis()));
            emailErrorLogService.addEmailErrorLog(emailErrorLog);
            return false;
        }
        return true;
    }

    public boolean generatePDF(HttpServletResponse response, Integer thesisID) throws Exception {
        if (thesisID == null) {
            // 参数校验，确保thesisID有效
            return false;
        }
        Thesis thesis = paperCommentService.getThesisByTID(thesisID);
        Student student = studentService.getByUndergraduateId(thesis.getStudentID());
        Teacher teacher = teacherService.getById(thesis.getTutorID());
        List<PaperComment> paperComments = paperCommentService.selectCommentListStuOrderByNum(thesisID);


        if (!checkIfNecessaryFilesAndDirectoriesExist()) {
            return false;
        }

        String templatePath = paperComments.size() <= 10 ? TEMPLATE_PATH10 : TEMPLATE_PATH20;
        String fileName = student.getName() + "-" + UUID.randomUUID() + ".pdf";

        OutputStream os = null;
        PdfStamper ps = null;
        PdfReader reader = null;

        try {
            os = new FileOutputStream(new File(DEST + fileName));
            reader = new PdfReader(templatePath);
            ps = new PdfStamper(reader, os);
            AcroFields form = ps.getAcroFields();
            BaseFont FontSong = BaseFont.createFont(FONT_PATH_Song, BaseFont.IDENTITY_H, false);
            FontSong.setSubset(true);

            Map<String, Object> model = generatePDFTemplateData(thesis, student, teacher, paperComments);
            fillPDFTemplateFields(form, FontSong, model);
            ps.setFormFlattening(false);
        } catch (Exception e) {
            handlePDFExportError(e, fileName);
            return false;
        } finally {
            ps.close();
            reader.close();
            os.close();
            if (paperComments.size() != 10 && paperComments.size() != 20) {
                String fileNewName = student.getName() + "-" + UUID.randomUUID().toString() + ".pdf";
                removePageFromPDF(DEST + fileName, DEST + fileNewName, paperComments.size() + 1);
                getDownload(response, DEST + fileNewName, false);
            } else {
                getDownload(response, DEST + fileName, false);
            }
            deleteAllFiles();
        }
        return true;
    }


    private void handlePDFExportError(Exception e, String fileName) {
        EmailErrorLog emailErrorLog = new EmailErrorLog();
        emailErrorLog.setErrorType("PDF导出失败");
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        String errorDetails = sw.toString();
        emailErrorLog.setErrorDescription("PDF名字：" + fileName + "\n" + "其他错误信息：" + errorDetails);
        emailErrorLog.setTimestamp(new Timestamp(System.currentTimeMillis()));
        emailErrorLogService.addEmailErrorLog(emailErrorLog);
    }

    private Map<String, Object> generatePDFTemplateData(Thesis thesis, Student student, Teacher teacher, List<PaperComment> paperComments) {
        Map<String, Object> data = new HashMap<>();
        data.put("stuNameFirst", student.getName());
        data.put("stuName", student.getName());
        data.put("stuID", thesis.getStudentNumber());
        data.put("tutorName", teacher.getName());

        for (int i = 0; i < paperComments.size(); i++) {
            data.put("num" + (i + 1), paperComments.get(i).getNum());
            data.put("preSum" + (i + 1), adaptRows(paperComments.get(i).getPreSum(), PRESUMROWS));
            data.put("nextPlan" + (i + 1), adaptRows(paperComments.get(i).getNextPlan(), NEXTPLANROWS));
            data.put("tutorComment" + (i + 1), paperComments.get(i).getTutorComment() == null || paperComments.get(i).getTutorComment().isEmpty() ? " " : paperComments.get(i).getTutorComment());
            data.put("DateStu" + (i + 1), paperComments.get(i).getDateStu());
            data.put("DateTea" + (i + 1), paperComments.get(i).getDateTea() == null || paperComments.get(i).getDateTea().isEmpty() ? "" : paperComments.get(i).getDateTea());
        }

        return data;
    }

    private void fillPDFTemplateFields(AcroFields form, BaseFont FontSong, Map<String, Object> data) throws IOException, DocumentException {
        for (String key : data.keySet()) {
            form.setFieldProperty(key, "textfont", FontSong, null);
            if (key.equals("stuName") || key.equals("num")) {
                form.setFieldProperty(key, "textsize", 12f, null);
            } else if (key.equals("stuNameFirst") || key.equals("stuID") || key.equals("tutorName")) {
                form.setFieldProperty(key, "textsize", 16f, null);
            } else {
                form.setFieldProperty(key, "textsize", 10.5f, null);
            }
            form.setField(key, data.get(key).toString());
        }
    }

    public void getDownload(HttpServletResponse response, String filePath, boolean isOnLine) throws Exception {
        File f = new File(filePath);
        if (!f.exists()) {
            response.sendError(404, "File not found!");
            return;
        }
        BufferedInputStream br = new BufferedInputStream(new FileInputStream(f));
        byte[] buf = new byte[1024];
        int len = 0;

        response.reset(); // 非常重要
        int index = f.getName().indexOf("-");
        String filename = f.getName().substring(0, index) + "毕业设计(论文)记录本.pdf";
        if (isOnLine) {
            // 在线打开方式
            URL u = new URL("file:///" + filePath);
            response.setContentType(u.openConnection().getContentType());
            // 回头考虑使用GBK编码，防止文件名乱码，或者根据浏览器的类型返回各种编码
            response.setHeader("Content-Disposition", "inline; filename=" + java.net.URLEncoder.encode(filename, "UTF-8"));
        } else {
            // 纯下载方式
            response.setContentType("application/pdf;charset=utf-8");
            response.setHeader("Content-Disposition", "attachment; filename=" + java.net.URLEncoder.encode(filename, "UTF-8"));
        }
        OutputStream out = response.getOutputStream();
        while ((len = br.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        br.close();
        out.close();
    }

    public synchronized void deleteAllFiles() {
        File directory = new File(DEST);
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                try (FileChannel channel = FileChannel.open(file.toPath(), StandardOpenOption.WRITE)) {
                    FileLock lock = channel.lock();
                    try {
                        if (file.exists()) {
                            file.delete();
                        }
                    } catch (SecurityException e) {
                        EmailErrorLog emailErrorLog = new EmailErrorLog();
                        emailErrorLog.setErrorType("PDF删除失败");
                        StringWriter sw = new StringWriter();
                        PrintWriter pw = new PrintWriter(sw);
                        e.printStackTrace(pw);
                        String errorDetails = sw.toString();
                        emailErrorLog.setErrorDescription("PDF名字：" + file.getName() + "\n" + "其他错误信息：" + errorDetails);
                        emailErrorLog.setTimestamp(new Timestamp(System.currentTimeMillis()));
                        emailErrorLogService.addEmailErrorLog(emailErrorLog);
                    } finally {
                        lock.release();
                    }
                } catch (IOException e) {
                    EmailErrorLog emailErrorLog = new EmailErrorLog();
                    emailErrorLog.setErrorType("PDF获取锁失败");
                    StringWriter sw = new StringWriter();
                    PrintWriter pw = new PrintWriter(sw);
                    e.printStackTrace(pw);
                    String errorDetails = sw.toString();
                    emailErrorLog.setErrorDescription("PDF名字：" + file.getName() + "\n" + "其他错误信息：" + errorDetails);
                    emailErrorLog.setTimestamp(new Timestamp(System.currentTimeMillis()));
                    emailErrorLogService.addEmailErrorLog(emailErrorLog);
                }
            }
        }
    }

    public void removePageFromPDF(String path, String tempPath, int page) throws Exception {
        PdfReader reader = null;
        FileOutputStream fos = null;
        try {
            reader = new PdfReader(path);
            File tmpNewFile = new File(tempPath);
            fos = new FileOutputStream(tmpNewFile);
            Document d = new Document();
            PdfCopy copy = new PdfCopy(d, fos);
            d.open();
            for (int i = 1; i <= page; ++i) {
                copy.addPage(copy.getImportedPage(reader, i));
            }
            copy.freeReader(reader);
            reader.close();
            d.close();
        } catch (Exception e) {
            EmailErrorLog emailErrorLog = new EmailErrorLog();
            emailErrorLog.setErrorType("PDF删除多余页失败");
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            String errorDetails = sw.toString();
            emailErrorLog.setErrorDescription("PDF路径：" + path + "\n" + "其他错误信息：" + errorDetails);
            emailErrorLog.setTimestamp(new Timestamp(System.currentTimeMillis()));
            emailErrorLogService.addEmailErrorLog(emailErrorLog);
        } finally {
            if (fos != null) {
                fos.close();
            }
        }
    }

    public String adaptRows(String origin, int ROWSLIMIT) {
        int rows = 0, count = 0;
        origin = origin.trim().replace("\n\n", "\n");
        for (int i = 0; i < origin.length(); ++i) {
            if (count > ONEROWMAXCOUNT || origin.charAt(i) == '\n') {
                count = 1;
                rows++;
            } else {
                count++;
            }
        }
        return rows <= ROWSLIMIT ? origin : origin.replace("\n", "");
    }

}