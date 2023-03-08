package org.sys.rate.controller.admin;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.sys.rate.config.JsonResult;
import org.sys.rate.model.Paper;
import org.sys.rate.model.PaperComment;
import org.sys.rate.service.underFunction.PaperCommentService;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import java.util.List;

@RestController
@RequestMapping("/paperComment/basic")
public class PaperCommentController {
    @Resource
    PaperCommentService paperCommentService;

    // 根据stuID和thesisID和num获取某一次评论
    @GetMapping("/getOneComment")
    public JsonResult getOneComment(int stuID, int num) {
        return new JsonResult(paperCommentService.selectCommentOne(stuID, num));
    }

    // 根据stuID和thesisID获取关于thesisID的所有评论
    @GetMapping("/getAllComment")
    public JsonResult<List> list(int thesisID) {
        return new JsonResult(paperCommentService.selectCommentList(thesisID));
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
    /**
     * 查询论文成果列表
     */
    @GetMapping("/getTID")
    public JsonResult getTIDbySID(int stuID) throws MessagingException {
        Integer thesisID = paperCommentService.getTIDbySID(stuID);
        return new JsonResult(thesisID);
    }

}
