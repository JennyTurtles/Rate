package org.sys.rate.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.sys.rate.model.Admin;
import org.sys.rate.model.Role;
import org.sys.rate.model.Teacher;

import java.util.HashMap;
import java.util.List;

public interface AdminMapper {

    //由于第一个人写的代码非常乱，admin里的id应该是ID，id是不对的，碰到错误可以参考是不是id的原因，id的bug没有改完
    @Select("select * from admin where ID = #{ID}")
    Admin getById(Integer ID);


    int deleteByPrimaryKey(Integer id);

    int insert(Admin record);

    int update(Admin record);

    int insertSelective(Admin record);

    Admin selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Admin record);

    int updateByPrimaryKey(Admin record);

    Admin loadUserByUsername(String username);

    List<Role> getAdminRolesById(Integer id);

    List<Admin> getAllHrs(@Param("keywords") Integer keywords,@Param("page") Integer page, @Param("size") Integer size);

    List<Admin> getAllAdsByAdvancedMethod(@Param("keywords") String keywords,@Param("keywords_name") String keywords_name,@Param("page") Integer page, @Param("size") Integer size);

    Long getTotal(@Param("keywords") Integer key);

    Long getTotalByAdvancedMethod(@Param("keywords") String key,@Param("keywords_name") String key_name);

    int delete(Admin record);

    List<Admin> getAllHrsExceptCurrentHr(Integer id);

    Integer updatePasswd(@Param("adminid") Integer adminid, @Param("encodePass") String encodePass);

    Integer updateUserface(@Param("url") String url, @Param("id") Integer id);
    List<Admin> selectCurrentInstitutionAdmins(Integer institutionID);


}
