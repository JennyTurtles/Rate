package org.sys.rate.controller.student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.sys.rate.service.admin.StudentService;

@RestController
@RequestMapping("/system/student")
public class StudentController {
    @Autowired
    StudentService studentService;
}
