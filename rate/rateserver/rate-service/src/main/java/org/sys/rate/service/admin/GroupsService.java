package org.sys.rate.service.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.sys.rate.mapper.GroupsMapper;
import org.sys.rate.model.RespPageBean;
import org.sys.rate.model.Groups;
import org.sys.rate.model.ScoreDetail;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


@Service
public class GroupsService {
    @Autowired
    GroupsMapper groupsMapper;
    public final static Logger logger = LoggerFactory.getLogger(GroupsService.class);

    public RespPageBean getActivitiesPage(Integer keywords, Integer page, Integer size, Groups employee) {
        if (page != null && size != null) {
            page = (page - 1) * size;
        }
        Long total = groupsMapper.getTotal(keywords, employee);
        List<Groups> data = groupsMapper.getActivitiesByPage(keywords, page, size, employee);
        RespPageBean bean = new RespPageBean();
        bean.setData(data);
        bean.setTotal(total);
        return bean;
    }

    public RespPageBean getGroupScore(Integer keywords,Integer page,Integer size, Groups employee){
        if (page != null && size != null) {
            page = (page - 1) * size;
        }
        Long total = groupsMapper.getTotal(keywords, employee);
        List<ScoreDetail> data = groupsMapper.getGroupScore(keywords,page,size,employee);
        RespPageBean bean = new RespPageBean();
        bean.setData(data);
        bean.setTotal(total);
        return bean;
    }

    public List<Groups> getActivitiesName(Integer activityid) {
        Groups data=new Groups();
        return groupsMapper.getActivitiesByPage(activityid, null, null, data);
    }

    public Integer addParticipates(Groups groups) {
//        System.out.println("insert1");
        groupsMapper.insert(groups);
//        System.out.println(groups);
//        System.out.println(groupsMapper.queryMaxId());
        return groupsMapper.queryMaxId();
//        System.out.println("insert2");
//        if(result==1)
//            groupsMapper.insert_update(employee);
    }

    public Integer getactivityCount(int id) {
        int result = groupsMapper.getTotalforactivityCount(id);
        return result;
    }

    public Integer insertScoreItem(List<Groups> list) {
        for (int i = 0; i < list.size(); i++) {
            groupsMapper.insert(list.get(i));
        }
        return 1;
    }

    public Integer deleteActivities(Groups company) {
        return groupsMapper.delete(company);
    }

    public Integer updateActivities(List<Groups> company) {
        for (int i = 0; i < company.size(); i++) {
            int result = groupsMapper.update(company.get(i));
            if (result != 1)
                return 0;

        }
        return 1;
    }

    public Integer updateGroup(Groups groups) {
        return groupsMapper.update(groups);
    }

    public Integer insertGroup(Groups list) {
        groupsMapper.insert(list);

        return 1;
    }

    public Integer getMaxId() {
        return groupsMapper.queryMaxId();
    }

    /*public RespPageBean searchActivities(Integer page, Integer size,String company) {
        if (page != null && size != null) {
            page = (page - 1) * size;
        }
        System.out.println("company2");
        List<ScoreItem> data = scoreItemMapper.getActivitiesByPageAndCompany(page, size, company);
        Long total = scoreItemMapper.getTotalByCompany(company);
        RespPageBean bean = new RespPageBean();
        bean.setData(data);
        bean.setTotal(total);
        return bean;
    }*/
    /*public Groups getEmployeeById(Integer empId) {
        return groupsMapper.getEmployeeById(empId);
    }*/

    public RespPageBean getparticipantsByPage(Integer page, Integer size, Groups employee, Date[] beginDateScope) {
        if (page != null && size != null) {
            page = (page - 1) * size;
        }
        List<Groups> data = groupsMapper.getParticipatesByPage(page, size, employee, beginDateScope);
        Long total = groupsMapper.getTotaloutput(employee, beginDateScope);
        RespPageBean bean = new RespPageBean();
        bean.setData(data);
        bean.setTotal(total);
        return bean;
    }

}
