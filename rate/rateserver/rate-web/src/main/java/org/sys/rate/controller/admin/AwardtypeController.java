package org.sys.rate.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.sys.rate.config.JsonResult;
import org.sys.rate.model.Awardtype;
import org.sys.rate.service.admin.AwardtypeService;

import java.util.List;

/**
 * 类别Controller
 *
 * @author system
 * @date 2022-03-13
 */
@RestController
@RequestMapping("/awardtype/basic")
public class AwardtypeController
{

    @Autowired
    private AwardtypeService awardtypeService;

    /**
     * 查询类别列表
     */
    @PostMapping("/list")
    @ResponseBody
    public JsonResult list(Awardtype awardtype)
    {
        List<Awardtype> list = awardtypeService.selectAwardtypeList(awardtype);
        return new JsonResult(list);
    }


    /**
     * 新增保存类别
     */
    @PostMapping("/add")
    @ResponseBody
    public JsonResult addSave(Awardtype awardtype)
    {
        return new JsonResult(awardtypeService.insertAwardtype(awardtype));
    }

    /**
     * 修改保存类别
     */
    @PostMapping("/edit")
    @ResponseBody
    public JsonResult editSave(Awardtype awardtype)
    {
        return new JsonResult(awardtypeService.updateAwardtype(awardtype));
    }

    /**
     * 删除类别
     */
    @PostMapping( "/remove")
    @ResponseBody
    public JsonResult remove(Long ids)
    {
        return new JsonResult(awardtypeService.deleteAwardtypeById(ids));
    }
}
