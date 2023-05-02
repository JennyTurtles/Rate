package org.sys.rate.service.admin;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.sys.rate.mapper.ActivitiesMapper;
import org.sys.rate.mapper.AdminActivityMapper;
import org.sys.rate.mapper.AdminMenuMapper;
import org.sys.rate.model.AdminActivity;
import org.sys.rate.model.AdminMenu;
import org.sys.rate.model.RespBean;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


@Service
public class AdminMenuService {
    @Autowired
    ActivitiesMapper activitiesMapper;
    @Autowired
    RabbitTemplate rabbitTemplate;
    @Autowired
    MailSendLogService mailSendLogService;
    @Resource
    AdminMenuMapper adminMenuMapper;

    public RespBean getMenusOfAdminPermission(Integer adminID){
        List<AdminMenu> exsistList = new ArrayList<>();
        try{
            //已经存在该活动权限的管理员名单
            exsistList = adminMenuMapper.getMenusOfAdminPermission(adminID);
        }catch (Exception e){
            return RespBean.error("",null);
        }
        return RespBean.ok("exsistList",exsistList);
    }
    public RespBean changePermissionList(List<AdminMenu> amList){
        try{
            //该管理员已经有的菜单权限名单
            List<AdminMenu> exsistList = adminMenuMapper.getMenusOfAdminPermission(amList.get(0).getAdminID());
            //多的删除 少的添加
            List<Integer> amListCopy = new ArrayList<>();
            List<Integer> amListCopyC = new ArrayList<>();//因为后面移除操作会改变原数组，所以拷贝一个
            for(int i = 0;i < amList.size();i ++){
                if(amList.get(i).getMenuID() != -1){//如果传来的4个菜单都不给权限，则amList只有一个长度，menuid为-1,用来做表示标识
                    amListCopy.add(amList.get(i).getMenuID());
                    amListCopyC.add(amList.get(i).getMenuID());
                }
            }
            List<Integer> exsistlistCopy = new ArrayList<>();
            for(int i = 0;i < exsistList.size();i ++){
                exsistlistCopy.add(exsistList.get(i).getMenuID());
            }
            amListCopy.removeAll(exsistlistCopy);//前端传来的减去已经存在的，得到需要添加的
            if(amListCopy.size() > 0){//有需要添加的
                adminMenuMapper.insertRecordListOfAddMenuPermission(amListCopy,amList.get(0).getAdminID());
            }
            exsistlistCopy.removeAll(amListCopyC);
            if(exsistlistCopy.size() > 0){//有需要删除的
                adminMenuMapper.deletePermissions(exsistlistCopy,amList.get(0).getAdminID());
            }
        }catch (Exception e){
            return RespBean.error("修改失败",null);
        }
        return RespBean.ok("修改成功",null);
    }

}
