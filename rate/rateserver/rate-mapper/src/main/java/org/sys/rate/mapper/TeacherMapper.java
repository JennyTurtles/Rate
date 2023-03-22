package org.sys.rate.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.sys.rate.model.Activities;
import org.sys.rate.model.ScoreItem;
import org.sys.rate.model.Scores;
import org.sys.rate.model.Teacher;
import org.sys.rate.model.Teachers;

import java.util.List;

@Mapper
public interface TeacherMapper {
    Teacher loadUserByUsername(String username);

    Teacher getById(Integer ID);

    Teacher getByEmail(String email);

    int deleteById(Integer id);

    int insert(Teacher record);

    int insertSelective(Teacher record);


    List<Activities> selectActivitiesById(Integer id);

    Teacher selectByPrimaryId(Integer id);

    // Experts selectByPrimaryKey(ExpertsKey key);

    int updateByPrimaryKeySelective(Teacher record);

    int updateByPrimaryKey(Teacher record);

    Long getTotalActivities(Integer id);

    Integer selectByUsername(String expertUsername);

    String selectNameById(Integer expertID);

    //    List<Participates> getParticipates(Integer activitiesId);
    List<Teacher> getTotal();

    int delete(Teacher record);

    int update(Teacher record);

    Integer check(Integer eID,Integer aID,Integer pID,Integer sID);

    int updateScore(Scores NewScore);

    int insertScore(Scores NewScore);
}
