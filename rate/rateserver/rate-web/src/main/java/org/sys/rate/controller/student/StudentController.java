package org.sys.rate.controller.student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.sys.rate.mapper.StudentMapper;
import org.sys.rate.model.RespBean;
import org.sys.rate.model.Student;
import org.sys.rate.service.admin.StudentService;
import org.sys.rate.service.expert.ExpertService;

@RestController
@RequestMapping("/system/student")
public class StudentController {
    @Autowired
    StudentService studentService;
    @Autowired
    StudentMapper studentMapper;

//   要注册为研究生，就根据用户输入的学号去研究生表里查询
//   如果没查到就在研究生表添加记录
//   如果查到了就更新那条记录的studentID，然后去学生表把老的studentID删掉
    @Transactional
    @PostMapping("/registerGraduate")
    public RespBean registerGraduate(Student student){
        boolean res = studentService.registerGraduate(student);
        return res ? RespBean.ok("注册成功") : RespBean.error("请检查学号是否正确！");
    }


    // 本科生也是一样的
    @Transactional
    @PostMapping("/registerUndergraduate")
    public RespBean registerUndergraduate(Student student){
        boolean res = studentService.registerUndergraduate(student);
        return res ? RespBean.ok("注册成功") : RespBean.error("请检查学号是否正确！");
    }

    // 注册为博士生
    @Transactional
    @PostMapping("/registerDoctor")
    public RespBean registerDoctor(Student student){
        boolean res = studentService.registerDoctor(student);
        return res ? RespBean.ok("注册成功") : RespBean.error("请检查学号是否正确！");
    }
    @PostMapping("/basic/updatePassword")
    public RespBean updatePassword(@RequestBody Student student) {
        try {
            String encryptedPassword = ExpertService.sh1(student.getPassword()); // 确保密码已加密
            student.setPassword(encryptedPassword);
            int result = studentMapper.updatePassword(student.getID(), encryptedPassword);
            return result > 0 ? RespBean.ok("密码更新成功") : RespBean.error("密码更新失败");
        } catch (Exception e) {
            e.printStackTrace();
            return RespBean.error("服务器错误");
        }
    }
    // 注册为选手
    @Transactional
    @PostMapping("/registerParticipant")
    public RespBean registerParticipant(Integer activityID, Student student){
        RespBean res = studentService.registerParticipant(student, activityID);
        return res;
    }
}
