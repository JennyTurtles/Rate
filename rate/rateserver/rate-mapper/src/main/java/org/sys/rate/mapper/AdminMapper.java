package org.sys.rate.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.sys.rate.model.Admin;
import org.sys.rate.model.Role;
import org.sys.rate.model.Teacher;

import java.util.List;

public interface AdminMapper {

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

    List<Admin> getAllHrs(@Param("hrid") Integer hrid, @Param("keywords") String keywords,@Param("page") Integer page, @Param("size") Integer size);

    List<Admin> getAllAdsByAdvancedMethod(@Param("keywords") String keywords,@Param("keywords_name") String keywords_name,@Param("page") Integer page, @Param("size") Integer size);

    Long getTotal(@Param("keywords") String key);

    Long getTotalByAdvancedMethod(@Param("keywords") String key,@Param("keywords_name") String key_name);

    int delete(Admin record);

    List<Admin> getAllHrsExceptCurrentHr(Integer id);

    Integer updatePasswd(@Param("adminid") Integer adminid, @Param("encodePass") String encodePass);

    Integer updateUserface(@Param("url") String url, @Param("id") Integer id);
}
