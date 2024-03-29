package org.sys.rate.mapper;

import org.apache.ibatis.annotations.*;
import org.sys.rate.model.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Mapper
public interface ParticipatesMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Participates record);
    void insert_update(Participates record);

    Integer getID(@Param("idNumber") String idNumber);

    Integer getlastDisplaySequence(@Param("groupID") Integer groupID);

    Integer getPID(Participates idNumber);

    int insert_relationship(Participates record);

    int update_relationship(Participates record);

    Integer checkByIDandActivityID(@Param("id") Integer id, @Param("activityid") Integer activityid);

    Integer checkGroupIDExists(@Param("studentID") Integer id, @Param("activityID") Integer activityid);

    Integer getOldGroupByIDandActivityID(@Param("id") Integer id, @Param("activityid") Integer activityid);

    String selectGroupNameById(Integer groupId);

    int delete(Participates record);

    @Delete("delete from participants where ID = #{ID} and activityID = #{activityID}")
    int deletePar(Integer ID, Integer activityID);

    int update(Participates record);

    int insertSelective(Participates record);

    Participates selectByPrimaryKey(Integer id);

    List<Participates> getActivitiesByPage(@Param("page") Integer page, @Param("size") Integer size, @Param("groupID") Integer groupID,@Param("emp") Participates employee);

    List<Participates> getPByGID(@Param("page") Integer page, @Param("size") Integer size, @Param("groupID") Integer groupID,@Param("emp") Participates employee);

    List<Participates> getPByACID(@Param("page") Integer page, @Param("size") Integer size, @Param("activityID") Integer activityID);

    List<Participates> getParticipantsPageByACID(@Param("page") Integer page, @Param("size") Integer size, @Param("activitiesID") Integer activitiesID,@Param("emp") Participates employee);
    List<Participates> getAllByActivityID(@Param("page") Integer page,@Param("size") Integer size,@Param("activityID") Integer activityID);
    List<Participates> getParticipantsPageByACIDNoGroup(@Param("page") Integer page, @Param("size") Integer size, @Param("activitiesID") Integer activitiesID,@Param("emp") Participates employee);
    List<Participates> getParticipantsPageByACIDHaveGroup(@Param("page") Integer page, @Param("size") Integer size, @Param("activitiesID") Integer activitiesID,@Param("emp") Participates employee);

    List<Participates> getLongParticipantsByPage(@Param("page") Integer page, @Param("size") Integer size, @Param("groupID") Integer groupID, @Param("activitiesID") Integer activitiesID,@Param("emp") Participates employee);

    List<Participates> getActivitiesByPageAndCompany(@Param("page") Integer page, @Param("size") Integer size, @Param("company") String company);

    List<Participates> getParticipatesByPage(@Param("page") Integer page, @Param("size") Integer size, @Param("emp") Participates employee,@Param("beginDateScope") Date[] beginDateScope);

    List<PEexport> getExpertScore(@Param("page") Integer page, @Param("size") Integer size,@Param("groupID") Integer groupID,@Param("activityID") Integer activityID);

    Long getTotalEA(@Param("activityID") Integer activityID,@Param("groupID") Integer groupID);

    Long getTotal(@Param("groupID") Integer groupID,@Param("emp") Participates employee);

    Long getTotalByACID(@Param("activitiesID") Integer activitiesID,@Param("emp") Participates employee);
    Long getActivityTotal(@Param("activityID") Integer activityID);
    Long getTotalByACIDNogroup(@Param("activitiesID") Integer activitiesID,@Param("emp") Participates employee);
    Long getTotalByACIDHaveGroup(@Param("activitiesID") Integer activitiesID,@Param("emp") Participates employee);

    Long getTotaloutput(@Param("emp") Participates employee,@Param("beginDateScope") Date[] beginDateScope);

    Long getTotalByCompany(@Param("company") String company);

    Integer maxWorkID();


    Integer addParticipatess(@Param("list") List<Participates> list);

    Participates getEmployeeById(@Param("ID")Integer id);

    List<Participates> getEmployeeByPageWithSalary(@Param("page") Integer page, @Param("size") Integer size);

    Integer updateEmployeeSalaryById(@Param("eid") Integer eid, @Param("sid") Integer sid);

    Integer subDisplaySequence(@Param("groupID") Integer groupID, @Param("small") Integer small,@Param("big") Integer big);

    Integer addDisplaySequence(@Param("groupID") Integer groupID, @Param("small") Integer small,@Param("big") Integer big);

    Integer saveDisplaySequence(@Param("groupID") Integer groupID, @Param("newP") Integer newP,@Param("ID") Integer ID);

    List<Participates> getPartByGroupId(@Param("activitiesID") Integer activitiesID,
                                        @Param("groupId") Integer groupId);

    List<PEexport> getPartByGroupIdForPEexpert(@Param("activityID") Integer activitiesID);
    List<PEexport> getPartByGroupIdForPEexpert_null(@Param("activityID") Integer activitiesID);

    List<PEexport> getPartByGroupIdForPEexpertByGID(@Param("groupID") Integer groupID);
    List<PEexport> getPartByGroupIdForPEexpert_nullByGID(@Param("groupID") Integer groupID);

    List<PEexport> getExpertSituationByExpertID(@Param("page") Integer page, @Param("size") Integer size,@Param("groupID") Integer groupID,@Param("expertID") Integer expertID);

    Long getExpertSituationTotal(@Param("groupID") Integer groupID,@Param("expertID") Integer expertID);


    List<Participates> selectAllParticipants(@Param("activityId") Integer activityId,
                                             @Param("groupId") Integer groupId);

    Integer saveScore(@Param("participateId") Integer participateId, @Param("score") Double score);

    void saveLscore(@Param("participantID") Integer participantID,
                    @Param("sum") Double sum);

    Integer saveAvgscore(@Param("participantID") Integer participantID,
                         @Param("activityID") Integer activityID);

    @Select("SELECT COUNT(*) from participants WHERE studentID = #{stuId}")
    int isParticipants(Integer stuId);
    int insertParByRegister(Participates par);
    List<Participates> getParticipantByAIdAndID(Integer activityID, List<Integer> participantID);

    Participates getParticipantIDByIDNumber(Integer activityID,String IDNumber);

    void deleteGroupsOfParticipantsAnaAcitivity(Integer activityID);
    void deleteGroups(Integer activityID);



    void updateGroupID(Integer activityID,Integer groupID,List<Integer> parID);
    List<Participates> getParticipantByAId(Integer activityID);
    List<Participates> getParticipantsByAId(Integer activityID);

    List<FinalValue> getInfoScoreByInfoID(@Param("activityID") Integer activityID, @Param("infoItemID") Integer infoItemID,@Param("page") Integer page,@Param("size") Integer size);
    List<Participates> getParticipantsBySID(Integer studentID);

    @Select("select ID from participants where studentID = #{studentID} limit 1")
    Integer existPar(Integer studentID);


    @Select("select ID from participants where activityID = #{activityID} and groupID = #{groupID}")
    List<Integer> getParticipantsIDByAIdAndGId(Integer activityID, Integer groupID);

    @Select("select ID from participants where activityID = #{activityID}")
    List<Integer> getParticipantsIDByAId(Integer id);


    // 通过活动ID获得ParticipantsDisplay的ID,name,code,score,groupName。连接participants表、groups表、student
    @Select("select p.ID as ID,s.name as name,p.code,p.score,g.name as groupName\n" +
            "from participants p,`groups` g,student s \n" +
            "where p.activityID = #{activityID} and p.groupID = g.ID and p.studentID = s.ID")
    List<ParticipantsDisplay> getParticipantsDisplay(Integer activityID);

    @Select("select p.ID as ID,s.name as name,p.code,p.score,g.name as groupName\n" +
            "from participants p,`groups` g,student s \n" +
            "where p.activityID = #{activityID} and p.groupID = #{groupID} and p.groupID = g.ID and p.studentID = s.ID")
    List<ParticipantsDisplay> getParticipantsDisplayByGroup(Integer activityID, Integer groupID);

    @Insert("INSERT INTO score_average \n" +
            "(SELECT NULL,scores.participantID,si.ID as scoreItemID,AVG(scores.score) as score \n" +
            "from scoreitem si, scores \n" +
            "WHERE si.ID = scores.scoreItemID AND si.activityID = #{activityID} AND participantID = #{participantID}\n" +
            "GROUP BY si.ID)\n" +
            "on duplicate key update score = VALUES(score)")
    Integer saveAvgScores(Integer participantID, Integer activityID);

    Integer addPars(List<Participates> list);

    Integer deletePars(List<Participates> list);

    @Select("SELECT p.ID,name,pp.groupID as groupID FROM participants pp RIGHT JOIN \n" +
            "(SELECT p.ID,name,IDNumber,code,activityID,groupID,studentID,s.institutionID FROM student s,participants p \n" +
            "WHERE s.ID = p.studentID AND p.groupID = #{groupID}) as p\n" +
            "on pp.studentID = p.studentID WHERE pp.groupID != #{groupID}")
    List<Participates> checkInOtherGroup(Integer groupID);

