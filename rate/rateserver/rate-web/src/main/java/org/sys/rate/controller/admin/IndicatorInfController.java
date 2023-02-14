package org.sys.rate.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.sys.rate.model.*;
import org.sys.rate.service.admin.IndicatorInfService;

import java.util.ArrayList;
import java.util.List;

@RestController
public class IndicatorInfController {
    @Autowired
    private IndicatorInfService indicatorInfService;

    //期刊
    @GetMapping("/publication/{id}")
    public RespBean getAllPublication(@PathVariable Integer id)
    {
        Integer maxId= indicatorInfService.getMaxID("publication") + 1;
        List<IndicatorPublication> res = indicatorInfService.getPublication(id);
        List<Object> idAndRes = new ArrayList<>();
        idAndRes.add(maxId);
        idAndRes.add(res);
        return RespBean.ok("success",idAndRes);
    }

    @GetMapping("/maxYear/{indicatorId}")
    public RespBean getMaxYear(@PathVariable Integer indicatorId)
    {
        Integer year = indicatorInfService.getMaxYearByIndicatorId(indicatorId);
        return year != null ? RespBean.ok("success!",year) : RespBean.ok("无记录!");
    }

    @PostMapping("/publicationByYear")
    public RespBean getPublicationByIndicatorAndYear(@RequestBody IndicatorPublication indicatorPublication){
        Integer maxId= indicatorInfService.getMaxID("publication") + 1;
        List<IndicatorPublication> res = indicatorInfService.getPublicationByIndicatorAndYear(indicatorPublication.getIndicatorID(), indicatorPublication.getYear());
        List<Object> idAndRes = new ArrayList<>();
        idAndRes.add(maxId);
        idAndRes.add(res);
        return RespBean.ok("success",idAndRes);
    }

    @PostMapping("/publication")
    public RespBean savePublication(@RequestBody IndicatorPublication indicatorPublication)
    {

        boolean flag = indicatorInfService.savePublication(indicatorPublication);
        return flag ? RespBean.ok("update success!") : RespBean.error("update fail!");
    }

    @DeleteMapping("/publication/{id}")
    public RespBean deletePublication(@PathVariable Integer id)
    {
        boolean flag = indicatorInfService.deletePublication(id);
        return flag ? RespBean.ok("delete success!") : RespBean.error("delete fail!");
    }

    @PutMapping("/publication")
    public RespBean updatePublication(@RequestBody IndicatorPublication indicatorPublication)
    {
        boolean flag = indicatorInfService.updatePublicationById(indicatorPublication);
        return flag ? RespBean.ok("update success!") : RespBean.error("更新失败！当前年份存在同名期刊！");
    }

    //奖项
    @GetMapping("/award/{id}")
    public RespBean getAllAward(@PathVariable Integer id)
    {
        Integer maxId= indicatorInfService.getMaxID("award") + 1;
        List<IndicatorAward> res = indicatorInfService.getAward(id);
        List<Object> idAndRes = new ArrayList<>();
        idAndRes.add(maxId);
        idAndRes.add(res);
        return RespBean.ok("success",idAndRes);
    }

    @PostMapping("/award")
    public RespBean saveAward(@RequestBody IndicatorAward indicatorAward)
    {
        boolean flag = indicatorInfService.saveAward(indicatorAward);
        return flag ? RespBean.ok("update success!") : RespBean.error("update fail!");
    }

    @DeleteMapping("/award/{id}")
    public RespBean deleteAward(@PathVariable Integer id)
    {
        boolean flag = indicatorInfService.deleteAward(id);
        return flag ? RespBean.ok("delete success!") : RespBean.error("delete fail!");
    }

    @PutMapping("/award")
    public RespBean updateAward(@RequestBody IndicatorAward indicatorAward)
    {
        boolean flag = indicatorInfService.updateAward(indicatorAward);
        return flag ? RespBean.ok("update success!") : RespBean.error("update fail!");
    }

