package org.sys.rate.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.sys.rate.mapper.GraduateStudentMapper;
import org.sys.rate.model.*;
import org.sys.rate.service.admin.GraduateStudentService;
import org.sys.rate.service.admin.LogService;
import org.sys.rate.service.admin.UnderGraduateService;
import org.sys.rate.utils.POIUtils;

import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/graduatestudentM/basic")
public class GraduateStudentMController {
    @Autowired
    LogService logService;
    @Autowired
    GraduateStudentService graduateStudentService;

    @GetMapping("/exportGraduate")
    public ResponseEntity<byte[]> downloadExample_Participants_exportMoPar_Group(HttpServletResponse response){
        return POIUtils.writeGraduateStudent();
    }
    @PostMapping("/importGraduate")
    public RespBean importUnderGraduate(Integer institutionID,MultipartFile file) throws ParseException {
        Map<String,List> mm = POIUtils.readExcel_graduatestudent(institutionID,file);
        List<GraduateStudent> graduate = mm.get("graduatelist");
        List<Student> stu = mm.get("studentlist");
        if(graduate.size() == 0 || stu.size() == 0 || graduate.size() != stu.size())
        {
            return RespBean.error("未读取到有效导入数据");
        }
        RespBean res = graduateStudentService.addGraduate(graduate,stu);
        return res;
    }
    @GetMapping("/getGraduateStudents")
    public Msg getGraduateStudents(){
        return graduateStudentService.getGraduatesStudent();
    }
    @PostMapping("/deleteGraduateStudent")
    public Msg deleteGraduateStudent(@RequestBody GraduateStudent grad){
        return graduateStudentService.deleteGraduateStudent(grad);
    }
    @PostMapping("/editGraduateStudent")
    public RespBean editGraduateStudent(@RequestBody GraduateStudent graduate){
        return graduateStudentService.editgraduateStudent(graduate);
    }
    //根据页面的筛选框进行查找老师姓名
    @GetMapping("/getTeaNamesBySelect")
    public RespBean getTeaNamesBySelect(String teaName){
        return graduateStudentService.getTeaNamesBySelect(teaName);
    }
    @GetMapping("/getGraduateStudentsBySelect")
    public RespBean getGraduateStudentsBySelect(Integer year, String teaName){
        return graduateStudentService.getGraduateStudentsBySelect(year,teaName);
    }
}
