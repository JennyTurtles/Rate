package org.sys.rate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.sys.rate.config.RsaUtil;
import org.sys.rate.model.Account;
import org.sys.rate.model.Admin;
import org.sys.rate.model.LoginInf;
import org.sys.rate.model.RespBean;
import org.sys.rate.service.LoginService;

import java.util.Objects;


@RestController
@RequestMapping("/doLogin")

public class LoginController {
    @Autowired
    private LoginService loginService;
//    @Autowired
//    private RsaUtil rsaUtil;

    @PostMapping
    public RespBean login(@RequestBody LoginInf loginInf) {
//        loginInf.setPassword(rsaUtil.decrypt(loginInf.getPassword())); // 解密加密后的密码，并放入loginInf中
        LoginInf res = loginService.login(loginInf);
        if (res == null) // 未找到用户则返回用户名不存在
            return RespBean.error("用户名不存在!");
        else if (res.getToken() == null) // 未得到token则返回密码错误
            return RespBean.error("密码错误!");
        else
        {
            res.setPassword("");
            return RespBean.ok ("登陆成功!",res);
        }

    }
}