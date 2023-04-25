package org.sys.rate.service.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.sys.rate.mapper.*;
import org.sys.rate.model.*;
import org.sys.rate.service.expert.ExpertService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

@Service
public class UnderGraduateService {
    @Autowired
    StudentMapper studentMapper;
    @Autowired
    TeachersMapper teachersMapper;
    @Autowired
    ParticipatesMapper participatesMapper;
    @Autowired
    GraduateStudentMapper graduateStudentMapper;
    @Autowired
    UnderGraduateMapper underGraduateMapper;
    //管理员导入本科生，只添加，即使已经存在了该条数据也不更新
    public RespBean addUnderGraduate( List<UnderGraduate> underList,List<Student> stuList) {
        //根据身份证号得到已经存在的student
        List<Student> checkStudents = studentMapper.checkAndReturnID(stuList);
        List<String> checkIDNumbers = new ArrayList<>();
        List<Student> updateStus = new ArrayList<>();
        List<Student> insertStus = new ArrayList<>();
        if(checkStudents.size() != 0){
            for(Student i : checkStudents){//拿到已经存在的student的idnumber
                checkIDNumbers.add(i.getIDNumber());
            }
            for(int i = 0;i < stuList.size();i++){
                //不在更新列表中，说明表里没有这个数据
                if(checkIDNumbers.indexOf(stuList.get(i).getIDNumber()) == -1){
                    insertStus.add(stuList.get(i));
                }
            }
        }else {
            insertStus = stuList;
        }
//        if(insertStus.size() > 0){
//            for(Student i : insertStus){
//                if(i.getUsername() == null || i.getUsername().equals(""))
//                {//为空
//                    i.setUsername(i.getTelephone());
//                }
//                String encodePass;
//                if(i.getPassword() == null || i.getPassword().equals(""))
//                {//为空
//                    encodePass = ExpertService.sh1(i.getTelephone());
//                }
//                else
//                {//默认密码为手机号
//                    encodePass = ExpertService.sh1(i.getPassword());
//                }
//                i.setPassword(encodePass);
//            }
//        }
        //因为不确定java的List数据类型是不是无序，所以确保准确性，用双重循环赋值studentID
        List<Student> newStuList = new ArrayList<>();
        try{
            if(checkStudents.size() != 0) {//有已经存在的
//                studentMapper.updateFromAdminExcel(stuList);
                newStuList.addAll(checkStudents);
            }
            if(insertStus.size() > 0){
                studentMapper.insertFromAdminExcel(insertStus);
                newStuList.addAll(insertStus);
            }
        }catch (Exception e) {
            return RespBean.error("error");
        }

        //对本科生循环 设置stuid和tutorid
        List<String> jobTeas = new ArrayList<>();
        List<String> nameTeas = new ArrayList<>();
        for(int i = 0;i < underList.size();i++){
            for(int j = 0;j < newStuList.size();j++){
                if(underList.get(i).getIDNumber().equals(newStuList.get(j).getIDNumber())){
                    underList.get(i).setStudentID(newStuList.get(j).getID());
                    break;
                }
            }
            //工号和姓名都有按照工号来，都没有tutorid为空，只有姓名就按照姓名查找
            if(underList.get(i).getTeachers().getJobnumber() == null && underList.get(i).getTeachers().getName() == null){
                underList.get(i).setTutorID(null);
            }
            if(underList.get(i).getTeachers().getJobnumber() != null){
                jobTeas.add(underList.get(i).getTeachers().getJobnumber());
            }
            if(underList.get(i).getTeachers().getJobnumber() == null && underList.get(i).getTeachers().getName() != null){
                nameTeas.add(underList.get(i).getTeachers().getName());
            }
        }
        List<Teachers> jobTeachers = new ArrayList<>();
        List<Teachers> nameTeachers = new ArrayList<>();
        List<Teachers> updateTeachers = new ArrayList<>();
        if(jobTeas.size() > 0){
            jobTeachers = teachersMapper.selectTeasByJobnumber(jobTeas);
            for(int i = 0;i < underList.size();i++) {
                if(underList.get(i).getTeachers().getJobnumber() == null || underList.get(i).getTeachers().getJobnumber().equals("")){
                    continue;
                }
                for (int j = 0; j < jobTeachers.size(); j++) {
                    if (underList.get(i).getTeachers().getJobnumber().equals(jobTeachers.get(j).getJobnumber())) {
                        String tearole = jobTeachers.get(j).getRole();
                        if(!tearole.contains("10")){
                            tearole += ";10";
                            jobTeachers.get(j).setRole(tearole);
                            updateTeachers.add(jobTeachers.get(j));
                        }
                        underList.get(i).setTeachers(jobTeachers.get(j));
                        underList.get(i).setTutorID(jobTeachers.get(j).getID());
                        break;
                    }
                }
            }
        }
        if(nameTeas.size() > 0){
            nameTeachers = teachersMapper.selectTeasByName(nameTeas);
            for(int i = 0;i < underList.size();i++) {
                if(underList.get(i).getTeachers().getName() == null || underList.get(i).getTeachers().getName().equals("")){
                    continue;
                }
                for (int j = 0; j < nameTeachers.size(); j++) {
                    if (underList.get(i).getTeachers().getName().equals(nameTeachers.get(j).getName())) {
                        String tearole = nameTeachers.get(j).getRole();
                        if(!tearole.contains("10")){
                            tearole += ";10";
                            nameTeachers.get(j).setRole(tearole);
                            updateTeachers.add(nameTeachers.get(j));
                        }
                        underList.get(i).setTeachers(nameTeachers.get(j));
                        underList.get(i).setTutorID(nameTeachers.get(j).getID());
                        break;
                    }
                }
            }
        }

        //拿到已经存在的本科生列表的studentid
        List<Integer> updateInter = underGraduateMapper.check(underList);
        List<UnderGraduate> updateUnders = new ArrayList<>();
        List<UnderGraduate> insertUnder = new ArrayList<>();
        //有已经存在的本科生了
        if(updateInter.size() != 0){
            for(int i = 0;i < underList.size();i++){
                //不在更新列表中，说明本科生表里没有这个数据
                if(updateInter.indexOf(underList.get(i).getStudentID()) == -1){
                    insertUnder.add(underList.get(i));
                }else {
                    updateUnders.add(underList.get(i));
                }
            }
        }else {
            insertUnder = underList;
        }
        try {
//            if(updateUnders.size() > 0){
//                underGraduateMapper.updateFROMImport(updateUnders);
//            }
            if(insertUnder.size() > 0){
                underGraduateMapper.insertFROMImport(insertUnder);
            }
            if(updateTeachers.size() > 0){//需要对老师的role字段进行更新
                teachersMapper.updateRoleOfTeachers(updateTeachers);
            }
        }catch (Exception e){
            return RespBean.error("error");
        }
        return RespBean.ok("ok");
    }

