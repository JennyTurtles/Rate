package org.sys.rate.service.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.sys.rate.mapper.InfosMapper;
import org.sys.rate.model.Infos;

import java.util.List;

@Service
public class InfosService {
    @Autowired
    InfosMapper infosMapper;

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
}
