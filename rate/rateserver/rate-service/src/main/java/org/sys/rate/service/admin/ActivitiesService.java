package org.sys.rate.service.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.sys.rate.mapper.*;
import org.sys.rate.model.*;
import org.sys.rate.service.expert.ExpertactivitiesService;

import javax.annotation.Resource;
import java.sql.SQLException;
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
    @Resource
    ExpertsMapper expertsMapper;
    @Resource
    ExpertactivitiesMapper expertactivitiesMapper;
    @Resource
    ParticipatesMapper participatesMapper;
    @Resource
    ParticipatesService participatesService;
    @Resource
    ExpertactivitiesService expertactivitiesService;

    public final static Logger logger = LoggerFactory.getLogger(ActivitiesService.class);
    SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
    SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
    DecimalFormat decimalFormat = new DecimalFormat("##.00");

    // 获得主活动
    public List<Activities> getActivitiesPage(Integer institutionID,Integer ID) {
//        if (page != null && size != null) {
//            page = (page - 1) * size;
//        }
        List<Activities> data = activitiesMapper.getActivitiesByPageHelper(institutionID,ID);
//        Long total = (long) data.size();
//        RespPageBean bean = new RespPageBean();
//        bean.setData(data);
//        bean.setTotal(total);
        return data;
    }

    public List<Activities> getRecycleActivitiesByPage(Integer institutionID,Integer ID) {

        List<Activities> data = activitiesMapper.getRecycleActivitiesByPage(institutionID,ID);

        return data;
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
                employee.setScore(0.0);
                activitiesMapper.insert(employee);
                activitiesMapper.insertScoreItem(employee);//原本是合并写的，改成分开写
                activitiesMapper.insertDisplayItem(employee);
                if(employee.getParentID() == null)
                   activitiesMapper.insert_update(employee);
                else if (employee.getRequireGroup() == 0){
                    CreateGroupForSubWithoutGroup(employee);
                }
                //在管理员_活动表中添加记录
                activityGrantMapper.insertRecordOfAddActivity(employee.getAdminID(),employee.getId());
            }
        }
        catch (Exception e){
            return RespBean.error("已存在同名的活动！",null);
        }
        if(!result) return RespBean.error("活动达到上限",null);
        return RespBean.ok("添加成功",employee);
    }

    public void CreateGroupForSubWithoutGroup(Activities activity){
        int parentID = activity.getParentID();
        int activityID = activity.getId();
        List<Groups> groupParents = groupsMapper.getGroupByActID(parentID);
        for (Groups group : groupParents){
            int groupIDParent = group.getID();
            Groups newGroup = new Groups(group.getParticipantCount(), group.getExpertCount(), activityID,groupIDParent,"不分组");
            groupsMapper.insertGroup(newGroup); // 插入并返回ID
            participatesService.copyParticipates(groupIDParent,activityID,newGroup.getID());
            expertactivitiesService.copyExperts(groupIDParent,activityID,newGroup.getID());
        }
    }

    public Integer predeleteActivities(Activities activities) {
        int result = activitiesMapper.predelete(activities.getId());
        return result;
    }

    public Integer deleteActivities(Activities activities) {
        try {
            activitiesMapper.delete(activities.getId());
            if (activities.getParentID() == null)
                activitiesMapper.delete_update(activities);
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
        return (activities.getScoreItemCount() == null || activities.getScoreItemCount() == 0) && activities.getGroupCount() == 0 && activities.getExpertCount() == 0;
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
    public Integer cloneActivity(Activities newActivityInfo){ // newActivityInfo中ID为老活动ID，三个时间和备注为新活动的
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
                //cloneGroupWithSub(newActID,oldActID);
            }//else
                //(newActID,oldActID); //分组不克隆，先注释
        }
        return newActID;
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
        // k-v: 老子活动ID -> 新子活动ID
        Map<Integer,Integer> subActMap = new HashMap<>();
        activitiesMapper.getSubActivities(oldActID).forEach(subActivity -> {
            subActivity.setParentID(newActID);
            subActivity.setSub(true);
            Integer newSubActID =  cloneActivity(subActivity); // 获得克隆后的子活动ID
            subActMap.put(subActivity.getId(),newSubActID);
        });
        // k-v : 老子活动评分项ID -> 新子活动评分项ID
        // 遍历subActMap，获取老子活动的评分项和新子活动的评分项，通过评分项名字判断是否为同一评分项，若是则将老评分项ID和新评分项ID存入map
        Map<Integer,Integer> scoreItemMap = new HashMap<>();
        subActMap.forEach((oldSubActID,newSubActID) -> {
            List<ScoreItem> oldScoreItems = scoreItemMapper.getAllByActicityID(oldSubActID);
            List<ScoreItem> newScoreItems = scoreItemMapper.getAllByActicityID(newSubActID);
            for (ScoreItem oldScoreItem : oldScoreItems) {
                for (ScoreItem newScoreItem : newScoreItems) {
                    if (oldScoreItem.getName().equals(newScoreItem.getName())){
                        scoreItemMap.put(oldScoreItem.getId(),newScoreItem.getId());
                        break;
                    }
                }
            }
        });
        // 克隆成绩评定表设置
        // 获取老主活动的所有成绩评定表
        List<GradeFormEntry> oldGradeForms = expertsMapper.getGradeForm(oldActID);
        // 遍历旧的成绩评定表，将其activityID改为新的主活动ID，如果typeID在1到3之间，则基于subActMap修改targetID，否则基于scoreItemMap修改targetID
        for (GradeFormEntry oldGradeForm : oldGradeForms) {
            oldGradeForm.setActivityID(newActID);
            if (oldGradeForm.getTypeID() >= 1 && oldGradeForm.getTypeID() <= 3){
                oldGradeForm.setTargetID(subActMap.get(oldGradeForm.getTargetID()));
            }else {
                oldGradeForm.setTargetID(scoreItemMap.get(oldGradeForm.getTargetID()));
            }
        }
        // 将新的成绩评定表插入数据库
        expertsMapper.saveAllGradeForm(oldGradeForms);
    }

//    public void cloneGradeForm(Integer newActID, Integer oldActID){
//        activitiesMapper.cloneGradeForm(newActID,oldActID);
//    }

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

    public void deleteCompletely(Activities activity) {
        List<ScoreItem> scoreItems = scoreItemMapper.getAllByActicityID(activity.getId());
        for (ScoreItem scoreItem : scoreItems){
            scoreItemMapper.deleteByScoreItemID(scoreItem.getId());
        }
        activitiesMapper.deleteActivityCompletely(activity.getId());
    }
}
