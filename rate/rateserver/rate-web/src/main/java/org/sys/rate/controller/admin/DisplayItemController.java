package org.sys.rate.controller.admin;

import org.apache.ibatis.javassist.expr.NewArray;
import org.springframework.web.bind.annotation.*;
import org.sys.rate.mapper.DisplayItemMapper;
import org.sys.rate.mapper.InfoItemMapper;
import org.sys.rate.model.*;
import org.sys.rate.service.admin.DisplayItemService;

import java.text.Collator;

import javax.annotation.Resource;
import java.util.*;

@RestController
@RequestMapping("/displayItem")
public class DisplayItemController {

    @Resource
    DisplayItemMapper displayItemMapper;
    @Resource
    DisplayItemService displayItemService;

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
            if (displayItem.getSource() != null && displayItem.getSource().contains("*"))
                res.add(new DisplayItem(displayItem.getName(), displayItem.getSource()));
        Collections.sort(res); // 按照sourceName排序
        return RespBean.ok("success",res);
    }

    @PostMapping("/save")
    public RespBean save(@RequestBody DisplayItem displayItems) {
        int res = 0;
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
}
