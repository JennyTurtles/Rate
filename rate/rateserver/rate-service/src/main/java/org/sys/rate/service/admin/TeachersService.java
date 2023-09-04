package org.sys.rate.service.admin;

import cn.hutool.core.util.StrUtil;
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
        System.out.println("service.tea:" + teacher);
        return teacher;
    }

    public Teachers selectTeachersById(Long ID) {
        return teachersMapper.selectTeachersById(ID);
    }

    /**
     * 查询老师列表
     *
     * @param teachers 老师
     * @return 老师集合
     */
    public List<Teachers> selectTeachersList(Teachers teachers) {
        return teachersMapper.selectTeachersList(teachers);
    }

    /**
     * 新增老师
     *
     * @param teachers 老师
     * @return 结果
     */
    public int insertTeachers(Teachers teachers) {
        return teachersMapper.insertTeachers(teachers);
    }

    /**
     * 修改老师
     *
     * @param teachers 老师
     * @return 结果
     */
    public int updateTeachers(Teachers teachers) {
        return teachersMapper.updateTeachers(teachers);
    }

    /**
     * 删除老师
     *
     * @param ID 老师ID
     * @return 结果
     */
    public int deleteTeachersById(Long ID) {
        return teachersMapper.deleteTeachersById(ID);
    }

    public List<Teachers> selectList() {
        return teachersMapper.selectList();
    }

    public List<Teachers> getTeachers(String teaName) {
        List<Teachers> res = new ArrayList<>();
        try {
            res = teachersMapper.getTeachers(teaName);
        } catch (Exception e) {
        }
        return res;
    }

    public RespBean getTeaNamesBySelect(String teaName) {
        List<String> res;
        try {
            res = teachersMapper.getTeaNamesBySelect(teaName);
        } catch (Exception e) {
            return RespBean.error("error");
        }
        return RespBean.ok("ok", res);
    }

    public RespBean addTeachers(List<Teachers> teachers) {
        //返回的是已经存在的列表
        List<Teachers> checkTeachers = teachersMapper.check(teachers);
        List<String> checkIDNumbers = new ArrayList<>();
        List<Teachers> insertTeas = new ArrayList<>();
        //有已经存在的老师了
        if (checkTeachers.size() != 0) {
            for (Teachers i : checkTeachers) {
                checkIDNumbers.add(i.getIDNumber());
            }
            for (int i = 0; i < teachers.size(); i++) {
                //不在更新列表中，说明表里没有这个数据
                if (checkIDNumbers.indexOf(teachers.get(i).getIDNumber()) == -1) {
                    insertTeas.add(teachers.get(i));
                }
            }
        } else {
            insertTeas = teachers;
        }
        try {
            if (insertTeas.size() > 0) {
                teachersMapper.insertFROMImport(insertTeas);
            }
        } catch (Exception e) {
            return RespBean.error("error");
        }
        return RespBean.ok("ok");
    }

    public Integer checkTeacherExist(String teacherJobNumber, String teacherName, Integer institutionID) {
        List<Integer> ids = teachersMapper.getIdByName(teacherName, institutionID);
        if(ids.size()==0){
            return null;
        }
        if (StrUtil.isBlank(teacherJobNumber) && ids.size()>1) {
            return -1;
        }
        Integer idsBasedOnJobNumber = teachersMapper.checkTeacherExist(teacherJobNumber, teacherName, institutionID);
        return idsBasedOnJobNumber == null ? -2 : idsBasedOnJobNumber;
    }
}
