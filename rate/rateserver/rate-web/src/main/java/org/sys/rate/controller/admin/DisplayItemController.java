package org.sys.rate.controller.admin;

import org.springframework.web.bind.annotation.*;
import org.sys.rate.mapper.ActivitiesMapper;
import org.sys.rate.mapper.DisplayItemMapper;
import org.sys.rate.mapper.ScoreItemMapper;
import org.sys.rate.model.*;
import org.sys.rate.service.admin.DisplayItemService;

import javax.annotation.Resource;
import java.util.*;

@RestController
@RequestMapping("/displayItem")
public class DisplayItemController {

    @Resource
    DisplayItemMapper displayItemMapper;
    @Resource
    DisplayItemService displayItemService;
    @Resource
    ActivitiesMapper activitiesMapper;
    @Resource
    ScoreItemMapper scoreItemMapper;

    @GetMapping("/all")
    public RespPageBean getAllDisplayItem(@RequestParam Integer activityID) {
        List<DisplayItem> res = displayItemMapper.getAllDisplayItem(activityID);
        for (DisplayItem displayItem : res)
            displayItem.setSourceName(displayItemService.getSourceName(displayItem.getSource()));
        long total=displayItemMapper.getAll(activityID);
        RespPageBean bean = new RespPageBean();
        bean.setData(res);
        bean.setTotal(total);
        return bean;
    }

    @GetMapping("/first") // 获取所有第一类展示项
    public RespBean getFirst(@RequestParam Integer activityID) {
        List<DisplayItem> res = displayItemService.getFirstDisplayItem(activityID,1);
        //Collections.sort(res); // 按照sourceName排序，方便前端查看
        return RespBean.ok("success",res);
    }

    @GetMapping("/second") // 获取所有展示项，在第一类的基础上加上了第二类展示项，第二类指的是displayitem表中涉及计算操作的项
    public RespBean getSecond(@RequestParam Integer activityID) {
        List<DisplayItem> res = displayItemService.getFirstDisplayItem(activityID,0);
        List<DisplayItem> displayItems = displayItemMapper.getAllDisplayItem(activityID);
        for (DisplayItem displayItem : displayItems)
            // 如果displayItem的source包含"*"则加到res中，包含"*"的都是第二类展示项
            if (displayItem.getSource() != null && displayItem.getSource().contains("*")){
                DisplayItem displayItem1 = new DisplayItem(displayItem.getName(),displayItem.getSource());
                displayItem1.setID(displayItem.getID());
                res.add(displayItem1);
            }
        //Collections.sort(res); // 按照sourceName排序
        return RespBean.ok("success",res);
    }

    @PostMapping("/save")
    public RespBean save(@RequestBody DisplayItem displayItems) {
        int res;
        if (displayItems.getID() == null)
            res =  displayItemMapper.insert(displayItems);
        else
            res =  displayItemMapper.update(displayItems);
        return RespBean.ok("success",res);
    }

    @DeleteMapping("/")
    public RespBean delete(@RequestParam Integer ID,@RequestParam Integer activityID,@RequestParam Integer total) {
        Integer small = displayItemMapper.getDisplaySequenceByID(ID);
        displayItemMapper.subDisplaySequence(activityID,small,total);
        int res = displayItemMapper.delete(ID);
        return RespBean.ok("success",res);
    }

    // 用于"查看选手评分"，获取所有选手的展示项
    @GetMapping("/allPar")
    public RespBean getAllPar(@RequestParam Integer activityID,@RequestParam(defaultValue = "-1") Integer groupID) {
        List<ParticipantsDisplay> pars = displayItemService.getParticipantsDisplay(activityID,groupID);
        List<DisplayItem> displayItems = displayItemService.getDisplayItem(activityID);
        List<Object> res = new ArrayList<>();
        res.add(pars);
        res.add(displayItems);
        return RespBean.ok("success",res);
    }

    //alterDisplay
    @PostMapping("/alterDisplay")
    public RespBean alterDisplay(@RequestParam Integer total,@RequestParam Integer activityID,@RequestBody DisplayItem displayItem){
        if(displayItemService.alterDisplay(total, activityID, displayItem).equals("success"))
            return RespBean.ok("success");
        return RespBean.error("fail");
    }

