package org.sys.rate.controller.admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.sys.rate.mapper.*;
import org.sys.rate.model.*;
import org.sys.rate.service.admin.GraduateStudentService;
import org.sys.rate.service.admin.OperationService;
import org.sys.rate.service.admin.StudentService;
import org.sys.rate.service.admin.UnderGraduateService;
import org.sys.rate.service.expert.ExpertService;

import javax.annotation.Resource;
import java.applet.AppletStub;
import java.util.List;

@RestController
@RequestMapping("/student/basic")
public class StudentBasicController {

    @Resource
    private OperationService operationService;
    @Resource
    private PatentMapper patentMapper;
    @Resource
    private PaperMapper paperMapper;
    @Resource
    private DecisionMapper decisionMapper;
    @Resource
    private StandardMapper standardMapper;
    @Resource
    private AwardMapper awardMapper;
    @Resource
    private CompetitionMapper competitionMapper;
    @Resource
    private MonographMapper monographMapper;
    @Resource
    private ProductMapper productMapper;
    @Resource
    private ProjectMapper projectMapper;

    @Autowired
    StudentService StudentService;
    @Autowired
    StudentMapper studentMapper;
    @Autowired
    TeacherMapper teacherMapper;
    @Autowired
    UnderGraduateService underGraduateService;
    @Autowired
    GraduateStudentService graduateStudentService;
    @Autowired
    GraduateStudentMapper graduateStudentMapper;
    @Autowired
    UnderGraduateMapper underGraduateMapper;
    @Autowired
    TeachersMapper teachersMapper;
    @Autowired
    DoctorMapper doctorMapper;

    @GetMapping("/getall")
    public List<Student> getAllStudent() {
        return StudentService.getAllStudent();
    }
    @GetMapping("/getAllAchievement") //获取该学生所有类别成果
    public Msg getAllAchievement(Integer studentId) {
        List<Patent> patentList = patentMapper.selectListByIds(studentId);
        List<Paper> paperList = paperMapper.selectListByIds(studentId);
        List<Project> projectList = projectMapper.selectProjectListById(studentId);
        List<Project> horizontalProjectList = projectMapper.selectHorizontalProjectListById(studentId);
        List<Product> productList = productMapper.selectListByIds(studentId);
        List<Monograph> monographList = monographMapper.selectMonographListById(studentId);
        List<Decision> decisionList = decisionMapper.selectDecisionListById(studentId);
        List<Standard> standardList = standardMapper.selectListByIds(studentId);
        List<Award> awardList = awardMapper.selectAwardListById(studentId);
        List<Competition> competitionList = competitionMapper.selectCompetitionListById(studentId);
        return Msg.success().add("patent", patentList)
                .add("paper", paperList).add("project", projectList).add("product", productList)
                .add("monograph", monographList).add("decision", decisionList).add("standard", standardList)
                .add("award", awardList).add("competition", competitionList).add("horizontalProject", horizontalProjectList);
    }

    @GetMapping("/getStuByIDNumber")
    public RespBean getStuByIDNumber(String IDNumber,String stuType) {
        Student res = StudentService.getStuByIDNumber(IDNumber);
        UnderGraduate under = null;
        GraduateStudent grad = null;
        //说明查询到了这个学生
        if(res != null){
            if(stuType != null && !stuType.equals("")){
                if(stuType.equals("本科生")){
                    under = underGraduateService.getUnderByStuID(res.getID());
                    if (under != null) return RespBean.ok("res",under);
                }else if(stuType.equals("研究生")){
                    grad = graduateStudentService.getGradByStuID(res.getID());
                    if (grad != null) return RespBean.ok("res",grad);
                }

            }
        }
        return RespBean.ok("res",res);
    }

    @PostMapping("/insert")
    public RespBean addStudent(@RequestBody Student record) {
        if (StudentService.addStudent(record) == 1) {
            return RespBean.ok("添加成功!");
        }
        return RespBean.error("添加失败!");
    }

    @PostMapping("/delete")
    public RespBean deleteStudent(@RequestBody Student record) {
        if (StudentService.deleteStudent(record) == 1) {
            return RespBean.ok("删除成功!");
        }
        return RespBean.error("删除失败!");
    }


    @PostMapping("/update")
    public RespBean updateStudent(@RequestBody Student record) {
        if (StudentService.updateStudent(record) == 1) {
            return RespBean.ok("更新成功!");
        }
        return RespBean.error("更新失败!");
    }
    @PostMapping("/updatePassword")
    public RespBean updatePassword(@RequestBody Student record) {
        String pass = ExpertService.sh1(record.getPassword());//加密
        if (studentMapper.updatePassword(record.getID(),pass) == 1) {
            return RespBean.ok("更新成功!");
        }
        return RespBean.error("更新失败!");
    }
    @GetMapping("/getTutorInfo")
    public RespBean getTutorInfo(Integer id) {
        return RespBean.ok("ok", teacherMapper.getTutorInfo(id));
    }

    @GetMapping("/getStuInfo")
    public RespBean getStuInfo(@RequestParam Integer id, @RequestParam String stuType){
        if (stuType.equals("本科生")){
            UnderGraduate underGraduate = underGraduateMapper.getUnderByStuID(id);
            underGraduate.setStuType("本科生");
            return RespBean.ok("success",underGraduate);
        }
        else if (stuType.equals("硕士生")){
            GraduateStudent graduateStudent = graduateStudentMapper.getGradByStuID(id);
            Teachers teacher = teachersMapper.selectByPrimaryId(graduateStudent.getTutorID());
            graduateStudent.setTeachers(teacher);
            graduateStudent.setStuType("硕士生");
            return RespBean.ok("success",graduateStudent);
        }
        else if (stuType.equals("博士生")){
            Doctor doctor = doctorMapper.getDocByStuID(id);
            Teachers teacher = teachersMapper.selectByPrimaryId(doctor.getTutorID());
            doctor.setTeachers(teacher);
            doctor.setStuType("博士生");
            return RespBean.ok("success",doctor);
        }
        else
        {
            Student student = studentMapper.getById(id);
            student.setStuType("活动选手");
            return RespBean.ok("success",student);
        }
    }
}


