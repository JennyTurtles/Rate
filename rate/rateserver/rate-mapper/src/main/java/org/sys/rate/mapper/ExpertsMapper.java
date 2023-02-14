package org.sys.rate.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
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

    Integer getID(@Param("idNumber") String idNumber);
}
