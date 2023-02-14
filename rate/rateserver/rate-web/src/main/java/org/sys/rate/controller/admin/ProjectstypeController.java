package org.sys.rate.controller.admin;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.sys.rate.config.JsonResult;
import org.sys.rate.model.Projectstype;
import org.sys.rate.service.admin.ProjectstypeService;


/**
 * 著作类型Controller
 * 
 * @author system
 * @date 2022-03-13
 */
@RestController
@RequestMapping("/projectstype/basic")
public class ProjectstypeController
{

    @Autowired
    private ProjectstypeService projectstypeService;

    @GetMapping("/List")
    public JsonResult<List> getCollect(){
        return new JsonResult<>(projectstypeService.selectList());
    }

    /**
     * 查询著作类型列表
     */
    @PostMapping("/list")
    @ResponseBody
    public JsonResult list(Projectstype projectstype)
    {
        List<Projectstype> list = projectstypeService.selectProjectstypeList(projectstype);
        return new JsonResult(list);
    }

    /**
     * 新增保存著作类型
     */
    @PostMapping("/add")
    @ResponseBody
    public JsonResult addSave(Projectstype projectstype)
    {
        return new JsonResult(projectstypeService.insertProjectstype(projectstype));
    }


    /**
     * 修改保存著作类型
     */
    @PostMapping("/edit")
    @ResponseBody
    public JsonResult editSave(Projectstype projectstype)
    {
        return new JsonResult(projectstypeService.updateProjectstype(projectstype));
    }

    /**
     * 删除著作类型
     */
    @PostMapping( "/remove")
    @ResponseBody
    public JsonResult remove(Long ids)
    {
        return new JsonResult(projectstypeService.deleteProjectstypeById(ids));
    }
}
