package org.sys.rate.service.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.sys.rate.mapper.StudentMapper;
import org.sys.rate.mapper.UnderGraduateMapper;
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
    UnderGraduateMapper underGraduateMapper;
    //管理员导入本科生
    public RespBean addUnderGraduate( List<UnderGraduate> underList,List<Student> stuList) {
        //怎么判断学生表里是否有这个学生了？凭一个身份证判断可能会有多种角色，选手本科生研究生
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
        //对本科生循环 设置stuid
        for(int i = 0;i < underList.size();i++){
            for(int j = 0;j < newStuList.size();j++){
                if(underList.get(i).getIDNumber().equals(newStuList.get(j).getIDNumber())){
                    underList.get(i).setStudentID(newStuList.get(j).getID());
                    break;
                }
            }
        }
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
            if(updateUnders.size() > 0){
                underGraduateMapper.updateFROMImport(updateUnders);
            }
            if(insertUnder.size() > 0){
                underGraduateMapper.insertFROMImport(insertUnder);
            }
        }catch (Exception e){
            return RespBean.error("error");
        }
            return RespBean.ok("ok");
    }

    public Msg getUnderStudent(){
        List<UnderGraduate> res = new ArrayList<>();
        try {
             res = underGraduateMapper.getUnderStudent();
        }catch (Exception e){
            return Msg.fail();
        }
        return Msg.success().add("res",res);
    }
    public Msg deleteUnderStudent(UnderGraduate under){
        try {
            underGraduateMapper.deleteUnderStudent(under);
        }catch (Exception e){
            return Msg.fail();
        }return Msg.success();
    }
}
