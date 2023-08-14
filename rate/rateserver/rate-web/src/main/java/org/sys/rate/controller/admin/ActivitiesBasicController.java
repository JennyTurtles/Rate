package org.sys.rate.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.sys.rate.mapper.ActivitiesMapper;
import org.sys.rate.mapper.GroupsMapper;
import org.sys.rate.model.*;
import org.sys.rate.service.admin.ActivitiesService;
import org.sys.rate.service.admin.LogService;
import org.sys.rate.service.admin.ParticipatesService;
import org.sys.rate.service.admin.ScoreItemService;

import javax.annotation.Resource;
import javax.validation.GroupSequence;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@RestController
@RequestMapping("/activities/basic")
public class ActivitiesBasicController {
    @Autowired
    ActivitiesService activitiesService;

    @Autowired
    ParticipatesService participatesService;
    @Autowired
    ScoreItemService scoreItemService;

    @Autowired
    LogService logService;

    @Resource
    ActivitiesMapper activitiesMapper;

    @Autowired
    GroupsMapper groupsMapper;

    @GetMapping("/")
    public RespPageBean getActivitiesByPage(@RequestParam(defaultValue = "1") Integer page,
                                            @RequestParam(defaultValue = "10") Integer size,
                                            @RequestParam(defaultValue = "1") Integer institutionID,
                                            @RequestParam Integer adminID,
                                            Activities employee) {
        return activitiesService.getActivitiesPage(page, size, employee, institutionID,adminID);
    }

    @GetMapping("/one")
    public RespBean getActivitiesByID(@RequestParam Integer activityID) {
        Activities res = activitiesMapper.getByID(activityID);
        return RespBean.ok("success",res);
    }

    @GetMapping("/sub")
    public RespBean getSubActivities(Integer activityID) {
        List<Activities> res = activitiesMapper.getSubActivities(activityID);
//        res.add(activitiesMapper.getByID(activityID));
        return RespBean.ok("success",res);
    }

    @GetMapping("/subWithComment")
    public RespBean getSubActivitiesWithComment(Integer activityID) {
        List<Activities> res = activitiesMapper.getSubActivitiesWithComment(activityID);
//        Activities self = activitiesMapper.getByID(activityID);
//        if (self.getHaveComment() == 1)
//            res.add(self);
        return RespBean.ok("success",res);
    }

    @PostMapping("/changeRequireGroup")
    public RespBean changeRequireGroup(@RequestParam Integer activityID, @RequestParam Integer requireGroup) {
        int res = activitiesMapper.changeRequireGroup(activityID, requireGroup);
        if (res == 1) {
            return RespBean.ok("success");
        }
        return RespBean.error("error");
    }


    @PostMapping("/insert")
    public RespBean addActivities(@RequestBody Activities activities) throws ParseException {
         return activitiesService.addActivities(activities);
    }

    @PostMapping("/predelete")
    public RespBean predeleteActivities(@RequestBody Activities activities) throws ParseException {
        if (activitiesService.predeleteActivities(activities) == 1) {
            Log log=new Log();
            SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = sdf.parse(sdf.format(System.currentTimeMillis()));
            Timestamp nousedate = new Timestamp(date.getTime());
            log.setLog(nousedate,activities.getinstitutionID(),"活动","删除成功");
            logService.addLogs(log);
            return RespBean.ok("删除成功!");
        }
        Log log=new Log();
        SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = sdf.parse(sdf.format(System.currentTimeMillis()));
        Timestamp nousedate = new Timestamp(date.getTime());
        log.setLog(nousedate,activities.getinstitutionID(),"活动","删除失败");
        logService.addLogs(log);
        return RespBean.error("删除失败!");
    }

    @PostMapping("/delete")
    public RespBean deleteActivities(@RequestBody Activities activities) throws ParseException {
        if (activitiesService.deleteActivities(activities) == 1) {
            Log log=new Log();
            SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = sdf.parse(sdf.format(System.currentTimeMillis()));
            Timestamp nousedate = new Timestamp(date.getTime());
            log.setLog(nousedate,activities.getinstitutionID(),"活动","删除成功");
            logService.addLogs(log);
            return RespBean.ok("删除成功!");
        }
        Log log=new Log();
        SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = sdf.parse(sdf.format(System.currentTimeMillis()));
        Timestamp nousedate = new Timestamp(date.getTime());
        log.setLog(nousedate,activities.getinstitutionID(),"活动","删除失败");
        logService.addLogs(log);
        return RespBean.error("删除失败!");
    }