    @GetMapping("/subFirst") //获取子活动的第一类展示项
    public RespBean getSubFirst(@RequestParam Integer activityID){
        List<Activities> subActivities = activitiesMapper.getSubActivities(activityID);
        subActivities.add(0,activitiesMapper.queryById(activityID)); //添加主活动
        HashMap<Integer,HashMap<String,DisplayItem>> map = new LinkedHashMap<>();//<activityID,<displayName,displayItem>>
        for (Activities activities:subActivities){//循环遍历子活动
            List<DisplayItem> displayItems;
            if(activities.getId()==activityID)
                displayItems=displayItemService.getFirstDisplayItem(activities.getId(),1);
            else
                displayItems=displayItemService.getFirstDisplayItem(activities.getId(),0);
            //Collections.sort(displayItems);//按展示项名称排序
            for (DisplayItem item:displayItems){//循环遍历展示项，获得展示项哈希表
                if (map.get(activities.getId())==null){//未添加当前活动，进行插入
                    HashMap<String,DisplayItem> value = new LinkedHashMap<>();
                    value.put(item.getSourceName(),item);
                    map.put(activities.getId(),value);
                }
                else {//已添加当前活动，进行添加
                    map.get(activities.getId()).put(item.getSourceName(),item);
                }
            }
        }
        HashPEexport hashPEexport = new HashPEexport();
        hashPEexport.setDmap(map);//封装哈希表
        List<Object> res = new ArrayList<>();
        res.add(subActivities);
        res.add(hashPEexport);//将子活动和展示项哈希表都添加到返回值中
        return RespBean.ok("success",res);
    }

    @GetMapping("/subSecond") //获取子活动的第一类展示项
    public RespBean getSubSecond(@RequestParam Integer activityID){
        List<Activities> subActivities = activitiesMapper.getSubActivities(activityID);
        subActivities.add(0,activitiesMapper.queryById(activityID)); //添加主活动
        HashMap<Integer,HashMap<String,DisplayItem>> map = new LinkedHashMap<>();//<activityID,<displayName,displayItem>>
        for (Activities activities:subActivities){//循环遍历子活动
            List<DisplayItem> displayItems;
            if(activities.getId()==activityID)
                displayItems=displayItemService.getFirstDisplayItem(activities.getId(),1);
            else
                displayItems=displayItemService.getFirstDisplayItem(activities.getId(),0);
            List<DisplayItem> all = displayItemMapper.getAllDisplayItem(activities.getId());
            for (DisplayItem displayItem : all)
                // 如果displayItem的source包含"*"则加到res中，包含"*"的都是第二类展示项
                if (displayItem.getSource() != null && displayItem.getSource().contains("*")){
                    DisplayItem displayItem1 = new DisplayItem(displayItem.getName(),displayItem.getSource());
                    displayItem1.setID(displayItem.getID());
                    displayItems.add(displayItem1);
                }
            //Collections.sort(displayItems);//按展示项名称排序
            for (DisplayItem item:displayItems){//循环遍历展示项，获得展示项哈希表
                if (map.get(activities.getId())==null){//未添加当前活动，进行插入
                    HashMap<String,DisplayItem> value = new LinkedHashMap<>();
                    value.put(item.getSourceName(),item);
                    map.put(activities.getId(),value);
                }
                else {//已添加当前活动，进行添加
                    map.get(activities.getId()).put(item.getSourceName(),item);
                }
            }
        }
        HashPEexport hashPEexport = new HashPEexport();
        hashPEexport.setDmap(map);//封装哈希表
        return RespBean.ok("success",hashPEexport);
    }

    @GetMapping("/getSetMethod") //获取成绩查看设置方式
    public Integer getSetMethod(@RequestParam Integer activityID){
        return activitiesMapper.getScoreSet(activityID);
    }

    @PostMapping("/changeMethod") //更改成绩查看设置方式
    public RespBean changeMethod(@RequestParam Integer activityID,@RequestParam Integer setByself){
        if (activitiesMapper.changeMethod(activityID,setByself)==1)
            return RespBean.ok("success");
        else
            return RespBean.error("fail");
    }
}
