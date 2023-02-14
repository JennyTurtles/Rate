package org.sys.rate.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.sys.rate.model.Activities;

import java.util.Date;
import java.util.List;

@Mapper
public interface ActivitiesMapper {
    int deleteByPrimaryKey(Integer id);

    int getInstitution_Total(Activities record);
    int getInstitution_Current_Total(Activities record);
    int insert(Activities record);
    void insert_update(Activities record);

    int predelete(Integer id);

    int delete(Integer id);

    int endActivities(Activities record);

    int update(Activities record);

    int insertSelective(Activities record);

    Activities selectByPrimaryKey(Integer id);

    //接口：返回该活动管理员的InstituteId
    Integer selectByActivityId(Integer id);


    List<Activities> getActivitiesByPage(@Param("page") Integer page, @Param("size") Integer size, Integer institutionID);

    List<Activities> getActivitiesByPageAndCompany(@Param("page") Integer page, @Param("size") Integer size, @Param("company") String company,@Param("institutionID") Integer institutionID);

    Long getTotal(@Param("emp") Activities employee, @Param("institutionID") Integer institutionID);

    Long getTotalByCompany(@Param("company") String company, @Param("institutionID") Integer institutionID);

    Integer maxWorkID();

    List<Activities> getAllActivity_info();

    Integer updateEmployeeSalaryById(@Param("eid") Integer eid, @Param("sid") Integer sid);

    Integer selectScoreItemCount(Integer activitiesId);

    String selectActivitiesName(@Param("activityID") Integer activityID);

    String selectActivitiesDate(@Param("activityID") Integer activityID);

    Activities queryById(@Param("id")Integer id);
    List<Activities>  selectActivity(@Param("activityID") Integer activityID);
}
