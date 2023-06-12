package org.sys.rate.controller.admin;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.sys.rate.mapper.OperationMapper;
import org.sys.rate.model.Operation;
import org.sys.rate.model.RespBean;

import javax.annotation.Resource;

@RestController
@RequestMapping("/oper/basic")
public class OpertionController {
    @Resource
    private OperationMapper operMapper;

    @PostMapping("/add")
    public RespBean addOper(Operation oper) {
        try {
            operMapper.insertOper(oper);
        }catch (Exception e) {
            return RespBean.error("error", null);
        }
        return RespBean.ok("success", null);
    }
}
