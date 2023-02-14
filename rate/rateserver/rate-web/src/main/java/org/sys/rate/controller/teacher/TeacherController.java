package org.sys.rate.controller.teacher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.sys.rate.service.admin.TeacherService;

@RestController
@RequestMapping("/system/teacher")
public class TeacherController {
    @Autowired
    TeacherService teacherService;
}