    @PostMapping("/end")
    public RespBean endActivities(@RequestBody Activities activities) throws ParseException {
        if (activitiesService.endActivities(activities) == 1) {
            Log log=new Log();
            SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = sdf.parse(sdf.format(System.currentTimeMillis()));
            Timestamp nousedate = new Timestamp(date.getTime());
            log.setLog(nousedate,activities.getinstitutionID(),"活动","结束成功");
            logService.addLogs(log);
            return RespBean.ok("结束成功!");
        }
        Log log=new Log();
        SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = sdf.parse(sdf.format(System.currentTimeMillis()));
        Timestamp nousedate = new Timestamp(date.getTime());
        log.setLog(nousedate,activities.getinstitutionID(),"活动","结束失败");
        logService.addLogs(log);
        return RespBean.error("结束失败!");
    }


    @PostMapping("/update")
    public RespBean updateActivities(@RequestBody Activities activities) throws ParseException {
        if (activitiesService.updateActivities(activities) == 1) {
            Log log=new Log();
            SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = sdf.parse(sdf.format(System.currentTimeMillis()));
            Timestamp nousedate = new Timestamp(date.getTime());
            log.setLog(nousedate,activities.getinstitutionID(),"活动","更新成功");
            logService.addLogs(log);
            return RespBean.ok("更新成功!");
        }
        Log log=new Log();
        SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = sdf.parse(sdf.format(System.currentTimeMillis()));
        Timestamp nousedate = new Timestamp(date.getTime());
        log.setLog(nousedate,activities.getinstitutionID(),"活动","更新失败");
        logService.addLogs(log);
        return RespBean.error("更新失败!");
    }

    @GetMapping("/search")
    public RespPageBean searchActivities(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer size, @RequestParam String company, @RequestParam Integer institutionID) {
        return activitiesService.searchActivities(page, size, company, institutionID);
    }
    @GetMapping("/score")
    public RespPageBean getActivityScore(@RequestParam Integer activityID){
        return activitiesService.getActivityScore(activityID);
    }
    @GetMapping("/get_activity_info")
    public List<Activities> getAllActivity_info() {
        return activitiesService.getAllActivity_info();
    }

    @GetMapping("/check")
    public Boolean noRelative(@RequestParam Integer id) {
        return activitiesService.noRelative(id) && scoreItemService.noRelative(id);
    }
    @GetMapping("/getActivityOfStudent")
    public List<Activities> getActivityOfStudent(@RequestParam(defaultValue = "1") Integer page,
                                        @RequestParam(defaultValue = "10") Integer size,@RequestParam Integer studentID) {

        List<Participates> pars =  participatesService.getParticipantIDByStudentID(studentID);
        List<Activities> activities = new ArrayList<>();
        for (int i = 0;i < pars.size();i ++){
            activities.add(activitiesService.getActivity(pars.get(i).getActivityID()).get(0));
        }
        return activities;
    }

    @PostMapping("/fixCount")
    public RespBean fixCount(@RequestParam Integer parID,@RequestParam Integer subID){
        Integer res1 = activitiesMapper.fixExpertCount(parID,subID);
        Integer res2 = activitiesMapper.fixParCount(parID,subID);
        if (res1 > 0 && res2 > 0){
            return RespBean.ok("成功!");
        }else
            return RespBean.error("失败!");
    }

    @GetMapping("/checkHaveSub")
    public RespBean checkHaveSub(@RequestParam Integer activityID) {
        if (activitiesMapper.checkHaveSub(activityID) != null) {
            return RespBean.ok("有子活动", true);
        } else
            return RespBean.ok("无子活动",false);
    }

    // 返回所有活动和子活动的的信息
    @GetMapping("/getALl")
    public RespBean getALl() {
        List<Activities> activities = activitiesMapper.getAll();
        return RespBean.ok("成功", activities);
    }

    @PostMapping("/clone")
    public RespBean cloneActivity(@RequestBody Activities activity) {
        activitiesService.cloneActivity(activity);
        return RespBean.ok("克隆成功");
    }

    @GetMapping("/checkHaveGroup")
    public RespBean checkHaveGroup(@RequestParam Integer activityID) {
        if (activitiesMapper.checkHaveGroup(activityID) != null) {
            return RespBean.ok("已分组", true);
        } else
            return RespBean.ok("未分组",false);
    }

    @GetMapping("/getAllGroup")
    public RespBean getAllGroup(@RequestParam Integer activityID){
        List<Groups> res = groupsMapper.getGroupByActID(activityID);
        return RespBean.ok("success",res);
    }

    @GetMapping("/searchByName")
    public RespBean searchByName(@RequestParam String name) {
        List<Activities> activities = activitiesMapper.searchByName(name);
        return RespBean.ok("success", activities);
    }

    // 获取该教师参与的含有成绩评定表的活动
    @GetMapping("/getWithGradeForm")
    public RespBean getWithGradeForm(@RequestParam Integer teacherID) {
        List<Activities> activities = activitiesMapper.getWithGradeForm(teacherID);
        return RespBean.ok("success", activities);
    }
}

