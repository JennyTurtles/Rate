package org.sys.rate.controller.admin;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;
import org.sys.rate.mapper.DoctorMapper;
import org.sys.rate.mapper.StudentMapper;
import org.sys.rate.model.Doctor;
import org.sys.rate.model.GraduateStudent;
import org.sys.rate.model.Msg;
import org.sys.rate.model.RespBean;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/doctorM/basic")
public class DoctorController {
    @Resource
    DoctorMapper doctorMapper;
    @Resource
    StudentMapper studentMapper;

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
}
