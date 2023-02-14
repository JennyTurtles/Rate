package org.sys.rate.controller.admin;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import org.sys.rate.config.JsonResult;
import org.sys.rate.model.Patent;
import org.sys.rate.service.admin.PatentService;

/**
 * 著作Controller
 * 
 * @author system
 * @date 2022-03-13
 */
@RestController
@RequestMapping("/patent/basic")
public class PatentController
{

    @Autowired
    private PatentService patentService;

    @GetMapping("/List")
    public JsonResult<List> getCollect(){
        return new JsonResult<>(patentService.selectList());
    }

    @PostMapping("/list")
    @ResponseBody
    public JsonResult list(Patent patent)
    {
        List<Patent> list = patentService.selectPatentList(patent);
        return new JsonResult(list);
    }




    /**
     * 新增保存著作
     */
    @PostMapping("/add")
    @ResponseBody
    public JsonResult addSave(Patent patent)
    {
        return new JsonResult(patentService.insertPatent(patent));
    }


    /**
     * 修改保存著作
     */
    @PostMapping("/edit")
    @ResponseBody
    public JsonResult editSave(Patent patent)
    {
        return new JsonResult(patentService.updatePatent(patent));
    }

    /**
     * 删除著作
     */
    @PostMapping( "/remove")
    @ResponseBody
    public JsonResult remove(Long ids)
    {
        return new JsonResult(patentService.deletePatentById(ids));
    }
}
