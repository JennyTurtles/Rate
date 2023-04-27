package org.sys.rate.mapper;

import org.sys.rate.model.Admin;
import org.sys.rate.model.AdminMenu;

import java.util.List;

public interface AdminMenuMapper {
    int insertRecordsOfAddAdmin(List<AdminMenu> list);
    List<AdminMenu> selectHaveAdminMenuRecord(Integer adminID,List<Integer> menuID);

}
