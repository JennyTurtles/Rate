package org.sys.rate.service.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.sys.rate.mapper.StudentMapper;
import org.sys.rate.model.Admin;
import org.sys.rate.model.RespPageBean;
import org.sys.rate.model.Student;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;


@Service
public class StudentService implements UserDetailsService {
    @Autowired
    StudentMapper StudentMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //accountservice loadUserByUsername
        Student student = StudentMapper.loadUserByUsername(username);
        if (student == null) {
            throw new UsernameNotFoundException("用户名不存在!");
        }
//        student.setRoles(StudentMapper.getAdminRolesById(student.getId()));
//        System.out.println("service.stu:"+student.getTeachers().getName());
        return student;
    }
    //通过ID查找学生信息
    public Student getById(Integer ID){
        Student stu = StudentMapper.getById(ID);
        if(stu != null){
            return stu;
        }
        return null;
    }
    public List<Student> getAllStudent() {
        return StudentMapper.getTotal();
    }

    public Integer addStudent(Student record) {
        int result = StudentMapper.insert(record);
        return result;
    }

    public Integer deleteStudent(Student record) {
        int result = StudentMapper.delete(record);
        return result;
    }

    public Integer updateStudent(Student record) {
        int result = StudentMapper.update(record);
        return result;
    }
    public List<Student> selectList(){
        return StudentMapper.selectList();
    }


}

