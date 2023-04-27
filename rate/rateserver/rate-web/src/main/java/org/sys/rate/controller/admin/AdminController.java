package org.sys.rate.controller.admin;

import org.sys.rate.model.Admin;
import org.sys.rate.model.RespBean;
import org.sys.rate.model.RespPageBean;
import org.sys.rate.model.Role;
import org.sys.rate.service.admin.HrService;
import org.sys.rate.service.admin.AdminService;
import org.sys.rate.service.admin.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @作者 江南一点雨
 * @公众号 江南一点雨
 * @微信号 a_java_boy
 * @GitHub https://github.com/lenve
 * @博客 http://wangsong.blog.csdn.net
 * @网站 http://www.javaboy.org
 * @时间 2019-10-24 8:09
 */
@RestController("ratesystemAdmin")
@RequestMapping("/system/admin")
public class AdminController {
    @Autowired
    HrService hrService;
    @Autowired
    AdminService adminService;
    @Autowired
    RoleService roleService;
    @GetMapping("/")
    public RespPageBean getAllAds(@RequestParam String keywords, @RequestParam Integer ID,@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer size) {
        return adminService.getAllHrs(keywords,ID,page, size);
    }

    @GetMapping("/advanced")
    public RespPageBean getAllAdsByAdvancedMethod(@RequestParam String keywords,@RequestParam String keywords_name,@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer size) {
        return adminService.getAllAdsByAdvancedMethod(keywords,keywords_name,page, size);
    }

    @PostMapping("/update")
    public RespBean updateInstitution(@RequestBody Admin admin) {
        if (adminService.updateAdmin(admin) == 1) {
            return RespBean.ok("更新成功!");
        }
        return RespBean.error("更新失败!");
    }
    @PutMapping("/")
    public RespBean updateHr(@RequestBody Admin admin) {
        if (adminService.updateAdmin(admin) == 1) {
            return RespBean.ok("更新成功!");
        }
        return RespBean.error("更新失败!");
    }
    @GetMapping("/roles")
    public List<Role> getAllRoles() {
        return roleService.getAllRoles();
    }

    @PutMapping("/role")
    public RespBean updateHrRole(Integer hrid, Integer[] rids) {
        if (hrService.updateHrRole(hrid, rids)) {
            return RespBean.ok("更新成功!");
        }
        return RespBean.error("更新失败!");
    }

    @PostMapping("/delete")
    public RespBean deleteAdminById(@RequestBody Admin id) {
        if (adminService.deleteAdmin(id) == 1) {
            return RespBean.ok("删除成功!");
        }
        return RespBean.error("删除失败!");
    }

    @PutMapping("/insert")
    public RespBean addAdmin(@RequestBody Admin admin) {
        if (adminService.addNew(admin) == 1) {
            return RespBean.ok("添加成功!");
        }
        return RespBean.error("添加失败!");
    }
}
