package org.sys.rate.service.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.sys.rate.mapper.TeachersMapper;
import org.sys.rate.model.*;
import org.sys.rate.service.expert.ExpertService;

import java.util.ArrayList;
import java.util.List;

@Service
public class TeachersService implements UserDetailsService {

    @Autowired
    TeachersMapper teachersMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Teachers teacher = teachersMapper.loadUserByUsername(username);
        if (teacher == null) {
            throw new UsernameNotFoundException("用户名不存在!");
        }
        System.out.println("service.tea:"+teacher);
        return teacher;
    }
    public Teachers selectTeachersById(Long ID){
        return teachersMapper.selectTeachersById(ID);
    }

    /**
     * 查询老师列表
     *
     * @param teachers 老师
     * @return 老师集合
     */
    public List<Teachers> selectTeachersList(Teachers teachers){
        return teachersMapper.selectTeachersList(teachers);
    }

    /**
     * 新增老师
     *
     * @param teachers 老师
     * @return 结果
     */
    public int insertTeachers(Teachers teachers){
        return teachersMapper.insertTeachers(teachers);
    }

    /**
     * 修改老师
     *
     * @param teachers 老师
     * @return 结果
     */
    public int updateTeachers(Teachers teachers){
      return   teachersMapper.updateTeachers(teachers);
    }

    /**
     * 删除老师
     *
     * @param ID 老师ID
     * @return 结果
     */
    public int deleteTeachersById(Long ID){
        return teachersMapper.deleteTeachersById(ID);
    }

    public List<Teachers> selectList(){
        return teachersMapper.selectList();
    }
    public RespBean addTeachers(List<Teachers> teachers,String role) {
        //如果更新role，deleteflag，用户名，密码，身份证不做处理
        //返回的是已经存在的列表
        List<Teachers> checkTeachers = teachersMapper.check(teachers);
        List<String> checkIDNumbers = new ArrayList<>();
        List<Teachers> updateTeas = new ArrayList<>();
        List<Teachers> insertTeas = new ArrayList<>();
        //有已经存在的老师了
        if(checkTeachers.size() != 0){
            for(Teachers i : checkTeachers){
                checkIDNumbers.add(i.getIDNumber());
            }
            for(int i = 0;i < teachers.size();i++){
                //不在更新列表中，说明表里没有这个数据
                if(checkIDNumbers.indexOf(teachers.get(i).getIDNumber()) == -1){
//                    if(teachers.get(i).getUsername() == null || teachers.get(i).getUsername().equals(""))
//                    {//为空
//                        teachers.get(i).setUsername(teachers.get(i).getPhone());
//                    }
//                    String encodePass;
//                    if(teachers.get(i).getPassword() == null || teachers.get(i).getPassword().equals(""))
//                    {//为空
//                        encodePass = ExpertService.sh1(teachers.get(i).getPhone());
//                    }
//                    else
//                    {//默认密码为手机号
//                        encodePass = ExpertService.sh1(teachers.get(i).getPassword());
//                    }
//                    teachers.get(i).setPassword(encodePass);
                    insertTeas.add(teachers.get(i));
                }else {
                    //如果teacher表中有这个数据，用户名和密码还是用之前的
                    Teachers temp = checkTeachers.get(checkIDNumbers.indexOf(teachers.get(i).getIDNumber()));
                    teachers.get(i).setUsername(temp.getUsername());
                    teachers.get(i).setPassword(temp.getPassword());
                    teachers.get(i).setRole(role);
                    updateTeas.add(teachers.get(i));
                }
            }
        }else {
            insertTeas = teachers;
        }
        //设置角色和密码
        for(Teachers i : insertTeas){
            if(i.getUsername() == null || i.getUsername().equals(""))
            {//为空
                i.setUsername(i.getPhone());
            }
            String encodePass;
            if(i.getPassword() == null || i.getPassword().equals(""))
            {//为空
                encodePass = ExpertService.sh1(i.getPhone());
            }
            else
            {//默认密码为手机号
                encodePass = ExpertService.sh1(i.getPassword());
            }
            i.setPassword(encodePass);
            i.setRole(role);
        }
        try{
            if(checkTeachers.size() != 0) {
                teachersMapper.updateFROMImport(updateTeas);
            }
            if(insertTeas.size() > 0){
                teachersMapper.insertFROMImport(insertTeas);
            }
        }catch (Exception e) {
            return RespBean.error("error");
        }
        return RespBean.ok("ok");
    }
}
