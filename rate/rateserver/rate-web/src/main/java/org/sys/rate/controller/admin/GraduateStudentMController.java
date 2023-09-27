package org.sys.rate.controller.admin;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.sys.rate.config.RsaUtil;
import org.sys.rate.mapper.GraduateStudentMapper;
import org.sys.rate.mapper.StudentMapper;
import org.sys.rate.model.*;
import org.sys.rate.service.admin.GraduateStudentService;
import org.sys.rate.service.admin.LogService;
import org.sys.rate.service.expert.ExpertService;
import org.sys.rate.utils.POIUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/graduatestudentM/basic")
public class GraduateStudentMController {
    @Resource
    LogService logService;
    @Resource
    GraduateStudentService graduateStudentService;
    @Resource
    StudentMapper studentMapper;
    @Resource
    RsaUtil rsaUtil;
    @Resource
    GraduateStudentMapper graduateStudentMapper;

    @GetMapping("/exportGraduate")
    public ResponseEntity<byte[]> downloadExample_Participants_exportMoPar_Group(HttpServletResponse response){
        return POIUtils.writeGraduateStudent();
    }

    @PostMapping("/importGraduate")
    public RespBean importUnderGraduate(Integer institutionID, MultipartFile file) throws ParseException {
        Map<String, List> mm = POIUtils.readExcel_graduatestudent(institutionID, file);
        List<GraduateStudent> graduate = mm.get("graduatelist");
        List<Student> stu = mm.get("studentlist");
        if (graduate.size() == 0) { //先将excel中读取到的数据行拿出来，student和graduate列表是同样的数量才对
            return RespBean.error("未读取到有效导入数据");
        }
        RespBean res = graduateStudentService.addGraduate(graduate);
        return res;
    }

    @GetMapping("/getGraduateStudents")
    public Msg getGraduateStudents(@RequestParam("pageNum") Integer pageNum, @RequestParam("pageSize") Integer pageSize) {
        Page page = PageHelper.startPage(pageNum, pageSize); // 设置当前所在页和每页显示的条数
        List<GraduateStudent> t = graduateStudentService.getGraduatesStudent();
        PageInfo info = new PageInfo<>(page.getResult());
        Object[] res = {t, info.getTotal()}; // res是分页后的数据，info.getTotal()是总条数
        return Msg.success().add("res", res);
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
    public RespBean getGraduateStudentsBySelect(@RequestParam("year")Integer year,@RequestParam("teaName")String teaName,@RequestParam("pageNum")Integer pageNum,@RequestParam("pageSize")Integer pageSize){
        Page page = PageHelper.startPage(pageNum, pageSize); // 设置当前所在页和每页显示的条数
        List<GraduateStudent> t = graduateStudentService.getGraduateStudentsBySelect(year,teaName);
        PageInfo info = new PageInfo<>(page.getResult());
        Object[] res = {t, info.getTotal()}; // res是分页后的数据，info.getTotal()是总条数
        return RespBean.ok("ok", res);
    }

    @GetMapping("/getGraduateListByTutorID")
    public RespBean getGraduateListByTutorID(@RequestParam("tutorID") Integer tutorID, @RequestParam("pageNum") Integer pageNum, @RequestParam("pageSize") Integer pageSize) {
        // 设置当前所在页和每页显示的条数
        Page page = PageHelper.startPage(pageNum, pageSize);
        List<GraduateStudent> t = graduateStudentService.getGraduateListByTutorID(tutorID);
        PageInfo info = new PageInfo<>(page.getResult());
        // res是分页后的数据，info.getTotal()是总条数
        Object[] res = {t, info.getTotal()};
        return RespBean.ok("ok", res);
    }

    @PostMapping("/resetUnderPassword")
    public RespBean resetUnderPassword(@RequestBody GraduateStudent grad){
        Integer res = 0;
        try{
            String p = ExpertService.sh1(grad.getPassword());
            res = studentMapper.updatePassword(grad.getStudentID(),p);
        }catch (Exception e){
            return RespBean.error("error",null);
        }
        if(res > 0){
            return RespBean.ok("ok",null);
        }
        return RespBean.error("error",null);
    }
    @PostMapping("/update")
    public RespBean updateStudent(@RequestBody GraduateStudent record) {
        if (graduateStudentMapper.checkHaveStudentOfStuNumber(record.getInstitutionID(),record.getStuNumber(),record.getStudentID()) == 1){
            return RespBean.error("学号已存在，请重新修改或联系管理员！");
        }
        if (studentMapper.update(record) == 1){
            if (graduateStudentMapper.update(record) == 1) {
                return RespBean.ok("更新成功!");
            }
        }
        return RespBean.error("更新失败!");
    }
}
