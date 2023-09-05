package org.sys.rate.mapper;

import org.sys.rate.model.Role;

import java.util.List;

public interface RoleMapper {
    List<Role> selectEspecialRoleID();
    int deleteByPrimaryKey(Integer id);

    int insert(Role record);

    int insertSelective(Role record);

    Role selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Role record);

    int updateByPrimaryKey(Role record);

    List<Role> getAllRoles();
    List<String> selectNameByRoleID(List<String> ids);
}
