package org.sys.rate.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.sys.rate.model.Admin;
import org.sys.rate.model.AdminActivity;
import org.sys.rate.model.Role;

import java.util.List;

public interface AdminActivityMapper {
    int insertRecordOfAddActivity(Integer adminID,Integer activityID);
    List<AdminActivity> selectAllOfCurrentActivity(Integer activityID,Integer institutionID);
    int insertRecordListOfAddActivity(List<Integer> adminID,Integer activityID,Integer institutionID);
    int deletePermissions(List<Integer> adminID,Integer activityID,Integer institutionID);
}
