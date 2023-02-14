package org.sys.rate.controller;

import org.sys.rate.config.FastDFSUtils;
import org.sys.rate.model.Account;
import org.sys.rate.model.Hr;
import org.sys.rate.model.Admin;
import org.sys.rate.model.RespBean;
import org.sys.rate.service.admin.HrService;
import org.sys.rate.service.admin.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
public class HrInfoController {

    @Autowired
    HrService hrService;

    @Autowired
    AdminService adminService;

    @Value("${fastdfs.nginx.host}")
    String nginxHost;

    @GetMapping("/hr/info")
    public Account getCurrentHr(Authentication authentication) {
        return ((Account) authentication.getPrincipal());//直接注入 Authentication，注入成功后，就能直接使用
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
