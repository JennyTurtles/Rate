package org.sys.rate.mapper;

import org.sys.rate.model.Admin;
import org.sys.rate.model.AdminMenu;

import java.util.List;

public interface AdminMenuMapper {
    int insertRecordListOfAddMenuPermission(List<Integer> menuID,Integer adminID);
    int insertRecordsOfAddAdmin(List<AdminMenu> list);
    List<AdminMenu> selectHaveAdminMenuRecord(Integer adminID,List<Integer> menuID);
    List<Integer> judgeAdminPermission(Integer adminID,List<Integer> menuIdList);
    List<AdminMenu> getMenusOfAdminPermission(Integer adminID);
    int deletePermissions(List<Integer> menuID,Integer adminID);
}
