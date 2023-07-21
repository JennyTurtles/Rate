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
    StudentMapper studentMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //accountservice loadUserByUsername
        Student student = studentMapper.loadUserByUsername(username);
        if (student == null) {
            throw new UsernameNotFoundException("用户名不存在!");
        }
//        student.setRoles(StudentMapper.getAdminRolesById(student.getId()));
//        System.out.println("service.stu:"+student.getTeachers().getName());
        return student;
    }
    //通过ID查找学生信息
    public Student getById(Integer ID){
        Student stu = studentMapper.getById(ID);
        if(stu != null){
            return stu;
        }
        return null;
    }
    public List<Student> getAllStudent() {
        return studentMapper.getTotal();
    }
    public Student getStuByIDNumber(String IDNumber){
        return studentMapper.getStuByIDNumber(IDNumber);
    }

    public Integer addStudent(Student record) {
        int result = studentMapper.insert(record);
        return result;
    }

    public Integer deleteStudent(Student record) {
        int result = studentMapper.delete(record);
        return result;
    }

    public Integer updateStudent(Student record) {
        int result = studentMapper.update(record);
        return result;
    }
    public List<Student> selectList(){
        return studentMapper.selectList();
    }

    //   要注册为研究生，就根据用户输入的学号去研究生表里查询
    //   如果没查到就在研究生表添加记录
    //   如果查到了就更新那条记录的studentID，然后去学生表把老的studentID删掉
    public void registerGraduate(Student student) {
        Integer studentID = studentMapper.getGraduateByStudentNumber(student.getStudentnumber());
        if(studentID == null){ // 研究生表无记录
            studentMapper.registerGraduate(student);
        }else if (!studentID.equals(student.getID())){ // 检查该记录的studentID是否与当前用户ID相同
            studentMapper.updateGraduateStudentID(studentID,student.getID());
            studentMapper.deleteStudent(studentID);
        }
        studentMapper.update(student);
    }

    public void registerUndergraduate(Student student) {
        Integer studentID = studentMapper.getUndergraduateByStudentNumber(student.getStudentnumber());
        if(studentID == null){ // 研究生表无记录
            studentMapper.registerUndergraduate(student);
        }else if (!studentID.equals(student.getID())){ // 检查该记录的studentID是否与当前用户ID相同
            studentMapper.updateUndergraduateStudentID(studentID,student.getID());
            studentMapper.deleteStudent(studentID);
        }
        studentMapper.update(student);
    }
}

