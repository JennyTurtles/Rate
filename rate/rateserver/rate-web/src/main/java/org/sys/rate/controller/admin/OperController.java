package org.sys.rate.controller.admin;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.sys.rate.mapper.OperMapper;
import org.sys.rate.model.Oper;
import org.sys.rate.model.RespBean;

import javax.annotation.Resource;

@RestController
@RequestMapping("/oper/basic")
public class OperController {
    @Resource
    OperMapper operMapper;

    @PostMapping("/add")
    public RespBean addOper(Oper oper) {
        try {
            operMapper.insertOper(oper);
        }catch (Exception e) {
            return RespBean.error("error", null);
        }
        return RespBean.ok("success", null);
    }
}
