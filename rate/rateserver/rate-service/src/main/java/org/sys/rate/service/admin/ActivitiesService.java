package org.sys.rate.service.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.sys.rate.mapper.ActivitiesMapper;
import org.sys.rate.model.Activities;
import org.sys.rate.model.RespPageBean;
import org.sys.rate.model.ScoreDetail;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


@Service
public class ActivitiesService {
    @Autowired
    ActivitiesMapper activitiesMapper;
    @Autowired
    RabbitTemplate rabbitTemplate;
    @Autowired
    MailSendLogService mailSendLogService;

    public final static Logger logger = LoggerFactory.getLogger(ActivitiesService.class);
    SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
    SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
    DecimalFormat decimalFormat = new DecimalFormat("##.00");

    public RespPageBean getActivitiesPage(Integer page, Integer size, Activities employee, Integer institutionID) {
        if (page != null && size != null) {
            page = (page - 1) * size;
        }
        List<Activities> data = activitiesMapper.getActivitiesByPage(page, size, institutionID);
        Long total = activitiesMapper.getTotal(employee, institutionID);
        RespPageBean bean = new RespPageBean();
        bean.setData(data);
        bean.setTotal(total);
        return bean;
    }

    public List<Activities> getAllActivity_info() {
        return activitiesMapper.getAllActivity_info();
    }

    public Integer addActivities(Activities employee) {
        int result;
        int current=activitiesMapper.getInstitution_Current_Total(employee);
        int total=activitiesMapper.getInstitution_Total(employee);
        if(current<total)
        {result=1;}
        else
        {result=2;}
        if (result == 1)
            result = activitiesMapper.insert(employee);
        if (result == 1)
            activitiesMapper.insert_update(employee);
        return result;
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
