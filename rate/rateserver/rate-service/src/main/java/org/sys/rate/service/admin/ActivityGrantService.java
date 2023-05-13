package org.sys.rate.service.admin;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.sys.rate.mapper.ActivitiesMapper;
import org.sys.rate.mapper.ActivityGrantMapper;
import org.sys.rate.model.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


@Service
public class ActivityGrantService {
    @Autowired
    ActivitiesMapper activitiesMapper;
    @Autowired
    RabbitTemplate rabbitTemplate;
    @Autowired
    MailSendLogService mailSendLogService;
    @Resource
    ActivityGrantMapper activityGrantMapper;

    public RespBean changePermissionList(List<ActivityGrant> aaList){
        try{
            //已经存在该活动权限的管理员名单
            List<ActivityGrant> exsistlist = activityGrantMapper.selectAllOfCurrentActivity(aaList.get(0).getActivityID(),aaList.get(0).getInstitutionID());
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
                activityGrantMapper.insertRecordListOfAddActivity(aaListCopy,aaList.get(0).getActivityID(),aaList.get(0).getInstitutionID());
            }
            exsistlistCopy.removeAll(aaListCopyC);
            if(exsistlistCopy.size() > 0){//有需要删除的
                activityGrantMapper.deletePermissions(exsistlistCopy,aaList.get(0).getActivityID(),aaList.get(0).getInstitutionID());
            }
        }catch (Exception e){
            return RespBean.error("修改失败",null);
        }
        return RespBean.ok("修改成功",null);
    }

}
