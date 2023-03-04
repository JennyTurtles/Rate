package org.sys.rate.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.sys.rate.model.InfoItem;
import org.sys.rate.model.InfoItemValue;
import org.sys.rate.model.ScoreItemValue;

import java.util.List;

@Mapper
public interface InfoItemMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(InfoItem record);

    List<InfoItemValue> getInfoItemAll(@Param("activitiesID") Integer activitiesID, @Param("participantID") Integer participantID);

    List<InfoItemValue> getInfoItemValue(@Param("activitiesID") Integer activitiesID,@Param("participantID") Integer participantID,@Param("emp") InfoItemValue scoreItemValue);



    InfoItem selectByPrimaryKey(Integer id);

    List<InfoItem> selectScoreItemByActivityId(Integer activityId);

    List<InfoItem> selectInfoItemByActivityIdWhereNotByExpert(Integer activityId);

    int ScoreItemByActivityIdWhereNotByExpertCount(Integer activityId);

    void insert_update(InfoItem record);

    int delete(InfoItem record);

    int update(InfoItem record);

    List<InfoItem> getActivitiesByPage(@Param("keywords") Integer keywords,@Param("page") Integer page, @Param("size") Integer size, @Param("emp") InfoItem employee);

    List<InfoItem> getActivitiesByPageAndCompany(@Param("page") Integer page, @Param("size") Integer size, @Param("company") String company);

    //List<ScoreItem> getParticipatesByPage(@Param("page") Integer page, @Param("size") Integer size, @Param("emp") ScoreItem employee,@Param("beginDateScope") Date[] beginDateScope);

    Long getTotal(@Param("keywords") Integer keywords,@Param("emp") InfoItem employee);

    Integer getTotalforactivityCount(@Param("keywords") Integer keywords);

    Integer maxWorkID();

    List<InfoItem> selectInfoItemByActivityId(@Param("activitiesID") Integer activitiesID);

    List<InfoItem> selectInforItemAndContentByActivityIdAndPIdAndInfoId(@Param("activitiesID") Integer activitiesID,@Param("participantID") Integer participantID);


    List<InfoItem> selectInfoItemByActivityIdIsShow(@Param("activitiesID") Integer activitiesID);

    @Select("SELECT * FROM infoitem WHERE activityID = #{activityID}")
    List<InfoItem> getAll(Integer activityID);
}
