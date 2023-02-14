package org.sys.rate.service.expert;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.sys.rate.mapper.ScoreItemMapper;
import org.sys.rate.model.ScoreItem;

import java.util.List;

@Service
public class ScoreitemService {
    @Autowired
    ScoreItemMapper scoreitemMapper;

    public List<ScoreItem> getScoreItemByActivityId(Integer activityId){
       List<ScoreItem> scoreitems= scoreitemMapper.selectScoreItemByActivityId(activityId);
       return scoreitems;
    }
}
