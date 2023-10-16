package org.sys.rate.controller.admin;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.sys.rate.mapper.DoctorMapper;
import org.sys.rate.mapper.StudentMapper;
import org.sys.rate.model.*;
import org.sys.rate.service.admin.DoctorService;
import org.sys.rate.utils.POIUtils;

import javax.annotation.Resource;
import javax.print.Doc;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/doctorM/basic")
public class DoctorController {
    @Resource
    DoctorMapper doctorMapper;
    @Resource
    DoctorService doctorService;
    @Resource
    StudentMapper studentMapper;


    @GetMapping("/exportDoctor")
    public ResponseEntity<byte[]> downloadExample_Participants_exportMoPar_Group(HttpServletResponse response){
        return POIUtils.writeDoctorStudent();
    }
    @PostMapping("/update")
    public RespBean updateStudent(@RequestBody Doctor record) {
        if (doctorMapper.checkHaveStudentOfStuNumber(record.getInstitutionID(),record.getStuNumber(),record.getStudentID()) == 1){
            return RespBean.error("学号已存在，请重新修改或联系管理员！");
        }
        if (studentMapper.update(record) == 1){
            if (doctorMapper.update(record) == 1) {
                return RespBean.ok("更新成功!");
            }
        }
        return RespBean.error("更新失败!");
    }
    @GetMapping("/getDoctorStudents")
    public Msg getGraduateStudents(@RequestParam("pageNum") Integer pageNum, @RequestParam("pageSize") Integer pageSize) {
        Page page = PageHelper.startPage(pageNum, pageSize); // 设置当前所在页和每页显示的条数
        List<Doctor> t = doctorMapper.getDoctorStudents();
        PageInfo info = new PageInfo<>(page.getResult());
        Object[] res = {t, info.getTotal()}; // res是分页后的数据，info.getTotal()是总条数
        return Msg.success().add("res", res);
    }
    @GetMapping("/getDoctorsBySelect")
    public RespBean getDoctorsBySelect(@RequestParam("year")Integer year,@RequestParam("teaName")String teaName,@RequestParam("pageNum")Integer pageNum,@RequestParam("pageSize")Integer pageSize){
        Page page = PageHelper.startPage(pageNum, pageSize); // 设置当前所在页和每页显示的条数
        List<Doctor> t = doctorService.getDoctorsBySelect(year,teaName);
        PageInfo info = new PageInfo<>(page.getResult());
        Object[] res = {t, info.getTotal()}; // res是分页后的数据，info.getTotal()是总条数
        return RespBean.ok("ok", res);
    }
    @PostMapping("/importDoctors")
    public RespBean importUnderGraduate(Integer institutionID, MultipartFile file) throws ParseException {
        Map<String, List> mm = POIUtils.readExcel_doctrstudent(institutionID, file);
        List<Doctor> doctors = mm.get("doctorlist");
        if (doctors.size() == 0) { //先将excel中读取到的数据行拿出来，student和graduate列表是同样的数量才对
            return RespBean.error("未读取到有效导入数据");
        }
        RespBean res = doctorService.addDoctor(doctors);
        return res;
    }
    @PostMapping("/deleteDoctorStudent")
    public Msg deleteDoctorStudent(@RequestBody Doctor doctor){
        return doctorService.deleteDoctorStudent(doctor);
    }
    @PostMapping("/editDoctorStudent")
    public RespBean editDoctorStudent(@RequestBody Doctor doctor){
        return doctorService.editDoctorStudent(doctor);
    }

    @PostMapping("/updateScore") //加法
    public RespBean updateScoreAdd(@RequestBody Doctor record) {
        Integer res = doctorMapper.updateScore(Long.valueOf(record.getStudentID().intValue()),Long.parseLong(record.getPoint()));
        return RespBean.ok("ok", res);
    }
    @PostMapping("/updateScoreSub") //减法
    public RespBean updateScoreSub(@RequestBody Doctor record) {
        Integer res = doctorMapper.updateScoreSub(Long.valueOf(record.getStudentID().intValue()),Long.parseLong(record.getPoint()));
        return RespBean.ok("ok", res);
    }
}
