package org.sys.rate.exception;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.sys.rate.model.EmailErrorLog;
import org.sys.rate.model.RespBean;
import org.sys.rate.service.mail.EmailErrorLogService;

import javax.annotation.Resource;
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
    public RespBean sqlException(Exception e) {
        EmailErrorLog emailErrorLog = new EmailErrorLog();
        emailErrorLog.setErrorType("异常未处理");
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        String errorDetails = sw.toString();
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
