package org.sys.rate.service.admin;

import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.sys.rate.mapper.*;
import org.sys.rate.model.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

@Service
public class TotalItemService {
    @Autowired
    TotalItemMapper totalItemMapper;
    @Autowired
    ScoreItemMapper scoreItemMapper;
    @Autowired
    InfoItemMapper infoItemMapper;
    @Autowired
    ParticipatesMapper participatesMapper;
    @Autowired
    ActivitiesMapper activitiesMapper;
    @Resource
    ParticipatesService participatesService;

    public RespPageBean getByActivityID(Integer activityID) {
        List<TotalItem> data = totalItemMapper.findbyActivityID(activityID);
        for (TotalItem solo : data) {
            if (solo.getInfoItemIDs() != null) {
                String[] info = solo.getInfoItemIDs().split(",");
                String[] name = new String[info.length];
                Integer count = 0;
                for (String s : info){
                    Integer value = Integer.valueOf(s);
                    name[count++] = infoItemMapper.getNameByID(value);
                }
                solo.setInfoName(name);
            }
        }
        Long total = totalItemMapper.getTotal(activityID);
        RespPageBean bean = new RespPageBean();
        bean.setTotal(total);
        bean.setData(data);
        return bean;
    }

    public RespPageBean getFinalScore(Integer activityID, Integer page, Integer size){
        HashFianlScore hashFianlScore = new HashFianlScore();
        HashMap<Integer, String> Tmap = new LinkedHashMap<>();
        HashMap<Integer, Participates> map = new LinkedHashMap<>();//map(PID,P)
        Long total = participatesMapper.getActivityTotal(activityID);
        List<TotalItem> data = totalItemMapper.findbyActivityID(activityID);
        for (TotalItem solo : data) { //遍历总分项
            if (solo.getInfoItemIDs() != null) {
                String[] info = solo.getInfoItemIDs().split(",");
                Integer[] IDs = new Integer[info.length];
                Integer count = 0;
                for (String s : info){ //遍历信息项
                    Integer infoid = Integer.valueOf(s);
                    List<FinalValue> value = participatesMapper.getInfoScoreByInfoID(activityID,infoid,page,size);
                    for (FinalValue single: value ){ //获得该信息项对应的participant信息
                        Participates par = new Participates();
                        par.setCode(single.getCode());
                        par.setName(single.getStudentName());
                        par.setGroupName(single.getGroupName());
                        par.setScore(single.getActivityScore() == null ? 0 : single.getActivityScore()); // 活动得分
                        par.setID(single.getParticipantID());
                        Double activityFullScore = activitiesMapper.getFullScore(activityID);
                        par.setFullScore(activityFullScore);
                        Double totalParScore = Double.parseDouble(single.getContent());
                        TotalItemValue totalItemValue = new TotalItemValue();
                        totalItemValue.setName(solo.getName());
                        totalItemValue.setId(solo.getID());
                        totalItemValue.setFullScore(solo.getFullScore());
                        //没有pid
                        if (map.get(single.getParticipantID())==null){
                            if (solo.getAddParticipantScore()==1){ //加上活动总分
                                totalParScore = totalParScore + par.getScore();
                                BigDecimal two = new BigDecimal(totalParScore);
                                totalParScore = two.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
                            }
                            totalItemValue.setScore(totalParScore);
                            HashMap<Integer,TotalItemValue> finalmap = new LinkedHashMap<>();
                            finalmap.put(solo.getID(),totalItemValue);
                            par.setFinalmap(finalmap);
                            par.setDisplaySequence(single.getDisplaySequence());
                            map.put(single.getParticipantID(),par);
                        }
                        else{//有pid
                            if (map.get(single.getParticipantID()).getFinalmap().get(solo.getID())==null){
                                totalItemValue.setScore(totalParScore);
//                                HashMap<Integer,TotalItemValue> finalmap = new LinkedHashMap<>();
//                                finalmap.put(solo.getID(),totalItemValue);
                                map.get(single.getParticipantID()).getFinalmap().put(solo.getID(),totalItemValue);
                            }
                            else {
                                totalParScore += map.get(single.getParticipantID()).getFinalmap().get(solo.getID()).getScore();
                                BigDecimal two = new BigDecimal(totalParScore);
                                totalParScore = two.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
                                map.get(single.getParticipantID()).getFinalmap().get(solo.getID()).setScore(totalParScore);
                            }
                        }
                    }
                    IDs[count++] = infoid;
                }
                solo.setInfos(IDs);
            }
            if (solo.getTotalItemIDs() != null) {
                String[] calc = solo.getTotalItemIDs().split(",");
                Integer[] IDs = new Integer[calc.length];
                Integer count = 0;
                for (String s : calc){
                    Integer calID = Integer.valueOf(s);
                    List<Participates> participates = participatesMapper.getAllByActivityID(page,size,activityID);
                    for (Participates participant : participates){
                        Participates par = new Participates();
                        par.setCode(participant.getCode());
                        par.setName(participant.getName());
                        par.setGroupName(participant.getGroupName());
                        par.setScore(participant.getScore());
                        par.setID(participant.getID());
                        par.setDisplaySequence(participant.getDisplaySequence());
                        Double activityFullScore = activitiesMapper.getFullScore(activityID);
                        par.setFullScore(activityFullScore);
                        Double totalParScore=0.0;
                        TotalItemValue totalItemValue = new TotalItemValue();
                        totalItemValue.setName(solo.getName());
                        totalItemValue.setId(solo.getID());
                        totalItemValue.setFullScore(solo.getFullScore());
                        if (map.get(participant.getID())==null){
                            //totalItemValue.setScore(totalParScore);
                            HashMap<Integer,TotalItemValue> finalmap = new LinkedHashMap<>();
                            finalmap.put(solo.getID(),totalItemValue);
                            par.setFinalmap(finalmap);
                            map.put(participant.getID(),par);
                        }
//                        else{//有pid
                        if (map.get(participant.getID())!=null &&
                                map.get(participant.getID()).getFinalmap().get(calID)!=null&&
                                map.get(participant.getID()).getFinalmap().get(solo.getID())!=null) {
                            Double score = map.get(participant.getID()).getFinalmap().get(calID).getScore() +
                                    map.get(participant.getID()).getFinalmap().get(solo.getID()).getScore();
                                BigDecimal two = new BigDecimal(score);
                                score = two.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                                map.get(participant.getID()).getFinalmap().get(solo.getID()).setScore(score);
                        }
                    }
                    IDs[count++] = calID;
                }
                solo.setTotal(IDs);
            }
            Tmap.put(solo.getID(),solo.getName());
        }
        for (Participates participates : map.values()){
            participates.setTotalscorewithdot(participatesService.getTotalscorewithdot(activityID,participates.getID()));
        }
        // 缝缝补补
        // 对所有选手遍历一次，保证他们有所有的总分项，没有的话就赋初值，addParticipantScore=1的话，加上活动总分，同时检查检查其他的总分项目需不需要加上增加的分数;=0的话，不加。
        map.forEach((k,v)->{
            for (TotalItem totalItem : data){
                if (v.getFinalmap().get(totalItem.getID())==null){
                    TotalItemValue totalItemValue = new TotalItemValue();
                    totalItemValue.setName(totalItem.getName());
                    totalItemValue.setId(totalItem.getID());
                    totalItemValue.setFullScore(totalItem.getFullScore());
                    if (totalItem.getAddParticipantScore()==1){
                        totalItemValue.setScore(v.getScore());
                        Integer ID = totalItem.getID();
                        // 遍历data，如果totalItemIDs里面包含ID，就把这个ID的分数加上
                        for (TotalItem totalItem1 : data){
                            if (totalItem1.getTotalItemIDs()!=null){
                                String[] calc = totalItem1.getTotalItemIDs().split(",");
                                for (String s : calc){
                                    Integer calID = Integer.valueOf(s);
                                    if (calID.equals(ID)){
                                        v.getFinalmap().get(totalItem1.getID()).setScore(totalItemValue.getScore()+v.getFinalmap().get(totalItem1.getID()).getScore());
                                    }
                                }
                            }
                        }
                    }
                    else {
                        totalItemValue.setScore(0.0);
                    }
                    v.getFinalmap().put(totalItem.getID(),totalItemValue);
                }
            }
        });

        hashFianlScore.setMap(map);
        hashFianlScore.setTmap(Tmap);
        List<HashFianlScore> list=new ArrayList<>();
        list.add(hashFianlScore);
        RespPageBean bean = new RespPageBean();
        bean.setTotal(total);
        bean.setData(list);
        return bean;
    }

    // 暂时不考虑性能问题
    public RespPageBean getFinalScoreGroup(Integer activityID, Integer page, Integer size, String groupName){
        HashFianlScore hashFianlScore = getHashFinalScore(activityID);
        // 遍历hashFianlScore的map，将value中的groupName为groupName的取出来
        HashMap<Integer,Participates> map = hashFianlScore.getMap();
        HashMap<Integer,Participates> newmap = new LinkedHashMap<>();
        for (Participates participates : map.values()){
            if (participates.getGroupName().equals(groupName)){
                newmap.put(participates.getID(),participates);
            }
        }
        hashFianlScore.setMap(newmap);

        RespPageBean bean = new RespPageBean();
        List<HashFianlScore> list=new ArrayList<>();
        list.add(hashFianlScore);
        bean.setData(list);
        bean.setTotal((long)newmap.size());
        return bean;
    }

    // 用于导出excel
    public HashFianlScore getHashFinalScore(Integer activityID){
        return (HashFianlScore)getFinalScore(activityID,1,1000).getData().get(0);
    }

    public HashFianlScore getHashFinalScoreGroup(Integer activityID, String groupName){
        return (HashFianlScore)getFinalScoreGroup(activityID,1,1000,groupName).getData().get(0);
    }

}