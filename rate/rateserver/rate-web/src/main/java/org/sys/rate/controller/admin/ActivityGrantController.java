package org.sys.rate.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.sys.rate.mapper.ActivityGrantMapper;
import org.sys.rate.mapper.AdminMapper;
import org.sys.rate.model.*;
import org.sys.rate.service.admin.ActivityGrantService;
import org.sys.rate.service.admin.AdminService;
import org.sys.rate.service.admin.RoleService;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/activitygrant/basic")
public class ActivityGrantController {
    @Autowired
    HrService hrService;
    @Autowired
    AdminService adminService;
    @Autowired
    RoleService roleService;
    @Resource
    AdminMapper adminMapper;
    @Resource
    ActivityGrantMapper activityGrantMapper;
    @Resource
    ActivityGrantService activityGrantService;

    @PostMapping("/changePermissionList")
    public RespBean changePermissionList(@RequestBody List<ActivityGrant> aaList) {
        return activityGrantService.changePermissionList(aaList);
    }
}
