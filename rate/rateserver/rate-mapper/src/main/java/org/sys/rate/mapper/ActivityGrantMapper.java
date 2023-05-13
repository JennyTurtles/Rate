package org.sys.rate.mapper;

import org.sys.rate.model.ActivityGrant;

import java.util.List;

public interface ActivityGrantMapper {
    int insertRecordOfAddActivity(Integer adminID,Integer activityID);
    List<ActivityGrant> selectAllOfCurrentActivity(Integer activityID, Integer institutionID);
    int insertRecordListOfAddActivity(List<Integer> adminID,Integer activityID,Integer institutionID);
    int deletePermissions(List<Integer> adminID,Integer activityID,Integer institutionID);
}
