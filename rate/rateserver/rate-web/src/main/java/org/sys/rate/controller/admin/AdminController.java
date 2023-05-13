package org.sys.rate.controller.admin;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.sys.rate.mapper.ActivityGrantMapper;
import org.sys.rate.mapper.AdminMapper;
import org.sys.rate.model.*;
import org.sys.rate.service.admin.AdminService;
import org.sys.rate.service.admin.HrService;
import org.sys.rate.service.admin.RoleService;
import org.sys.rate.service.mail.MailService;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import java.util.ArrayList;
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
    @Resource
    AdminMapper adminMapper;
    @Resource
    ActivityGrantMapper activityGrantMapper;
    @Resource
    MailService mailService;

    @GetMapping("/")
    public RespPageBean getAllAds(@RequestParam String keywords, @RequestParam Integer ID, @RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer size) {
        return adminService.getAllHrs(keywords, ID, page, size);
    }

    @GetMapping("/advanced")
    public RespPageBean getAllAdsByAdvancedMethod(@RequestParam String keywords, @RequestParam String keywords_name, @RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer size) {
        return adminService.getAllAdsByAdvancedMethod(keywords, keywords_name, page, size);
    }

    @PostMapping("/updateMail")
    public RespBean updateMail(@RequestBody Mail mail) throws MessagingException {
        if (mailService.updateMail(mail)) {
//            if(mailService.checkMail(mail)) {
            if(true) {
                mailService.setMail();
                return RespBean.ok("更新成功!");
            }else{
                return RespBean.error("邮箱设置错误!请检查各项邮箱配置。");
            }
        }
        return RespBean.error("更新失败!");
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
        return adminService.addNew(admin);
    }

    @GetMapping("/selectAdminOfCurrentInstitution")
    public Msg selectAdminOfCurrentInstitution(@RequestParam("dialogAddTeaPermissionPage") Integer dialogAddTeaPermissionPage,
                                               @RequestParam("dialogAddTeaPermissionSize") Integer dialogAddTeaPermissionSize,
                                               @RequestParam("activityID") Integer activityID, @RequestParam("institutionID") Integer institutionID) {
        //因为要考虑分页情况，但是两者又不太能联合查询，前端也要添加属性判断状态，所以先用Msg吧
        List<Admin> adm = new ArrayList<>();
        List<Admin> allAdmList = new ArrayList<>();
        Object[] res = null;
        List<ActivityGrant> adminActivities = new ArrayList<>();
        try {
            //拿到关于这个活动已经存在的管理员名单
            adminActivities = activityGrantMapper.selectAllOfCurrentActivity(activityID, institutionID);
            allAdmList = adminMapper.selectCurrentInstitutionAdmins(institutionID);//先拿到所有数据，因为前端是selection和分页结合,但可以做些优化，这样写不好
            //拿到关于这个单位下的所有管理员名单，并做分页处理
            Page page = PageHelper.startPage(dialogAddTeaPermissionPage, dialogAddTeaPermissionSize);
            adm = adminMapper.selectCurrentInstitutionAdmins(institutionID);
            PageInfo info = new PageInfo<>(page.getResult());
            res = new Object[]{adm, info.getTotal()};
        } catch (Exception e) {
            return Msg.fail();
        }
        return Msg.success().add("adm", res).add("aa", adminActivities).add("allAdmList", allAdmList);//返回形式不太好

    }
}
