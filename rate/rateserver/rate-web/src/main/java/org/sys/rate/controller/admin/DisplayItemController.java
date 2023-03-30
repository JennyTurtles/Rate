package org.sys.rate.controller.admin;

import org.apache.ibatis.javassist.expr.NewArray;
import org.springframework.web.bind.annotation.*;
import org.sys.rate.mapper.DisplayItemMapper;
import org.sys.rate.mapper.InfoItemMapper;
import org.sys.rate.model.DisplayItem;
import org.sys.rate.model.InfoItem;
import org.sys.rate.model.RespBean;
import java.text.Collator;

import javax.annotation.Resource;
import java.util.*;

@RestController
@RequestMapping("/displayItem")
public class DisplayItemController {

    @Resource
    DisplayItemMapper displayItemMapper;
    @Resource
    InfoItemMapper infoItemMapper;

    @GetMapping("/all")
    public RespBean getAllDisplayItem(@RequestParam Integer activityID) {
        List<DisplayItem> res = displayItemMapper.getAllDisplayItem(activityID);
        return RespBean.ok("success",res);
    }

    @GetMapping("/first") // 获取所有第一类展示项
    public RespBean getFirst(@RequestParam Integer activityID) {
        List<DisplayItem> res = getFirstDisplayItem(activityID);
        Collections.sort(res); // 按照sourceName排序
        return RespBean.ok("success",res);
    }

    @GetMapping("/second") // 获取所有第一类和第二类展示项
    public RespBean getSecond(@RequestParam Integer activityID) {
        List<DisplayItem> res = getFirstDisplayItem(activityID);
        List<DisplayItem> displayItems = displayItemMapper.getAllDisplayItem(activityID);
        for (DisplayItem displayItem : displayItems)
            // 如果displayItem的source包含"*"则加到res中，包含"*"的都是第二类展示项
            if (displayItem.getSource().contains("*"))
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

    // 获得所有第一类展示项，包括：基础信息、信息项目
    public List<DisplayItem> getFirstDisplayItem(@RequestParam Integer activityID){
        List<DisplayItem> res = new ArrayList<>();
        // 首先处理特殊情况（非信息项）
        res.add(new DisplayItem("编号","code"));
        res.add(new DisplayItem("组名","groups"));
        res.add(new DisplayItem("专家打分","scores"));
        res.add(new DisplayItem("活动得分","score"));
        // 获取信息项
        List<InfoItem> infoItems = infoItemMapper.getAll(activityID);
        for (InfoItem infoItem : infoItems)
            res.add(new DisplayItem(infoItem.getName(), "infoitem." +infoItem.getID()));
        return res;
    }
}
