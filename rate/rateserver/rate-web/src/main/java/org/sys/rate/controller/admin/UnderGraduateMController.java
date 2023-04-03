package org.sys.rate.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.sys.rate.model.*;
import org.sys.rate.service.admin.LogService;
import org.sys.rate.service.admin.UnderGraduateService;
import org.sys.rate.utils.POIUtils;

import javax.servlet.http.HttpServletResponse;
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

    @GetMapping("/exportUnderGraduate")
    public ResponseEntity<byte[]> downloadExample_UnderGraduateStudents(HttpServletResponse response){
        return POIUtils.writeUnderGraduate();
    }
    @PostMapping("/importUnderGraduate")
    public RespBean importUnderGraduate(MultipartFile file) throws ParseException {
        Map<String,List> mm = POIUtils.readExcel_undergraduate(file);
        List<UnderGraduate> under = mm.get("underlist");
        List<Student> stu = mm.get("studentlist");
        if(under.size() == 0 || stu.size() == 0 || under.size() != stu.size())
        {
            return RespBean.error("未读取到有效导入数据");
        }
        RespBean res = underGraduateService.addUnderGraduate(under,stu);
        return res;
    }
    @GetMapping("/getUnderGraduateStudents")
    public Msg getUnderStudent(){
        return underGraduateService.getUnderStudent();
    }
    @PostMapping("/deleteUnderGraduateStudent")
    public Msg deleteUnderStudent(@RequestBody UnderGraduate under){
        return underGraduateService.deleteUnderStudent(under);
    }
}
