package org.sys.rate.controller.admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.sys.rate.mapper.*;
import org.sys.rate.model.*;
import org.sys.rate.service.admin.GraduateStudentService;
import org.sys.rate.service.admin.StudentService;
import org.sys.rate.service.admin.UnderGraduateService;
import org.sys.rate.service.expert.ExpertService;

import java.applet.AppletStub;
import java.util.List;

@RestController
@RequestMapping("/student/basic")
public class StudentBasicController {
    @Autowired
    StudentService StudentService;
    @Autowired
    StudentMapper studentMapper;
    @Autowired
    TeacherMapper teacherMapper;
    @Autowired
    UnderGraduateService underGraduateService;
    @Autowired
    GraduateStudentService graduateStudentService;
    @Autowired
    GraduateStudentMapper graduateStudentMapper;
    @Autowired
    UnderGraduateMapper underGraduateMapper;
    @Autowired
    TeachersMapper teachersMapper;
    @Autowired
    DoctorMapper doctorMapper;

    @GetMapping("/getall")
    public List<Student> getAllStudent() {
        return StudentService.getAllStudent();
    }
    @GetMapping("/getStuByIDNumber")
    public RespBean getStuByIDNumber(String IDNumber,String stuType) {
        Student res = StudentService.getStuByIDNumber(IDNumber);
        UnderGraduate under = null;
        GraduateStudent grad = null;
        //说明查询到了这个学生
        if(res != null){
            if(stuType != null && !stuType.equals("")){
                if(stuType.equals("本科生")){
                    under = underGraduateService.getUnderByStuID(res.getID());
                    if (under != null) return RespBean.ok("res",under);
                }else if(stuType.equals("研究生")){
                    grad = graduateStudentService.getGradByStuID(res.getID());
                    if (grad != null) return RespBean.ok("res",grad);
                }

            }
        }
        return RespBean.ok("res",res);
    }

    @PostMapping("/insert")
    public RespBean addStudent(@RequestBody Student record) {
        if (StudentService.addStudent(record) == 1) {
            return RespBean.ok("添加成功!");
        }
        return RespBean.error("添加失败!");
    }

    @PostMapping("/delete")
    public RespBean deleteStudent(@RequestBody Student record) {
        if (StudentService.deleteStudent(record) == 1) {
            return RespBean.ok("删除成功!");
        }
        return RespBean.error("删除失败!");
    }


    @PostMapping("/update")
    public RespBean updateStudent(@RequestBody Student record) {
        if (StudentService.updateStudent(record) == 1) {
            return RespBean.ok("更新成功!");
        }
        return RespBean.error("更新失败!");
    }
    @PostMapping("/updatePassword")
    public RespBean updatePassword(@RequestBody Student record) {
        String pass = ExpertService.sh1(record.getPassword());//加密
        if (studentMapper.updatePassword(record.getID(),pass) == 1) {
            return RespBean.ok("更新成功!");
        }
        return RespBean.error("更新失败!");
    }
    @GetMapping("/getTutorInfo")
    public RespBean getTutorInfo(Integer id) {
        return RespBean.ok("ok", teacherMapper.getTutorInfo(id));
    }

    @GetMapping("/getStuInfo")
    public RespBean getStuInfo(@RequestParam Integer id, @RequestParam String stuType){
        if (stuType.equals("本科生")){
            UnderGraduate underGraduate = underGraduateMapper.getUnderByStuID(id);
            underGraduate.setStuType("本科生");
            return RespBean.ok("success",underGraduate);
        }
        else if (stuType.equals("硕士生")){
            GraduateStudent graduateStudent = graduateStudentMapper.getGradByStuID(id);
            Teachers teacher = teachersMapper.selectByPrimaryId(graduateStudent.getTutorID());
            graduateStudent.setTeachers(teacher);
            graduateStudent.setStuType("硕士生");
            return RespBean.ok("success",graduateStudent);
        }
        else if (stuType.equals("博士生")){
            Doctor doctor = doctorMapper.getDocByStuID(id);
            Teachers teacher = teachersMapper.selectByPrimaryId(doctor.getTutorID());
            doctor.setTeachers(teacher);
            doctor.setStuType("博士生");
            return RespBean.ok("success",doctor);
        }
        else
        {
            Student student = studentMapper.getById(id);
            student.setStuType("不是大学生");
            return RespBean.ok("success",student);
        }
    }
}


