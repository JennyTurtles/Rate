package org.sys.rate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.sys.rate.mapper.*;
import org.sys.rate.model.*;
import org.sys.rate.service.admin.GraduateStudentService;
import org.sys.rate.service.admin.UnderGraduateService;
import org.sys.rate.service.expert.ExpertService;

import java.util.Map;

@RestController
@RequestMapping("/registerUser")
public class RegisterController {
    @Autowired
    UnderGraduateMapper underGraduateMapper;
    @Autowired
    GraduateStudentMapper graduateStudentMapper;
    @Autowired
    ParticipatesMapper participatesMapper;
    @Autowired
    StudentMapper studentMapper;
    @Autowired
    TeachersMapper teachersMapper;

    @PostMapping("/stu")
    public RespBean registerStu(@RequestBody Student student){
        //根据身份证判断 如果有这个student，就设置用户名密码和密保
        //没有就插入，同时判断选择注册的身份
        String stuType = student.getStuType();
        try{
            String password = ExpertService.sh1(student.getPassword());
            student.setPassword(password);
            //可以直接根据id判断，因为在填写时已经做了查询，查到了id会存在，没查到就是null
            if(student.getID() == null){//插入学生表 并返回id
                //老师用excel导入没有密保 怎么处理？？？
                studentMapper.insertStuFromRegister(student);
            }else {//有这个学生，就更新用户名和密码和密保
                studentMapper.updatePasswordAndUsername(student);
            }

            if(stuType.equals("本科生")){
                UnderGraduate under = new UnderGraduate();
                under.setStuNumber(student.getStudentnumber());
                under.setTutorID(null);
                under.setInstitutionID(null);//怎么处理？
                under.setYear(student.getYear());
                under.setStudentID(student.getID());
                underGraduateMapper.insert(under);
            }else if(stuType.equals("研究生")){
                GraduateStudent grad = new GraduateStudent();
                grad.setStuNumber(student.getStudentnumber());
                grad.setTutorID(null);
                grad.setInstitutionID(null);//怎么处理？
                grad.setYear(student.getYear());
                grad.setStudentID(student.getID());
                grad.setStudentType(student.getGradType());
                grad.setPoint(null);//怎么处理？
                graduateStudentMapper.insert(grad);
            }else if(stuType.equals("没有本校学号")){//选手
                Participates par = null;//code?display?都怎么处理？
                par.setStudentID(student.getID());
                participatesMapper.insertParByRegister(par);
            }
        }catch (Exception e){
            return RespBean.error("error",null);
        }
        return RespBean.ok("ok",null);
    }

    @GetMapping("/getUserByIdNumber")
    public RespBean getUserByIdNumber(String role,String idNumber){
        if(idNumber.equals("")){
            return RespBean.ok("null",null);
        }else {
            try {
                if(role.equals("student")){
                    Student stu = studentMapper.getStuByIDNumber(idNumber);
                    return RespBean.ok("student",stu);
                }else if(role.equals("teacher")){
                    Teachers tea =teachersMapper.getTeaByIDNumber(idNumber);
                    return RespBean.ok("teacher",tea);
                }
            }catch (Exception e){
            }
        }
        return RespBean.error("error",null);
    }

}
