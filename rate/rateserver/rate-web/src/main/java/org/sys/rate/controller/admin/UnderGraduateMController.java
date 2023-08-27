package org.sys.rate.controller.admin;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.sys.rate.mapper.StudentMapper;
import org.sys.rate.mapper.UnderGraduateMapper;
import org.sys.rate.model.*;
import org.sys.rate.service.admin.LogService;
import org.sys.rate.service.admin.UnderGraduateService;
import org.sys.rate.service.expert.ExpertService;
import org.sys.rate.utils.POIUtils;
import org.sys.rate.utils.ReadExcel;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/undergraduateM/basic")
public class UnderGraduateMController {
    @Autowired
    LogService logService;
    @Autowired
    UnderGraduateService underGraduateService;
    @Autowired
    StudentMapper studentMapper;
    @Autowired
    UnderGraduateMapper underGraduateMapper;

    @GetMapping("/exportUnderGraduate")
    public ResponseEntity<byte[]> downloadExample_UnderGraduateStudents(@RequestParam("type") String type, HttpServletResponse response) {
        return POIUtils.writeUnderGraduate(type);
    }

    @PostMapping("/importUnderGraduate")
    public RespBean importUnderGraduate(Integer institutionID, MultipartFile file) throws ParseException {
        Map<String, List> mm = POIUtils.readExcel_undergraduate(institutionID, file);
        List<UnderGraduate> under = mm.get("underlist");
        List<Student> stu = mm.get("studentlist");
        if (under.size() == 0 || stu.size() == 0 || under.size() != stu.size()) {
            return RespBean.error("未读取到有效导入数据");
        }
        RespBean res = underGraduateService.addUnderGraduate(under, stu);
        return res;
    }

    @GetMapping("/getUnderGraduateStudents")
    public Msg getUnderStudent(@RequestParam("pageNum") Integer pageNum, @RequestParam("pageSize") Integer pageSize) {
        Page page = PageHelper.startPage(pageNum, pageSize); // 设置当前所在页和每页显示的条数
        List<UnderGraduate> t = underGraduateService.getUnderStudent();
        PageInfo info = new PageInfo<>(page.getResult());
        Object[] res = {t, info.getTotal()}; // res是分页后的数据，info.getTotal()是总条数
        return Msg.success().add("res", res);
    }

    @PostMapping("/deleteUnderGraduateStudent")
    public Msg deleteUnderStudent(@RequestBody UnderGraduate under) {
        return underGraduateService.deleteUnderStudent(under);
    }

    @PostMapping("/editUnderGraduateStudent")
    public RespBean editUnderStudent(@RequestBody UnderGraduate under) {
        return underGraduateService.editUnderStudent(under);
    }

    //根据页面的筛选框进行查找本科生信息
    @GetMapping("/getTeaNamesBySelect")
    public RespBean getTeaNamesBySelect(String teaName) {
        return underGraduateService.getTeaNamesBySelect(teaName);
    }

    @GetMapping("/getUnderStudentsBySelect")
    public RespBean getUnderStudentsBySelect(@RequestParam("year") Integer year, @RequestParam("teaName") String teaName, @RequestParam("pageNum") Integer pageNum, @RequestParam("pageSize") Integer pageSize) {
        Page page = PageHelper.startPage(pageNum, pageSize); // 设置当前所在页和每页显示的条数
        List<UnderGraduate> t = underGraduateService.getUnderStudentsBySelect(year, teaName);
        PageInfo info = new PageInfo<>(page.getResult());
        Object[] res = {t, info.getTotal()}; // res是分页后的数据，info.getTotal()是总条数
        return RespBean.ok("ok", res);
    }

    @PostMapping("/resetUnderPassword")
    public RespBean resetUnderPassword(@RequestBody UnderGraduate under) {
        Integer res = 0;
        try {
            String p = ExpertService.sh1(under.getPassword());
            res = studentMapper.updatePassword(under.getStudentID(), p);
        } catch (Exception e) {
            return RespBean.error("error", null);
        }
        if (res > 0) {
            return RespBean.ok("ok", null);
        }
        return RespBean.error("error", null);
    }

