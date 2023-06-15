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
import java.util.List;

@RestController
@RequestMapping("/oper/basic")
public class OperationController {
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

    @GetMapping("/List")
    public RespBean selectOperList(@Param("prodId") Integer prodId,@Param("prodType") String prodType) {
        List<Operation> operationList;
        try {
            operationList = operMapper.selectList(prodId, prodType);
        }catch (Exception e) {
            return RespBean.error("error", null);
        }
        return RespBean.ok("success", operationList);
    }
}
