package org.sys.rate.service.admin;

import org.apache.poi.util.StringUtil;
import org.sys.rate.mapper.*;
import org.sys.rate.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;


@Service
@CacheConfig(cacheNames = "menus_cache")
public class MenuService {
    @Autowired(required = false)
    MenuMapper menuMapper;
    @Autowired(required = false)
    MenuRoleMapper menuRoleMapper;
    @Autowired
    ExpertsMapper expertsMapper;
    @Autowired
    ParticipatesMapper participatesMapper;
    @Resource
    SecretaryMapper secretaryMapper;
    @Autowired
    GraduateStudentMapper graduateStudentMapper;
    @Autowired
    UnderGraduateMapper underGraduateMapper;


    //获取子菜单
    public List<Menu> getChild(Integer id,List<Menu> rootMenu,Menu newMenu){
        // 子菜单
        List<Menu> childList = new ArrayList<Menu>();
        for (Menu menu : rootMenu){
            //遍历所有的一级菜单和全部菜单，全部菜单id和一级菜单比较
            if (menu.getParentId() != 1){
                if (menu.getParentId()==id){
                    childList.add(menu);
                }
            }
        }
        return childList;
    }
    //获取每个角色的菜单
    public List<Menu> getAllRoleMenus(Integer id, String role){
        List<Menu> res = new ArrayList<>();
//        if (role.equals(("4")))
//            System.out.println("4");
        if(role.equals("8") || role.equals("9") || role.equals(("3")) || role.equals(("4")) ){//8本科生老师 9研究生老师 3是专家 4是秘书
            res=menuMapper.getMenusByTeaId(id,Integer.parseInt(role));
        }
        else if(role.equals("10") || role.equals("11")){//10本科生 11研究生
            res=menuMapper.getMenusByStuId(Integer.parseInt(role));
        }
        else if(role.equals("7")) //7选手
        {
            res = menuMapper.getMenusWithParticipants(Integer.parseInt(role));
        }
        else {
            res = menuMapper.getMenusById(Integer.parseInt(role));
            return res;
        }
        List<Menu> newMenu=new ArrayList<>();
        for (int i = 0 ;i<res.size();i++){

            if(res.get(i).getParentId() == 1){
                newMenu.add(res.get(i));
            }
        }
        //设置子菜单
        for(Menu menu:newMenu) {
            if(menu.getChildren() != null){
                //有三级
                List<Menu> subMenu = getChild(menu.getId(),res,menu);
                if(subMenu.size() != 0){
                    menu.setChildren(subMenu);
                }
            }
        }
        return newMenu;
    }

    public List<Menu> getMenusByAdminId(Integer id, String role) {
        if (participatesMapper.isParticipants(id) != 0){//选手
            if(!role.contains("7")){
                role = role + ";7";
            }
        }
        if (underGraduateMapper.isUndergraduate(id) != 0){//本科生
            if(!role.contains("10")){
                role = role + ";10";
            }
        }
        if (graduateStudentMapper.isGraduateStudent(id) != 0){//研究生
            if(!role.contains("11")){
                role = role + ";11";
            }
        }
        if (expertsMapper.isExpert(id) != 0){
            if(!role.contains("3")){
                role = role + ";3";
            }
        }
        if (secretaryMapper.isSecretary(id) != 0){
            // 是秘书
            if(!role.contains("4")){
                role = role + ";4";
            }
        }
        String[] roles = role.split(";");
        List<Menu> res=new ArrayList<>();
        for (int i = 0;i < roles.length;i++){
            //获取每个角色的菜单
            if(!roles[i].equals("")){
                List<Menu> list = getAllRoleMenus(id,roles[i]);
                for(Menu li:list){
                    //最终菜单
                    res.add(li);
                }
            }
        }
        return res;
    }


    public List<Menu> getMenusByExpertId() {
        return menuMapper.getMenusById(((Student) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getID());
    }
//    public List<Menu> getMenusByStuId() {
//        Integer id=((Student) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
//        System.out.println("id为："+id);
//        return menuMapper.getMenusByStuId(id);
//    }

    @Cacheable
    public List<Menu> getAllMenusWithRole() {
        return menuMapper.getAllMenusWithRole();
    }

    public List<Menu> getAllMenus() {
        return menuMapper.getAllMenus();
    }

    public List<Integer> getMidsByRid(Integer rid) {
        return menuMapper.getMidsByRid(rid);
    }

    @Transactional
    public boolean updateMenuRole(Integer rid, Integer[] mids) {
        menuRoleMapper.deleteByRid(rid);
        if (mids == null || mids.length == 0) {
            return true;
        }
        Integer result = menuRoleMapper.insertRecord(rid, mids);
        return result==mids.length;
    }
    public RespBean getEspecialMenusOfAdmin(){
        List<Menu> menusOfAdmin = new ArrayList<>();
        try{
            menusOfAdmin = menuMapper.selectEspecialMenuID();
        }catch (Exception e){
            return RespBean.error("error",null);
        }
        return RespBean.ok("ok",menusOfAdmin);
    }
}
