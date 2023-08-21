package org.sys.rate.mapper;

import java.util.List;

import org.apache.ibatis.annotations.*;
import org.sys.rate.model.*;

@Mapper
public interface ExpertsMapper {
    int updateByIdNumber(Experts experts);

    int checkUsername(String username);

    Experts loadUserByUsername(String username);

    int update(Experts record);

    int updatePasswd(@Param("id") Integer id,@Param("password") String password);

    int check(Experts record);

    List<Experts> getExpertsByPage(@Param("keywords") Integer keywords,@Param("page") Integer page, @Param("size") Integer size, @Param("emp") Experts employee);

    List<PEexport> getExperts(@Param("activityID") Integer activityID);

    Long getTotal(@Param("keywords") Integer keywords,@Param("emp") Experts employee);

    List<Role> getAdminRolesById(Integer id);


    int deleteById(Integer id);

    int insert(Experts record);

    int insertSelective(Experts record);

    List<Activities>selectActivitiesById(Integer id);

    Experts selectByPrimaryId(Integer id);

   // Experts selectByPrimaryKey(ExpertsKey key);

    int updateByPrimaryKeySelective(Experts record);

    int updateByPrimaryKey(Experts record);

    Long getTotalActivities(Integer id);

    int withdrawScore(@Param("activityID") Integer activityID,@Param("groupID") Integer groupID,@Param("teacherID") Integer teacherID);

    Integer getID(@Param("idNumber") String idNumber);

    @Select("SELECT COUNT(*) from expertactivities WHERE teacherID = #{teaId}")
    int isExpert(Integer teaId);

    void addParent(List<Experts> list);

    @Select("select t.ID as id, g.`name` as groupName,g.ID as groupID,e.finished,e.role , t.username, t.institutionID, t.name, t.department, t.IDNumber, t.password,t.phone, t.email \n" +
            "from teacher t\n" +
            "INNER JOIN expertactivities e ON t.ID=e.teacherID\n" +
            "LEFT JOIN `groups` g on e.groupID = g.ID\n" +
            "WHERE e.activityID = #{activityID}")
    List<Experts> getExpertsByActID(Integer activityID);

    @Select("SELECT * from grade_form WHERE activityID = #{activityID}")
    List<GradeFormEntry> getGradeForm(Integer activityID);

    @Insert("INSERT INTO grade_form(activityID,typeID,targetID,coef) \n" +
            "VALUES(#{activityID},#{typeID},#{targetID},#{coef}) ON DUPLICATE KEY UPDATE\n" +
            "activityID = VALUES(activityID),typeID = VALUES(typeID),\n" +
            "targetID = VALUES(targetID),coef = VALUES(coef)")
    void saveGradeForm(GradeFormEntry gradeFormEntry);

    void saveAllGradeForm(List<GradeFormEntry> gradeFormEntryList);

    @Select("SELECT ID,name,jobnumber,institutionID,sex,department,IDNumber,phone,email\n" +
            "FROM teacher t\n" +
            "WHERE deleteFlag = 0 AND institutionID = #{institutionID}")
    List<Experts> getByInstitutionID(Integer institutionID);


    @Insert("INSERT IGNORE INTO teacher (name,phone,IDNumber,email,institutionID) VALUES (#{name},#{phone},#{idnumber},#{email},#{institutionid})")
    @Options(useGeneratedKeys = true, keyProperty = "ID")
    Integer manualAdd(Experts experts);

    @Select("SELECT * FROM teacher WHERE IDNumber = #{idnumber}")
    Experts getByIDNumber(String idnumber);

    @Insert("INSERT IGNORE INTO teacher (name,phone,IDNumber,email,institutionID,username,password) " +
            "VALUES (#{name},#{phone},#{idnumber},#{email},#{institutionid},#{username},#{password})")
    @Options(useGeneratedKeys = true, keyProperty = "ID")
    Integer manualAddWithUserName(Experts experts);


    @Update("UPDATE expertactivities SET role = '组长' WHERE groupID = #{groupID} AND teacherID = #{teacherID}")
    Integer setLeader(Integer groupID, Integer teacherID);

    @Select("SELECT ID \n" +
            "FROM expertactivities \n" +
            "WHERE role = '组长' AND groupID = #{groupID} LIMIT 1")
    Integer checkLeader(Integer groupID);

    @Select("SELECT t.ID,name FROM expertactivities e,teacher t\n" +
            "WHERE groupID = #{groupID} AND e.teacherID = t.ID LIMIT 1")
    Experts getCandidateLeader(Integer groupID);


    @Select("SELECT s.ID AS ID,s.`name`\n" +
            "FROM expertactivities ea, participants p,thesis t,student s,undergraduate u\n" +
            "WHERE ea.teacherID = #{tutorID} AND ea.activityID = #{activityID} AND ea.groupID = p.groupID \n" +
            "AND u.studentID = p.studentID AND t.tutorID = #{tutorID} AND t.studentID = u.ID\n" +
            "AND u.studentID = s.ID")
    List<Student> getStudentsForTutor(Integer tutorID, Integer activityID);
}
