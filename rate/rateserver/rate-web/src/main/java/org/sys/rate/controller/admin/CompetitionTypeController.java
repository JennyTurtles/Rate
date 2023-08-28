package org.sys.rate.controller.admin;

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
            competitionTypeMapper.addCompetitionType(competitionType);
            return RespBean.ok("添加competitionType成功！");
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
            return RespBean.ok("修改CompetitionType成功！");
        } catch (Exception e) {
            return RespBean.error("修改CompetitionType失败！");
        }
    }
}
