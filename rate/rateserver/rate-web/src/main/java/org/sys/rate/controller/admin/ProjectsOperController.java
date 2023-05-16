package org.sys.rate.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.sys.rate.config.JsonResult;
import org.sys.rate.model.ProjectsOper;
import org.sys.rate.service.admin.ProjectsOperService;

import java.util.List;

@RestController
@RequestMapping("/projectoper/basic")
public class ProjectsOperController {
    @Autowired
    ProjectsOperService projectoperService;

    @PostMapping("/add")
    @ResponseBody
    public JsonResult addSave(ProjectsOper projectoper)
    {
        return new JsonResult(projectoperService.insertProjectOper(projectoper));
//        return new JsonResult();
    }

    @GetMapping("/List")//查询关于该论文的所有操作
    public JsonResult selectList(Long ID){
        List<ProjectsOper> res=projectoperService.selectList(ID);
        return new JsonResult(res);
    }
}