    public List<UnderGraduate> getUnderStudent(){
        List<UnderGraduate> res = new ArrayList<>();
        try {
             res = underGraduateMapper.getUnderStudent();
        }catch (Exception e){
        }
        return res;
    }
    public Msg deleteUnderStudent(UnderGraduate under){
        try {
            underGraduateMapper.deleteUnderStudent(under);
            //如果没有导师就直接删除
            if(under.getTutorID() != null && !under.getTutorID().equals("")){
                //说明只是这一个学生的本科生老师，需要去掉这个老师的本科生导师角色
                if(underGraduateMapper.checkHaveStudentOftutorID(under.getTutorID(),under.getID()) == 0){
                    Teachers tea = teachersMapper.selectByPrimaryId(under.getTutorID());
                    String r = tea.getRole();
                    //区分要不要删掉多余分号，我懒得思考了，就这样吧，好累，有bug再说
                    if(r.contains("10;")){
                        r = r.replace("10;","");
                        tea.setRole(r);
                        teachersMapper.updateRoleOfOneTeacher(tea);
                    }else if(r.contains("10")){
                        r = r.replace("10","");
                        tea.setRole(r);
                        teachersMapper.updateRoleOfOneTeacher(tea);
                    }
                }
            }
            if(graduateStudentMapper.checkHaveStudentOfstudenID(under.getStudentID()) == 0 &&
                    participatesMapper.isParticipants(under.getStudentID()) == 0){
                //如果在选手表中和研究生表中查不到关于这个stuid关联的数据，说明可以删除
                studentMapper.deleteStudent(under.getStudentID());
            }
        }catch (Exception e){
            return Msg.fail();
        }return Msg.success();
    }
    public RespBean editUnderStudent(UnderGraduate under){
        //目的不在于更改老师表中的信息
        //直接限定老师名字和工号必须填写了
        try {
            Teachers tea = null;
            //先判断有没有这个导师的存在
            if(under.getTeachers().getJobnumber() != null && !under.getTeachers().getJobnumber().equals("")){
                tea = teachersMapper.selectTeaByJobnumber(under.getTeachers().getJobnumber());
                if(tea == null){//有工号，但是没有查到，说明工号错误
                    return RespBean.error("未查询到该老师");
                }
                //如果根据工号查到了这个老师，但是数据库的老师名字和前端传来的老师名字不同，说明信息填写错误
                if(under.getTeachers().getName()!= null && !under.getTeachers().getName().equals("") && !tea.getName().equals(under.getTeachers().getName())){
                    return RespBean.error("该老师工号和姓名不符，请仔细检查信息");
                }
                under.setTutorID(tea.getID());//重新设置导师id
                underGraduateMapper.editUnderStudent(under);
            }
        }catch (Exception e){
            return RespBean.error("处理失败");
        }return RespBean.ok("更新成功");
    }
    public RespBean getTeaNamesBySelect(String teaName){
        List<Teachers> res;
        try {
            res = underGraduateMapper.getTeaNamesBySelect(teaName);
        }catch (Exception e){
            return RespBean.error("error");
        }
        return RespBean.ok("ok",res);
    }
    public List<UnderGraduate> getUnderStudentsBySelect(Integer year,String teaName){
        List<UnderGraduate> res = null;
        try {
            res = underGraduateMapper.getUnderStudentsBySelect(teaName,year);
        }catch (Exception e){
        }
        return res;
    }
    public UnderGraduate getUnderByStuID(Integer studentID){
        return underGraduateMapper.getUnderByStuID(studentID);
    }
}
