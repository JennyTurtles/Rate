package org.sys.rate.controller.teacher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.sys.rate.mapper.TeacherMapper;
import org.sys.rate.model.RespBean;
import org.sys.rate.model.Teacher;
import org.sys.rate.model.Teachers;
import org.sys.rate.model.UnderGraduate;
import org.sys.rate.service.admin.TeacherService;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/system/teacher")
public class TeacherController {
    @Autowired
    TeacherService teacherService;
    @Resource
    TeacherMapper teacherMapper;

    // 通过名字模糊查询
    @GetMapping("searchByName")
    public RespBean searchByName(String name) {
        List<Teachers> teachers = teacherMapper.selectByName(name);
        return RespBean.ok("success", teachers);
    }

    // 通过名字或者工号进行模糊查询
    @GetMapping("searchByNameOrJobNumber")
    public RespBean searchByNameOrJobNumber(@RequestParam("nameOrJobNumber") String nameOrJobNumber,
                                            @RequestParam("institutionID")Integer institutionID) {
        try {
            List<Teachers> teachers = teacherMapper.searchByNameOrJobNumber(nameOrJobNumber, institutionID);
            return RespBean.ok("success", teachers);
        } catch (Exception e) {
            return RespBean.error("获取教师信息出现错误！");
        }
    }

    @PostMapping("/uploadSign")
    public RespBean uploadSign(@RequestParam("id") String id, @RequestParam("file") MultipartFile file) {
        if (!file.isEmpty()) {
            try {
                String filePath = new File("files").getAbsolutePath() + "\\template\\signs\\"+ UUID.randomUUID() +"-";
                String fileName = file.getOriginalFilename();

                // 在指定路径下创建文件
                File dest = new File(filePath + fileName);
                file.transferTo(dest);

                teacherMapper.save(id, dest.getAbsolutePath());

                return RespBean.ok("");
            } catch (IOException e) {
                return RespBean.error("");
            }
        } else {
            return RespBean.error("");
        }
    }

}

