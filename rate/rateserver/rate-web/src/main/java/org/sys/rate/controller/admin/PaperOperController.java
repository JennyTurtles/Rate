package org.sys.rate.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.sys.rate.config.JsonResult;
import org.sys.rate.model.Paper;
import org.sys.rate.model.PaperOper;
import org.sys.rate.service.admin.PaperOperService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

@RestController
@RequestMapping("/paperoper/basic")
public class PaperOperController {
    @Autowired
    PaperOperService paperoperService;

    @PostMapping("/add")
    @ResponseBody
    public JsonResult addSave(PaperOper paperoper)
    {
        return new JsonResult(paperoperService.insertPaperOper(paperoper));
//        return new JsonResult();
    }

    @GetMapping("/List")//查询关于该论文的所有操作
    public JsonResult selectList(Long ID){
        List<PaperOper> res=paperoperService.selectList(ID);
        return new JsonResult(res);
    }

}
