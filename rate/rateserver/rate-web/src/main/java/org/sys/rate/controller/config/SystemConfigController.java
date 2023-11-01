package org.sys.rate.controller.config;

import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.HandlerMethod;
import org.sys.rate.exception.Constants;
import org.sys.rate.exception.ServiceException;
import org.sys.rate.mapper.RoleMapper;
import org.sys.rate.model.*;
import org.sys.rate.service.LoginService;
import org.sys.rate.service.admin.MenuService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/system/config")
public class SystemConfigController {
    @Autowired
    MenuService menuService;
    @Resource
    LoginService loginService;
    @Resource
    RoleMapper roleMapper;

    @GetMapping("/menu")
    public Msg getMenusByAdminId(Integer id, String role) {
        //获得具有特殊权限的菜单和id列表
        List<Menu> res = menuService.getMenusByAdminId(id, role);
        //如果是管理员，则需要对菜单进行具体判断，看是否有权限

        //调成一样的形式是因为前端需要进行判断，但同时又不想失去菜单之间父子关系的结构，更不想在前端进行三重循环判断
        List<String> res_same = new ArrayList<>();
        Menu item, item1, item2;
        for (int i = 0; i < res.size(); i++) {
            item = res.get(i);
            if (item.getChildren() != null) {
                for (int j = 0; j < item.getChildren().size(); j++) {
                    item1 = item.getChildren().get(j);
                    if (item1.getChildren() == null) {
                        res_same.add(item1.getPath());
                    } else {
                        for (int k = 0; k < item1.getChildren().size(); k++) {
                            item2 = item1.getChildren().get(k);
                            if (item2.getChildren() == null) {
                                res_same.add(item2.getPath());
                            }
                        }
                    }
                }
            }
        }
        res_same.add("/home");
        return Msg.success().add("res", res).add("res_same", res_same);
    }

    @GetMapping("/logout")
    public void logout() {
    }

    @GetMapping("/getEspecialMenusOfAdmin")
    public RespBean getEspecialMenusOfAdmin() {
        List<Role> roles;
        try {
            roles = roleMapper.selectEspecialRoleID();
        } catch (Exception e) {
            return RespBean.error("error", null);
        }
        return RespBean.ok("ok", roles);
    }

    // 接受前端POST的密码，修改密码
    @PostMapping("/updatePassword")
    public RespBean updatePassword(HttpServletRequest request, @RequestParam Integer ID, @RequestParam String password) {
        String token = request.getHeader("token");
        if (StrUtil.isBlank((token))) {
            //没有token，重新获取
            throw new ServiceException(Constants.CODE_401, "无token，请重新登录");
        }
        //获取token中的userid
        String userRole;
        try {
            userRole = JWT.decode(token).getClaims().get("role").asString();
        } catch (JWTDecodeException j) {
            throw new ServiceException(Constants.CODE_401, "token验证失败，请重新登录");
        }
        return RespBean.ok("修改成功", loginService.updatePassword(userRole, ID, password));
    }
}
