package org.sys.rate.service.admin;

import ch.qos.logback.core.joran.util.beans.BeanDescriptionFactory;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import org.springframework.stereotype.Service;
import org.sys.rate.mapper.DisplayItemMapper;
import org.sys.rate.mapper.InfoItemMapper;
import org.sys.rate.mapper.ParticipatesMapper;
import org.sys.rate.model.DisplayItem;
import org.sys.rate.model.InfoItem;
import org.sys.rate.model.ParticipantsDisplay;
import org.sys.rate.model.Participates;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DisplayItemService {
    @Resource
    InfoItemMapper infoItemMapper;
    @Resource
    ParticipatesMapper participatesMapper;
    @Resource
    ParticipatesService participatesService;
    @Resource
    DisplayItemMapper displayItemMapper;

    // 获得所有第一类展示项，包括：基础信息、信息项目
    public List<DisplayItem> getFirstDisplayItem(Integer activityID) {
        List<DisplayItem> res = new ArrayList<>();
        // 首先获取基础信息（非信息项）
        res.add(new DisplayItem("编号", "code"));
        res.add(new DisplayItem("组名", "group"));
        res.add(new DisplayItem("专家打分", "scores"));
        res.add(new DisplayItem("活动得分", "score"));
        // 获取信息项
        List<InfoItem> infoItems = infoItemMapper.getAll(activityID);
        for (InfoItem infoItem : infoItems)
            res.add(new DisplayItem(infoItem.getName(), "infoitem." + infoItem.getID()));
        return res;
    }

    // 获取所有成员和他们所有的展示项
    public List<ParticipantsDisplay> getParticipantsDisplay(Integer activityID, Integer groupID) {
        // 获取所有角色以及他们的基础信息
        List<ParticipantsDisplay> pars;
        if (groupID == -1)
            pars = participatesMapper.getParticipantsDisplay(activityID);
        else
            pars = participatesMapper.getParticipantsDisplayByGroup(activityID, groupID);

        // 信息项：建立table，(pID,iID):content
        Table<Integer, Integer,String> tableInfoItem = getInfoContentTable(activityID,pars);

        // 展示项：建立map，ID:displayItem
        List<DisplayItem> displayItems = displayItemMapper.getAllDisplayItem(activityID); // 获取所有列信息
        Map<Integer, DisplayItem> displayItemMap = new HashMap<>();
        for (DisplayItem displayItem : displayItems){
            displayItem.setSourceName(getSourceName(displayItem.getSource())); // 解析sourceName
            displayItemMap.put(displayItem.getID(), displayItem);
        }

        // 为每个选手添加展示项
        for (ParticipantsDisplay par : pars) {
            par.setMap(new HashMap<>());

            // 为该选手添加展示项
            for (DisplayItem displayItem : displayItems) {
                if (!displayItem.getSource().contains("*")) {  // 如果displayItem的source不包含"*"则为第一类
                    String displayContent = getDisplayContentPart(displayItem.getSource(), par, tableInfoItem, displayItemMap, activityID);
                    par.getMap().put(displayItem.getName(), formatDouble(displayContent));
                } else // 第二类需要计算
                {
                    String[] split = displayItem.getSource().split("\\+");
                    double score = 0;
                    int error = 0;
                    for (String s : split) {
                        String displayContent = getDisplayContentPart(s, par, tableInfoItem, displayItemMap, activityID);
                        if (displayContent == null)
                            continue;
                        if (displayContent.equals("error")){
                            error = 1;
                            break;
                        }
                        score += Double.parseDouble(displayContent);
                    }
                    par.getMap().put(displayItem.getName(), error == 0 ? formatDouble(score + "") : "error");
                }
            }
        }
        return pars;
    }

    public List<DisplayItem> getDisplayItem(Integer activityID) {
        List<DisplayItem> res =  displayItemMapper.getAllDisplayItem(activityID);
        for (DisplayItem displayItem : res)
            displayItem.setSourceName(getSourceName(displayItem.getSource()));
        return res;
    }

    // 用于解析字符串，传入系数*项名 或 项名，返回该展示项的值，同时适用于第一类和第二类展示项
    private String getDisplayContentPart(String str, ParticipantsDisplay par, Table<Integer,Integer,String> tableInfoItem, Map<Integer, DisplayItem> displayItemMap, Integer activityID) {
        String[] split = str.split("\\*");
        String target = split.length == 1 ? split[0] : split[1]; // 区分第一类和第二类展示项
        double coefficient = split.length == 1 ? 1 : Double.parseDouble(split[0]);
        double score;
        switch (target) {
            case "code":
                return split.length == 1 ? par.getCode() : null;
            case "group":
                return split.length == 1 ? par.getGroupName() : null;
            case "scores":
                return split.length == 1 ? participatesService.getTotalscorewithdot(activityID, par.getID()) : null;
            case "score":
                if (par.getScore() == null)
                    return null;
                return split.length == 1 ? par.getScore().toString() : par.getScore() * coefficient + "";
        }
        // 表名.ID
        String[] split2 = target.split("\\.");
        String tableName = split2[0];
        if (split.length == 1 && !tableName.equals("infoitem") && !tableName.equals("displayitem"))
            return "error";
        Integer ID = Integer.parseInt(split2[1]);
        // 处理infoitem表
        if (tableName.equals("infoitem")) {
            if (!tableInfoItem.contains(par.getID(), ID)) // source中填写的id有问题
                return "error";
            String content = tableInfoItem.get(par.getID(), ID); // 在已有的分数上继续计算
            try {
                score = Double.parseDouble(content); // content如果不为小数，对于第一类展示项，直接返回，对于第二类展示项，返回null
            } catch (Exception e) {
                return split.length == 1 ? content : null;
            }
        } else if (split.length == 2 && tableName.equals("displayitem")) { // 第一类展示项中不包含总分项
            DisplayItem displayItem = displayItemMap.get(ID);
            if (displayItem == null)
                return "error";
            String content = par.getMap().get(displayItem.getName()); // 在已有的分数上继续计算
            try {
                score = Double.parseDouble(content);
            } catch (Exception e) {
                return null;
            }
        } else
            return "error";
        return score * coefficient + "";
    }

    // 输入一个字符串，判断是否为小数，如果是则将其保留2位小数并去除末尾的0，然后返回新字符串
    private String formatDouble(String str) {
        try {
            double d = Double.parseDouble(str);
            DecimalFormat decimalFormat = new DecimalFormat("###################.##");
            return decimalFormat.format(d);
        } catch (Exception e) {
            return str;
        }
    }

    // 将source解析成sourceName
    public String getSourceName(String source) {
        if (source == null)
            return null;
        String[] split = source.split("\\+");
        String res = "";
        for (String s : split) {
            String sourceNamePart = getSourceNamePart(s);
            if (sourceNamePart == null)
                return "error";
            res += sourceNamePart + "+";
        }
        return res.substring(0, res.length() - 1);
    }

    public String getSourceNamePart(String source) {
        if (source == null)
            return null;
        String[] split = source.split("\\*");
        String target = split.length == 1 ? split[0] : split[1];
        String coefficient = split.length == 1 || split[0].equals("1") ? "" : split[0];
        String name = "";
        switch (target) {
            case "code":
                name = "编号";
                break;
            case "group":
                name = "组名";
                break;
            case "scores":
                name = "专家打分";
                break;
            case "score":
                name = "活动得分";
                break;
        }
        if (!name.equals(""))
            return coefficient.equals("") ? name : coefficient + "*" + name;
        String[] split2 = target.split("\\.");
        String tableName = split2[0];
        if (split.length == 1 && !tableName.equals("infoitem") && !tableName.equals("displayitem"))
            return "error";
        Integer ID = Integer.parseInt(split2[1]);
        if (tableName.equals("infoitem")) {
            name = infoItemMapper.getNameByID(ID);
        } else if (tableName.equals("displayitem")) {
            name = displayItemMapper.getNameByID(ID);
        }else
            return "";
        return coefficient.equals("") ? name : coefficient + "*" + name;
    }

    // 信息项table (pID,iID):content，使用multi-key，获取所有角色的信息项的内容，优化数据库访问次数。
    // 此处infoItem的name不重要，因此value为content而不需要是infoItem。
    public Table<Integer,Integer,String> getInfoContentTable(Integer activityID,List<ParticipantsDisplay> pars) {
        List<InfoItem> infoItems = infoItemMapper.getInfoItemByActivityID(activityID);
        List<InfoItem> infoItemPars = infoItemMapper.getInfoItemParByActivityID(activityID); // 包含所有选手的content
        Table<Integer,Integer,String> table = HashBasedTable.create();
        // 建立table
        for (InfoItem infoItem : infoItemPars){
            if (infoItem.getParticipantID() == null)
                continue;
            table.put(infoItem.getParticipantID(), infoItem.getID(), infoItem.getContent());
        }
        // 查漏补缺
        for (InfoItem infoItem : infoItems){
            for (ParticipantsDisplay par : pars){
                if (!table.contains(par.getID(),infoItem.getID())){
                    table.put(par.getID(), infoItem.getID(), "");
                }
            }
        }
        return table;
    }
}
