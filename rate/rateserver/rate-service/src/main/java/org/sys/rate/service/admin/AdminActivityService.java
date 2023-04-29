package org.sys.rate.service.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.sys.rate.mapper.ActivitiesMapper;
import org.sys.rate.mapper.AdminActivityMapper;
import org.sys.rate.model.*;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


@Service
public class AdminActivityService {
    @Autowired
    ActivitiesMapper activitiesMapper;
    @Autowired
    RabbitTemplate rabbitTemplate;
    @Autowired
    MailSendLogService mailSendLogService;
    @Resource
    AdminActivityMapper adminActivityMapper;

    public RespBean changePermissionList(List<AdminActivity> aaList){
        try{
            //已经存在该活动权限的管理员名单
            List<AdminActivity> exsistlist = adminActivityMapper.selectAllOfCurrentActivity(aaList.get(0).getActivityID(),aaList.get(0).getInstitutionID());
            //多的删除 少的添加
            List<Integer> aaListCopy = new ArrayList<>();
            List<Integer> aaListCopyC = new ArrayList<>();//因为后面移除操作会改变原数组，所以拷贝一个
            for(int i = 0;i < aaList.size();i ++){
                aaListCopy.add(aaList.get(i).getAdminID());
                aaListCopyC.add(aaList.get(i).getAdminID());
            }
            List<Integer> exsistlistCopy = new ArrayList<>();
            for(int i = 0;i < exsistlist.size();i ++){
                exsistlistCopy.add(exsistlist.get(i).getAdminID());
            }
            aaListCopy.removeAll(exsistlistCopy);
            if(aaListCopy.size() > 0){//有需要添加的
                adminActivityMapper.insertRecordListOfAddActivity(aaListCopy,aaList.get(0).getActivityID(),aaList.get(0).getInstitutionID());
            }
            exsistlistCopy.removeAll(aaListCopyC);
            if(exsistlistCopy.size() > 0){//有需要删除的
                adminActivityMapper.deletePermissions(exsistlistCopy,aaList.get(0).getActivityID(),aaList.get(0).getInstitutionID());
            }
        }catch (Exception e){
            return RespBean.error("修改失败",null);
        }
        return RespBean.ok("修改成功",null);
    }

}
