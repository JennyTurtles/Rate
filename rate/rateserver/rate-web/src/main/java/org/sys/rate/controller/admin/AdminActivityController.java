package org.sys.rate.controller.admin;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.sys.rate.mapper.AdminActivityMapper;
import org.sys.rate.mapper.AdminMapper;
import org.sys.rate.model.*;
import org.sys.rate.service.admin.AdminActivityService;
import org.sys.rate.service.admin.AdminService;
import org.sys.rate.service.admin.HrService;
import org.sys.rate.service.admin.RoleService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/adminactivity/basic")
public class AdminActivityController {
    @Autowired
    HrService hrService;
    @Autowired
    AdminService adminService;
    @Autowired
    RoleService roleService;
    @Resource
    AdminMapper adminMapper;
    @Resource
    AdminActivityMapper adminActivityMapper;
    @Resource
    AdminActivityService adminActivityService;

    @PostMapping("/changePermissionList")
    public RespBean changePermissionList(@RequestBody List<AdminActivity> aaList) {
        return adminActivityService.changePermissionList(aaList);
    }
}
