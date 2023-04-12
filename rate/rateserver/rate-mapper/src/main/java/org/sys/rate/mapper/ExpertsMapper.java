package org.sys.rate.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
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

    @Select("select t.ID, g.`name` groupName,e.finished,e.role , t.username, t.institutionID, t.name, t.department, t.IDNumber, t.password,t.phone, t.email \n" +
            "from teacher t,expertactivities e,`groups` g\n" +
            "where e.`teacherID`=t.`id` and e.activityID = #{activityID} and g.ID = e.groupID")
    List<Experts> getExpertsByActID(Integer activityID);
}
