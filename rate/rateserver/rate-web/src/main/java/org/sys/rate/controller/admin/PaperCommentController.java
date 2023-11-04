package org.sys.rate.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.sys.rate.config.JsonResult;
import org.sys.rate.model.EmailErrorLog;
import org.sys.rate.model.PaperComment;
import org.sys.rate.model.RespBean;
import org.sys.rate.model.Student;
import org.sys.rate.service.admin.PaperCommentService;
import org.sys.rate.service.admin.ThesisService;
import org.sys.rate.service.mail.EmailErrorLogService;
import org.sys.rate.utils.ExportPDF;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.Timestamp;
import java.util.List;

@RestController
@RequestMapping("/paperComment/basic")
public class PaperCommentController {
    @Resource
    private PaperCommentService paperCommentService;

    @Resource
    private ThesisService thesisService;

    @Autowired
    private EmailErrorLogService emailErrorLogService;

    @Resource
    private ExportPDF exportPDF;

    // 根据thesisID返回学生和老师是否上传图片
    // 1:都上传，0:学生没有上传，-1：教师没有上传,-2:都没有上传
    @GetMapping("/checkSign")
    public RespBean checkSign(Integer thesisID) {
        int res = paperCommentService.checkSign(thesisID);
        return RespBean.ok("", res);
    }
    // 根据教师ID返回学生和老师是否上传图片


    // 根据stuID和thesisID和num获取某一次评论
    @GetMapping("/getOneComment")
    public JsonResult getOneComment(int stuID, int num) {
        return new JsonResult(paperCommentService.selectCommentOne(stuID, num));
    }

    // 根据stuID和thesisID获取关于thesisID的所有评论 + 排序
    @GetMapping("/getAllComment")
    public JsonResult<List> list(int thesisID) {
        return new JsonResult(paperCommentService.selectCommentListTea(thesisID));
    }

    @GetMapping("/getAllCommentStu")
    public JsonResult<List> listStu(Integer thesisID) {
        return new JsonResult(paperCommentService.selectCommentListStu(thesisID));
    }

    @GetMapping("/getThesis")
    public JsonResult getThesis(int stuID, int year, int month) {
        return new JsonResult(paperCommentService.getThesis(stuID, year, month));
    }

    // 更新导师的评价时间和评价
    @PostMapping("/updateTea")
    public JsonResult updateTeaComment(@RequestBody PaperComment paperComment) {
        return new JsonResult(paperCommentService.updateTeaComment(paperComment));
    }


    /**
     * 新增保存评论
     */
    @PostMapping("/add")
    public JsonResult addSave(PaperComment paperComment) throws MessagingException {
        Integer res = paperCommentService.insertComment(paperComment);
        return new JsonResult(res);

    }

    /**
     * 修改保存评论
     */
    @PostMapping("/edit")
    public JsonResult editSave(PaperComment paperComment) throws MessagingException {
        return new JsonResult(paperCommentService.updateStuComment(paperComment));
    }


    /**
     * 删除评论
     */
    @DeleteMapping("/remove/{num}/{thesisID}")
    public JsonResult deleteCommentById(@PathVariable("num") int num, @PathVariable("thesisID") int thesisID) {
        Integer res = paperCommentService.deleteCommentById(num, thesisID);
        return new JsonResult(res);
    }

    /**
     * 通过学号返回该生最新的毕业论文ID
     */
    @GetMapping("/getThesisID")
    public JsonResult getThesisID(int stuID) throws MessagingException {
        Integer thesisID = paperCommentService.getThesisID(stuID);
        return new JsonResult(thesisID);
    }


    /**
     * 通过teaID返回其下所有的stuID和最新的毕设ID  getStuThesis
     */
    @GetMapping("/getStuThesis")
    public RespBean getStuThesis(@RequestParam("tutorId") Integer tutorId,
                                 @RequestParam("year") Integer year,
                                 @RequestParam("month") Integer month) {
        try {
            List<Student> stuThesis = paperCommentService.getStuThesis(tutorId, year, month);
            return RespBean.ok("", stuThesis);
        } catch (Exception e) {
            return RespBean.error("获取毕业设计信息错误！");
        }
    }

    /**
     * 通过thesisID返回需要打印的pdf文件
     */
    @GetMapping("/exportPDF")
    public void exportDataPDF(HttpServletResponse response, @RequestParam Integer thesisID) throws Exception {
        boolean generatePDF = false;
        try {
            generatePDF = exportPDF.generatePDF(response, thesisID);
        } catch (Exception e) {
            // 处理异常
            EmailErrorLog emailErrorLog = new EmailErrorLog();
            emailErrorLog.setErrorType("导出PDF出现错误");
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            String errorDetails = sw.toString();
            emailErrorLog.setErrorDescription(errorDetails);
            emailErrorLog.setTimestamp(new Timestamp(System.currentTimeMillis()));
            emailErrorLogService.addEmailErrorLog(emailErrorLog);
        }

        if (!generatePDF) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/editThesisName")
    public RespBean editThesisName(@RequestParam("thesisId") Integer thesisId,
                                   @RequestParam("thesisName") String thesisName) {
        try {
            thesisService.editThesisName(thesisId, thesisName);
            return RespBean.ok("");
        } catch (Exception e) {
            return RespBean.error("修改本科生毕业论文题目失败！");
        }
    }


}
