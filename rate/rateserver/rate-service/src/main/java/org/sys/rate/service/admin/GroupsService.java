package org.sys.rate.service.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.sys.rate.mapper.GroupsMapper;
import org.sys.rate.mapper.InfosMapper;
import org.sys.rate.mapper.ParticipatesMapper;
import org.sys.rate.model.*;
import org.sys.rate.utils.createGroups;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


@Service
public class GroupsService {
    @Autowired
    GroupsMapper groupsMapper;
    @Autowired
    ParticipatesMapper participatesMapper;
    @Autowired
    InfosService infosService;
    @Autowired
    InfosMapper infosMapper;
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
        groupsMapper.insert(groups);
        return groupsMapper.queryMaxId();
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
    public List<List<double []>> createGroupsByScore(List<Integer> arr,Integer exchangeNums,Integer groupsNums,List<Double> point,List<double []> point_participant){
        Integer studentNums = point.size();
        createGroups cp = new createGroups();//分组
        Collections.sort(point, (a, b) -> Double.compare(b, a));
        Collections.sort(point_participant, (a, b) -> Double.compare(b[0], a[0]));

        List<List<double []>> resgroup = cp.devideGroupsFixedNums(arr,point,exchangeNums,groupsNums,studentNums,point_participant);
        System.out.println("groups:");
        for(List<double []> x : resgroup){
            for(double[] y : x){
                for (double mm : y){
                    System.out.print(mm);
                    System.out.print(",");
                }
                System.out.print("||");
            }
            System.out.println();
        }
        List<List<double []>> resgroupExchange = cp.createG(point,exchangeNums,groupsNums,point_participant.size());
        System.out.println("交换后groups:");
        for(List<double []> x : resgroupExchange){
            for(double[] y : x){
                for (double mm : y){
                    System.out.print(mm);
                    System.out.print(",");
                }
                System.out.print("||");
            }
            System.out.println();
        }
        return resgroupExchange;
    }

    //判断分组依据是字符串还是数字
    public String judgeNumber(Integer activityID,
                            Integer infoItemID,List<Integer> arr,
                            Integer exchangeNums,Integer groupsNums,List<String> infoContent,Integer sortByItemID){
        List<Infos> infosList = new ArrayList<>();
        List<Participates> participates = new ArrayList<>();
        //通过infoItemID筛选出没有分组的选手
        //没有子信息项
        if(infoContent.size() == 0){
            infosList = infosMapper.getParticipantIDtByAIdAndInfoItemID(activityID,infoItemID);
            List<Integer> participantID = new ArrayList<>();
            for(int i = 0;i<infosList.size();i++){
                participantID.add(infosList.get(i).getParticipantID());
            }
            participates = participatesMapper.getParticipantByAIdAndID(activityID,participantID);
            if(participates.size() == 0){
                return "没有未分组的选手";
            }
            //分组依据和排序依据不同
            if(sortByItemID != infoItemID){
                infosList = infosMapper.getParticipantIDtByAIdAndInfoItemID(activityID,sortByItemID);
            }
        }else {
            participates = infosService.getPartipicantByActivityId(activityID,infoItemID,infoContent);
            List<Integer> participantID = new ArrayList<>();
            for(int i = 0;i < participates.size(); i++){
                participantID.add(participates.get(i).getID());
            }
            //分组依据和排序依据不同
            if(sortByItemID != infoItemID){
                infosList = infosMapper.getInfoitemsListByParAndAcID(activityID,participantID,sortByItemID);
            }else {
                //拿到没分组选手的info content
                infosList = infosMapper.getInfoitemsListByParAndAcID(activityID,participantID,infoItemID);
            }
        }

        //通过sortByItemID筛选出没有分组的选手
        boolean flage = true;
        try {
            for(Infos info : infosList){
                if(!isDouble(info.getContent())){//如果有一个不是double数字，就break
                    flage = false;
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "分组失败";
        }
        if(!flage){//字符串，不是全分数
            for (int i = 0;i< arr.size();i++){//每组多少人
                participates = creatGroupsByString(arr.get(i),participates,activityID,0,arr.get(i),i);
            }
        }else {//全是分数
            List<Double> point = new ArrayList<>();
            List<double []> point_participant = new ArrayList<>();
//            List<Double> points = switchTypeOfScore();//转换数据类型
            for(Infos info : infosList){
                double [] temp = new double[4];
                point.add(Double.valueOf(info.getContent()));
                temp[0] = Double.valueOf(info.getContent());//分数
                temp[1] = Double.valueOf(info.getParticipantID());//选手id
                temp[2] = Double.valueOf(-1);//组号标识
                temp[3] = Double.valueOf(-1);
                point_participant.add(temp);
            }
//            for(int nn = 0;nn<167;nn++){
//                double [] temp = new double[3];
//                temp[0] = Double.valueOf(points.get(nn));
//                temp[1] = Double.valueOf(nn);
//                temp[2] = Double.valueOf(-1);
//                point_participant.add(temp);
//            }
            //得到交换后的groups
            List<List<double []>>res = createGroupsByScore(arr,exchangeNums,groupsNums,point,point_participant);
            String name = "";
            //对每组遍历
            try {
                for(int residx = 0;residx < res.size();residx ++){
                    name = "第" + Integer.toString(residx + 1) + "组";
                    Groups gp = new Groups();
                    gp.setActivityID(activityID);
                    gp.setName(name);
                    gp.setParticipantCount(res.get(residx).size());
                    gp.setExpertCount(0);
                    groupsMapper.insertMultipleGroups(gp);
                    groupsMapper.updateGroupCount(activityID);
                    List<Integer> parID = new ArrayList<>();//给定分组的选手id列表
                    for(int item = 0;item < res.get(residx).size(); item ++){
                        parID.add((int) res.get(residx).get(item)[1]);
                    }
                    participatesService.updateGroupID(activityID,gp.getID(),parID);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "分组失败";
            }
        }
        return "分组成功";
    }
    //正则判断是否含有字符串
    public static boolean isDouble(String s) {
        Pattern pattern = Pattern.compile("[+-]?\\d+(.\\d+)?");
        return pattern.matcher(s).matches();
    }
    //不是数字的分组
    public List<Participates> creatGroupsByString(Integer nums,List<Participates> participates,Integer activityID,Integer expertCount,Integer participantCount,Integer idx){
        //插入一组的数据
        String name = "第" + Integer.toString(idx + 1) + "组";
        Groups gp = new Groups();
        gp.setActivityID(activityID);
        gp.setExpertCount(expertCount);
        gp.setParticipantCount(participantCount);
        gp.setName(name);
        //groups表插入新数据
        Integer res = groupsMapper.insertMultipleGroups(gp);
        //更新activi表的groupcount
        groupsMapper.updateGroupCount(activityID);
        int i = 0;
        int j = 0;
        //给每个选手给定分组
        List<Integer> parID = new ArrayList<>();//给定分组的选手id列表
        //按照选手的顺序分组 因为不是按照分数分组
        while (i < nums){
            if(participates.get(j).getGroupID() == null || participates.get(j).getGroupID() == 0){
                participates.get(j).setGroupID(gp.getID());
                participates.get(j).setGroupName(name);
                parID.add(participates.get(j).getID());
                i ++;
            }
            j ++;
        }
        //更新数据库信息
        participatesService.updateGroupID(activityID,gp.getID(),parID);
        return participates;
    }
}
