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

    //学生注册
    @PostMapping("/stu")
    public RespBean registerStu(@RequestBody Student student){
        //根据身份证判断 如果有这个student，就设置用户名密码和密保
        //没有就插入，同时判断选择注册的身份
        String stuType = student.getStuType();
        try{
            String password = ExpertService.sh1(student.getPassword());
            student.setPassword(password);
            switch (stuType){
                case "本科生" : student.setRole("10");break;
                case "研究生" : student.setRole("11");break;
                default:student.setRole("7");break; //选手
            }
            //可以直接根据id判断，因为在填写时已经做了查询，查到了id会存在，没查到就是null
            if(student.getID() == null){//插入学生表 并返回id
                studentMapper.insertStuFromRegister(student);
            }else {//有这个学生，就更新用户名和密码和密保
                studentMapper.updatePasswordAndUsername(student);
            }

            if(stuType.equals("本科生")){
                UnderGraduate under = new UnderGraduate();
                under.setStuNumber(student.getStudentnumber());
                under.setTutorID(null);
                under.setInstitutionID(student.getInstitutionID());//怎么处理？
                under.setYear(student.getYear());
                under.setStudentID(student.getID());
                if(underGraduateMapper.getUnderByStuID(student.getID()) == null){
                    underGraduateMapper.insert(under);
                }
            }else if(stuType.equals("研究生")){
                GraduateStudent grad = new GraduateStudent();
                grad.setStuNumber(student.getStudentnumber());
                grad.setTutorID(null);
                grad.setInstitutionID(student.getInstitutionID());//怎么处理？
                grad.setYear(student.getYear());
                grad.setStudentID(student.getID());
                grad.setStudentType(student.getGradType());
                grad.setPoint(null);//怎么处理？
                if(graduateStudentMapper.getGradByStuID(student.getID()) == null){
                    graduateStudentMapper.insert(grad);
                }
            }
//            else if(stuType.equals("没有本校学号")){//选手
//                Participates par = null;//code?display?都怎么处理？
//                par.setStudentID(student.getID());
////                participatesMapper.insertParByRegister(par);
//            }
        }catch (Exception e){
            return RespBean.error("error",null);
        }
        return RespBean.ok("ok",null);
    }

    //忘记密码里面，通过输入身份证号和role查找用户
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

    //教师注册
    @PostMapping("/tea")
    public RespBean registerTea(@RequestBody Teachers teacher){
        //根据身份证判断 如果有这个teacher，就设置用户名密码和密保
        //没有就插入，同时判断选择注册的身份
        try{
            String password = ExpertService.sh1(teacher.getPassword());
            teacher.setPassword(password);
            //可以直接根据id判断，因为在填写时已经做了查询，查到了id会存在，没查到就是null
            if(teacher.getID() == null){//插入学生表 并返回id
                //老师用excel导入没有密保 怎么处理？？？
                teachersMapper.insertTeaFromRegister(teacher);
            }else {//有这个老师，就更新用户名和密码和密保
                teachersMapper.updatePasswordAndUsername(teacher);
            }
        }catch (Exception e){
            return RespBean.error("error",null);
        }
        return RespBean.ok("ok",null);
    }
}
