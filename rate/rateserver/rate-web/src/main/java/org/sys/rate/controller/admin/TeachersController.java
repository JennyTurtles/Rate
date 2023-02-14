package org.sys.rate.controller.admin;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.sys.rate.config.JsonResult;
import org.sys.rate.model.Teachers;
import org.sys.rate.service.admin.TeachersService;

/**
 * 老师Controller
 * 
 * @author system
 * @date 2022-03-13
 */
@RestController
@RequestMapping("/teachers/basic")
public class TeachersController
{

    @Autowired
    private TeachersService teachersService;

    @GetMapping("/List")
    public JsonResult<List> getCollect(){
        return new JsonResult<>(teachersService.selectList());
    }
    /**
     * 查询老师列表
     */
    @PostMapping("/list")
    @ResponseBody
    public JsonResult list(Teachers teachers)
    {
        List<Teachers> list = teachersService.selectTeachersList(teachers);
        return new JsonResult(list);
    }

    /**
     * 新增保存老师
     */
    @PostMapping("/add")
    @ResponseBody
    public JsonResult addSave(Teachers teachers)
    {
        return new JsonResult(teachersService.insertTeachers(teachers));
    }


    @PostMapping("/edit")
    @ResponseBody
    public JsonResult editSave(Teachers teachers)
    {
        return new JsonResult(teachersService.updateTeachers(teachers));
    }

    /**
     * 删除老师
     */
    @PostMapping( "/remove")
    @ResponseBody
    public JsonResult remove(Long ids)
    {
        return new JsonResult(teachersService.deleteTeachersById(ids));
    }
}
