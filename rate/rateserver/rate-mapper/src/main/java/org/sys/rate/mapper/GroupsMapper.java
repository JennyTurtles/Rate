package org.sys.rate.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.web.bind.annotation.RequestParam;
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

    @Select("SELECT name,IDNumber,code,activityID,groupID,studentID FROM student s,participants p\n" +
            "WHERE s.ID = p.studentID AND groupID = #{groupID}")
    List<Participates> getGroupPars(Integer groupID);

    @Update("UPDATE `groups` SET participantCount = (\n" +
            "SELECT count(*) FROM participants WHERE activityID = #{activityID} and groupID = #{groupID})\n" +
            "WHERE activityID = #{activityID} AND ID = #{groupID}")
    Integer updateParCount(Integer activityID, Integer groupID);
}
