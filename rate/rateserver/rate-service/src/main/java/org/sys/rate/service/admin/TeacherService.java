package org.sys.rate.service.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.sys.rate.mapper.TeacherMapper;
import org.sys.rate.model.RespPageBean;
import org.sys.rate.model.Student;
import org.sys.rate.model.Teacher;
import org.sys.rate.model.Teachers;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

@Service
public class TeacherService implements UserDetailsService {
    @Autowired
    TeacherMapper TeacherMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //accountservice loadUserByUsername
        Teacher teacher = TeacherMapper.loadUserByUsername(username);
        if (teacher == null) {
            throw new UsernameNotFoundException("用户名不存在!");
        }
//        student.setRoles(StudentMapper.getAdminRolesById(student.getId()));
        System.out.println("service.tea:"+teacher);
        return teacher;
    }
    public List<Teacher> getAllTeacher() {
        return TeacherMapper.getTotal();
    }

    public Teacher getById(Integer ID){
        Teacher tea = TeacherMapper.getById(ID);
        if(tea != null){
            return tea;
        }
        return null;
    }
    public Integer addTeacher(Teacher record) {
        int result = TeacherMapper.insert(record);
        return result;
    }

    public Integer deleteTeacher(Teacher record) {
        int result = TeacherMapper.delete(record);
        return result;
    }

    public Integer updateTeacher(Teacher record) {
        int result = TeacherMapper.update(record);
        return result;
    }
}
