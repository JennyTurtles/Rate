package org.sys.rate.service.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.sys.rate.mapper.ActivitiesMapper;
import org.sys.rate.mapper.ActivityGrantMapper;
import org.sys.rate.model.Activities;
import org.sys.rate.model.RespBean;
import org.sys.rate.model.RespPageBean;
import org.sys.rate.model.ScoreDetail;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;


@Service
public class ActivitiesService {
    @Autowired
    ActivitiesMapper activitiesMapper;
    @Autowired
    RabbitTemplate rabbitTemplate;
    @Autowired
    ActivityGrantMapper activityGrantMapper;

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
}
