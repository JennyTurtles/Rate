package org.sys.rate.controller.teacher;

import org.springframework.web.bind.annotation.GetMapping;
import org.sys.rate.model.RespBean;

public class TeacherLoginController {
    @GetMapping("/teacherLogin")
    public RespBean login() {
        return RespBean.error("尚未登录，请登录!");
    }
}
