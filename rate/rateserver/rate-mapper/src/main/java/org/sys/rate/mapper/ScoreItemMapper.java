package org.sys.rate.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.sys.rate.model.InfoItem;
import org.sys.rate.model.ScoreAverage;
import org.sys.rate.model.ScoreItem;
import org.sys.rate.model.ScoreItemValue;

import java.util.List;

@Mapper
public interface ScoreItemMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(ScoreItem record);

    int insertSelective(ScoreItem record);

    List<ScoreItemValue> getScoreItemAll(@Param("activitiesID") Integer activitiesID,@Param("participantID") Integer participantID);

    List<ScoreItemValue> getScoreItemValue(@Param("activitiesID") Integer activitiesID,@Param("participantID") Integer participantID,@Param("emp") ScoreItemValue scoreItemValue);

    List<ScoreItemValue> getScoreTotalValue(@Param("activitiesID") Integer activitiesID,@Param("participantID") Integer participantID);

    ScoreItem selectByPrimaryKey(Integer id);


    int updateByPrimaryKeySelective(ScoreItem record);

    int updateByPrimaryKey(ScoreItem record);

    List<ScoreItem> selectScoreItemByActivityId(Integer activityId);

    List<ScoreItem> selectScoreItemByEActivityId(Integer activityId);

    List<ScoreItem> selectScoreItemByActivityIdWhereNotByExpert(Integer activityId);

    Integer selectScoreItemFinal(Integer activityId);

    int ScoreItemByActivityIdWhereNotByExpertCount(Integer activityId);

    void insert_update(ScoreItem record);

    int delete(ScoreItem record);

    int update(ScoreItem record);

    List<ScoreItem> getActivitiesByPage(@Param("keywords") Integer keywords,@Param("page") Integer page, @Param("size") Integer size, @Param("emp") ScoreItem employee);

    List<ScoreItem> getActivitiesByPageAndCompany(@Param("page") Integer page, @Param("size") Integer size, @Param("company") String company);

    //List<ScoreItem> getParticipatesByPage(@Param("page") Integer page, @Param("size") Integer size, @Param("emp") ScoreItem employee,@Param("beginDateScope") Date[] beginDateScope);

    Long getTotal(@Param("keywords") Integer keywords,@Param("emp") ScoreItem employee);

    Integer getTotalforactivityCount(@Param("keywords") Integer keywords);

    //Long getTotaloutput(@Param("emp") ScoreItem employee,@Param("beginDateScope") Date[] beginDateScope);

    //Long getTotalByCompany(@Param("company") String company);

    public Integer maxWorkID();

    List<ScoreItemValue> getScoreEx(@Param("activityID") Integer activityID,@Param("expertID") Integer expertID,@Param("participantID") Integer participantID);

    List<ScoreItem> getAllByActicityID(Integer activityID);

    @Select("select name from scoreitem where id=#{id}")
    String getNameByID(Integer id);

    @Select("select * from scoreitem where id=#{ID}")
    ScoreItem getById(Integer ID);

    @Select("select * from scoreitem where activityID=#{activityID}")
    List<ScoreItem> getByActivityID(Integer activityID);


    @Select("select score_average.participantID,scoreitem.ID as scoreItemID,score_average.score,scoreitem.coef \n" +
            "from scoreitem LEFT JOIN score_average ON scoreitem.ID=score_average.scoreItemID\n" +
            "WHERE scoreitem.activityID = #{activityID}")
    List<ScoreAverage> getScoreAverageByActivityID(Integer activityID);

    @Select("SELECT sa.scoreItemID as id,s.name,sa.score\n" +
            "FROM participants p,score_average sa,scoreitem s\n" +
            "WHERE p.studentID = #{studentID} AND sa.participantID = p.ID AND sa.scoreItemID = s.ID")
    List<ScoreItem> getScoreItemsByStuID(Integer studentID);


    @Select("SELECT s.ID,activityID,s.name FROM scoreitem s, activities a\n" +
            "WHERE s.activityID = a.ID AND parentID = #{activityID}")
    List<ScoreItem> selectSubScoreItemByActId(Integer activityID);

    @Select("SELECT ID,activityID,name FROM scoreitem s WHERE activityID = #{activityID}")
    List<ScoreItem> selectScoreItemByActId(Integer activityID);

    @Select("SELECT a.name FROM scoreitem s,activities a WHERE s.ID = #{ID} AND s.activityID = a.ID")
    String getActivityName(Integer ID);

    @Select("select ID from scoreitem where activityID = #{activityID}")
    List<Integer> getIDByActivityID(Integer ID);

    @Delete("delete from score_average where scoreItemID = #{scoreItemID}")
    void deleteByScoreItemID(Integer scoreItemID);
}
