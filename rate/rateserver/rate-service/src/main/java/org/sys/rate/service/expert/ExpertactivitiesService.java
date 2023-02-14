package org.sys.rate.service.expert;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.sys.rate.mapper.ExpertactivitiesMapper;
import org.sys.rate.model.Expertactivities;
import org.sys.rate.model.Experts;

import java.util.List;

@Service
public class ExpertactivitiesService {
    @Autowired
    ExpertactivitiesMapper expertactivitiesMapper;


    public Integer getgroupId(Integer activitiesId, Integer expertid) {
        Integer groupId= expertactivitiesMapper.selectGroupId(activitiesId,expertid);
        return  groupId;
    }
    public Boolean getState(Integer activitiesID, Integer expertID,Integer groupId) {
        Boolean finished= expertactivitiesMapper.selectState(activitiesID,expertID,groupId);
        return  finished;
    }

    public String getgroupNameById(Integer activityID,Integer groupId) {
        String groupName=expertactivitiesMapper.selectGroupNameById(activityID,groupId);
        return groupName;
    }
    public void updateState(Integer activityId,Integer expertID,Integer groupId,Boolean finished){
        expertactivitiesMapper.updateState(activityId,expertID,groupId,finished);
    }

    public List<Expertactivities> selectAllExpertsByFinished(Integer activityId, Integer groupId){
        List<Expertactivities> Experts=expertactivitiesMapper.selectAllExpertsByFinished(activityId,groupId);
        return Experts;
    }

    public List<Expertactivities> selectActByExpertID(Integer expertID){
        List<Expertactivities> ExActivities=expertactivitiesMapper.getActByExpertID(expertID);
        return ExActivities;
    }

}
