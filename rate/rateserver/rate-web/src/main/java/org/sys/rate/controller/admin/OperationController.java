package org.sys.rate.controller.admin;

import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.sys.rate.mapper.OperationMapper;
import org.sys.rate.model.Operation;
import org.sys.rate.model.RespBean;

import javax.annotation.Resource;
import javax.xml.crypto.Data;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

@RestController
@RequestMapping("/oper/basic")
public class OperationController {
    @Resource
    private OperationMapper operMapper;

    @PostMapping("/add")
    public RespBean addOper(Operation oper) {
        try {
            oper.setTime((Timestamp) oper.getTime());
            operMapper.insertOper(oper);
        }catch (Exception e) {
            return RespBean.error("error", null);
        }
        return RespBean.ok("success", null);
    }

    @GetMapping("/List")
    public RespBean selectOperList(Integer prodId,String type) {
        List<Operation> operationList;
        try {
            operationList = operMapper.selectList(prodId, type);
        }catch (Exception e) {
            return RespBean.error("error", null);
        }
        return RespBean.ok("success", operationList);
    }
}
