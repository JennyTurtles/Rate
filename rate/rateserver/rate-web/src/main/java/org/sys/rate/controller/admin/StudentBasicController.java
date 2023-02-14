package org.sys.rate.controller.admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.sys.rate.model.Student;
import org.sys.rate.model.RespBean;
import org.sys.rate.model.RespPageBean;
import org.sys.rate.service.admin.StudentService;
import java.applet.AppletStub;
import java.util.List;

@RestController
@RequestMapping("/student/basic")
public class StudentBasicController {
    @Autowired
    StudentService StudentService;


    @GetMapping("/getall")
    public List<Student> getAllStudent() {
        return StudentService.getAllStudent();
    }

    @PostMapping("/insert")
    public RespBean addStudent(@RequestBody Student record) {
        if (StudentService.addStudent(record) == 1) {
            return RespBean.ok("添加成功!");
        }
        return RespBean.error("添加失败!");
    }

    @PostMapping("/delete")
    public RespBean deleteStudent(@RequestBody Student record) {
        if (StudentService.deleteStudent(record) == 1) {
            return RespBean.ok("删除成功!");
        }
        return RespBean.error("删除失败!");
    }


    @PostMapping("/update")
    public RespBean updateStudent(@RequestBody Student record) {
        if (StudentService.updateStudent(record) == 1) {
            return RespBean.ok("更新成功!");
        }
        return RespBean.error("更新失败!");
    }
}


