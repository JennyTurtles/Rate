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
import javax.servlet.http.HttpServletResponse;
import java.io.*;
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

    @GetMapping("/downloadSign")
    public void downloadSign(@RequestParam("id") String tutorId,
                             @RequestParam(value = "isOnLine", defaultValue = "false") boolean isOnLine,
                             HttpServletResponse response) {
        String signUrl = teacherMapper.getSignUrl(tutorId);
        File sign = new File(signUrl);

        if (sign.exists()) {
            try (FileInputStream fis = new FileInputStream(sign);
                 BufferedInputStream bis = new BufferedInputStream(fis);
                 OutputStream os = response.getOutputStream()) {

                // 设置响应头信息
                response.reset(); // 非常重要

//                String filename = signUrl.substring(signUrl.length() - 3).equals("jpg") ? "个人签名.jpg" : "个人签名.png";
                String filename = "个人签名.jpg";

                if (isOnLine) {
                    // 在线打开方式
                    response.setContentType("application/octet-stream");
                    response.setHeader("Content-Disposition", "inline; filename=" + java.net.URLEncoder.encode(filename, "UTF-8"));
                } else {
                    // 纯下载方式
                    response.setContentType("application/octet-stream");
                    response.setHeader("Content-Disposition", "attachment; filename=" + java.net.URLEncoder.encode(filename, "UTF-8"));
                }

                // 读取文件内容并写入响应流
                byte[] buffer = new byte[1024];
                int len;
                while ((len = bis.read(buffer)) != -1) {
                    os.write(buffer, 0, len);
                }
                os.flush();

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

}

