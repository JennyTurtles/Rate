package org.sys.rate.utils;


import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.Image;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.sys.rate.model.*;
import org.sys.rate.service.admin.PaperCommentService;
import org.sys.rate.service.admin.StudentService;
import org.sys.rate.service.admin.TeachersService;
import org.sys.rate.service.mail.EmailErrorLogService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.StandardOpenOption;
import java.sql.Timestamp;
import java.time.LocalDateTime;
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
    private TeachersService teachersService;

    @Resource
    private PaperCommentService paperCommentService;

    @Autowired
    private EmailErrorLogService emailErrorLogService;

    private final static int PRESUMROWS = 17;
    private final static int NEXTPLANROWS = 21;
    private final static int ONEROWMAXCOUNT = 35;

    private String uploadPath;
    private String DEST;
    private String FONT_PATH_Song;
    private String TEMPLATE_PATH10;
    private String TEMPLATE_PATH20;

    public ExportPDF() {
        this.uploadPath = new File("files").getAbsolutePath() + "\\template\\";
        this.DEST = uploadPath + "exportFiles\\";
        this.FONT_PATH_Song = uploadPath + "song.ttf";
        this.TEMPLATE_PATH10 = uploadPath + "template_10.pdf";
        this.TEMPLATE_PATH20 = uploadPath + "template_20.pdf";
    }


    public boolean generatePDF(HttpServletResponse response, Integer thesisID) throws Exception {
        if (thesisID == null) {
            // 参数校验，确保thesisID有效
            return false;
        }
        Thesis thesis = null;
        UnderGraduate student = null;
        Teachers teacher = null;
        List<PaperComment> paperComments = null;
        try {
            thesis = paperCommentService.getThesisByTID(thesisID);
            student = studentService.getByUndergraduateId(thesis.getStudentID());
            teacher = teachersService.getById(thesis.getTutorID());
            paperComments = paperCommentService.selectCommentListStuOrderByNum(thesisID);
        } catch (Exception e) {
            throw new RuntimeException(e);
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
            fillPDFTemplateFields(ps, form, FontSong, model);
            ps.setFormFlattening(true);
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

    private Map<String, Object> generatePDFTemplateData(Thesis thesis, UnderGraduate student, Teachers teacher, List<PaperComment> paperComments) {
        Map<String, Object> data = new HashMap<>();
        data.put("stuNameFirst", student.getName());
        data.put("stuName", student.getName());
        data.put("stuID", thesis.getStudentNumber());
        if(!thesis.getName().isEmpty()) {
            data.put("thesisName1", thesis.getName().length() <= 15 ? thesis.getName() : thesis.getName().substring(0, 15));
            data.put("thesisName2", thesis.getName().length() <= 15 ? "" : " " + thesis.getName().substring(15));
        }

        data.put("tutorName", teacher.getName());
        data.put("stuSign", student.getSign());
        data.put("teaSign", teacher.getSign());


        for (int i = 0; i < paperComments.size(); i++) {
            data.put("num" + (i + 1), paperComments.get(i).getNum());
            data.put("preSum" + (i + 1), adaptRows(paperComments.get(i).getPreSum(), PRESUMROWS));
            data.put("nextPlan" + (i + 1), adaptRows(paperComments.get(i).getNextPlan(), NEXTPLANROWS));
            data.put("tutorComment" + (i + 1), paperComments.get(i).getTutorComment() == null || paperComments.get(i).getTutorComment().isEmpty() ? " " : paperComments.get(i).getTutorComment());
            data.put("DateStu" + (i + 1), paperComments.get(i).getDateStu());
            data.put("DateTea" + (i + 1), paperComments.get(i).getDateTea() == null || paperComments.get(i).getDateTea().isEmpty() ? "" : paperComments.get(i).getDateTea());
        }
        data.put("year", LocalDateTime.now().getYear() + "");
        data.put("month", LocalDateTime.now().getMonthValue() + "");
        data.put("day", LocalDateTime.now().getDayOfMonth() + "");

        return data;
    }

    private void fillPDFTemplateFields(PdfStamper ps, AcroFields form, BaseFont FontSong, Map<String, Object> data) throws IOException, DocumentException {
        for (String key : data.keySet()) {
            form.setFieldProperty(key, "textfont", FontSong, null);
            if (key.equals("stuName") || key.equals("num")) {
                form.setFieldProperty(key, "textsize", 12f, null);
            } else if (key.equals("stuNameFirst") || key.equals("stuID") || key.equals("tutorName") || key.equals("year") || key.equals("month") || key.equals("day") || key.equals("thesisName1") || key.equals("thesisName2")) {
                form.setFieldProperty(key, "textsize", 16f, null);
            } else {
                form.setFieldProperty(key, "textsize", 10.5f, null);
            }
            form.setField(key, data.get(key) != null ? data.get(key).toString() : "");
        }

        File stuSign = new File((String) data.get("stuSign"));
        form.setField("stuSign", "");
        if (stuSign.exists()) {
            // 读图片
            List<AcroFields.FieldPosition> positions = form.getFieldPositions("stuSign");
            for (AcroFields.FieldPosition position : positions) {
                int pageNo = position.page;
                Rectangle signRect = position.position;
                float x = signRect.getLeft();
                float y = signRect.getBottom();
                PdfContentByte under = ps.getOverContent(pageNo);
                Image image = Image.getInstance(stuSign.getAbsolutePath());
//                image.scaleToFit(signRect.getWidth(), signRect.getHeight());
                image.scaleAbsolute(signRect.getWidth(), signRect.getHeight());

                image.setAbsolutePosition(x, y);
                under.addImage(image);
            }
        }

        File teaSign = new File((String) data.get("teaSign"));
        form.setField("teaSign", "");
        if (teaSign.exists()) {
            // 读图片
            List<AcroFields.FieldPosition> positions = form.getFieldPositions("teaSign");
            for (AcroFields.FieldPosition position : positions) {
                int pageNo = position.page;
                Rectangle signRect = position.position;
                float x = signRect.getLeft();
                float y = signRect.getBottom();
                PdfContentByte under = ps.getOverContent(pageNo);
                Image image = Image.getInstance(teaSign.getAbsolutePath());
//                image.scaleToFit(signRect.getWidth(), signRect.getHeight());
                image.scaleAbsolute(signRect.getWidth(), signRect.getHeight());

                image.setAbsolutePosition(x, y);
                under.addImage(image);
            }
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