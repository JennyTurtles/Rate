package org.sys.rate.controller.config;

import org.springframework.security.core.context.SecurityContextHolder;
import org.sys.rate.model.Account;
import org.sys.rate.model.Menu;
import org.sys.rate.service.admin.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/system/config")
public class SystemConfigController {
    @Autowired
    MenuService menuService;
    @GetMapping("/menu")
    public List<Menu> getMenusByAdminId(Integer id, String role) {
        List<Menu> res=menuService.getMenusByAdminId(id,role);

        return res;
    }
/*    @GetMapping("/menuex")
    public List<Menu> getMenusByExpertId() {
        List<Menu> res=menuService.getMenusByStuId();
//        System.out.println("menuex结果："+res);
        return res;
    }*/
}
