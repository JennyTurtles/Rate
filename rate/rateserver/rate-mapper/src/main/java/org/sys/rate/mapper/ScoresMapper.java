package org.sys.rate.mapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.sys.rate.model.Scores;

import java.util.List;

@Mapper
public interface ScoresMapper {

    int insertScore(@Param("expertID") Integer expertID,
                    @Param("activityID")Integer activityID,
                    @Param("participantID")Integer participantID,
                    @Param("scoreItemID")Integer scoreItemID,
                    @Param("score")Double score);

    Integer insertScore1(Scores scores);

    List<Scores> selectScoreListByAE(@Param("activitiesId") Integer activitiesId,
                                     @Param("groupId") Integer groupId);

    Integer CheckExists(Scores scores);

    void UpdateScore1(Scores scores);

    List<Scores> selectScoreListByAE_null(@Param("activitiesId") Integer activitiesId, @Param("expertid") Integer expertid);

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

    List<Scores> selectScoreListByAP(@Param("expertID") Integer expertID,
                                     @Param("activitiesId") Integer activitiesId,
                                     @Param("groupId") Integer groupId);

    List<Scores> selectScoreListExpert(@Param("expertID") Integer expertID,
                                     @Param("activitiesId") Integer activitiesId,
                                     @Param("groupId") Integer groupId);
}
