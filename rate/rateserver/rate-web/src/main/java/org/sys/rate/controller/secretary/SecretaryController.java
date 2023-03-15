package org.sys.rate.controller.secretary;

import org.springframework.web.bind.annotation.*;
import org.sys.rate.mapper.SecretaryMapper;
import org.sys.rate.model.Activities;
import org.sys.rate.model.Experts;
import org.sys.rate.model.Participates;
import org.sys.rate.model.RespBean;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/secretary")
public class SecretaryController {

    @Resource
    SecretaryMapper secretaryMapper;

    @GetMapping("/setRole")
    @ResponseBody
    public RespBean setSecretary(@RequestParam Integer teacherID,@RequestParam Integer activityID,@RequestParam Integer groupID,@RequestParam String role){
        int res = secretaryMapper.setSecretary(teacherID,activityID,groupID,role);
        return res == 1 ? RespBean.ok("success",1) : RespBean.ok("fail",0);
    }

    @GetMapping("/getAct")
    @ResponseBody
    public RespBean getAct(@RequestParam Integer teacherID){
        List<Activities> res= secretaryMapper.getAct(teacherID);
        return RespBean.ok("success",res);
    }

    @GetMapping("/getMember")
    @ResponseBody
    public RespBean getMember(@RequestParam Integer activityID,@RequestParam Integer groupID){
        List<Participates> participates = secretaryMapper.getPar(activityID,groupID);
        List<Experts> experts = secretaryMapper.getEx(activityID,groupID);
        List<Object> res = new ArrayList<>();
        res.add(participates);
        res.add(experts);
        return RespBean.ok("success",res);
    }
}
