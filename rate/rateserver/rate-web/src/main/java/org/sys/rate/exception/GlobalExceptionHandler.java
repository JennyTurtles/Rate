package org.sys.rate.exception;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.sys.rate.model.*;
import org.sys.rate.service.admin.AdminService;
import org.sys.rate.service.admin.StudentService;
import org.sys.rate.service.admin.TeacherService;
import org.sys.rate.service.mail.EmailErrorLogService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Timestamp;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @Resource
    private EmailErrorLogService emailErrorLogService;
    @Autowired
    private AdminService adminService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private TeacherService teacherService;

    @ExceptionHandler(SQLException.class)
    public RespBean sqlException(SQLException e) {
        if (e instanceof SQLIntegrityConstraintViolationException) {
            return RespBean.error("SQL错误!");
        }
        return RespBean.error(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public RespBean sqlException(Exception e, HttpServletRequest request) {

        String token = null;
        String userId = null;
        Admin admin = null;
        Teacher teacher = null;
        Student student = null;
        try {
            token = request.getHeader("token");
            userId = JWT.decode(token).getAudience().get(0);
            admin = adminService.getById(Integer.parseInt(userId));
            teacher = teacherService.getById(Integer.parseInt(userId));
            student = studentService.getById(Integer.parseInt(userId));
        } catch (Exception ex) {
            EmailErrorLog emailErrorLog = new EmailErrorLog();
            emailErrorLog.setErrorType("获取错误的token");
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            emailErrorLog.setTimestamp(new Timestamp(System.currentTimeMillis()));
            emailErrorLogService.addEmailErrorLog(emailErrorLog);

            return RespBean.error("请邮件联系管理员ratemail@126.com，并截图说明相关操作。" + ex);
        }


        Account loggedInUser = new Account();
        if (admin == null && teacher == null && student == null) {
            loggedInUser = null;
        } else if (admin != null) {
            loggedInUser.setName(admin.getName());
            loggedInUser.setID(admin.getID());
            loggedInUser.setRole("admin");
        } else if (teacher != null) {
            loggedInUser.setName(teacher.getName());
            loggedInUser.setID(teacher.getID());
            loggedInUser.setRole("teacher");
        } else if (student != null) {
            loggedInUser.setName(student.getName());
            loggedInUser.setID(student.getID());
            loggedInUser.setRole("student");
        }

        // 获取请求的操作
        String operation = "；请求url：" + request.getRequestURL() + "；请求参数：" + request.getRequestURI() + "；";

        EmailErrorLog emailErrorLog = new EmailErrorLog();
        emailErrorLog.setErrorType("异常未处理");
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        String errorDetails = sw.toString();

        // 存储用户信息和操作
        if (loggedInUser != null) {
            errorDetails = "用户角色：" + loggedInUser.getRole() + "；用户id：" + loggedInUser.getID() + "；用户姓名：" + loggedInUser.getName() + operation + errorDetails;
        } else {
            errorDetails = "用户信息：null" + operation + errorDetails;
        }


        emailErrorLog.setErrorDescription(errorDetails);


        emailErrorLog.setTimestamp(new Timestamp(System.currentTimeMillis()));
        emailErrorLogService.addEmailErrorLog(emailErrorLog);

        return RespBean.error("请邮件联系管理员ratemail@126.com，并截图说明相关操作。" + errorDetails);
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public RespBean paramExceptionHandler(MethodArgumentNotValidException e) {
        BindingResult exceptions = e.getBindingResult();
        if (exceptions.hasErrors()) {
            List<ObjectError> errors = exceptions.getAllErrors();
            if (!errors.isEmpty()) {
                FieldError fieldError = (FieldError) errors.get(0);
                return RespBean.error(fieldError.getDefaultMessage());
            }
        }
        return RespBean.error("参数错误");
    }

}
