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
        boolean stufalg = false;
        boolean graduateflag = false;
        for (Student stu : stuList) {
            Student ss = studentMapper.checkAndReturnID(stu.getIDNumber());
            if (ss != null) {//存在这条数据
                if(stu.getUsername() == null || stu.getUsername().equals(""))
                {//不为空
                    stu.setUsername(null);
                }
                if(stu.getPassword() == null || stu.getPassword().equals(""))
                {//如果没有密码，更新的时候会被忽略
                    stu.setPassword(null);
                }else {
                    String encodePass = stu.getPassword();
                    stu.setPassword(ExpertService.sh1(encodePass));
                }
                System.out.println("student表中已经有该选手信息。");
                //更新name和instituteId，如果不是null,新增username和password
                int insert0=studentMapper.updateFROMImport(stu);
                if(insert0 > 0){
                    stufalg = true;
                    stu.setID(ss.getID());
                }
            } else {
                if(stu.getUsername() == null || stu.getUsername().equals(""))
                {//为空
                    stu.setUsername(stu.getTelephone());
                }
                String encodePass;
                if(stu.getPassword() == null || stu.getPassword().equals(""))
                {//为空
                    encodePass = ExpertService.sh1(stu.getTelephone());
                }
                else
                {//默认密码为手机号
                    encodePass = ExpertService.sh1(stu.getPassword());
                }
                stu.setPassword(encodePass);
                //如果没有就插入选手信息
                int insert0 = studentMapper.insertStuFromExcel(stu);
                stufalg = true;
            }
        }
        //对本科生循环 设置stuid
        for(int i = 0;i < graduateList.size();i++){
            graduateList.get(i).setStudentID(stuList.get(i).getID());
        }
        List<Integer> updateInter = graduateStudentMapper.check(graduateList);
        List<GraduateStudent> updateGraduates = new ArrayList<>();
        List<GraduateStudent> insertGraduates = new ArrayList<>();
        //有已经存在的研究生了
        if(updateInter.size() != 0){
            for(int i = 0;i < graduateList.size();i++){
                //不在更新列表中，说明本科生表里没有这个数据
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
}
