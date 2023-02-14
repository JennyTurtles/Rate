package org.sys.rate.service.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.sys.rate.mapper.ScoresMapper;
import org.sys.rate.model.Scores;

import java.util.List;

@Service
public class ScoresService {
    @Autowired
    ScoresMapper scoresMapper;

    public Integer saveScore(Integer expertID, Integer activityID, Integer participantID, Integer scoreItemID, Double score) {
        int i=scoresMapper.insertScore(expertID,activityID,participantID,scoreItemID,score);
        return i;

    }

    public Integer saveScore1(Scores scores) {
        Integer i= scoresMapper.insertScore1(scores);
        return i;
    }

    public List<Scores> getScoreListNoExpert(Integer activitiesId, Integer groupId) {
        List<Scores> scoresList= scoresMapper.selectScoreListByAE(activitiesId,groupId);
        return scoresList;
    }

    public List<Scores> getScoreList_null(Integer activitiesId, Integer expertid) {
        List<Scores> scoresList= scoresMapper.selectScoreListByAE_null(activitiesId,expertid);
        return scoresList;
    }

    public Integer getScoreByP(Integer expertID, Integer activityID, Integer participantID, Integer scoreItemID) {
        Integer i=scoresMapper.selectScoreByP(expertID,activityID,participantID,scoreItemID);
        return i;
    }

    public Double getScoreByPE(Integer expertId,Integer participantID,Integer activityId){
        Double S=scoresMapper.selectScoreByPE(expertId,participantID,activityId);
        return S;
    }

    public void updateScore(Integer expertID, Integer activityID, Integer participantID, Integer scoreItemID, Double score) {
        scoresMapper.updateScore(expertID,activityID,participantID,scoreItemID,score);

    }
    public void delete(Integer activityID){
        scoresMapper.deleteByActivityID(activityID);
    }

    public  List<Scores> getScoreListByExpert(Integer expertID,Integer activitiesId, Integer groupId) {
        List<Scores> scoresList= scoresMapper.selectScoreListByAP(expertID,activitiesId,groupId);
        return scoresList;
    }

    public  List<Scores> getScoreListExpert(Integer expertID,Integer activitiesId, Integer groupId) {
        List<Scores> scoresList= scoresMapper.selectScoreListExpert(expertID,activitiesId,groupId);
        return scoresList;
    }
}
