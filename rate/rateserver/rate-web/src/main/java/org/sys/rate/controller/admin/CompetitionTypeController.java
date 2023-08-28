package org.sys.rate.controller.admin;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.*;
import org.sys.rate.mapper.CompetitionTypeMapper;
import org.sys.rate.model.CompetitionType;
import org.sys.rate.model.DecisionType;
import org.sys.rate.model.RespBean;

import javax.annotation.Resource;

@RestController
@RequestMapping("/competitionType")
public class CompetitionTypeController {
    @Resource
    CompetitionTypeMapper competitionTypeMapper;
    @PostMapping("")
    public RespBean addCompetitionType(@RequestBody CompetitionType competitionType){
        try {
            Integer res = competitionTypeMapper.addCompetitionType(competitionType);
            return res != 0 ? RespBean.ok("添加成功！") : RespBean.ok("重复添加，已忽略");
        } catch (Exception e) {
            return RespBean.error("添加competitionType失败！");
        }
    }

    @DeleteMapping("")
    public RespBean deleteCompetitionTypeById(@RequestParam("id") Integer id){
        try {
            competitionTypeMapper.deleteById(id);
            return RespBean.ok("delete competitionType successfully!");
        } catch (Exception e) {
            return RespBean.error("delete competitionType error!");
        }
    }

    @PutMapping("")
    public RespBean editCompetitionType(@RequestBody CompetitionType competitionType){
        try {
            competitionTypeMapper.editCompetitionType(competitionType);
            return RespBean.ok("修改成功！");
        } catch (DuplicateKeyException e) {
            return RespBean.error("重名！");
        }
    }
}
