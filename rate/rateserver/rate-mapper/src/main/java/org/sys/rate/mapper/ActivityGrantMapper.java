package org.sys.rate.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.sys.rate.model.ActivityGrant;

import java.util.List;

@Mapper
public interface ActivityGrantMapper {
    int insertRecordOfAddActivity(Integer adminID,Integer activityID);
    List<ActivityGrant> selectAllOfCurrentActivity(Integer activityID, Integer institutionID);
    int insertRecordListOfAddActivity(List<Integer> adminID,Integer activityID,Integer institutionID);
    int deletePermissions(List<Integer> adminID,Integer activityID,Integer institutionID);
}
