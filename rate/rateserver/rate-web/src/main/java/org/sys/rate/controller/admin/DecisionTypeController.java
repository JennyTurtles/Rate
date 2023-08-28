package org.sys.rate.controller.admin;

import org.springframework.web.bind.annotation.*;
import org.sys.rate.mapper.DecisionTypeMapper;
import org.sys.rate.model.DecisionType;
import org.sys.rate.model.RespBean;

import javax.annotation.Resource;

@RestController
@RequestMapping("/decisionType")
public class DecisionTypeController {
    @Resource
    DecisionTypeMapper decisionTypeMapper;

    @PostMapping("")
    public RespBean addDecisionType(@RequestBody DecisionType decisionType){
        try {
            decisionTypeMapper.addDecisionType(decisionType);
            return RespBean.ok("添加decisionType成功！");
        } catch (Exception e) {
            return RespBean.error("添加decisionType失败！");
        }
    }

    @PutMapping("")
    public RespBean editDecisionType(@RequestBody DecisionType decisionType){
        try {
            decisionTypeMapper.editDecisionType(decisionType);
            return RespBean.ok("修改decisionType成功！");
        } catch (Exception e) {
            return RespBean.error("修改decisionType失败！");
        }
    }

    @DeleteMapping("")
    public RespBean deleteDecisionTypeById(@RequestParam("id") Integer id,@RequestParam Integer year){
        try {
            decisionTypeMapper.deleteById(id);
            return RespBean.ok("delete decision successfully!");
        } catch (Exception e) {
            return RespBean.error("delete decision error!");
        }
    }
}
