package org.sys.rate.mapper;

import org.apache.ibatis.annotations.Select;
import org.sys.rate.model.Menu;

import java.util.List;

public interface MenuMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Menu record);

    int insertSelective(Menu record);

    Menu selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Menu record);

    int updateByPrimaryKey(Menu record);

    List<Menu> getMenusById(Integer hrid);

    List<Menu> getMenusByStuId(Integer role);
    List<Menu> getMenusByTeaId(Integer hrid,Integer role);
    List<Menu> getMenusWithParticipants(Integer role);

    List<Menu> getAllMenusWithRole();

    List<Menu> getAllMenus();

    List<Integer> getMidsByRid(Integer rid);

    @Select("SELECT COUNT(*) from participants WHERE studentID = #{stuId}")
    int isParticipants(Integer stuId);

    @Select("SELECT COUNT(*) from expertactivities WHERE teacherID = #{teaId}")
    int isExpert(Integer teaId);
}
