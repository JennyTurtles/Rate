package org.sys.rate.service.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.sys.rate.mapper.*;
import org.sys.rate.model.*;

import javax.annotation.Resource;
import javax.print.Doc;
import java.util.ArrayList;
import java.util.List;

@Service
public class DoctorService {
    @Autowired
    StudentMapper studentMapper;
    @Autowired
    TeachersMapper teachersMapper;
    @Autowired
    TeacherMapper teacherMapper;
    @Autowired
    ParticipatesMapper participatesMapper;
    @Autowired
    DoctorMapper doctorMapper;
    @Resource
    UnderGraduateMapper underGraduateMapper;
    @Resource
    GraduateStudentMapper graduateStudentMapper;

    //管理员导入博士生
    public RespBean addDoctor(List<Doctor> doctorList) {
        List<String> jobTeas = new ArrayList<>(); //记录导师的工号
        List<String> nameTeas = new ArrayList<>(); //记录导师的姓名
        for(int i = 0;i < doctorList.size();i++){
            //工号和姓名都有按照工号来，都没有tutorid为空，只有姓名就按照姓名查找
            if(doctorList.get(i).getTeachers().getJobnumber() == null && doctorList.get(i).getTeachers().getName() == null){
                doctorList.get(i).setTutorID(null);
            }
            if(doctorList.get(i).getTeachers().getJobnumber() != null){
                jobTeas.add(doctorList.get(i).getTeachers().getJobnumber());
            }
            if(doctorList.get(i).getTeachers().getJobnumber() == null && doctorList.get(i).getTeachers().getName() != null){
                nameTeas.add(doctorList.get(i).getTeachers().getName());
            }
        }
        List<Teachers> jobTeachers = new ArrayList<>();
        jobTeachers = teachersMapper.selectTeasByJobnumber(jobTeas);
        if(jobTeachers.size() == 0) {
            return RespBean.error("未找到老师信息！请仔细检查工号");
        }

        //改为根据机构id和学号判断在student表中是否已经存在
        Integer id = null;
        List<Student> updateStus = new ArrayList<>();
        List<Doctor> insertDoctors = new ArrayList<>();
        List<Doctor> updateDoctors = new ArrayList<>();
        for(int i = 0;i < doctorList.size(); i ++) {
            Doctor doctor = doctorList.get(i);
            id = doctorMapper.checkStudentExist(doctor.getStuNumber(), doctor.getName(), doctor.getInstitutionID());
            if (id != null) {
                if (id.equals(-1)) {
                    return RespBean.error("学生学号和姓名不匹配");
                }
                doctor.setStudentID(id);
                try {
                    // 更新研究生表信息
                    Student student = new Student();
                    student.setID(doctor.getStudentID());
                    student.setInstitutionID(doctor.getInstitutionID());
                    student.setTelephone(doctor.getTelephone());
                    student.setEmail(doctor.getEmail());
                    student.setName(doctor.getName());
                    updateStus.add(student);
                } catch (Exception e) {
                    return RespBean.error("更新研究生信息出现错误！");
                }
            } else { //需要插入新学生记录
                Student student = new Student();
                student.setInstitutionID(doctor.getInstitutionID());
                student.setTelephone(doctor.getTelephone());
                student.setEmail(doctor.getEmail());
                student.setRole("17");
                student.setName(doctor.getName());
                try {
                    studentMapper.insertStudentByImportGraduate(student);
                } catch (Exception e) {
                    return RespBean.error("插入学生出错！");
                }
                doctor.setStudentID(student.getID());
            }
        }
        if(updateStus.size() > 0) {
            studentMapper.updateFromAdminExcel(updateStus); // 更新student表的信息
        }

        List<Teachers> nameTeachers = new ArrayList<>();
        List<Teachers> updateTeachers = new ArrayList<>();
        if(jobTeas.size() > 0){ //找到对应的导师信息，更新导师的role
            for(int i = 0;i < doctorList.size();i++) {
                if(doctorList.get(i).getTeachers().getJobnumber() == null || doctorList.get(i).getTeachers().getJobnumber().equals("")){
                    continue;
                }
                for (int j = 0; j < jobTeachers.size(); j++) {
                    if (doctorList.get(i).getTeachers().getJobnumber().equals(jobTeachers.get(j).getJobnumber())) {
                        String tearole = jobTeachers.get(j).getRole();
                        if(!tearole.contains("9")){
                            tearole += ";9";
                            jobTeachers.get(j).setRole(tearole);
                            updateTeachers.add(jobTeachers.get(j));
                        }
                        doctorList.get(i).setTeachers(jobTeachers.get(j));
                        doctorList.get(i).setTutorID(jobTeachers.get(j).getID());
                        break;
                    }
                }
            }
        }
        if(nameTeas.size() > 0){
            nameTeachers = teachersMapper.selectTeasByName(nameTeas);
            for(int i = 0;i < doctorList.size();i++) {
                if(doctorList.get(i).getTeachers().getName() == null || doctorList.get(i).getTeachers().getName().equals("")){
                    continue;
                }
                for (int j = 0; j < nameTeachers.size(); j++) {
                    if (doctorList.get(i).getTeachers().getName().equals(nameTeachers.get(j).getName())) {
                        String tearole = nameTeachers.get(j).getRole();
                        if(!tearole.contains("9")){
                            tearole += ";9";
                            nameTeachers.get(j).setRole(tearole);
                            updateTeachers.add(nameTeachers.get(j));
                        }
                        doctorList.get(i).setTeachers(nameTeachers.get(j));
                        doctorList.get(i).setTutorID(nameTeachers.get(j).getID());
                        break;
                    }
                }
            }
        }

        for(int i = 0;i < doctorList.size(); i ++) { //拿到需要插入博士生表的数据
            Doctor doctor = doctorList.get(i);
            id = doctorMapper.checkStudentExist(doctor.getStuNumber(), doctor.getName(), doctor.getInstitutionID());
            if (id == null) {
                insertDoctors.add(doctor);
            } else {
                updateDoctors.add(doctor);
            }
        }

        try {
            if(insertDoctors.size() > 0){ //需要插入的博士生
                doctorMapper.insertFROMImport(insertDoctors);
            }
            if(updateDoctors.size() > 0){ //需要更新的博士生
                doctorMapper.updateFROMImport(updateDoctors);
            }
            if(updateTeachers.size() > 0){//需要对老师的role字段进行更新
                teachersMapper.updateRoleOfTeachers(updateTeachers);
            }
        }catch (Exception e){
            return RespBean.error("error");
        }
        return RespBean.ok("ok");
    }
    public List<Doctor> getDoctorsBySelect(Integer year, String teaName){
        List<Doctor> res = new ArrayList<>();
        res = doctorMapper.getDoctorsBySelect(teaName,year);
        return res;
    }
    public Msg deleteDoctorStudent(Doctor doctor){
        //点击删除也要删除student表中的学生信息，所以要判断这个student是不是还是选手或其他身份
        //同时也要删除对应老师的角色，所以也要判断这个老师是不是还是别的学生的导师
        try {
            doctorMapper.deleteDoctorStudent(doctor); //博士生的删除没有判断导师方面
            //如果没有导师就直接删除
//            if(doctor.getTutorID() != null && !doctor.getTutorID().equals("")){
//                //说明只是这一个学生的研究生老师，需要去掉这个老师的研究生导师角色
//                if(doctorMapper.checkHaveStudentOftutorID(doctor.getTutorID(),doctor.getID()) == 0){
//                    Teachers tea = teachersMapper.selectByPrimaryId(doctor.getTutorID());
//                    String r = tea.getRole();
//                    //区分要不要删掉多余分号，我懒得思考了，有bug再说
//                    if(r.contains("11;")){
//                        r = r.replace("11;","");
//                        tea.setRole(r);
//                        teachersMapper.updateRoleOfOneTeacher(tea);
//                    }else if(r.contains("11")){
//                        r = r.replace("11","");
//                        tea.setRole(r);
//                        teachersMapper.updateRoleOfOneTeacher(tea);
//                    }
//                }
//            }
            if(underGraduateMapper.checkHaveStudentOfstudenID(doctor.getStudentID()) == 0 &&
                    participatesMapper.isParticipants(doctor.getStudentID()) == 0 &&
                    graduateStudentMapper.checkHaveStudentOfstudenID(doctor.getStudentID()) == 0){
                //如果在选手表中和本科生表中查不到关于这个stuid关联的数据，说明可以删除
                studentMapper.deleteStudent(doctor.getStudentID());
            }
        }catch (Exception e){
            return Msg.fail();
        }return Msg.success();
    }
    public RespBean editDoctorStudent(Doctor doctor){
        //目的不在于更改老师表中的信息
        //直接限定老师名字和工号必须填写了
        try {
            Teachers tea = null;
            //先判断有没有这个导师的存在
            if(doctor.getTeachers().getJobnumber() != null && !doctor.getTeachers().getJobnumber().equals("")){
                tea = teachersMapper.selectTeaByJobnumber(doctor.getTeachers().getJobnumber());
                if(tea == null){//有工号，但是没有查到，说明工号错误
                    return RespBean.error("未查询到该老师");
                }
                //如果根据工号查到了这个老师，但是数据库的老师名字和前端传来的老师名字不同，说明信息填写错误
                if(doctor.getTeachers().getName()!= null && !doctor.getTeachers().getName().equals("") && !tea.getName().equals(doctor.getTeachers().getName())){
                    return RespBean.error("该老师工号和姓名不符，请仔细检查信息");
                }
                doctor.setTutorID(tea.getID());//重新设置导师id
                doctorMapper.editDoctorStudent(doctor);
            }
        }catch (Exception e){
            return RespBean.error("处理失败");
        }return RespBean.ok("更新成功");
    }

}
