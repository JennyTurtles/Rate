package org.sys.rate.service.admin;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import org.springframework.stereotype.Service;
import org.sys.rate.mapper.*;
import org.sys.rate.model.*;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
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
    DisplayItemMapper displayItemMapper;
    @Resource
    ScoreItemMapper scoreItemMapper;
    @Resource
    ActivitiesMapper activitiesMapper;

    // 获得所有第一类展示项，包括：基础信息、信息项目
    public List<DisplayItem> getFirstDisplayItem(Integer activityID, Integer flag) {
        List<DisplayItem> res = new ArrayList<>();
        // 首先获取基础信息（非信息项）
        if (flag==1){ //flag=0:不添加基础信息 flag=1:添加基础信息
            res.add(new DisplayItem("编号", "code"));
            res.add(new DisplayItem("组名", "group"));
        //    res.add(new DisplayItem("专家打分", "scores"));
            res.add(new DisplayItem("姓名", "name"));
        }
        // 获取信息项
        List<InfoItem> infoItems = infoItemMapper.getAll(activityID);
        for (InfoItem infoItem : infoItems)
            res.add(new DisplayItem(infoItem.getName(), "infoitem." + infoItem.getID()));
        // 获取评分项
        List<ScoreItem> scoreItems = scoreItemMapper.getByActivityID(activityID);
        for (ScoreItem scoreItem : scoreItems)
            res.add(new DisplayItem(scoreItem.getName(), "scoreitem." + scoreItem.getId()));
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

        // 评分项：建立table，(pID,sID):score
        Table<Integer, Integer,Double> tableScoreItem = getScoreAverageTable(activityID,pars);
        List<Activities> subActivities = activitiesMapper.getSubActivities(activityID);

        for (Activities subActivity:subActivities) {//循环遍历子活动
            // 建立子活动的pID和父活动的pID的映射
            List<subID2ParID> subID2ParIDs = participatesMapper.getSubID2ParID(subActivity.getId(),activityID);
            if (subID2ParIDs == null) // 当前子活动无选手
                continue;
            Map<Integer,Integer> subID2ParIDMap = new HashMap<>();
            for (subID2ParID subID2ParID:subID2ParIDs){
                subID2ParIDMap.put(subID2ParID.getSubID(),subID2ParID.getParID());
            }
            if (!tableInfoItem.containsRow(subActivity.getId())){
                Table<Integer, Integer,String> tableInfoItem1 = getInfoContentTable(subActivity.getId(),pars);
                // 遍历tableInfoItem1，替换parID为父活动的parID
                for (Table.Cell<Integer, Integer, String> cell : tableInfoItem1.cellSet()) {
                    Integer parID = subID2ParIDMap.get(cell.getRowKey());
                    if (parID == null)
                        continue;
                    tableInfoItem.put(parID,cell.getColumnKey(),cell.getValue());
                }
//                tableInfoItem.putAll(tableInfoItem1);
            }
            if (!tableScoreItem.containsRow(subActivity.getId())){
                Table<Integer, Integer,Double> tableScoreItem1 = getScoreAverageTable(subActivity.getId(),pars);
                // 遍历tableScoreItem1，替换parID为父活动的parID
                for (Table.Cell<Integer, Integer, Double> cell : tableScoreItem1.cellSet()) {
                    Integer parID = subID2ParIDMap.get(cell.getRowKey());
                    if (parID == null)
                        continue;
                    tableScoreItem.put(parID,cell.getColumnKey(), cell.getValue());
                }
//                tableScoreItem.putAll(tableScoreItem1);
            }
        }
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
                if (displayItem.getSource() == null || !displayItem.getSource().contains("*")) {  // 第一类：displayItem的source不包含"*"
                    String displayContent = getDisplayContentPart(displayItem.getSource(), par, tableInfoItem, tableScoreItem, displayItemMap, activityID);
                    par.getMap().put(displayItem.getName(), formatDouble(displayContent));
                } else // 第二类，需要计算
                {
                    String[] split = displayItem.getSource().split("\\+");
                    double score = 0;
                    int error = 0;
                    for (String s : split) {
                        String displayContent = getDisplayContentPart(s, par, tableInfoItem, tableScoreItem, displayItemMap, activityID);
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
    private String getDisplayContentPart(String str, ParticipantsDisplay par, Table<Integer,Integer,String> tableInfoItem,
                                         Table<Integer, Integer,Double> tableScoreItem, Map<Integer, DisplayItem> displayItemMap, Integer activityID) {
        if (str == null)
            str = "error";
        String[] split = str.split("\\*");
        String target = split.length == 1 ? split[0] : split[1]; // 区分第一类和第二类展示项
        double coefficient = split.length == 1 ? 1 : Double.parseDouble(split[0]);
        double score;
        switch (target) {
            case "code":
                return split.length == 1 ? par.getCode() : null;
            case "group":
                return split.length == 1 ? par.getGroupName() : null;
//            case "scores":
//                return split.length == 1 ? participatesService.getTotalscorewithdot(activityID, par.getID()) : null;
            case "name":
                return split.length == 1 ? par.getName() : null;
        }
        // 表名.ID
        String[] split2 = target.split("\\.");
        String tableName = split2[0];
        if (!tableName.equals("infoitem") && !tableName.equals("displayitem") && !tableName.equals("scoreitem"))
            return "item不存在";
        Integer ID = Integer.parseInt(split2[1]);
        // 处理infoitem表
        if (tableName.equals("infoitem")) {
            if (!tableInfoItem.contains(par.getID(), ID)) // source中填写的id有问题
                return "error";
            String content = tableInfoItem.get(par.getID(), ID); // 在已有的分数上继续计算
            if (content.equals(""))
                return null;
            try {
                score = Double.parseDouble(content); // content如果不为小数，对于第一类展示项，直接返回，对于第二类展示项，返回null
            } catch (Exception e) { // 解析到了脏东西
                return split.length == 1 ? content : "error";
            }
        } else if (tableName.equals("displayitem")) { // 第一类展示项中不包含总分项
            DisplayItem displayItem = displayItemMap.get(ID);
            if (displayItem == null)
                return "error";
            String content = par.getMap().get(displayItem.getName()); // 在已有的分数上继续计算
            try {
                score = Double.parseDouble(content);
            } catch (Exception e) {
                return null;
            }
        } else if (tableName.equals("scoreitem")) {
            if (!tableScoreItem.contains(par.getID(), ID))
                return "0";
            score = tableScoreItem.get(par.getID(), ID); // 必定为double类型
        }
        else
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
//            case "scores":
//                name = "专家打分";
//                break;
            case "name":
                name = "姓名";
                break;
        }
        if (!name.equals(""))
            return coefficient.equals("") ? name : coefficient + "*" + name;
        String[] split2 = target.split("\\.");
        String tableName = split2[0];
        if (!tableName.equals("infoitem") && !tableName.equals("displayitem") && !tableName.equals("scoreitem"))
            return "error";
        Integer ID = Integer.parseInt(split2[1]);
        if (tableName.equals("infoitem")) {
            name = infoItemMapper.getNameByID(ID);
        } else if (tableName.equals("displayitem")) {
            name = displayItemMapper.getNameByID(ID);
        }else if (tableName.equals("scoreitem")) {
            if (scoreItemMapper.getNameByID(ID).equals("活动得分")){
                name = scoreItemMapper.getActivityName(ID) + ".活动得分";
            }
            else
                name = scoreItemMapper.getNameByID(ID);
        }
        else
            return "error";
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

    // 对于评分项，还需要额外乘以系数
    private Table<Integer, Integer, Double> getScoreAverageTable(Integer activityID, List<ParticipantsDisplay> pars) {
        // 哎，处理子活动的时候效率有点低，以后再优化吧TcT
//        List<Integer> actIDs = activitiesMapper.getSubActivitiesID(activityID);
        List<ScoreItem> scoreItems = scoreItemMapper.getByActivityID(activityID);
        List<ScoreAverage> scoreAverages = scoreItemMapper.getScoreAverageByActivityID(activityID);
//        if (actIDs != null){
//            for (Integer actID : actIDs){
//                scoreItems.addAll(scoreItemMapper.getByActivityID(actID));
//                scoreAverages.addAll(scoreItemMapper.getScoreAverageByActivityID(actID));
//            }
//        }
        Table<Integer, Integer, Double> table = HashBasedTable.create();
        // 建立table
        for (ScoreAverage scoreAverage : scoreAverages) {
            if (scoreAverage.getParticipantID() == null)
                continue;
            if (scoreAverage.getScore() == null)
                scoreAverage.setScore(0.0);
            table.put(scoreAverage.getParticipantID(), scoreAverage.getScoreItemID(), scoreAverage.getScore()*scoreAverage.getCoef());
        }
        // 查漏补缺
        for (ScoreItem scoreItem : scoreItems) {
            for (ParticipantsDisplay par : pars) {
                if (!table.contains(par.getID(), scoreItem.getId())) {
                    table.put(par.getID(), scoreItem.getId(), 0.0);
                }
            }
        }
        return table;
    }

    //修改展示顺序
    public String alterDisplay(Integer total,Integer activityID,DisplayItem displayItem) {
        //Participates old=participatesMapper.getEmployeeById(company.getID());
        Integer maxDisplaySequence = total;
        if (displayItem.getID() != null
                && displayItem.getDisplaySequence() > 0) {
            DisplayItem old=displayItemMapper.getDisplayItemByID(displayItem.getID());
            // id确实存在
            if (old != null) {
                // 显示顺序没有变化
                if (old.getDisplaySequence() == displayItem
                        .getDisplaySequence())
                    return "success";
                // 修改的显示序号不能大于最大的显示序号
                if (displayItem.getDisplaySequence() <= maxDisplaySequence) {
                    if (displayItem.getDisplaySequence() > old.getDisplaySequence()) {
                        // 修正显示顺序
                        displayItemMapper.subDisplaySequence(activityID,old.getDisplaySequence(),displayItem.getDisplaySequence());
                        // 保存
                        displayItemMapper.saveDisplaySequence(activityID,displayItem.getDisplaySequence(),displayItem.getID());
                        return "success";

                    }
                    if (displayItem.getDisplaySequence() < old.getDisplaySequence()) {
                        // 修正显示顺序
                        displayItemMapper.addDisplaySequence(activityID,displayItem.getDisplaySequence(),old.getDisplaySequence());
                        // 保存
                        displayItemMapper.saveDisplaySequence(activityID,displayItem.getDisplaySequence(),displayItem.getID());
                        return "success";
                    }
                }
            }
        }
        return "false";
    }
}
