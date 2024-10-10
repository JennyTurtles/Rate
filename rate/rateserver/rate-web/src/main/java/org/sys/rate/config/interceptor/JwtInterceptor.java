package org.sys.rate.config.interceptor;
import com.auth0.jwt.JWT;
import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.sys.rate.exception.ServiceException;
import org.sys.rate.model.Admin;
import org.sys.rate.model.Student;
import org.sys.rate.model.Teacher;
import org.sys.rate.service.admin.AdminService;
import org.sys.rate.service.admin.StudentService;
import org.sys.rate.service.admin.TeacherService;
import org.sys.rate.exception.Constants;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class JwtInterceptor implements HandlerInterceptor {
    @Autowired
    private AdminService adminService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private TeacherService teacherService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //token拦截器
        String token = request.getHeader("token");
        // 如果不是映射到方法直接通过
        if(!(handler instanceof HandlerMethod)){
            return true;
        }
        if(StrUtil.isBlank((token))){
            //没有token，重新获取
            throw new ServiceException(Constants.CODE_401, "无token，请重新登录");
        }
        //获取token中的userid
        String userId;
        String userRole;
        try {
            userId = JWT.decode(token).getAudience().get(0);
            userRole = JWT.decode(token).getClaims().get("role").asString();
        } catch (JWTDecodeException j) {
            throw new ServiceException(Constants.CODE_401, "token验证失败，请重新登录");
        }

        Admin admin = null;
        Teacher teacher = null;
        Student student = null;
        // 根据token中的userid和role查询数据库
        if(userRole.indexOf("admin") >= 0) {
            admin = adminService.getById(Integer.parseInt(userId));
        } else if (userRole.indexOf("teacher") >= 0) {
            teacher = teacherService.getById(Integer.parseInt(userId));
        } else if (userRole.indexOf("student") >= 0) {
            student = studentService.getById(Integer.parseInt(userId));
        }
        if (admin == null && teacher == null && student == null) {
            throw new ServiceException(Constants.CODE_401, "用户不存在，请重新登录");
        }
        //用户密码加签验证token
        JWTVerifier jwtVerifier = null;
        String defaultKey = "defaultKey";  // 设置一个默认密钥
        if (admin != null) {
            String password = admin.getPassword();
            jwtVerifier = JWT.require(Algorithm.HMAC256(password != null && !password.isEmpty() ? password : defaultKey)).build();
        } else if (teacher != null) {
            String password = teacher.getPassword();
            jwtVerifier = JWT.require(Algorithm.HMAC256(password != null && !password.isEmpty() ? password : defaultKey)).build();
        } else if (student != null) {
            String password = student.getPassword();
            jwtVerifier = JWT.require(Algorithm.HMAC256(password != null && !password.isEmpty() ? password : defaultKey)).build();
        }

        try {
            jwtVerifier.verify(token); // 验证token
        } catch (JWTVerificationException e) {
            throw new ServiceException(Constants.CODE_401, "token验证失败，请重新登录");
        }
        return true;
    }
}



