package org.sys.rate.service.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.sys.rate.mapper.GraduateStudentMapper;
import org.sys.rate.mapper.StudentMapper;
import org.sys.rate.mapper.TeachersMapper;
import org.sys.rate.mapper.UnderGraduateMapper;
import org.sys.rate.model.*;
import org.sys.rate.service.expert.ExpertService;

import java.util.ArrayList;
import java.util.List;

@Service
public class GraduateStudentService {
    @Autowired
    StudentMapper studentMapper;
    @Autowired
    TeachersMapper teachersMapper;
    @Autowired
    GraduateStudentMapper graduateStudentMapper;
    //管理员导入研究生，只添加，即使已经存在了该条数据也不更新
    public RespBean addGraduate(List<GraduateStudent> graduateList, List<Student> stuList) {
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
        List<Student> newStuList = new ArrayList<>();
        try{
            if(checkStudents.size() != 0) {
                newStuList.addAll(checkStudents);
            }
            if(insertStus.size() > 0){
                studentMapper.insertFromAdminExcel(insertStus);
                newStuList.addAll(insertStus);
            }
        }catch (Exception e) {
            return RespBean.error("error");
        }
        //对研究生循环 设置stuid
        List<String> jobTeas = new ArrayList<>();
        List<String> nameTeas = new ArrayList<>();
        for(int i = 0;i < graduateList.size();i++){
            for(int j = 0;j < newStuList.size();j++){//按照身份证号进行对比，赋值stuid
                if(graduateList.get(i).getIDNumber().equals(newStuList.get(j).getIDNumber())){
                    graduateList.get(i).setStudentID(newStuList.get(j).getID());
                    break;
                }
            }
            //工号和姓名都有按照工号来，都没有tutorid为空，只有姓名就按照姓名查找
            if(graduateList.get(i).getTeachers().getJobnumber() == null && graduateList.get(i).getTeachers().getName() == null){
                graduateList.get(i).setTutorID(null);
            }
            if(graduateList.get(i).getTeachers().getJobnumber() != null){
                jobTeas.add(graduateList.get(i).getTeachers().getJobnumber());
            }
            if(graduateList.get(i).getTeachers().getJobnumber() == null && graduateList.get(i).getTeachers().getName() != null){
                nameTeas.add(graduateList.get(i).getTeachers().getName());
            }
        }
        List<Teachers> jobTeachers = new ArrayList<>();
        List<Teachers> nameTeachers = new ArrayList<>();
        List<Teachers> updateTeachers = new ArrayList<>();
        if(jobTeas.size() > 0){
            jobTeachers = teachersMapper.selectTeasByJobnumber(jobTeas);
            for(int i = 0;i < graduateList.size();i++) {
                if(graduateList.get(i).getTeachers().getJobnumber() == null || graduateList.get(i).getTeachers().getJobnumber().equals("")){
                    continue;
                }
                for (int j = 0; j < jobTeachers.size(); j++) {
                    if (graduateList.get(i).getTeachers().getJobnumber().equals(jobTeachers.get(j).getJobnumber())) {
                        String tearole = jobTeachers.get(j).getRole();
                        if(!tearole.contains("11")){
                            tearole += ";11";
                            jobTeachers.get(j).setRole(tearole);
                            updateTeachers.add(jobTeachers.get(j));
                        }
                        graduateList.get(i).setTeachers(jobTeachers.get(j));
                        graduateList.get(i).setTutorID(jobTeachers.get(j).getID());
                        break;
                    }
                }
            }
        }
        if(nameTeas.size() > 0){
            nameTeachers = teachersMapper.selectTeasByName(nameTeas);
            for(int i = 0;i < graduateList.size();i++) {
                if(graduateList.get(i).getTeachers().getName() == null || graduateList.get(i).getTeachers().getName().equals("")){
                    continue;
                }
                for (int j = 0; j < nameTeachers.size(); j++) {
                    if (graduateList.get(i).getTeachers().getName().equals(nameTeachers.get(j).getName())) {
                        String tearole = nameTeachers.get(j).getRole();
                        if(!tearole.contains("11")){
                            tearole += ";11";
                            nameTeachers.get(j).setRole(tearole);
                            updateTeachers.add(nameTeachers.get(j));
                        }
                        graduateList.get(i).setTeachers(nameTeachers.get(j));
                        graduateList.get(i).setTutorID(nameTeachers.get(j).getID());
                        break;
                    }
                }
            }
        }
        List<Integer> updateInter = graduateStudentMapper.check(graduateList);
        List<GraduateStudent> updateGraduates = new ArrayList<>();
        List<GraduateStudent> insertGraduates = new ArrayList<>();
        //有已经存在的研究生了
        if(updateInter.size() != 0){
            for(int i = 0;i < graduateList.size();i++){
                //不在更新列表中，说明表里没有这个数据
                if(updateInter.indexOf(graduateList.get(i).getStudentID()) == -1){
                    insertGraduates.add(graduateList.get(i));
                }else {
                    updateGraduates.add(graduateList.get(i));
                }
            }
        }else {
            insertGraduates = graduateList;
        }
        try {
            if(insertGraduates.size() > 0){
                graduateStudentMapper.insertFROMImport(insertGraduates);
            }
            if(updateTeachers.size() > 0){//需要对老师的role字段进行更新
                teachersMapper.updateRoleOfTeachers(updateTeachers);
            }
        }catch (Exception e){
            return RespBean.error("error");
        }
        return RespBean.ok("ok");
    }