    //纵向科研项目
    @GetMapping("/program/{id}")
    public RespBean getAllProgram(@PathVariable Integer id)
    {
        Integer maxId= indicatorInfService.getMaxID("program") + 1;
        List<IndicatorProgram> res = indicatorInfService.getProgram(id);
        List<Object> idAndRes = new ArrayList<>();
        idAndRes.add(maxId);
        idAndRes.add(res);
        return RespBean.ok("success",idAndRes);
    }

    @PostMapping("/program")
    public RespBean saveProgram(@RequestBody IndicatorProgram indicatorProgram)
    {
        boolean flag = indicatorInfService.saveProgram(indicatorProgram);
        return flag ? RespBean.ok("update success!") : RespBean.error("update fail!");
    }

    @DeleteMapping("/program/{id}")
    public RespBean deleteProgram(@PathVariable Integer id)
    {
        boolean flag = indicatorInfService.deleteProgram(id);
        return flag ? RespBean.ok("delete success!") : RespBean.error("delete fail!");
    }

    @PutMapping("/program")
    public RespBean updateProgram(@RequestBody IndicatorProgram indicatorProgram)
    {
        boolean flag = indicatorInfService.updateProgram(indicatorProgram);
        return flag ? RespBean.ok("update success!") : RespBean.error("update fail!");
    }

    //决策咨询成果
    @GetMapping("/decision/{id}")
    public RespBean getAllDecision(@PathVariable Integer id)
    {
        Integer maxId= indicatorInfService.getMaxID("decision") + 1;
        List<IndicatorDecision> res = indicatorInfService.getDecision(id);
        List<Object> idAndRes = new ArrayList<>();
        idAndRes.add(maxId);
        idAndRes.add(res);
        return RespBean.ok("success",idAndRes);
    }

    @PostMapping("/decision")
    public RespBean saveDecision(@RequestBody IndicatorDecision indicatorDecision)
    {
        boolean flag = indicatorInfService.saveDecision(indicatorDecision);
        return flag ? RespBean.ok("update success!") : RespBean.error("update fail!");
    }

    @DeleteMapping("/decision/{id}")
    public RespBean deleteDecision(@PathVariable Integer id)
    {
        boolean flag = indicatorInfService.deleteDecision(id);
        return flag ? RespBean.ok("delete success!") : RespBean.error("delete fail!");
    }

    @PutMapping("/decision")
    public RespBean updateDecision(@RequestBody IndicatorDecision indicatorDecision)
    {
        boolean flag = indicatorInfService.updateDecision(indicatorDecision);
        return flag ? RespBean.ok("update success!") : RespBean.error("update fail!");
    }

    @GetMapping("/searchAll")
    public RespBean searchAll()
    {
        List<IndicatorSearchResult> results = indicatorInfService.getSearchPublication();
        results.addAll(indicatorInfService.getSearchAward());
        results.addAll(indicatorInfService.getSearchProgram());
        results.addAll(indicatorInfService.getSearchDecision());
        return RespBean.ok("success",results);
    }

    @GetMapping("/searchAll/{id}")
    public RespBean searchIndicator(@PathVariable Integer id)
    {
        return RespBean.ok("success",indicatorInfService.getSearchIndicator(id));
    }

    @GetMapping("/searchAll/publication/{id}")
    public RespBean searchPublicationById(@PathVariable Integer id)
    {
        return RespBean.ok("success",indicatorInfService.getSearchPublicationById(id));
    }

    @GetMapping("/searchAll/award/{id}")
    public RespBean searchAwardById(@PathVariable Integer id)
    {
        return RespBean.ok("success",indicatorInfService.getSearchAwardById(id));
    }

    @GetMapping("/searchAll/program/{id}")
    public RespBean searchProgramById(@PathVariable Integer id)
    {
        return RespBean.ok("success",indicatorInfService.getSearchProgramById(id));
    }

    @GetMapping("/searchAll/decision/{id}")
    public RespBean searchDecisionById(@PathVariable Integer id)
    {
        return RespBean.ok("success",indicatorInfService.getSearchDecisionById(id));
    }
}
