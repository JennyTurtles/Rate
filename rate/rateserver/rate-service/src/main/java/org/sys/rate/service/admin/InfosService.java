package org.sys.rate.service.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.sys.rate.mapper.InfosMapper;
import org.sys.rate.mapper.ParticipatesMapper;
import org.sys.rate.model.Infos;
import org.sys.rate.model.Participates;

import java.util.*;

@Service
public class InfosService {
    @Autowired
    InfosMapper infosMapper;
    @Autowired
    ParticipatesMapper participatesMapper;

    public Integer saveScore(Integer expertID, Integer activityID, Integer participantID, Integer scoreItemID, Double score) {
        int i=infosMapper.insertScore(expertID,activityID,participantID,scoreItemID,score);
        return i;

    }

    public Integer saveScore1(Infos scores) {
        Integer i= infosMapper.insertScore1(scores);
        return i;
    }

    public List<Infos> getScoreList(Integer activitiesId, Integer expertid) {
        List<Infos> scoresList= infosMapper.selectScoreListByAE(activitiesId,expertid);
        return scoresList;
    }

    public List<Infos> getScoreList_null(Integer activitiesId, Integer expertid) {
        List<Infos> scoresList= infosMapper.selectScoreListByAE_null(activitiesId,expertid);
        return scoresList;
    }

    public Integer getScoreByP(Integer expertID, Integer activityID, Integer participantID, Integer scoreItemID) {
        Integer i=infosMapper.selectScoreByP(expertID,activityID,participantID,scoreItemID);
        return i;
    }

    public Double getScoreByPE(Integer expertId,Integer participantID,Integer activityId){
        Double S=infosMapper.selectScoreByPE(expertId,participantID,activityId);
        return S;
    }

    public void updateScore(Integer expertID, Integer activityID, Integer participantID, Integer scoreItemID, Double score) {
        infosMapper.updateScore(expertID,activityID,participantID,scoreItemID,score);

    }
    public void delete(Integer activityID){
        infosMapper.deleteByActivityID(activityID);
    }

    public List<Infos> getInforsList(Integer activitiesID,Integer groupId){
        List<Infos> infosList= infosMapper.selectInforsList(activitiesID,groupId);
        return infosList;
    }

    public void savetext(Infos info){
        Integer res = infosMapper.selectStudent(info.getStudentID(),info.getActivityID());
        info.setParticipantID(res);
        if (infosMapper.UpdateScore1(info) == 0){ // 更新不到记录则添加
            infosMapper.insertScore1(info);
        }
    }
    public List<Participates> getPartipicantByActivityId(Integer activityID,Integer infoItemID,List<String> infoContent){
        List<Integer> participantID = new ArrayList<>();
        if(infoContent.size() == 0){
            List<Infos> infosList = infosMapper.getParticipantIDtByAIdAndInfoItemID(activityID,infoItemID);
            for(int i = 0;i<infosList.size();i++){
                participantID.add(infosList.get(i).getParticipantID());
            }
        }else {
            List<Infos> res = infosMapper.getParticipantIDtByAIdAndInfoItemID(activityID,infoItemID);
            //根据infos的活动ID和infoitemid得到选手id
            for(int i = 0;i < res.size(); i++){
                if(infoContent.contains(res.get(i).getContent())){
                    participantID.add(res.get(i).getParticipantID());
                }
            }
        }
        List<Participates> pares = participatesMapper.getParticipantByAIdAndID(activityID,participantID);
        return pares;
    }
}