    public List<GraduateStudent> getGraduatesStudent(){
        List<GraduateStudent> res = new ArrayList<>();
        try {
            res = graduateStudentMapper.getGraduateStudent();
        }catch (Exception e){
        }
        return res;
    }
    public Msg deleteGraduateStudent(GraduateStudent grad){
        try {
            graduateStudentMapper.deleteGraduateStudent(grad);
        }catch (Exception e){
            return Msg.fail();
        }return Msg.success();
    }
    public RespBean editgraduateStudent(GraduateStudent graduate){
        //目的不在于更改老师表中的信息
        //直接限定老师名字和工号必须填写了
        try {
            Teachers tea = null;
            //先判断有没有这个导师的存在
            if(graduate.getTeachers().getJobnumber() != null && !graduate.getTeachers().getJobnumber().equals("")){
                tea = teachersMapper.selectTeaByJobnumber(graduate.getTeachers().getJobnumber());
                if(tea == null){//有工号，但是没有查到，说明工号错误
                    return RespBean.error("未查询到该老师");
                }
                //如果根据工号查到了这个老师，但是数据库的老师名字和前端传来的老师名字不同，说明信息填写错误
                if(graduate.getTeachers().getName()!= null && !graduate.getTeachers().getName().equals("") && !tea.getName().equals(graduate.getTeachers().getName())){
                    return RespBean.error("该老师工号和姓名不符，请仔细检查信息");
                }
                graduate.setTutorID(tea.getID());//重新设置导师id
                graduateStudentMapper.editGraduateStudent(graduate);
            }
        }catch (Exception e){
            return RespBean.error("处理失败");
        }return RespBean.ok("更新成功");
    }
    public RespBean getTeaNamesBySelect(String teaName){
        List<String> res;
        try {
            res = graduateStudentMapper.getTeaNamesBySelect(teaName);
        }catch (Exception e){
            return RespBean.error("error");
        }
        return RespBean.ok("ok",res);
    }
    public RespBean getGraduateStudentsBySelect(Integer year, String teaName){
        List<GraduateStudent> res;
        try {
            res = graduateStudentMapper.getGraduateStudentsBySelect(teaName,year);
        }catch (Exception e){
            return RespBean.error("error");
        }
        return RespBean.ok("ok",res);
    }
}
