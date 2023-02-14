package org.sys.rate.controller.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.sys.rate.config.JsonResult;
import org.sys.rate.model.Projects;
import org.sys.rate.service.admin.ProjectsService;

/**
 * 项目Controller
 * 
 * @author system
 * @date 2022-03-13
 */
@RestController
@RequestMapping("/projects/basic")
public class ProjectsController
{

    @Autowired
    private ProjectsService projectsService;

    @GetMapping("/List")
    public JsonResult<List> getCollect(){
        return new JsonResult<>(projectsService.selectList());
    }


    /**
     * 查询项目列表
     */
    @PostMapping("/list")
    @ResponseBody
    public JsonResult list(Projects projects)
    {
        List<Projects> list = projectsService.selectProjectsList(projects);
        return new JsonResult(list);
    }

    /**
     * 新增保存项目
     */
    @PostMapping("/add")
    @ResponseBody
    public JsonResult addSave(Projects projects)
    {
        return new JsonResult(projectsService.insertProjects(projects));
    }


    /**
     * 修改保存项目
     */
    @PostMapping("/edit")
    @ResponseBody
    public JsonResult editSave(Projects projects)
    {
        return new JsonResult(projectsService.updateProjects(projects));
    }


    /**
     * 删除项目
     */
    @PostMapping( "/remove")
    @ResponseBody
    public JsonResult remove(Long ids)
    {
        return new JsonResult(projectsService.deleteProjectsById(ids));
    }
}
