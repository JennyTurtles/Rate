package org.sys.rate.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.sys.rate.model.Activities;
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
    String getTutorInfo(Integer id);

    @Select("select * from teacher where name like CONCAT('%', #{name}, '%')")
    List<Teachers> selectByName(String name);

    @Select("SELECT t.name, t.jobnumber, t.ID FROM teacher t WHERE t.institutionID = 1 and (t.name like CONCAT('%',#{nameOrJobNumber},'%') or t.jobnumber like CONCAT('%',#{nameOrJobNumber},'%'))")
    List<Teachers> searchByNameOrJobNumber(String nameOrJobNumber, Integer institutionID);

    @Select("select t.id, t.name from teacher t, graduatestudent g where g.studentID = #{studentID} and g.tutorID = t.ID order by g.ID limit 1;")
    Teacher getByStudentId(Long studentID);

    @Update("update teacher set sign = #{absolutePath} where ID = #{id}")
    void save(String id, String absolutePath);

    @Select("select sign from teacher where ID = #{tutorId}")
    String getSignUrl(String tutorId);
}
