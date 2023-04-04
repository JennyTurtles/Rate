package org.sys.rate.controller.admin;

import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import org.springframework.web.bind.annotation.*;
import org.sys.rate.config.JsonResult;
import org.sys.rate.model.PaperComment;
import org.sys.rate.model.Student;
import org.sys.rate.model.Teacher;
import org.sys.rate.model.Thesis;
import org.sys.rate.service.admin.StudentService;
import org.sys.rate.service.admin.TeacherService;
import org.sys.rate.service.underFunction.PaperCommentService;
import org.sys.rate.utils.Download;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/paperComment/basic")
public class PaperCommentController {
    @Resource
    PaperCommentService paperCommentService;

    @Resource
    StudentService studentService;

    @Resource
    TeacherService teacherService;

    @Resource
    Download download;

    // 根据stuID和thesisID和num获取某一次评论
    @GetMapping("/getOneComment")
    public JsonResult getOneComment(int stuID, int num) {
        return new JsonResult(paperCommentService.selectCommentOne(stuID, num));
    }

    // 根据stuID和thesisID获取关于thesisID的所有评论 + 排序
    @GetMapping("/getAllComment")
    public JsonResult<List> list(int thesisID) {
        return new JsonResult(paperCommentService.selectCommentList(thesisID));
    }

    @GetMapping("/getAllCommentStu")
    public JsonResult<List> listStu(int thesisID) {
        return new JsonResult(paperCommentService.selectCommentListStu(thesisID));
    }

    // 根据thesisID获取thesisName
    @GetMapping("/getThesis")
    public JsonResult getThesis(int stuID) {
        return new JsonResult(paperCommentService.getThesis(stuID));
    }

    // 更新导师的评价时间和评价
    @PostMapping("/updateTea")
    @ResponseBody
    public JsonResult updateTeaComment(PaperComment paperComment) throws MessagingException {
//        System.out.println("新增的dateStu:" + paperComment.getDateStu());
        return new JsonResult(paperCommentService.updateTeaComment(paperComment));
    }


    /**
     * 新增保存评论
     */
    @PostMapping("/add")
    @ResponseBody
    public JsonResult addSave(PaperComment paperComment) throws MessagingException {
        Integer res = paperCommentService.insertComment(paperComment);
        System.out.println("新增的dateStu:" + paperComment.getDateStu());
        return new JsonResult(res);

    }

    /**
     * 修改保存评论
     */
    @PostMapping("/edit")
    @ResponseBody
    public JsonResult editSave(PaperComment paperComment) throws MessagingException {
        System.out.println("新增的dateStu:" + paperComment.getDateStu());
        return new JsonResult(paperCommentService.updateStuComment(paperComment));
    }


    /**
     * 删除评论
     */
    @DeleteMapping("/remove/{num}/{thesisID}")
    public JsonResult deleteCommentById(@PathVariable("num") int num, @PathVariable("thesisID") int thesisID) {
        // code to delete the resource
        Integer res = paperCommentService.deleteCommentById(num, thesisID);
        return new JsonResult(res);
    }

    /**
     * 通过学号返回该生最新的毕业论文ID
     */
    @GetMapping("/getTID")
    public JsonResult getTIDbySID(int stuID) throws MessagingException {
        Integer thesisID = paperCommentService.getTIDbySID(stuID);
        return new JsonResult(thesisID);
    }

    /**
     * 通过老师ID返回所有的StuID
     */
    @GetMapping("/getStuID")
    public JsonResult<List> getStuIDbyTeaID(int teaID) throws MessagingException {
        List<Student> stuID = paperCommentService.getStuIDbyTeaID(teaID);
        return new JsonResult<>(stuID);
    }

    /**
     * 通过teaID返回其下所有的stuID和最新的毕设ID  getStuThesis
     */
    @GetMapping("/getStuThesis")
    public JsonResult<List> getStuThesis(int teaID) throws MessagingException {
        List<Student> Stu = paperCommentService.getStuThesis(teaID);
        return new JsonResult<>(Stu);
    }




    /**
     * 通过thesisID返回需要打印的pdf文件
     */

    @ResponseBody
    @GetMapping("/exportPDF")

