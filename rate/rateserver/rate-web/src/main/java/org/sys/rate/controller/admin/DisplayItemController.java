package org.sys.rate.controller.admin;

import org.springframework.web.bind.annotation.*;
import org.sys.rate.mapper.ActivitiesMapper;
import org.sys.rate.mapper.DisplayItemMapper;
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
        List<DisplayItem> res = displayItemService.getFirstDisplayItem(activityID);
        Collections.sort(res); // 按照sourceName排序，方便前端查看
        return RespBean.ok("success",res);
    }

    @GetMapping("/second") // 获取所有展示项，在第一类的基础上加上了第二类展示项，第二类指的是displayitem表中涉及计算操作的项
    public RespBean getSecond(@RequestParam Integer activityID) {
        List<DisplayItem> res = displayItemService.getFirstDisplayItem(activityID);
        List<DisplayItem> displayItems = displayItemMapper.getAllDisplayItem(activityID);
        for (DisplayItem displayItem : displayItems)
            // 如果displayItem的source包含"*"则加到res中，包含"*"的都是第二类展示项
            if (displayItem.getSource() != null && displayItem.getSource().contains("*")){
                DisplayItem displayItem1 = new DisplayItem(displayItem.getName(),displayItem.getSource());
                displayItem1.setID(displayItem.getID());
                res.add(displayItem1);
            }
        Collections.sort(res); // 按照sourceName排序
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
    public RespBean delete(@RequestParam Integer ID) {
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
        subActivities.add(activitiesMapper.queryById(activityID));
        HashMap<Integer,HashMap<String,DisplayItem>> map = new LinkedHashMap<>();//<activityID,<displayName,displayItem>>
        for (Activities activities:subActivities){
            List<DisplayItem> displayItems=displayItemService.getFirstDisplayItem(activities.getId());
            Collections.sort(displayItems);
            for (DisplayItem item:displayItems){
                if (map.get(activities.getId())==null){
                    HashMap<String,DisplayItem> value = new LinkedHashMap<>();
                    value.put(item.getSourceName(),item);
                    map.put(activities.getId(),value);
                }
                else {
                    map.get(activities.getId()).put(item.getSourceName(),item);
                }
            }
        }
        HashPEexport hashPEexport = new HashPEexport();
        hashPEexport.setDmap(map);
        List<Object> res = new ArrayList<>();
        res.add(subActivities);
        res.add(hashPEexport);
        return RespBean.ok("success",res);
    }
}
