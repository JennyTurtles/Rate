package org.sys.rate.controller.student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.sys.rate.model.RespBean;
import org.sys.rate.model.Student;
import org.sys.rate.service.admin.StudentService;

@RestController
@RequestMapping("/system/student")
public class StudentController {
    @Autowired
    StudentService studentService;

//   要注册为研究生，就根据用户输入的学号去研究生表里查询
//   如果没查到就在研究生表添加记录
//   如果查到了就更新那条记录的studentID，然后去学生表把老的studentID删掉
    @Transactional
    @PostMapping("/registerGraduate")
    public RespBean registerGraduate(Student student){
        studentService.registerGraduate(student);
        return RespBean.ok("注册成功");
    }


    // 本科生也是一样的
    @Transactional
    @PostMapping("/registerUndergraduate")
    public RespBean registerUndergraduate(Student student){
        studentService.registerUndergraduate(student);
        return RespBean.ok("注册成功");
    }

}
