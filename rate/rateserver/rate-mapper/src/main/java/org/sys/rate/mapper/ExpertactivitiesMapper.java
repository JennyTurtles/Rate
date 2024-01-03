package org.sys.rate.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.web.bind.annotation.RequestParam;
import org.sys.rate.model.Expertactivities;
import org.sys.rate.model.Experts;

@Mapper
public interface ExpertactivitiesMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(Expertactivities record);

    int update(Expertactivities record);

    int insertSelective(Expertactivities record);

    Expertactivities selectByPrimaryKey(Integer id);

    Integer updategroupid(@Param("groupid") Integer groupid, @Param("activityid") Integer activityid,@Param("id") Integer id);

    Integer deletegroupid(@Param("groupid") Integer groupid, @Param("activityid") Integer activityid,@Param("id") Integer id);

    Integer deleteNogroupid(@Param("activityid") Integer activityid,@Param("id") Integer id);

    Integer checkByIDandActivityID(@Param("id") Integer id, @Param("activityid") Integer activityid);

    Integer getOldGroupByIDandActivityID(@Param("id") Integer id, @Param("activityid") Integer activityid);

    int updateByPrimaryKeySelective(Expertactivities record);

    int updateByPrimaryKey(Expertactivities record);

    Integer selectGroupId(@Param("activitiesId") Integer activitiesId, @Param("expertid") Integer expertid);

    Boolean selectState(@Param("activitiesID") Integer activitiesID,
                        @Param("expertID") Integer expertID,
                        @Param("groupId") Integer groupId);

    String selectGroupNameById(Integer activityID,Integer groupId);

    void updateState(@Param("activityId") Integer activityId,
                     @Param("expertID") Integer expertID,
                     @Param("groupId") Integer groupId,
                     @Param("finished") Boolean finished);

    List<Expertactivities> selectAllExpertsByFinished(@Param("activityId") Integer activityId,
                                             @Param("groupId") Integer groupId);

    List<Expertactivities> getActByExpertID(@Param("expertID") Integer expertID);

    @Update("UPDATE activities set expertCount=\n" +
            "(SELECT COUNT(*) from expertactivities WHERE activityID = #{activityID} ) \n" +
            "where ID=#{activityID}")
    void updateActExpertCount(Integer activityID);

    @Update("UPDATE `groups` set expertCount=\n" +
            "(SELECT COUNT(*) from expertactivities WHERE groupID = #{groupID} ) \n" +
            "where ID = #{groupID}")
    void updateGroupExpertCount(Integer groupID);

    @Delete("DELETE FROM expertactivities WHERE activityID = #{activityID}")
    void deleteByActivityid(Integer activityID);
}
