package org.sys.rate.service.admin;

import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.sys.rate.mapper.*;
import org.sys.rate.model.*;

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
        if (page != null && size != null) {
            page = (page - 1) * size;
        }
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
                        par.setScore(single.getActivityScore());
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
                        Double score = map.get(participant.getID()).getFinalmap().get(calID).getScore() +
                                map.get(participant.getID()).getFinalmap().get(solo.getID()).getScore();
                        BigDecimal two = new BigDecimal(score);
                        score = two.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
                        map.get(participant.getID()).getFinalmap().get(solo.getID()).setScore(score);
                    }
                    IDs[count++] = calID;
                }
                solo.setTotal(IDs);
            }
            Tmap.put(solo.getID(),solo.getName());
        }
        hashFianlScore.setMap(map);
        hashFianlScore.setTmap(Tmap);
        List<HashFianlScore> list=new ArrayList<>();
        list.add(hashFianlScore);
        RespPageBean bean = new RespPageBean();
        bean.setTotal(total);
        bean.setData(list);
        return bean;
    }
}
