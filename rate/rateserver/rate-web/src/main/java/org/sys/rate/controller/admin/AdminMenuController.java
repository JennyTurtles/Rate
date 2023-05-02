package org.sys.rate.controller.admin;

import org.springframework.web.bind.annotation.*;
import org.sys.rate.mapper.AdminMenuMapper;
import org.sys.rate.model.AdminActivity;
import org.sys.rate.model.AdminMenu;
import org.sys.rate.model.RespBean;
import org.sys.rate.service.admin.AdminMenuService;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/adminmenu/basic")
public class AdminMenuController {
    @Resource
    AdminMenuMapper adminMenuMapper;
    @Resource
    AdminMenuService adminMenuService;

    @GetMapping("/getMenusOfAdminPermission")
    public RespBean getMenusOfAdminPermission(Integer adminID){
        return adminMenuService.getMenusOfAdminPermission(adminID);
    }
    @PostMapping("/changePermissionList")
    public RespBean changePermissionList(@RequestBody List<AdminMenu> amList) {
        return adminMenuService.changePermissionList(amList);
    }

}
