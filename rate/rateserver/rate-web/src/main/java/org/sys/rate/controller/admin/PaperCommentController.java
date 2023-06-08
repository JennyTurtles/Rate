package org.sys.rate.controller.admin;

import org.springframework.web.bind.annotation.*;
import org.sys.rate.config.JsonResult;
import org.sys.rate.model.PaperComment;
import org.sys.rate.model.Student;
import org.sys.rate.service.admin.PaperCommentService;
import org.sys.rate.utils.ExportPDF;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/paperComment/basic")
public class PaperCommentController {
    @Resource
    PaperCommentService paperCommentService;

    @Resource
    ExportPDF exportPDF;


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
    public JsonResult addSave(PaperComment paperComment) throws MessagingException {
        Integer res = paperCommentService.insertComment(paperComment);
        //System.out.println("新增的dateStu:" + paperComment.getDateStu());
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
    @PostMapping("/exportPDF")
    public void exportDataPDF(HttpServletResponse response, @RequestParam Integer thesisID) throws Exception {
        exportPDF.generatePDF(response, thesisID);
    }


}
