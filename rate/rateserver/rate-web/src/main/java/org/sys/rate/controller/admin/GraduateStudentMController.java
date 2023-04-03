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
    public RespBean importUnderGraduate(MultipartFile file) throws ParseException {
        Map<String,List> mm = POIUtils.readExcel_graduatestudent(file);
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
    public Msg getUnderStudent(){
        return graduateStudentService.getGraduatesStudent();
    }
    @PostMapping("/deleteGraduateStudent")
    public Msg deleteGraduateStudent(@RequestBody GraduateStudent grad){
        return graduateStudentService.deleteGraduateStudent(grad);
    }
}
