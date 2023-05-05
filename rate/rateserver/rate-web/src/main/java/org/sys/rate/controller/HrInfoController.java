package org.sys.rate.controller;

import org.sys.rate.config.FastDFSUtils;
import org.sys.rate.mapper.*;
import org.sys.rate.model.*;
import org.sys.rate.service.admin.HrService;
import org.sys.rate.service.admin.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @作者 江南一点雨
 * @公众号 江南一点雨
 * @微信号 a_java_boy
 * @GitHub https://github.com/lenve
 * @博客 http://wangsong.blog.csdn.net
 * @网站 http://www.javaboy.org
 * @时间 2020-03-01 13:07
 */
@RestController
@RequestMapping("/info")
public class HrInfoController {

    @Autowired
    HrService hrService;
    @Autowired
    UnderGraduateMapper underGraduateMapper;
    @Autowired
    GraduateStudentMapper graduateStudentMapper;
    @Autowired
    TeachersMapper teachersMapper;
    @Autowired
    StudentMapper studentMapper;

    @Autowired
    AdminService adminService;
    @Autowired
    AdminMapper adminMapper;
    @Resource
    MailMapper mailMapper;

    @Value("${fastdfs.nginx.host}")
    String nginxHost;

    @GetMapping("/hr/info")
    public Account getCurrentHr(Authentication authentication) {
        return ((Account) authentication.getPrincipal());//直接注入 Authentication，注入成功后，就能直接使用
    }
    @GetMapping("/mail")
    public RespBean getMailInfo() {
        Mail res = null;
        try {
            res = mailMapper.getMail();
            if(res != null){
                return RespBean.ok("ok",res);
            }
        }catch (Exception e){
            return RespBean.error("error",null);
        }
        return RespBean.error("error",null);
    }
    @GetMapping("/admin")
    public RespBean getAdminInfo(Integer id) {
        Admin res = null;
        try {
           res = adminMapper.getById(id);
            if(res != null){
                return RespBean.ok("ok",res);
            }
        }catch (Exception e){
            return RespBean.error("error",null);
        }
        return RespBean.error("error",null);
    }
    @GetMapping("/teacher")
    public RespBean getTeacherInfo(Integer id) {
        Teachers res = null;
        try {
            res = teachersMapper.selectByPrimaryId(id);
            if(res != null){
                return RespBean.ok("ok",res);
            }
        }catch (Exception e){
            return RespBean.error("error",null);
        }
        return RespBean.error("error",null);
    }
    @GetMapping("/student")
    public RespBean getStudentInfo(Integer id) {
        Student res = null;
        try {
            //因为可能既是研究生又是本科生，并且只传递id值，所以都要查询
            UnderGraduate under = underGraduateMapper.getUnderByStuID(id);
            GraduateStudent grad = graduateStudentMapper.getGradByStuID(id);
            if(grad != null) return RespBean.ok("ok",grad);
            else if(under != null) return RespBean.ok("ok",under);
            else {
                res = studentMapper.getById(id);
                if(res != null){
                    return RespBean.ok("ok",res);
                }
            }
        }catch (Exception e){
            return RespBean.error("error",null);
        }
        return RespBean.error("error",null);
    }

    @PutMapping("/admin/info")
    public RespBean updateHr(@RequestBody Admin admin, Authentication authentication) {
        if (adminService.updateAdminByPrimaryKey(admin) == 1) {
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(admin, authentication.getCredentials(), authentication.getAuthorities()));
            //就是重新构建一个 Authentication 实例放到 Context 中去。
            return RespBean.ok("更新成功!");
        }
        return RespBean.error("更新失败!");
    }

    @PutMapping("/admin/pass")
    public RespBean updateHrPasswd(@RequestBody Map<String, Object> info) {
        String oldpass = (String) info.get("oldpass");
        String pass = (String) info.get("pass");
        Integer adminid = (Integer) info.get("id");
        if (adminService.updateAdminPasswd(oldpass, pass, adminid)) {
            return RespBean.ok("更新成功!");
        }
        return RespBean.error("更新失败!");
    }

    @PostMapping("/hr/userface")
    public RespBean updateHrUserface(MultipartFile file, Integer id,Authentication authentication) {
        String fileId = FastDFSUtils.upload(file);
        String url = nginxHost + fileId;
        if (hrService.updateUserface(url, id) == 1) {
            Hr hr = (Hr) authentication.getPrincipal();
            hr.setUserface(url);
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(hr, authentication.getCredentials(), authentication.getAuthorities()));
            return RespBean.ok("更新成功!", url);
        }
        return RespBean.error("更新失败!");
    }

    @PutMapping("/admin/insert")
    public RespBean addHr(@RequestBody Hr hr) {
        if (hrService.addNew(hr) == 1) {
            return RespBean.ok("添加成功!");
        }
        return RespBean.error("添加失败!");
    }

}
