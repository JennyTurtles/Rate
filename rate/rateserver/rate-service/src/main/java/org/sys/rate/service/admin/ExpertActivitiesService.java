package org.sys.rate.service.admin;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.sys.rate.mapper.ExpertactivitiesMapper;

import javax.annotation.Resource;

@Service
public class ExpertActivitiesService {

    @Resource
    ExpertactivitiesMapper expertactivitiesMapper;

    @Transactional
    public void updateExpertCount(Integer activityID, Integer groupID){
        expertactivitiesMapper.updateGroupExpertCount(groupID);
        expertactivitiesMapper.updateActExpertCount(activityID);
    }

}
