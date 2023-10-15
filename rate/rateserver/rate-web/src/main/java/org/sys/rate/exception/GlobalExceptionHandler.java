package org.sys.rate.exception;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.sys.rate.model.Account;
import org.sys.rate.model.EmailErrorLog;
import org.sys.rate.model.RespBean;
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

    @ExceptionHandler(SQLException.class)
    public RespBean sqlException(SQLException e) {
        if (e instanceof SQLIntegrityConstraintViolationException) {
            return RespBean.error("SQL错误!");
        }
        return RespBean.error(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public RespBean sqlException(Exception e, HttpServletRequest request) {
        // 获取用户信息（假设您已经实现了用户登录系统并将用户信息存储在会话中）
        Account loggedInUser = (Account) request.getSession().getAttribute("user");

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
            errorDetails = "用户id：" + loggedInUser.getID() + "；用户姓名：" + loggedInUser.getName() + operation + errorDetails;

        } else {
            errorDetails = "用户id：null；用户姓名：null" + operation + errorDetails;
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
