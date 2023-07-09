package org.sys.rate.service.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.sys.rate.mapper.*;
import org.sys.rate.model.*;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class ActivitiesService {
    @Autowired
    ActivitiesMapper activitiesMapper;
    @Autowired
    RabbitTemplate rabbitTemplate;
    @Autowired
    ActivityGrantMapper activityGrantMapper;
    @Autowired
    DisplayItemMapper displayItemMapper;
    @Resource
    ScoreItemMapper scoreItemMapper;
    @Resource
    InfoItemMapper infoItemMapper;
    @Resource
    GroupsMapper groupsMapper;

    public final static Logger logger = LoggerFactory.getLogger(ActivitiesService.class);
    SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
    SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
    DecimalFormat decimalFormat = new DecimalFormat("##.00");

    // 获得主活动
    public RespPageBean getActivitiesPage(Integer page, Integer size, Activities employee, Integer institutionID,Integer ID) {
        if (page != null && size != null) {
            page = (page - 1) * size;
        }
        List<Activities> data = activitiesMapper.getActivitiesByPage(page, size, institutionID,ID);
        Long total = (long) data.size();
        RespPageBean bean = new RespPageBean();
        bean.setData(data);
        bean.setTotal(total);
        return bean;
    }

    public List<Activities> getAllActivity_info() {
        return activitiesMapper.getAllActivity_info();
    }

    public RespBean addActivities(Activities employee) {
        //写这样的代码你在公司会被骂shi  简直就是依托答辩
        //我是不可能重写的，所以我改成我看着顺眼的
        boolean result;
        int insertID;
        try {
            int current = activitiesMapper.getInstitution_Current_Total(employee);
            int total = activitiesMapper.getInstitution_Total(employee);
            if(current < total) result = true;
            else result = false;//说明达到上限 不能添加
            if (result){
                employee.setScoreItemCount(0);
                insertID = activitiesMapper.insert(employee);
                activitiesMapper.insertScoreItem(employee);//原本是合并写的，改成分开写
                activitiesMapper.insertDisplayItem(employee);
                activitiesMapper.insert_update(employee);
                //在管理员_活动表中添加记录
                activityGrantMapper.insertRecordOfAddActivity(employee.getAdminID(),employee.getId());
            }
        }catch (Exception e){
            return RespBean.error("添加失败",null);
        }
        if(!result) return RespBean.error("活动达到上限",null);
        return RespBean.ok("添加成功",null);
    }

    public Integer predeleteActivities(Activities activities) {
        int result = activitiesMapper.predelete(activities.getId());
        return result;
    }

    public Integer deleteActivities(Activities activities) {
        try {
            activitiesMapper.delete(activities.getId());
            return 1;
        } catch (Exception e){
            return -1;
        }
    }

    public Integer endActivities(Activities activities) {
        int result = activitiesMapper.endActivities(activities);
        return result;
    }

    public Integer updateActivities(Activities company) {
        int result = activitiesMapper.update(company);
        return result;
    }

    public RespPageBean searchActivities(Integer page, Integer size, String company, Integer institutionID) {
        if (page != null && size != null) {
            page = (page - 1) * size;
        }
        List<Activities> data = activitiesMapper.getActivitiesByPageAndCompany(page, size, company, institutionID);
        Long total = activitiesMapper.getTotalByCompany(company, institutionID);
        RespPageBean bean = new RespPageBean();
        bean.setData(data);
        bean.setTotal(total);
        return bean;
    }

    public Integer getScoreItemCount(Integer activitiesId) {
        Integer count = activitiesMapper.selectScoreItemCount(activitiesId);
        return count;
    }

    public RespPageBean getActivityScore(Integer activityID){
        List<ScoreDetail> data = activitiesMapper.getActivityScore(activityID);
        RespPageBean bean = new RespPageBean();
        bean.setData(data);
        return bean;
    }

    public String getActivitiesName(Integer activityID) {
        String activitiesList = activitiesMapper.selectActivitiesName(activityID);
        return activitiesList;
    }

    public String getActivitiesDate(Integer activityID) {
        String d = activitiesMapper.selectActivitiesDate(activityID);
        return d;
    }

    public Boolean noRelative(Integer id) {
        Activities activities = activitiesMapper.queryById(id);
        return activities.getScoreItemCount() == 0 && activities.getGroupCount() == 0 && activities.getExpertCount() == 0;
    }
    public List<Activities> getActivity(Integer activityID){
        List<Activities> activities= activitiesMapper.selectActivity(activityID);
        return activities;
    }
    public List<Activities> getActivities(Integer page, Integer size,List<Integer> activityID){
        if (page != null && size != null) {
            page = (page - 1) * size;
        }
        List<Activities> activities= activitiesMapper.selectActivities(activityID);
        return activities;
    }


    @Transactional
    public void cloneActivity(Activities newActivityInfo){ // newActivityInfo中ID为老活动ID，三个时间和备注为新活动的
        Activities activity = activitiesMapper.queryById(newActivityInfo.getId()); // 老活动
        activity.setHaveSub(activity.getHaveSub() == null ? 0 : activity.getHaveSub());
        activity.cleanCount();
        activity.fillNewInfo(newActivityInfo);
        Integer oldActID = activity.getId();
        if (!newActivityInfo.isSub()) // 用于克隆子活动，将子活动作为主活动
            activity.setParentID(null);
        activitiesMapper.insert(activity);
        Integer newActID = activity.getId();
        activityGrantMapper.insertRecordOfAddActivity(newActivityInfo.getAdminID(),newActID);
        cloneDetail(newActID,oldActID);
        if (!newActivityInfo.isSub()){
            if (activity.getHaveSub() == 1){ // 有子活动则克隆子活动，此处后续使用多线程优化
                cloneSubActivity(newActID,oldActID);
                cloneGroupWithSub(newActID,oldActID);
            }else
                cloneGroup(newActID,oldActID);
        }
    }

    @Transactional
    public void cloneGroupWithSub(Integer newActID, Integer oldActID) {
        cloneGroup(newActID,oldActID);
        Map<Integer,Integer> groupMap = new HashMap<>(); // 老活动小组ID -> 新活动小组ID
        List<Integer> oldGroupIDs = groupsMapper.getIDbyActivityID(oldActID);
        List<Integer> newGroupIDs = groupsMapper.getIDbyActivityID(newActID);
        for (int i = 0; i < oldGroupIDs.size(); i++) {
            groupMap.put(oldGroupIDs.get(i),newGroupIDs.get(i));
        }
        List<Activities> oldSubAct = activitiesMapper.getSubActivities(oldActID);
        List<Activities> newSubAct = activitiesMapper.getSubActivities(newActID);
        List<Groups> newGroups = new ArrayList<>();
        for (int i = 0; i < oldSubAct.size(); i++) {
            List<Groups> oldGroups = groupsMapper.getGroupByActID(oldSubAct.get(i).getId());
            // 将oldgroup中的parentID改为新的
            for (Groups group : oldGroups) {
                group.setParentID(groupMap.get(group.getParentID()));
                group.setActivityID(newSubAct.get(i).getId());
                group.setExpertCount(0);
                group.setParticipantCount(0);
            }
            newGroups.addAll(oldGroups);
        }
        groupsMapper.insertGroups(newGroups);
    }

    @Transactional
    public void cloneGroup(Integer newActID, Integer oldActID){
        activitiesMapper.cloneGroup(newActID,oldActID);
    }

    @Transactional
    public void cloneSubActivity(Integer newActID, Integer oldActID){
        activitiesMapper.getSubActivities(oldActID).forEach(subActivity -> {
            subActivity.setParentID(newActID);
            subActivity.setSub(true);
            cloneActivity(subActivity);
        });
    }

    // 克隆评分项，信息项，成绩查看设置，小组信息
    @Transactional
    public void cloneDetail(Integer newActID, Integer oldActID){
        activitiesMapper.cloneScoreItem(newActID,oldActID);
        activitiesMapper.cloneInfoItem(newActID,oldActID);
        cloneDisplayItem(newActID,oldActID);
    }

    @Transactional
    public void cloneDisplayItem(Integer newActID, Integer oldActID){
        // 获取老活动中所有的展示项目
        List<DisplayItem> displayItems = displayItemMapper.getAllDisplayItemNoOrder(oldActID); // 严格按照添加顺序返回，避免找不到displayItem的ID
        Map<Integer,Integer> scoreItemMap = new HashMap<>();
        Map<Integer,Integer> infoItemMap = new HashMap<>();
        Map<Integer,Integer> displayItemMap = new HashMap<>();

        // 构建新老活动评分项的映射 老活动评分项ID -> 新活动评分项ID
        List<Integer> oldScoreItemID = scoreItemMapper.getIDByActivityID(oldActID);
        List<Integer> newScoreItemID = scoreItemMapper.getIDByActivityID(newActID);
        for (int i = 0; i < oldScoreItemID.size(); i++) {
            scoreItemMap.put(oldScoreItemID.get(i),newScoreItemID.get(i));
        }

        // 构建新老活动信息项的映射 老活动评分项ID -> 新活动评分项ID
        List<Integer> oldInfoItemID = infoItemMapper.getIDByActivityID(oldActID);
        List<Integer> newInfoItemID = infoItemMapper.getIDByActivityID(newActID);
        for (int i = 0; i < oldInfoItemID.size(); i++) {
            infoItemMap.put(oldInfoItemID.get(i),newInfoItemID.get(i));
        }

        // 逐条解析List<DisplayItem>中的source
        for (DisplayItem item : displayItems) {
            List<DisplayItemSource> displayItemSources = item.source2list();
            if (displayItemSources != null) {  // 非基础类型，将source中id映射到新活动
                for (DisplayItemSource displayItemSource : displayItemSources) {
                    String type = displayItemSource.getType();
                    Integer oldID = displayItemSource.getId();
                    Integer newID = null;
                    switch (type) {
                        case "scoreitem":
                            newID = scoreItemMap.get(oldID);
                            break;
                        case "infoitem":
                            newID = infoItemMap.get(oldID);
                            break;
                        case "displayitem":
                            newID = displayItemMap.get(oldID);
                            break;
                        default:
                            break;
                    }
                    displayItemSource.setId(newID);
                }
                item.setSource(DisplayItem.list2source(displayItemSources));
            }
            item.setActivityID(newActID);
            // 将新的displayItem插入到数据库中
            Integer oldDisplayItemID = item.getID();
            displayItemMapper.insert(item);
            Integer newDisplayItemID = item.getID();
            displayItemMap.put(oldDisplayItemID,newDisplayItemID);
        }
    }
}
