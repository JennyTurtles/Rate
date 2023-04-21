package org.sys.rate.utils;


import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.*;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.sys.rate.model.PaperComment;
import org.sys.rate.model.Student;
import org.sys.rate.model.Teacher;
import org.sys.rate.model.Thesis;
import org.sys.rate.service.admin.StudentService;
import org.sys.rate.service.admin.TeacherService;
import org.sys.rate.service.underFunction.PaperCommentService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author zyk
 * @Return null
 * @description 完成下载功能
 * @date 2023/4/4 16:50
 */
@Service
public class Download {
    @Resource
    StudentService studentService;

    @Resource
    TeacherService teacherService;

    @Resource
    PaperCommentService paperCommentService;

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(Download.class);

    public void preDownload(HttpServletResponse response, Integer thesisID) {
        // 获取thesis的信息
        Thesis thesis = paperCommentService.getThesisByTID(thesisID);
        // 获取学生的信息
        Student student = studentService.getById(thesis.getStudentID());
        // 获取老师的信息
        Teacher teacher = teacherService.getById(student.getTutorID());
        // 获取其余页的信息，是来自papercomment中的信息
        List<PaperComment> paperComments = paperCommentService.selectCommentListStu(thesisID);

        // 与pdf有关的信息
        OutputStream os = null;
        PdfStamper ps = null;
        PdfReader reader = null;
        PdfStamper stamper = null;

        // 定义模板文件的路径
        String TEMP = "";
        if (paperComments.size() <= 10) {
            TEMP = "upload/templete/templete_10.pdf";
        } else {
            TEMP = "upload/templete/templete_20.pdf";
        }
        // 定义输出文件的路径
        String DEST = "upload/paperComment/";
        // 定义字体的路径
        final String FONT_PATH_Song = "upload/templete/song.ttf";

        // 填充表格中的信息
        Map<String, Object> model = new HashMap<>();
        model.put("thesis", thesis);
        model.put("student", student);
        model.put("teacher", teacher);
        model.put("paparcomment", paperComments);

//        String fileName = student.getName() + "-" + "" + ".pdf";
//        String fileName = student.getName() + "-" + System.currentTimeMillis()/1000 + ".pdf";
        String fileName = student.getName() + "-" + UUID.randomUUID().toString() + ".pdf";

        try {
            os = new FileOutputStream(new File(DEST + fileName));
            // 2 读入pdf表单
            reader = new PdfReader(TEMP);
            // 3 根据表单生成一个新的pdf
            ps = new PdfStamper(reader, os);
            // 4 获取pdf表单
            AcroFields form = ps.getAcroFields();
            // 5 给表单添加中文字体
            BaseFont FontSong = BaseFont.createFont(FONT_PATH_Song, BaseFont.IDENTITY_H, false);
            FontSong.setSubset(true);

            // 6查询数据================================================
            // 6.1 封面
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("stuNameFirst", student.getName());
            data.put("stuName", student.getName());
            data.put("stuID", student.getID());
            data.put("tutorName", teacher.getName());

            // 用于控制显示的行数！！！
            final int PRESUMROWS = 18;
            final int NEXTPLANROWS = 22;

            // 6.2 余页
            for (int i = 0; i < paperComments.size(); i++) {
                data.put("num" + (i + 1), paperComments.get(i).getNum());
                data.put("preSum" + (i + 1), adaptRows(paperComments.get(i).getPreSum(), PRESUMROWS));
                data.put("nextPlan" + (i + 1), adaptRows(paperComments.get(i).getNextPlan(), NEXTPLANROWS));
                data.put("tutorComment" + (i + 1), paperComments.get(i).getTutorComment() == null || paperComments.get(i).getTutorComment().isEmpty() ? " " : paperComments.get(i).getTutorComment());
                data.put("DateStu" + (i + 1), paperComments.get(i).getDateStu());
                data.put("DateTea" + (i + 1), paperComments.get(i).getDateTea() == null || paperComments.get(i).getDateTea().isEmpty() ? "" : paperComments.get(i).getDateTea());
            }

            // 7遍历data 给pdf表单表格赋值
            for (String key : data.keySet()) {
                // 进行key判断
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
            ps.setFormFlattening(true);
            System.out.println("===============PDF导出成功=============");
        } catch (Exception e) {
            System.out.println("===============PDF导出失败=============");
            e.printStackTrace();
        } finally {
            try {
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
                deleteAllFiles(DEST);
            } catch (Exception e) {
                e.printStackTrace();
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

    public void deleteAllFiles(String path) {
//        FileHandler fileHandler = new FileHandler("path/to/log/file.log");
        File directory = new File(path);
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                try (FileChannel channel = FileChannel.open(file.toPath(), StandardOpenOption.WRITE)) {
                    // Acquire an exclusive lock on the file
                    FileLock lock = channel.lock();
                    try {
                        // Delete the file
                        file.delete();
                    } catch (SecurityException e) {
                        // handle the exception here
                        logger.error("Unable to delete file: " + file.getName(), e);
                    } finally {
                        // Release the lock on the file
                        lock.release();
                    }
                } catch (IOException e) {
                    // handle the exception here
                    logger.error("Unable to acquire lock on file: " + file.getName(), e);
                }
            }
        }
    }

    public void removePageFromPDF(String path, String tempPath, int page) throws Exception {
        try (FileOutputStream fos = new FileOutputStream(tempPath)) {
            PdfReader reader = new PdfReader(path);
            Document d = new Document();
            PdfCopy copy = new PdfCopy(d, fos);
            d.open();
            for (int i = 1; i <= page; ++i) {
                copy.addPage(copy.getImportedPage(reader, i));
            }
            copy.freeReader(reader);
            d.close();
        } catch (Exception e) {
            throw new Exception("Error while removing page from PDF", e);
        }
    }


    public String adaptRows(String origin, int ROWSLIMIT) {
        int rows = 0, count = 0;
        final int oneRowCount = 35;
        // 0.清理首尾的空白符和\n && 清理多余的\n
        origin = origin.trim();
        origin = origin.replace("\n\n", "\n");
        // 1.统计有多少行数
        for (int i = 0; i < origin.length(); ++i) {
            if (count > oneRowCount || origin.charAt(i) == '\n') {
                count = 0;
                rows++;
            } else {
                count++;
            }
        }
        ++rows;
        // 2.返回修改后的字符串
        if (rows <= ROWSLIMIT) {
            return origin;
        } else {
            return origin.replace("\n", "");
        }
    }

}