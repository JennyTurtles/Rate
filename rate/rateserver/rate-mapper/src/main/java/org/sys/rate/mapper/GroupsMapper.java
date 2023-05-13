package org.sys.rate.mapper;

import org.apache.ibatis.annotations.*;
import org.springframework.web.bind.annotation.RequestParam;
import org.sys.rate.model.Experts;
import org.sys.rate.model.Groups;
import org.sys.rate.model.Participates;
import org.sys.rate.model.ScoreDetail;

import java.util.Date;
import java.util.List;

@Mapper
public interface GroupsMapper {
    int deleteByPrimaryKey(Integer id);
    int insert(Groups record);
    void insert_update(Groups record);

    int delete(Groups record);

    int update(Groups record);

    int insertSelective(Groups record);

    Groups selectByPrimaryKey(Integer id);

    //导入分组
    Integer insertMultipleGroups(Groups gp);
    void updateGroupCount(Integer activityID);

    List<Groups> getActivitiesByPage(@Param("keywords") Integer keywords,@Param("page") Integer page, @Param("size") Integer size, @Param("emp") Groups employee);

    List<Groups> getActivitiesByPageAndCompany(@Param("page") Integer page, @Param("size") Integer size, @Param("company") String company);

    List<Groups> getParticipatesByPage(@Param("page") Integer page, @Param("size") Integer size, @Param("emp") Groups employee,@Param("beginDateScope") Date[] beginDateScope);

    Long getTotal(@Param("keywords") Integer keywords,@Param("emp") Groups employee);

    Integer getTotalforactivityCount(@Param("keywords") Integer keywords);

    Long getTotaloutput(@Param("emp") Groups employee,@Param("beginDateScope") Date[] beginDateScope);

    List<ScoreDetail> getGroupScore(@Param("keywords") Integer keywords, @Param("page") Integer page, @Param("size") Integer size, @Param("emp") Groups employee);

    Long getTotalByCompany(@Param("company") String company);

    Integer maxWorkID();

    Integer queryMaxId();

    Integer addParticipatess(@Param("list") List<Groups> list);

    Groups getEmployeeById(Integer id);

    List<Groups> getEmployeeByPageWithSalary(@Param("page") Integer page, @Param("size") Integer size);

    Integer updateEmployeeSalaryById(@Param("eid") Integer eid, @Param("sid") Integer sid);

    Integer isGroupsExit(Integer activityID);

    @Select("select * from `groups` where ID = #{groupID}")
    List<Groups> getGroup(Integer groupID);

    @Select("SELECT p.ID,name,IDNumber,code,activityID,groupID,studentID,s.institutionID,telephone,username,email,password FROM student s,participants p\n" +
            "WHERE s.ID = p.studentID AND groupID = #{groupID}")
    List<Participates> getGroupPars(Integer groupID);

    @Select("SELECT p.ID,name,IDNumber,code,activityID,groupID,studentID,s.institutionID,telephone,username,email,password FROM student s,participants p\n" +
            "WHERE s.ID = p.studentID AND activityID = #{activityID}")
    List<Participates> getParsByID(Integer activityID);

    @Update("UPDATE `groups` SET participantCount = (\n" +
            "SELECT count(*) FROM participants WHERE activityID = #{activityID} and groupID = #{groupID})\n" +
            "WHERE activityID = #{activityID} AND ID = #{groupID}")
    Integer updateParCount(Integer activityID, Integer groupID);

    @Select("select * from `groups` where parentID = #{groupID} and activityID = #{activityID}")
    List<Groups> getSubGroups(Integer activityID,Integer groupID);

    @Select("select ID from `groups` where parentID = #{groupIDParent} and activityID = #{activityID} limit 1")
    Integer getSubGroupID(Integer activityID,Integer groupIDParent);

    @Insert("INSERT INTO `groups` (activityID, name, expertCount, participantCount, parentID)\n" +
            "VALUES (#{activityID},#{name},#{expertCount},#{participantCount},#{parentID})")
    @Options(useGeneratedKeys=true, keyProperty="ID", keyColumn="ID")
    void insertGroup(Groups group);

    @Select("SELECT e.ID,teacherID,name,jobnumber,institutionID,sex,department,IDNumber,phone,email,e.role,activityID,groupID\n" +
            "FROM expertactivities e, teacher t\n" +
            "WHERE e.teacherID = t.ID AND deleteFlag = 0 AND groupID = #{groupID}")
    List<Experts> getGroupExperts(Integer groupID);

    @Select("SELECT e.ID,teacherID,name,jobnumber,institutionID,sex,department,IDNumber,phone,email,e.role,activityID,groupID\n" +
            "FROM expertactivities e, teacher t\n" +
            "WHERE e.teacherID = t.ID AND deleteFlag = 0 AND activityID = #{groupID}")
    List<Experts> getGroupExpertsByActID(Integer activityID);

    @Update("UPDATE `groups` SET expertCount = (\n" +
            "SELECT count(*) FROM expertactivities WHERE activityID = #{activityID} and groupID = #{groupID})\n" +
            "WHERE activityID = #{activityID} AND ID = #{groupID}")
    Integer updateExpertCount(Integer activityID, Integer groupID);

    @Select("SELECT ID FROM `groups` WHERE activityID = #{activityID} AND name = #{groupName} LIMIT 1")
    Integer selectIDByActivityIdAndGroupName(@Param("activityID") Integer activityID, @Param("groupName") String groupName);


}
