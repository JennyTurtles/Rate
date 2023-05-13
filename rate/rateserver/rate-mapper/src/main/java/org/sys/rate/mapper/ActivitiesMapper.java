package org.sys.rate.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.sys.rate.model.Activities;
import org.sys.rate.model.ScoreDetail;

import java.util.Date;
import java.util.List;

@Mapper
public interface ActivitiesMapper {
    int deleteByPrimaryKey(Integer id);

    int getInstitution_Total(Activities record);
    int getInstitution_Current_Total(Activities record);
    int insert(Activities record);
    int insertScoreItem(Activities record);
    void insert_update(Activities record);

    int predelete(Integer id);

    int delete(Integer id);

    int endActivities(Activities record);

    int update(Activities record);

    int insertSelective(Activities record);

    Activities selectByPrimaryKey(Integer id);

    //接口：返回该活动管理员的InstituteId
    Integer selectByActivityId(Integer id);


    List<Activities> getActivitiesByPage(@Param("page") Integer page, @Param("size") Integer size, Integer institutionID,Integer ID);

    @Select("SELECT * FROM activities WHERE parentID = #{activityID}")
    List<Activities> getSubActivities(Integer activityID);

    List<Activities> getActivitiesByPageAndCompany(@Param("page") Integer page, @Param("size") Integer size, @Param("company") String company,@Param("institutionID") Integer institutionID);

    Long getTotal(@Param("emp") Activities employee, @Param("institutionID") Integer institutionID);

    Long getTotalByCompany(@Param("company") String company, @Param("institutionID") Integer institutionID);

    Integer maxWorkID();

    Double getFullScore(@Param("activityID") Integer activityID);

    List<Activities> getAllActivity_info();

    List<ScoreDetail> getActivityScore(@Param("activityID") Integer activityID);

    Integer updateEmployeeSalaryById(@Param("eid") Integer eid, @Param("sid") Integer sid);

    Integer selectScoreItemCount(Integer activitiesId);

    String selectActivitiesName(@Param("activityID") Integer activityID);

    String selectActivitiesDate(@Param("activityID") Integer activityID);

    Activities queryById(@Param("id")Integer id);
    List<Activities>  selectActivity(@Param("activityID") Integer activityID);
    List<Activities>  selectActivities( List<Integer> activityID);

    @Update("update activities set score = score + #{score} where ID = #{activityID}")
    Integer updateScore(Integer activityID, double score); // score为分差，新-旧

    @Update("update activities set requireGroup = #{requireGroup} where ID = #{activityID}")
    int changeRequireGroup(Integer activityID, Integer requireGroup);

    @Update("UPDATE activities\n" +
            "SET expertCount = (\n" +
            "    SELECT tmp.expertCount \n" +
            "    FROM (\n" +
            "        SELECT expertCount \n" +
            "        FROM activities \n" +
            "        WHERE id = #{parID}\n" +
            "    ) AS tmp\n" +
            ") WHERE id = #{subID};")
    Integer fixExpertCount(Integer parID, Integer subID);

    @Update("UPDATE activities\n" +
            "SET participantCount = (\n" +
            "    SELECT tmp.participantCount \n" +
            "    FROM (\n" +
            "        SELECT participantCount \n" +
            "        FROM activities \n" +
            "        WHERE id = #{parID}\n" +
            "    ) AS tmp\n" +
            ") WHERE id = #{subID};")
    Integer fixParCount(Integer parID, Integer subID);

    @Select("SELECT ID FROM activities WHERE parentID = #{activityID} AND deleteFlag = FALSE LIMIT 1")
    Integer checkHaveSub(Integer activityID);
}
