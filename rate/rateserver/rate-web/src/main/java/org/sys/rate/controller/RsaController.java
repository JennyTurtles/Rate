package org.sys.rate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.sys.rate.config.RsaUtil;
import org.sys.rate.model.RespBean;

@RestController

public class RsaController {
    @Autowired
    RsaUtil rsaUtil;

    @GetMapping("/getPublicKey")
    public RespBean getPublicKey(){
        return RespBean.ok ("登陆成功!",rsaUtil.getPublicKey()); // 将公钥发给前端
    }
}
