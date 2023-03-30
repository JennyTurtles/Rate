package org.sys.rate.service.admin;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.sys.rate.mapper.InfoItemMapper;
import org.sys.rate.mapper.ParticipatesMapper;
import org.sys.rate.model.DisplayItem;
import org.sys.rate.model.InfoItem;
import org.sys.rate.model.ParticipantsDisplay;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class DisplayItemService {
    @Resource
    InfoItemMapper infoItemMapper;
    @Resource
    ParticipatesMapper participatesMapper;
    @Resource
    ParticipatesService participatesService;
    // 获得所有第一类展示项，包括：基础信息、信息项目
    public List<DisplayItem> getFirstDisplayItem(Integer activityID){
        List<DisplayItem> res = new ArrayList<>();
        // 首先获取基础信息（非信息项）
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

    // 未完成...
    public List<ParticipantsDisplay> getParticipantsDisplay(Integer activityID,Integer groupID){
        List<ParticipantsDisplay> res;
        // 首先获取基础信息
        if (groupID == -1)
            res = participatesMapper.getParticipantsDisplay(activityID);
        else
            res = participatesMapper.getParticipantsDisplayByGroup(activityID,groupID);
        String Totalscorewithdot = participatesService.getTotalscorewithdot(activityID,1);
        return res;
    }
}
