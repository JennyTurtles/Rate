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

    List<InfoItem> selectInfoItemByActivityId(Integer activitiesID,List<Integer> participantID);

    List<InfoItem> selectInforItemAndContentByActivityIdAndPIdAndInfoId(@Param("activitiesID") Integer activitiesID,@Param("participantID") Integer participantID);


    List<InfoItem> selectInfoItemByActivityIdIsShow(@Param("activitiesID") Integer activitiesID);

    @Select("SELECT * FROM infoitem WHERE activityID = #{activityID}")
    List<InfoItem> getAll(Integer activityID);

    List<InfoItem> selectInfoItemsByActivityId(Integer activityID);

    @Select("select name from infoitem where id=#{id}")
    String getNameByID(Integer id);

    @Select("select ID from infoitem where name=#{name} and activityID=#{activityID}")
    Integer getIDByNameAndActivityID(@Param("name") String name,@Param("activityID") Integer activityID);

    @Select("select infoitem.ID,infoitem.name,infos.content \n" +
            "from infoitem\n" +
            "LEFT JOIN infos ON infoitem.ID=infos.infoitemID\n" +
            "AND infos.participantID=#{participantID} AND infoitem.activityID = #{activityID}")
    List<InfoItem> getAllByParticipantID(Integer activityID,Integer participantID);

    @Select("select * from infoitem where activityID=#{activityID}")
    List<InfoItem> getInfoItemByActivityID(Integer activityID);

    @Select("select infos.participantID,infoitem.ID,infos.content from infoitem LEFT JOIN infos ON infoitem.ID=infos.infoitemID\n" +
            "AND infoitem.activityID = #{activityID}")
    List<InfoItem> getInfoItemParByActivityID(Integer activityID);

    @Select("select ID from infoitem where activityID=#{activityID}")
    List<Integer> getIDByActivityID(Integer newActID);

    //获得报考专业代码
    @Select("select ID from infoitem where name = '报考专业代码' and activityID=#{activityID}")
    Integer getMajorCode(Integer activityID);
}
