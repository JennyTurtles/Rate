package org.sys.rate.controller.admin;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.sys.rate.mapper.TeacherMapper;
import org.sys.rate.mapper.TeachersMapper;
import org.sys.rate.model.*;
import org.sys.rate.service.admin.TeacherService;
import org.sys.rate.service.admin.TeachersService;
import org.sys.rate.service.expert.ExpertService;
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
    @Autowired
    TeacherMapper teacherMapper;
    @Autowired
    TeachersMapper teachersMapper;

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
    @PostMapping("/updateByInfo")
    public RespBean updateByInfo(@RequestBody Teacher record) {
        if (teacherMapper.updateByPrimaryKey(record) == 1) {
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
    public RespBean importData(Integer institutionID,MultipartFile file) throws IOException, ParseException {
        List<Teachers> teachersList= POIUtils.readExcel_teachers(institutionID,file);
        RespBean res;
        if(teachersList == null || teachersList.size() == 0){
            return RespBean.error("未读取到有效导入数据");
        }else {
            //角色是单独传过来的
            try {
                 res = teachersService.addTeachers(teachersList);
            }catch (Exception e){
                return RespBean.error("fail",e);
            }
        }
        return RespBean.ok("success",res);
    }
    @GetMapping("/getTeaNamesBySelect")
    public RespBean getTeaNamesBySelect(String teaName){
        return teachersService.getTeaNamesBySelect(teaName);
    }
    @GetMapping("/getTeachers")
    public Msg getGraduateStudents(@RequestParam("pageNum")Integer pageNum,@RequestParam("pageSize")Integer pageSize,
                                   @RequestParam("teaName")String teaName){
        Page page = PageHelper.startPage(pageNum, pageSize); // 设置当前所在页和每页显示的条数
        List<Teachers> t = teachersService.getTeachers(teaName);
        PageInfo info = new PageInfo<>(page.getResult());
        Object[] res = {t,info.getTotal()}; // res是分页后的数据，info.getTotal()是总条数
        return Msg.success().add("res",res);
    }
    @PostMapping("/updatePassword")
    public RespBean updatePassword(@RequestBody Teachers record) {
        String pass = ExpertService.sh1(record.getPassword());//加密
        if (teachersMapper.updatePassword(record.getID(),pass) == 1) {
            return RespBean.ok("更新成功!");
        }
        return RespBean.error("更新失败!");
    }
    @GetMapping("/getTeaByIdNumber")
    public RespBean getTeaByIdNumber(String IDNumber){
        Teachers tea = null;
        try{
            tea = teachersMapper.getTeaByIDNumber(IDNumber);
        }catch (Exception e){
            return RespBean.error("error",null);
        }
        return RespBean.ok("ok",tea);
    }

}

