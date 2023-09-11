package org.sys.rate.service.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.sys.rate.mapper.TeacherMapper;
import org.sys.rate.model.Teacher;

import java.util.List;

@Service
public class TeacherService implements UserDetailsService {
    @Autowired
    TeacherMapper teacherMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //accountservice loadUserByUsername
        Teacher teacher = teacherMapper.loadUserByUsername(username);
        if (teacher == null) {
            throw new UsernameNotFoundException("用户名不存在!");
        }
//        student.setRoles(StudentMapper.getAdminRolesById(student.getId()));
        System.out.println("service.tea:"+teacher);
        return teacher;
    }
    public List<Teacher> getAllTeacher() {
        return teacherMapper.getTotal();
    }

    public Teacher getById(Integer ID){
        Teacher tea = teacherMapper.getById(ID);
        if(tea != null){
            return tea;
        }
        return null;
    }

    public Teacher getByEmail(String email){
        Teacher tea = teacherMapper.getByEmail(email);
        if(tea != null){
            return tea;
        }
        return null;
    }

    public Integer addTeacher(Teacher record) {
        int result = teacherMapper.insert(record);
        return result;
    }

    public Integer deleteTeacher(Teacher record) {
        int result = teacherMapper.delete(record);
        return result;
    }

    public Integer updateTeacher(Teacher record) {
        int result = teacherMapper.update(record);
        return result;
    }

    public Teacher getByStudentId(Long studentID) {
        return teacherMapper.getByStudentId(studentID);
    }
}
