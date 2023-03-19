package org.sys.rate.service.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.sys.rate.mapper.ScoreItemMapper;
import org.sys.rate.model.ScoreItem;
import org.sys.rate.model.RespPageBean;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;


@Service
public class ScoreItemService {
    @Autowired
    ScoreItemMapper ScoreItemMapper;
    public final static Logger logger = LoggerFactory.getLogger(ScoreItemService.class);
    SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
    SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
    DecimalFormat decimalFormat = new DecimalFormat("##.00");

    public RespPageBean getActivitiesPage(Integer keywords, Integer page, Integer size, ScoreItem employee) {
        if (page != null && size != null) {
            page = (page - 1) * size;
        }
        List<ScoreItem> data = ScoreItemMapper.getActivitiesByPage(keywords, page, size, employee);
        Long total = ScoreItemMapper.getTotal(keywords, employee);
        RespPageBean bean = new RespPageBean();
        bean.setData(data);
        bean.setTotal(total);
        return bean;
    }

    public Integer addParticipates(ScoreItem employee) {
        int result = ScoreItemMapper.insert(employee);
        if (result == 1)
            ScoreItemMapper.insert_update(employee);
        return result;
    }

    public Integer getactivityCount(int id) {
        int result = ScoreItemMapper.getTotalforactivityCount(id);
        return result;
    }

    public Integer insertScoreItem(List<ScoreItem> list) {
        for (int i = 0; i < list.size(); i++) {
            ScoreItemMapper.insert(list.get(i));
        }
        return 1;
    }

    public Integer deleteActivities(ScoreItem company) {
        int result = ScoreItemMapper.delete(company);
        return result;
    }

    public Integer deleteByActivityId(Integer activityId) {
        return ScoreItemMapper.deleteByPrimaryKey(activityId);
    }

    public Integer updateActivities(List<ScoreItem> company) {
        for (int i = 0; i < company.size(); i++) {
            int result = ScoreItemMapper.update(company.get(i));
            if (result != 1)
                return 0;

        }
        return 1;
    }

    public Integer updateScoreItem(ScoreItem company) {

        int result = ScoreItemMapper.update(company);
        if (result != 1)
            return 0;
        return 1;
    }

    public List<ScoreItem> getScoreItemByActivityId(Integer activityId) {
        List<ScoreItem> scoreitem = ScoreItemMapper.selectScoreItemByActivityId(activityId);
        return scoreitem;
    }

    public List<ScoreItem> getScoreItemByEActivityId(Integer activityId) {
        List<ScoreItem> scoreitem = ScoreItemMapper.selectScoreItemByEActivityId(activityId);
        return scoreitem;
    }

    public List<ScoreItem> selectScoreItemByActivityIdWhereNotByExpert(Integer activityId) {
        List<ScoreItem> scoreitems = ScoreItemMapper.selectScoreItemByActivityIdWhereNotByExpert(activityId);
        return scoreitems;
    }
    public Integer selectScoreItemFinal(Integer activityId) {
        return ScoreItemMapper.selectScoreItemFinal(activityId);
    }

    public Integer ScoreItemByActivityIdWhereNotByExpertCount(Integer activityId) {
        return ScoreItemMapper.ScoreItemByActivityIdWhereNotByExpertCount(activityId);
    }

    public Integer latestId() {
        return ScoreItemMapper.maxWorkID();
    }


    public Boolean noRelative(Integer id) {
        List<ScoreItem> scoreItems = ScoreItemMapper.selectScoreItemByActivityId(id);
        //System.out.println(scoreItems.get(0).getName());
        if (scoreItems.size() == 1 && scoreItems.get(0).getName().equals("活动得分"))
            return true;
        return false;
    }
    public List<ScoreItem> getAllByActicityID(Integer activityID){
        return ScoreItemMapper.getAllByActicityID(activityID);
    }
}
