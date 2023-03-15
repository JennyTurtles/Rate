package org.sys.rate.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.sys.rate.mapper.ActivitiesMapper;
import org.sys.rate.model.Activities;
import org.sys.rate.model.Log;
import org.sys.rate.model.RespBean;
import org.sys.rate.model.RespPageBean;
import org.sys.rate.service.admin.ActivitiesService;
import org.sys.rate.service.admin.LogService;
import org.sys.rate.service.admin.ScoreItemService;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


@RestController
@RequestMapping("/activities/basic")
public class ActivitiesBasicController {
    @Autowired
    ActivitiesService activitiesService;

    @Autowired
    ScoreItemService scoreItemService;

    @Autowired
    LogService logService;

    @Resource
    ActivitiesMapper activitiesMapper;

    @GetMapping("/")
    public RespPageBean getActivitiesByPage(@RequestParam(defaultValue = "1") Integer page,
                                            @RequestParam(defaultValue = "10") Integer size,
                                            @RequestParam(defaultValue = "1") Integer institutionID,
                                            Activities employee) {
        return activitiesService.getActivitiesPage(page, size, employee, institutionID);
    }

    @GetMapping("/sub")
    public RespBean getSubActivities(Integer activityID) {
        List<Activities> res = activitiesMapper.getSubActivities(activityID);
        return RespBean.ok("success",res);
    }

    @PostMapping("/insert")
    public RespBean addActivities(@RequestBody Activities activities) throws ParseException {
        int result=activitiesService.addActivities(activities);
        if (result == 1) {
            Log log=new Log();
            SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = sdf.parse(sdf.format(System.currentTimeMillis()));
            Timestamp nousedate = new Timestamp(date.getTime());
            log.setLog(nousedate,activities.getinstitutionID(),"活动","添加成功");
            logService.addLogs(log);
            return RespBean.ok("添加成功!");
        }
        else if(result == 2)
        {Log log=new Log();
            SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = sdf.parse(sdf.format(System.currentTimeMillis()));
            Timestamp nousedate = new Timestamp(date.getTime());
            log.setLog(nousedate,activities.getinstitutionID(),"活动","活动到达上限无法添加");
            logService.addLogs(log);
            return RespBean.error("活动到达上限无法添加!");}
        Log log=new Log();
        SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = sdf.parse(sdf.format(System.currentTimeMillis()));
        Timestamp nousedate = new Timestamp(date.getTime());
        log.setLog(nousedate,activities.getinstitutionID(),"活动","添加失败");
        logService.addLogs(log);
        return RespBean.error("添加失败!");
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
}