//    void addParent(List<Participates> list); // 活动ID，小组ID，学生ID设置为唯一索引，如果重复则不添加

    @Select("SELECT studentID FROM participants WHERE groupID = #{groupID}")
    List<Integer> getStudentIDbyGroupID(Integer groupID);

    @Select("SELECT studentID FROM participants WHERE activityID = #{activityID}")
    List<Integer> getStudentIDbyActID(Integer activityID);

    @Select("SELECT t.studentID \n" +
            "FROM participants p, thesis t\n" +
            "WHERE activityID = #{activityID} AND t.tutorID = #{tutorID} AND t.studentID = p.studentID")
    List<Integer> getStudentIDForTutor(Integer activityID, Integer tutorID);

    @Select("SELECT p1.ID as subID, p2.ID as parID\n" +
            "FROM participants p1 LEFT JOIN participants p2 on p1.studentID = p2.studentID\n" +
            "WHERE p1.activityID = #{subID} and p2.activityID = #{parID}")
    List<subID2ParID> getSubID2ParID(Integer subID, Integer parID);


    @Update("UPDATE `groups` set participantCount=\n" +
            "(SELECT COUNT(*) from participants WHERE groupID = #{groupID} ) \n" +
            "where ID=#{groupID}")
    void updateGroupParCount(Integer groupID);

    @Update("UPDATE activities set participantCount=\n" +
            "(SELECT COUNT(*) from participants WHERE activityID = #{activityID} ) \n" +
            "where ID=#{activityID}")
    void updateActParCount(Integer activityID);

    @Select("SELECT s.ID,gs.studentID,name,gs.institutionID,telephone,username,email,gs.stuNumber studentNumber\n" +
            "FROM student s,graduatestudent gs \n" +
            "WHERE gs.institutionID = #{institutionID} AND s.ID = gs.studentID AND gs.stuNumber IS NOT NULL")
    List<Participates> getGraduateByInstitutionID(Integer institutionID);

    @Select("SELECT s.ID,u.studentID,name,u.institutionID,telephone,username,email,u.stuNumber studentNumber\n" +
            "FROM student s,undergraduate u \n" +
            "WHERE u.institutionID = #{institutionID} AND s.ID = u.studentID AND u.stuNumber IS NOT NULL")
    List<Participates> getUndergraduateByInstitutionID(Integer institutionID);


    @Insert("INSERT IGNORE INTO student (name,telephone,email,institutionID) VALUES (#{name},#{telephone},#{email},#{institutionid})")
    @Options(useGeneratedKeys = true, keyProperty = "ID")
    Integer manualAdd(Participates participates);


    @Select("SELECT s.ID,name,IDNumber,telephone,email\n" +
            "FROM student s\n" +
            "WHERE IDNumber = #{IDNumber}")
    Participates getByIDNumber(String IDNumber);

    @Select("SELECT s.ID,name,telephone,email,code\n" +
            "FROM student s,participants p\n" +
            "WHERE code = #{code} AND p.activityID = #{activityID} AND s.ID = p.studentID")
    Participates getByCodeActivityID(String code, Integer activityID);

    @Select("SELECT role FROM student WHERE ID = #{studentID}")
    String getRole(Integer studentID);

    @Update("UPDATE student SET role = #{role} WHERE ID = #{studentID}")
    void setParticipateRole(Integer studentID, String role);

    @Select("SELECT role FROM student WHERE IDNumber = #{IDNumber}")
    String getRoleByIDNumber(String IDNumber);

    @Select("SELECT p.ID, studentID, name FROM participants p,student s\n" +
            "WHERE code = #{code} AND activityID = #{activityID} AND p.studentID = s.ID")
    Participates getParticipateIDByCodeAndActivityID(String code, Integer activityID);

    @Insert("INSERT INTO participants (studentID,activityID,code) VALUES (#{studentID},#{activityID},#{code})")
    void addPar(Integer studentID, Integer activityID, String code);

    @Update("UPDATE participants SET studentID = #{studentID} WHERE ID = #{id}")
    void updatePar(Integer studentID, Integer id);

    @Delete("DELETE FROM student WHERE ID = #{studentID}")
    void deleteStudent(Integer studentID);

    @Select("SELECT * FROM participants WHERE groupID = #{groupID}")
    List<Participates> getPartByGroupID(@Param("groupID") Integer groupID);

    void insertParticipates(List<Participates> list);
}
