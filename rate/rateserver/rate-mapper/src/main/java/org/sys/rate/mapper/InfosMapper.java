package org.sys.rate.mapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.sys.rate.model.Infos;

import java.util.List;

@Mapper
public interface InfosMapper {

    int insertScore(@Param("expertID") Integer expertID,
                    @Param("activityID")Integer activityID,
                    @Param("participantID")Integer participantID,
                    @Param("scoreItemID")Integer scoreItemID,
                    @Param("score")Double score);

    Integer insertScore1(Infos scores);

    Integer CheckExists(Infos scores);

    void UpdateScore1(Infos scores);

    List<Infos> selectScoreListByAE(@Param("activitiesId") Integer activitiesId, @Param("expertid") Integer expertid);

    List<Infos> selectScoreListByAE_null(@Param("activitiesId") Integer activitiesId, @Param("expertid") Integer expertid);

    void updateScore(@Param("expertID")Integer expertID,
                     @Param("activityID")Integer activityID,
                     @Param("participantID")Integer participantID,
                     @Param("scoreItemID")Integer scoreItemID,
                     @Param("score")Double score);

    int selectScoreByP(@Param("expertID")Integer expertID, @Param("activityID")Integer activityID, @Param("participantID")Integer participantID, @Param("scoreItemID")Integer scoreItemID);

    Double selectScoreByPE(@Param("expertId") Integer expertId,
                           @Param("participantID") Integer participantID,
                           @Param("activityId") Integer activityId);

    void deleteByActivityID(@Param("activityID") Integer activityID);

    List<Infos> selectInforsList(@Param("activitiesID") Integer activitiesID,
                                 @Param("groupId") Integer groupId);
    void updateInfoContent(Infos info);

    Integer selectStudent(Integer studentID,Integer activityID);
    String selectInfosContent(Integer activityID,Integer participantID,Integer infoItemID);

}
