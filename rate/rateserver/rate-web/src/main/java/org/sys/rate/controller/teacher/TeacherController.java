package org.sys.rate.controller.teacher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.sys.rate.mapper.TeacherMapper;
import org.sys.rate.model.RespBean;
import org.sys.rate.model.Teachers;
import org.sys.rate.service.admin.TeacherService;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/system/teacher")
public class TeacherController {
    @Autowired
    TeacherService teacherService;
    @Resource
    TeacherMapper teacherMapper;

    // 通过名字模糊查询
    @GetMapping("searchByName")
    public RespBean searchByName(String name) {
        List<Teachers> teachers = teacherMapper.selectByName(name);
        return RespBean.ok("success", teachers);
    }

    // 通过名字或者工号进行模糊查询
    @GetMapping("searchByNameOrJobNumber")
    public RespBean searchByNameOrJobNumber(@RequestParam("nameOrJobNumber") String nameOrJobNumber,
                                            @RequestParam("institutionID")Integer institutionID) {
        try {
            List<Teachers> teachers = teacherMapper.searchByNameOrJobNumber(nameOrJobNumber, institutionID);
            return RespBean.ok("success", teachers);
        } catch (Exception e) {
            return RespBean.error("获取教师信息出现错误！");
        }
    }

}

