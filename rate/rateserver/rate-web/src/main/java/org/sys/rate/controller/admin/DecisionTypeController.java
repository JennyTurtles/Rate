package org.sys.rate.controller.admin;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.*;
import org.sys.rate.mapper.DecisionTypeMapper;
import org.sys.rate.model.DecisionType;
import org.sys.rate.model.RespBean;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@RequestMapping("/decisionType")
public class DecisionTypeController {
    @Resource
    DecisionTypeMapper decisionTypeMapper;

    @PostMapping("")
    public RespBean addDecisionType(@Valid @RequestBody DecisionType decisionType){
        try {
            Integer res = decisionTypeMapper.addDecisionType(decisionType);
            return res != 0 ? RespBean.ok("添加成功！") : RespBean.ok("重复添加，已忽略");
        } catch (Exception e) {
            return RespBean.error("添加decisionType失败！");
        }
    }

    @PutMapping("")
    public RespBean editDecisionType(@Valid @RequestBody DecisionType decisionType){
        try {
            decisionTypeMapper.editDecisionType(decisionType);
            return RespBean.ok("修改成功！");
        } catch (DuplicateKeyException e) {
            return RespBean.error("重名！");
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