    @PostMapping("/update")
    public RespBean updateStudent(@RequestBody UnderGraduate record) {
        if (underGraduateMapper.checkHaveStudentOfStuNumber(record.getInstitutionID(), record.getStuNumber(), record.getStudentID()) == 1) {
            return RespBean.error("学号已存在，请重新修改或联系管理员！");
        }
        if (studentMapper.update(record) == 1) {
            if (underGraduateMapper.update(record) == 1) {
                return RespBean.ok("更新成功!");
            }
        }
        return RespBean.error("更新失败!");
    }


    @PostMapping("/importThesis")
    public RespBean importThesis(@RequestParam("type") String type,
                                 @RequestParam("institutionID") Integer institutionID,
                                 @RequestParam("year") Integer year,
                                 @RequestParam("semester") String semester, MultipartFile file) {
        return underGraduateService.importThesis(type, institutionID, year, semester, file);
    }

    @GetMapping("/getStudents")
    public Msg getStudents(@RequestParam("institutionID") Integer institutionID,
                           @RequestParam("year") Integer year,
                           @RequestParam("semester") String semester,
                           @RequestParam("pageNum") Integer pageNum,
                           @RequestParam("pageSize") Integer pageSize) {
        Page page = PageHelper.startPage(pageNum, pageSize); // 设置当前所在页和每页显示的条数
        Integer month;
        if(semester.length()>1) {
             month= "春季".equals(semester) ? 3 : 9;
        }else {
            month= Integer.valueOf(semester);
            semester = 3 == month ? "春季" : "秋季";
        }
        List<UnderGraduate> student = underGraduateService.getStudent(institutionID, year, month);
        PageInfo info = new PageInfo<>(page.getResult());
        Object[] res = {student, info.getTotal()}; // res是分页后的数据，info.getTotal()是总条数
        // 再加上一个判断，该年该月该单位是否开启
        boolean havingStart = underGraduateMapper.havingStartThisThesis(institutionID, year, semester);
        return Msg.success().add("res", res).add("havingStart", havingStart);
    }

    @GetMapping("/getThesisExistDate")
    public RespBean getThesisExistDate(@RequestParam("institutionID") Integer institutionID) {
        return underGraduateService.getThesisExistDate(institutionID);
    }

    @PostMapping("/startThesis")
    public RespBean startThesis(@RequestParam("institutionID") @NotNull Integer institutionID,
                                @RequestParam("year") @NotNull Integer year,
                                @RequestParam("semester") @NotNull String semester) {
        return underGraduateService.startThesis(institutionID, year, semester);
    }

    @PutMapping("/updateUndergraduate")
    public RespBean updateUndergraduate(@RequestBody UnderGraduate under) {
        return underGraduateService.updateUndergraduate(under);
    }

    @PostMapping("/deleteThesis")
    public RespBean deleteThesis(@RequestParam("studentID") @NotNull Integer studentID,
                                 @RequestParam("tutorID") String tutorIDStr,
                                 @RequestParam("year") @NotNull Integer year,
                                 @RequestParam("month") @NotNull Integer month)  {
        Integer tutorID = tutorIDStr.equals("none") ? null : Integer.parseInt(tutorIDStr);

        UnderGraduate underGraduate = new UnderGraduate();
        underGraduate.setStudentID(studentID);
        underGraduate.setTutorID(tutorID);
        underGraduate.setYear(year);
        underGraduate.setMonth(month);

        return underGraduateService.deleteThesis(underGraduate);
    }

    @GetMapping("/getUngrouped")
    public RespBean getUngrouped(@RequestParam("year") Integer year,@RequestParam("month") Integer month) {
        List<Thesis> res = underGraduateMapper.getUngrouped(year,month);
        for (Thesis par:res){
            if (par.getGrade()==null)
                par.setGrade(0.00d);
        }
        return RespBean.ok("success",res);
    }

    @PostMapping("/createGroups")
    public String createGroup(@RequestBody Map<String,Object> data) {
        Integer year = (Integer) data.get("year");
        Integer month = (Integer) data.get("month");
        List<Integer> arr = (List<Integer>) data.get("arr");
        Integer exchangeNums = (Integer) data.get("exchangeNums");
        Integer groupsNums = (Integer) data.get("groupsNums");
        //List<Thesis> students = (List<Thesis>) data.get("students");
        List<Double> selectGrade = (List<Double>) data.get("selectGrade");
        return underGraduateService.createGroup(year,month,arr,exchangeNums,groupsNums,selectGrade);
        //返回分好组的选手信息
    }
}
