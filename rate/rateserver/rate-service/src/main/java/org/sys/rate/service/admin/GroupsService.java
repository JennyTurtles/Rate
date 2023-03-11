package org.sys.rate.service.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.sys.rate.mapper.GroupsMapper;
import org.sys.rate.model.Participates;
import org.sys.rate.model.RespPageBean;
import org.sys.rate.model.Groups;
import org.sys.rate.utils.createGroups;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class GroupsService {
    @Autowired
    GroupsMapper groupsMapper;
    @Autowired
    ParticipatesService participatesService;
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
    public List<Double> switchTypeOfScore(){
        double[] p = {401,397,391,389,389,388,387,387,386,385,384,383,383,383,
                382,382,382,382,382,382,381,381,380,380,379,379,378,377,376,376,375,375,373,373,372,
                372,371,371,370,369,369,368,368,367,366,366,366,365,365,365,364,364,364,363,363,363,
                363,362,362,361,361,361,361,361,360,360,360,360,360,359,359,359,358,358,358,356,356,
                356,356,355,355,355,355,355,355,354,354,353,353,353,353,353,353,353,353,353,352,352,
                352,352,352,351,351,351,351,351,351,350,350,350,350,350,350,350,349,349,348,348,348,348,348,348,347,347,347,346,346,346,345,345,345,345,344,344,344,344,344,344,344,344,343,343,343,343,343,343,341,341,341,341,340,340,340,340,340,340,340,340,340,340,337,333,331,330,328,327,324
        };
        return Arrays.stream(p).boxed().collect(Collectors.toList());
    }
    //按照数字分组
    public List<List<Double>> createGroupsByScore(List<Integer> arr,Integer exchangeNums,Integer groupsNums){
        List<Double> point = switchTypeOfScore();//转换数据类型
        Integer studentNums = point.size();
        createGroups cp = new createGroups();//分组
        return cp.devideGroupsFixedNums(arr,point,exchangeNums,groupsNums,studentNums);//返回打印分组结果
//        cp.createG();//交换
    }

    //按照不是数字的依据分组
//    public List<List<Double>> createGroupsByString(List<Integer> arr){
//        createGroups cp = new createGroups();
////        int [] arr = {20,60,57,15,15};
////        System.out.println(cp.devideGroupsFixedNums(arr));
//    }
//    public List<Groups> insertMultipleGroups(Integer activityID,Integer expertCount,Integer participantCount){
////        List<String> name =
////        groupsMapper.insertMultipleGroups(activityID,name,expertCount,participantCount);
//    }
    public List<Participates> creatGroups(Integer nums,List<Participates> participates,Integer activityID,Integer expertCount,Integer participantCount,Integer idx){
        //插入一组的数据
        String name = "第" + Integer.toString(idx + 1) + "组";
        Integer groupID = groupsMapper.insertMultipleGroups(activityID,name,expertCount,participantCount);
        int i = 0;
        int j = 0;
        //给每个选手给定分组
        List<Integer> parID = new ArrayList<>();//给定分组的选手id列表
        while (i < nums){
            if(participates.get(j).getGroupID() == null || participates.get(j).getGroupID() == 0){
                participates.get(j).setGroupID(groupID);
                participates.get(j).setGroupName(name);
                parID.add(participates.get(j).getID());
                i ++;
            }
            j ++;
        }
        //更新数据库信息
        participatesService.updateGroupID(activityID,groupID,parID);
        return participates;
    }
}
