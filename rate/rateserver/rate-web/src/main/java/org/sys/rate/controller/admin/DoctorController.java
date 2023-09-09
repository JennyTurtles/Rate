package org.sys.rate.controller.admin;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.sys.rate.mapper.DoctorMapper;
import org.sys.rate.mapper.StudentMapper;
import org.sys.rate.model.Doctor;
import org.sys.rate.model.GraduateStudent;
import org.sys.rate.model.RespBean;

import javax.annotation.Resource;

@RestController
@RequestMapping("/doctorM/basic")
public class DoctorController {
    @Resource
    DoctorMapper doctorMapper;
    @Resource
    StudentMapper studentMapper;

    @PostMapping("/update")
    public RespBean updateStudent(@RequestBody Doctor record) {
        if (doctorMapper.checkHaveStudentOfStuNumber(record.getInstitutionID(),record.getStuNumber(),record.getStudentID()) == 1){
            return RespBean.error("学号已存在，请重新修改或联系管理员！");
        }
        if (studentMapper.update(record) == 1){
            if (doctorMapper.update(record) == 1) {
                return RespBean.ok("更新成功!");
            }
        }
        return RespBean.error("更新失败!");
    }
}
