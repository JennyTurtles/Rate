package org.sys.rate.controller.admin;

import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.sys.rate.model.Teacher;
import org.sys.rate.model.RespBean;
import org.sys.rate.model.RespPageBean;
import org.sys.rate.service.admin.TeacherService;
import java.applet.AppletStub;
import java.util.List;

@RestController
@RequestMapping("/teacher/basic")
public class TeacherBasicController {
    @Autowired
    TeacherService TeacherService;


    @GetMapping("/getall")
    public List<Teacher> getAllTeacher() {
        return TeacherService.getAllTeacher();
    }

    @PostMapping("/insert")
    public RespBean addStudent(@RequestBody Teacher record) {
        if (TeacherService.addTeacher(record) == 1) {
            return RespBean.ok("添加成功!");
        }
        return RespBean.error("添加失败!");
    }

    @PostMapping("/delete")
    public RespBean deleteTeacher(@RequestBody Teacher record) {
        if (TeacherService.deleteTeacher(record) == 1) {
            return RespBean.ok("删除成功!");
        }
        return RespBean.error("删除失败!");
    }


    @PostMapping("/update")
    public RespBean updateTeacher(@RequestBody Teacher record) {
        if (TeacherService.updateTeacher(record) == 1) {
            return RespBean.ok("更新成功!");
        }
        return RespBean.error("更新失败!");
    }
}

