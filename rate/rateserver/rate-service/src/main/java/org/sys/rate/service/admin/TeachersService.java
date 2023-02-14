package org.sys.rate.service.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.sys.rate.mapper.TeachersMapper;
import org.sys.rate.model.Teacher;
import org.sys.rate.model.Teachers;

import java.util.List;

@Service
public class TeachersService implements UserDetailsService {

    @Autowired
    private TeachersMapper teachersMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //accountservice loadUserByUsername
        Teachers teacher = teachersMapper.loadUserByUsername(username);
        if (teacher == null) {
            throw new UsernameNotFoundException("用户名不存在!");
        }
//        student.setRoles(StudentMapper.getAdminRolesById(student.getId()));
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


}
