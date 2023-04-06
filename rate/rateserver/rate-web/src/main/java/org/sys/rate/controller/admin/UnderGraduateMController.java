package org.sys.rate.controller.admin;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
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
    public RespBean importUnderGraduate(Integer institutionID,MultipartFile file) throws ParseException {
        Map<String,List> mm = POIUtils.readExcel_undergraduate(institutionID,file);
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
    public Msg getUnderStudent(@RequestParam("pageNum")Integer pageNum,@RequestParam("pageSize")Integer pageSize){
        Page page = PageHelper.startPage(pageNum, pageSize); // 设置当前所在页和每页显示的条数
        List<UnderGraduate> t = underGraduateService.getUnderStudent();
        PageInfo info = new PageInfo<>(page.getResult());
        Object[] res = {t,info.getTotal()}; // res是分页后的数据，info.getTotal()是总条数
        return Msg.success().add("res",res);
    }
    @PostMapping("/deleteUnderGraduateStudent")
    public Msg deleteUnderStudent(@RequestBody UnderGraduate under){
        return underGraduateService.deleteUnderStudent(under);
    }
    @PostMapping("/editUnderGraduateStudent")
    public RespBean editUnderStudent(@RequestBody UnderGraduate under){
        return underGraduateService.editUnderStudent(under);
    }
    //根据页面的筛选框进行查找本科生信息
    @GetMapping("/getTeaNamesBySelect")
    public RespBean getTeaNamesBySelect(String teaName){
        return underGraduateService.getTeaNamesBySelect(teaName);
    }
    @GetMapping("/getUnderStudentsBySelect")
    public RespBean getUnderStudentsBySelect(@RequestParam("year")Integer year,@RequestParam("teaName")String teaName,@RequestParam("pageNum")Integer pageNum,@RequestParam("pageSize")Integer pageSize){
        Page page = PageHelper.startPage(pageNum, pageSize); // 设置当前所在页和每页显示的条数
        List<UnderGraduate> t = underGraduateService.getUnderStudentsBySelect(year,teaName);
        PageInfo info = new PageInfo<>(page.getResult());
        Object[] res = {t,info.getTotal()}; // res是分页后的数据，info.getTotal()是总条数
        return RespBean.ok("ok",res);
    }
}
