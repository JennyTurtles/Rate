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
        boolean stufalg = false;
        boolean underflag = false;
//怎么判断学生表里是否有这个学生了？凭一个身份证判断可能会有多种角色，选手本科生研究生
        for (Student stu : stuList) {
            Student ss = studentMapper.checkAndReturnID(stu.getIDNumber());
            if (ss != null) {//存在这条数据
                if(stu.getUsername().equals("") || stu.getUsername() == null)
                {//不为空
                    stu.setUsername(null);
                }
                if(!stu.getPassword().equals(""))
                {//不为空
                    String encodePass = stu.getPassword();
                    stu.setPassword(ExpertService.sh1(encodePass));
                }
                else
                {//如果没有密码，更新的时候会被忽略
                    stu.setPassword(null);
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
        for(int i = 0;i < underList.size();i++){
            underList.get(i).setStudentID(stuList.get(i).getID());
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
            underGraduateMapper.updateFROMImport(updateUnders);
        }else {
            insertUnder = underList;
        }
        if(underGraduateMapper.insertFROMImport(insertUnder) > 0){
            underflag = true;
        }
        if(underflag && stufalg){
            return RespBean.ok("ok");
        }
        return RespBean.error("error");
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
}
