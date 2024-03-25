package org.sys.rate.service.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.sys.rate.mapper.InfosMapper;
import org.sys.rate.mapper.ParticipatesMapper;
import org.sys.rate.mapper.StudentMapper;
import org.sys.rate.model.*;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;


@Service
public class StudentService implements UserDetailsService {
    @Autowired
    StudentMapper studentMapper;
    @Autowired
    ParticipatesMapper participatesMapper;
    @Autowired
    InfosMapper infosMapper;

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
    //   如果没查到，就检查表里是否有该学生的studentID，如果没有，就添加一条记录，如果有，就直接修改该记录（防止恶意添加）
    //   如果查到了，核对姓名，然后更新那条记录的studentID，然后去学生表把老的studentID删掉
    public boolean registerGraduate(Student student) {
        Student studentInTable = studentMapper.getGraduateByStudentNumber(student.getStudentnumber());
        if(studentInTable == null){ // 研究生表无该学号
            Integer studentID = studentMapper.checkIDInGraduate(student.getID());
            if(studentID == null) // 研究生表无该学生ID
                studentMapper.registerGraduate(student);
            else // 该学生已经是研究生了，重复注册，但是学号改了。修改研究生表的信息。
                studentMapper.updateGraduate(student);
            studentMapper.update(student);
        }else if (!studentInTable.getID().equals(student.getID())){ // 查到了，但是studentID不是本人
            // 检查姓名是否一样，防止填错学号
            if (!studentInTable.getName().equals(student.getName())){
                return false;
            }
            Integer studentID = studentInTable.getID();
            studentMapper.updateGraduateStudentID(studentID,student.getID());
            studentMapper.updateGraduate(student);
            studentMapper.deleteStudent(studentID);
            studentMapper.update(student);
        }else {
            studentMapper.updateGraduate(student);
            studentMapper.update(student);
        }
        return true;
    }

    public boolean registerDoctor(Student student) {
        Student studentInTable = studentMapper.getDoctorByStudentNumber(student.getStudentnumber());
        if(studentInTable == null){ // 博士生表无该学号
            Integer studentID = studentMapper.checkIDInDoctor(student.getID());
            if(studentID == null) // 博士生表无该学生ID
                studentMapper.registerDoctor(student);
            else // 该学生已经是博士生了，重复注册，但是学号改了。修改博士生表的信息。
                studentMapper.updateDoctor(student);
            studentMapper.update(student);
        }else if (!studentInTable.getID().equals(student.getID())){ // 查到了，但是studentID不是本人
            // 检查姓名是否一样，防止填错学号
            if (!studentInTable.getName().equals(student.getName())){
                return false;
            }
            Integer studentID = studentInTable.getID();
            studentMapper.updateDoctorStudentID(studentID,student.getID());
            studentMapper.updateDoctor(student);
            studentMapper.deleteStudent(studentID);
            studentMapper.update(student);
        }else {
            studentMapper.updateDoctor(student);
            studentMapper.update(student);
        }
        return true;
    }

    public boolean registerUndergraduate(Student student) {
        Student studentInTable = studentMapper.getUndergraduateByStudentNumber(student.getStudentnumber());
        if(studentInTable == null){ // 本科生表无该学号
            Integer studentID = studentMapper.checkIDInUndergraduate(student.getID());
            if(studentID == null) // 本科生表无该学生ID
                studentMapper.registerUndergraduate(student);
            else // 该学生已经是本科生了，重复注册，但是学号改了。修改研究生表的信息。
                studentMapper.updateUnderGraduate(student);
            studentMapper.update(student);
        }else if (!studentInTable.getID().equals(student.getID())){ // 查到了，但是studentID不是本人
            // 检查姓名是否一样，防止填错学号
            if (!studentInTable.getName().equals(student.getName())){
                return false;
            }
            Integer studentID = studentInTable.getID();
            studentMapper.updateUndergraduateStudentID(studentID,student.getID());
            studentMapper.updateUnderGraduate(student);
            studentMapper.deleteStudent(studentID);
            studentMapper.update(student);
        }else {
            studentMapper.updateUnderGraduate(student);
            studentMapper.update(student);
        }
        return true;
    }
    public RespBean registerParticipant(Student student, Integer activityID) {
        Participates participates = participatesMapper.getByCodeAndAID(activityID, student.getStudentnumber());
        if(participates == null){ // 活动表无该编号
            Integer studentID = participatesMapper.checkStudentID(student.getID(),activityID);
            if(studentID == null) // 活动表无该学生ID
                participatesMapper.addPar(student.getID(), activityID, student.getStudentnumber());
            else // 该学生已经是活动选手了，重复注册，但是编号改了。修改活动表的信息。
                studentMapper.updateUnderGraduate(student);
        }else if (!participates.getStudentID().equals(student.getID())){ // 查到了，但是studentID不是本人
            // 检查姓名是否一样，防止填错编号
            if (!studentMapper.getNameByID(participates.getStudentID()).equals(student.getName())){
                RespBean.error("请检查编号是否填写正确，若填写正确请联系管理员");
            }
            participatesMapper.updateStudentID(student.getID(),participates.getStudentID(),activityID);
            studentMapper.deleteStudent(participates.getStudentID());
        }else {
            RespBean.error("您已经在该活动注册");
        }
        studentMapper.update(student);
        return RespBean.ok("注册成功");
    }

    public UnderGraduate getByUndergraduateId(Integer studentID) {
        return studentMapper.getByUndergraduateId(studentID);
    }
}

