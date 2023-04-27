package org.sys.rate.controller.config;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.sys.rate.mapper.AdminMenuMapper;
import org.sys.rate.mapper.MenuMapper;
import org.sys.rate.model.*;
import org.sys.rate.service.LoginService;
import org.sys.rate.service.admin.MenuService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
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
    AdminMenuMapper adminMenuMapper;
    @Resource
    MenuMapper menuMapper;

    @GetMapping("/menu")
    public Msg getMenusByAdminId(Integer id, String role) {
        //获得具有特殊权限的菜单和id列表
        List<Menu> menusOfAdmin = menuMapper.selectEspecialMenuID();
        List<Integer> menusOfAdminID = new ArrayList<>();
        for(int j = 0;j < menusOfAdmin.size();j ++){
            menusOfAdminID.add(menusOfAdmin.get(j).getId());
        }
        List<Menu> res = menuService.getMenusByAdminId(id,role);
        //如果是管理员，则需要对菜单进行具体判断，看是否有权限
        if(role.equals("1")){
            //判断当前管理员有没有本科生、研究生、教师管理这三个菜单的权限
            List<Integer> permissionListMenu = adminMenuMapper.judgeAdminPermission(id,menusOfAdminID);
            menusOfAdminID.removeAll(permissionListMenu);//数组相减，得到不应该有的菜单id
            for(int j = 0;j < menusOfAdminID.size();j ++){
                for(int i = 0; i < res.size() ; i ++){
                    if(res.get(i).getId() == menusOfAdminID.get(j)){//不应该有的菜单，在res中存在了
                        res.remove(i);
                        break;
                    }
                }
            }
        }

        //调成一样的形式是因为前端需要进行判断，但同时又不想失去菜单之间父子关系的结构，更不想在前端进行三重循环判断
        List<String> res_same = new ArrayList<>();
        Menu item,item1,item2;
        for(int i = 0; i < res.size() ; i ++){
            item = res.get(i);
            if(item.getChildren() != null){
                for(int j = 0; j < item.getChildren().size() ; j ++){
                    item1 = item.getChildren().get(j);
                    if(item1.getChildren() == null){
                        res_same.add(item1.getPath());
                    }else{
                        for(int k = 0; k < item1.getChildren().size() ; k ++){
                            item2 = item1.getChildren().get(k);
                            if(item2.getChildren() == null){
                                res_same.add(item2.getPath());
                            }
                        }
                    }
                }
            }
        }
        res_same.add("/home");
        return Msg.success().add("res",res).add("res_same",res_same);
    }

    @GetMapping("/logout")
    public void logout() {
    }
    @GetMapping("/getEspecialMenusOfAdmin")
    public RespBean getEspecialMenusOfAdmin(){
        return menuService.getEspecialMenusOfAdmin();
    }

    // 接受前端POST的密码，修改密码
    @PostMapping("/updatePassword")
    public RespBean updatePassword(@RequestParam Integer ID,@RequestParam String password) {
        return RespBean.ok("修改成功",loginService.updatePassword(ID,password));
    }
}
