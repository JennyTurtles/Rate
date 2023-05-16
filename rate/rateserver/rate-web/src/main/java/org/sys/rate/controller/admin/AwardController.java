package org.sys.rate.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.sys.rate.config.JsonResult;
import org.sys.rate.model.Award;
import org.sys.rate.model.AwardOper;
import org.sys.rate.model.PatentOper;
import org.sys.rate.service.admin.AwardService;
import org.sys.rate.service.admin.AwardService;

import javax.mail.MessagingException;
import java.util.List;

/**
 * 类别Controller
 *
 * @author system
 * @date 2022-03-13
 */
@RestController
@RequestMapping("/award/basic")
public class AwardController
{

    @Autowired
    private AwardService awardtypeService;

    /**
     * 查询类别列表
     */
    @GetMapping("/list")
    @ResponseBody
    public JsonResult list(Award awardtype)
    {
        List<Award> list = awardtypeService.selectAwardList(awardtype);
        return new JsonResult(list);
    }

    /**
     * 查询类别列表
     */
    @PostMapping("/list")
    @ResponseBody
    public JsonResult postList(@RequestBody Award awardtype)
    {
        List<Award> list = awardtypeService.selectAwardList(awardtype);
        return new JsonResult(list);
    }


    /**
     * 新增保存类别
//     */
//    @PostMapping("/add")
//    @ResponseBody
//    public JsonResult addSave(Award awardtype)
//    {
//        return new JsonResult(awardtypeService.insertAward(awardtype));
//    }

    /**
     * 修改保存类别
     */
    @PostMapping("/edit")
    @ResponseBody
    public JsonResult editSave(Award awardtype)
    {
        return new JsonResult(awardtypeService.updateAward(awardtype));
    }

    /**
     * 删除类别
     */
    @PostMapping( "/remove")
    @ResponseBody
    public JsonResult remove(Long ids)
    {
        return new JsonResult(awardtypeService.deleteAwardById(ids));
    }

    //    修改论文状态
    @GetMapping("/edit_state")
    public JsonResult getById(String state, Long ID) throws MessagingException {
        return new JsonResult(awardtypeService.editState(state, ID));
    }

    @PostMapping("/add")
    @ResponseBody
    public JsonResult addSave(AwardOper paperoper)
    {
        return new JsonResult(awardtypeService.insertPaperOper(paperoper));
//        return new JsonResult();
    }
}