    public void exportDataPDF(HttpServletResponse response, @RequestParam Integer thesisID) throws Exception {
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
        final String FONT_PATH_Hei = "upload/templete/simhei.ttf";
        final String FONT_PATH_Song = "upload/templete/simsun.ttc,1";
        final String FONT_PATH_Kai = "upload/templete/GB2312.ttf";

        // 填充表格中的信息
        Map<String, Object> model = new HashMap<>();
        model.put("thesis", thesis);
        model.put("student", student);
        model.put("teacher", teacher);
        model.put("paparcomment", paperComments);

        String fileName = student.getName() + "毕业设计(论文)记录本" + ".pdf";
        DEST = DEST + fileName;
        try {
//            直接下载
//            getPDF.exportPDF(response, model);
            os = new FileOutputStream(new File(DEST));
            // 2 读入pdf表单
            reader = new PdfReader(TEMP);
            // 3 根据表单生成一个新的pdf,os是本地， response.getOutputStream()是网络
//            ps = new PdfStamper(reader, response.getOutputStream());
            ps = new PdfStamper(reader, os);
            // 4 获取pdf表单
            AcroFields form = ps.getAcroFields();
            // 5给表单添加中文字体
            BaseFont FontKai = BaseFont.createFont(FONT_PATH_Kai, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
            BaseFont FontSong = BaseFont.createFont(FONT_PATH_Song, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
            BaseFont FontHei = BaseFont.createFont(FONT_PATH_Hei, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);

//            BaseFont bf = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
//            BaseFont bf = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
//            BaseFont bf = BaseFont.createFont("C:\\Windows\\Fonts\\simsun.ttc,0", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
//            Font font = new Font(bf, 8);
//            ArrayList<BaseFont> fontList = new ArrayList<BaseFont>(1);
//            fontList.add(bf);
//            form.setSubstitutionFonts(fontList);
            // 6查询数据================================================
            // 6.1 封面
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("stuNameFirst", student.getName());
            data.put("stuName", student.getName());
            data.put("stuID", student.getID());
            data.put("tutorName", teacher.getName());

            // 用于控制显示的行数！！！
            final int PRESUMROWS = 18;
            final int NEXTPLANROWS = 23;

            // 6.2 余页
            for (int i = 0; i < paperComments.size(); i++) {
                data.put("num" + (i + 1), paperComments.get(i).getNum());
                data.put("preSum" + (i + 1), download.adaptRows(paperComments.get(i).getPreSum(), PRESUMROWS));
                data.put("nextPlan" + (i + 1), download.adaptRows(paperComments.get(i).getNextPlan(), NEXTPLANROWS));
                data.put("tutorComment" + (i + 1), paperComments.get(i).getTutorComment() == null || paperComments.get(i).getTutorComment().isEmpty() ? " " : paperComments.get(i).getTutorComment());
                data.put("DateStu" + (i + 1), paperComments.get(i).getDateStu());
                data.put("DateTea" + (i + 1), paperComments.get(i).getDateTea() == null || paperComments.get(i).getDateTea().isEmpty() ? "" : paperComments.get(i).getDateTea());
//                data.put("DateTea" + (i+1), paperComments.get(i).getDateTea());
            }


            // 7遍历data 给pdf表单表格赋值
            for (String key : data.keySet()) {
                // 进行key判断
                if (key.equals("stuNameFirst") || key.equals("stuID") || key.equals("tutorName")) {
                    form.setFieldProperty(key, "textfont", FontHei, null);
                    form.setFieldProperty(key, "textsize", 16f, null);
                    form.setFieldProperty(key, "alignment", Element.ALIGN_CENTER, null);
                    form.setField(key, data.get(key).toString());
                } else if (key.equals("stuName") || key.equals("num")) {
                    form.setFieldProperty(key, "textfont", FontKai, null);
                    form.setFieldProperty(key, "textsize", 12f, null);
//                    form.setFieldProperty(key, "alignment", Element.ALIGN_CENTER, null);
                    form.setField(key, data.get(key).toString());
                } else {
                    form.setFieldProperty(key, "textfont", FontSong, null);
                    form.setFieldProperty(key, "textsize", 10.5f, null);
//                    form.setFieldProperty(key, "leading", 22.0f, null);
                    form.setField(key, data.get(key).toString());
                    // Set the line spacing for a specific field
                }

            }
            ps.setFormFlattening(true);
            System.out.println("===============PDF导出成功=============");

        } catch (Exception e) {
            System.out.println("===============PDF导出失败=============");
            e.printStackTrace();
        } finally {
            try {
//                ps.setFullCompression();
                ps.close();
                reader.close();
                os.close();
                String DEST2 = "upload/paperComment/";
                if (paperComments.size() != 10) {
                    String fileNewName = student.getName() + "毕业设计(论文)记录本 " + ".pdf";
                    download.removePageFromPDF(DEST, DEST2 + fileNewName, paperComments.size() + 1);
                    download.getDownload(response, DEST2 + fileNewName, false);
                } else {
                    download.getDownload(response, DEST2 + fileName, false);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
