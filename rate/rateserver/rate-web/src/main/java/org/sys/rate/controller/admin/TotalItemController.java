package org.sys.rate.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.sys.rate.model.RespBean;
import org.sys.rate.model.RespPageBean;
import org.sys.rate.model.TotalItem;
import org.sys.rate.service.admin.TotalItemService;

@RestController
@RequestMapping("/totalItem/basic")
public class TotalItemController {
    @Autowired
    TotalItemService totalItemService;

    @GetMapping("/")
    public RespPageBean getByActivityID(Integer activityID){
        return totalItemService.getByActivityID(activityID);
    }

    // 计划重构
    @GetMapping("/getFianl")
    public RespPageBean getFinalByPage(Integer activityID,Integer page,Integer size){
        return totalItemService.getFinalScore(activityID,page,size);
    }

    @GetMapping("/getFianlGroup")
    public RespPageBean getFinalByPage(Integer activityID,Integer page,Integer size,String groupName){
        return totalItemService.getFinalScoreGroup(activityID,page,size,groupName);
    }
}
