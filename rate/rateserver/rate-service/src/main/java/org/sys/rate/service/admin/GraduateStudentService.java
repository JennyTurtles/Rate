package org.sys.rate.service.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.sys.rate.mapper.GraduateStudentMapper;
import org.sys.rate.mapper.StudentMapper;
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
    GraduateStudentMapper graduateStudentMapper;
    //管理员导入研究生
    public RespBean addGraduate(List<GraduateStudent> graduateList, List<Student> stuList) {
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
        if(insertStus.size() > 0){
            for(Student i : insertStus){
                if(i.getUsername() == null || i.getUsername().equals(""))
                {//为空
                    i.setUsername(i.getTelephone());
                }
                String encodePass;
                if(i.getPassword() == null || i.getPassword().equals(""))
                {//为空
                    encodePass = ExpertService.sh1(i.getTelephone());
                }
                else
                {//默认密码为手机号
                    encodePass = ExpertService.sh1(i.getPassword());
                }
                i.setPassword(encodePass);
            }
        }
        List<Student> newStuList = new ArrayList<>();
        try{
            if(checkStudents.size() != 0) {
                studentMapper.updateFromAdminExcel(stuList);
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
        for(int i = 0;i < graduateList.size();i++){
            for(int j = 0;j < newStuList.size();j++){
                if(graduateList.get(i).getIDNumber().equals(newStuList.get(j).getIDNumber())){
                    graduateList.get(i).setStudentID(newStuList.get(j).getID());
                    break;
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
            if(updateGraduates.size() > 0){
                graduateStudentMapper.updateFROMImport(updateGraduates);
            }
            if(insertGraduates.size() > 0){
                graduateStudentMapper.insertFROMImport(insertGraduates);
            }
        }catch (Exception e){
            return RespBean.error("error");
        }
        return RespBean.ok("ok");
    }

    public Msg getGraduatesStudent(){
        List<GraduateStudent> res = new ArrayList<>();
        try {
            res = graduateStudentMapper.getGraduateStudent();
        }catch (Exception e){
            return Msg.fail();
        }
        return Msg.success().add("res",res);
    }
    public Msg deleteGraduateStudent(GraduateStudent grad){
        try {
            graduateStudentMapper.deleteGraduateStudent(grad);
        }catch (Exception e){
            return Msg.fail();
        }return Msg.success();
    }
}
