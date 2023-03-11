package org.sys.rate.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.sys.rate.model.Log;
import org.sys.rate.model.RespBean;
import org.sys.rate.model.RespPageBean;
import org.sys.rate.model.Groups;
import org.sys.rate.service.admin.GroupsService;
import org.sys.rate.service.admin.LogService;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


@RestController
@RequestMapping("/groups/basic")
public class GroupsController {
    @Autowired
    GroupsService groupsService;
    @Autowired
    LogService logService;

    @GetMapping("/")
    public RespPageBean getGroupsByPage(@RequestParam Integer keywords, @RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer size, Groups employee) {
        return groupsService.getActivitiesPage(keywords, page, size, employee);
    }

    @GetMapping("/score")
    public RespPageBean getGroupScore(@RequestParam Integer keywords, @RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer size, Groups employee){
        return groupsService.getGroupScore(keywords,page,size,employee);
    }

    @PostMapping("/insert")
    public RespBean addParticipates(@RequestBody Groups employee,@RequestParam Integer institutionID) {
//        System.out.println("insert");
        employee.setName("输入分组名称");
        if (groupsService.addParticipates(employee) != -1) {
            RespBean res = new RespBean(200, "添加成功!", groupsService.getMaxId());
            return res;
        }
        return RespBean.error("添加失败!");
    }

    @PostMapping("/delete")
    public String deleteInstitution(@RequestBody Groups company,@RequestParam Integer institutionID) throws ParseException {
        if (groupsService.deleteActivities(company) == 1) {
            Log log=new Log();
            SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = sdf.parse(sdf.format(System.currentTimeMillis()));
            Timestamp nousedate = new Timestamp(date.getTime());
            log.setLog(nousedate,institutionID,"分组","删除成功");
            logService.addLogs(log);
            return "删除成功!";
        }
        Log log=new Log();
        SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = sdf.parse(sdf.format(System.currentTimeMillis()));
        Timestamp nousedate = new Timestamp(date.getTime());
        log.setLog(nousedate,institutionID,"分组","删除失败");
        logService.addLogs(log);
        return "删除失败!";
    }


/*    @PostMapping("/update")
    public RespBean updateActivities(@RequestBody List<Groups> scoreItem) {
        int activityID = scoreItem.get(0).getactivityID();
        int total = groupsService.getactivityCount(activityID);
        //System.out.println("total->" + total);
        if (scoreItem.size() == total) {
            if (groupsService.updateActivities(scoreItem) == 1) {
                return RespBean.ok("更新成功!");
            }
        } else if (scoreItem.size() > total && total != 0) {
            List<Groups> scoreItem_up = scoreItem.subList(0, total - 1);
            List<Groups> scoreItem_insert = scoreItem.subList(total, scoreItem.size());
            if (groupsService.updateActivities(scoreItem_up) == 1 && groupsService.insertScoreItem(scoreItem_insert) == 1) {
                return RespBean.ok("更新成功!");
            }
        } else if (total == 0) {
            if (groupsService.insertScoreItem(scoreItem) == 1) {
                return RespBean.ok("更新成功!");
            }
        }
        return RespBean.error("更新失败!");
    }*/

    @PostMapping("/UpdateOrNew")
    public String updateOrNewGroup(@RequestBody Groups group,@RequestParam Integer institutionID) throws ParseException {
        Integer ID = group.getID();

        if (ID == null) {
            if (groupsService.insertGroup(group) == 1) {
                Log log=new Log();
                SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = sdf.parse(sdf.format(System.currentTimeMillis()));
                Timestamp nousedate = new Timestamp(date.getTime());
                log.setLog(nousedate,institutionID,"分组","新增成功");
                logService.addLogs(log);
                return "新增成功!";
            }
        } else {
            if (groupsService.updateGroup(group) == 1) {
                Log log=new Log();
                SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = sdf.parse(sdf.format(System.currentTimeMillis()));
                Timestamp nousedate = new Timestamp(date.getTime());
                log.setLog(nousedate,institutionID,"分组","更新成功");
                logService.addLogs(log);
                return "更新成功!";
            }
        }
        Log log=new Log();
        SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = sdf.parse(sdf.format(System.currentTimeMillis()));
        Timestamp nousedate = new Timestamp(date.getTime());
        log.setLog(nousedate,institutionID,"分组","更新失败");
        logService.addLogs(log);
        return "更新失败!";
    }
}
