package org.sys.rate.controller.admin;

import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.sys.rate.model.*;
import org.sys.rate.service.admin.TeacherService;
import org.sys.rate.service.admin.TeachersService;
import org.sys.rate.utils.POIUtils;

import javax.servlet.http.HttpServletResponse;
import java.applet.AppletStub;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/teacher/basic")
public class TeacherBasicController {
    @Autowired
    TeacherService TeacherService;
    @Autowired
    TeachersService teachersService;

    @GetMapping("/getall")
    public List<Teacher> getAllTeacher() {
        return TeacherService.getAllTeacher();
    }

    @PostMapping("/insert")
    public RespBean addStudent(@RequestBody Teacher record) {
        if (TeacherService.addTeacher(record) == 1) {
            return RespBean.ok("添加成功!");
        }
        return RespBean.error("添加失败!");
    }

    @PostMapping("/delete")
    public RespBean deleteTeacher(@RequestBody Teacher record) {
        if (TeacherService.deleteTeacher(record) == 1) {
            return RespBean.ok("删除成功!");
        }
        return RespBean.error("删除失败!");
    }


    @PostMapping("/update")
    public RespBean updateTeacher(@RequestBody Teacher record) {
        if (TeacherService.updateTeacher(record) == 1) {
            return RespBean.ok("更新成功!");
        }
        return RespBean.error("更新失败!");
    }
    //管理员下载教师列表的excel
    @GetMapping("/exportTeachersByAdmin")
    public ResponseEntity<byte[]> downloadExampleOfTeachers(HttpServletResponse response){
        return POIUtils.writeTeachers();
    }
//    管理员导入excel上传教师列表
    @PostMapping("/importTeachers")
    public RespBean importData(String role,MultipartFile file) throws IOException, ParseException {
        List<Teachers> teachersList= POIUtils.readExcel_teachers(file);
        RespBean res;
        if(teachersList == null || teachersList.size() == 0){
            return RespBean.error("未读取到有效导入数据");
        }else {
            //角色是单独传过来的
            try {
                 res = teachersService.addTeachers(teachersList,role);
            }catch (Exception e){
                return RespBean.error("fail",e);
            }
        }
        return RespBean.ok("success",res);
    }
}

