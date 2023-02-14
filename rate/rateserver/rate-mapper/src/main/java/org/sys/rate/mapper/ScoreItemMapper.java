package org.sys.rate.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.sys.rate.model.InfoItemValue;
import org.sys.rate.model.ScoreItem;
import org.sys.rate.model.ScoreItemValue;

@Mapper
public interface ScoreItemMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(ScoreItem record);

    int insertSelective(ScoreItem record);

    List<ScoreItemValue> getScoreItemAll(@Param("activitiesID") Integer activitiesID,@Param("participantID") Integer participantID);

    List<ScoreItemValue> getScoreItemValue(@Param("activitiesID") Integer activitiesID,@Param("participantID") Integer participantID,@Param("emp") ScoreItemValue scoreItemValue);

    List<ScoreItemValue> getScoreTotalValue(@Param("activitiesID") Integer activitiesID,@Param("participantID") Integer participantID,@Param("emp") ScoreItemValue scoreItemValue);

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

}
