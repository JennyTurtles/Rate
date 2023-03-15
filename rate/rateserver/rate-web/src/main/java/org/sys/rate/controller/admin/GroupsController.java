package org.sys.rate.controller.admin;

import cn.hutool.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.sys.rate.model.*;
import org.sys.rate.service.admin.GroupsService;
import org.sys.rate.service.admin.InfosService;
import org.sys.rate.service.admin.LogService;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/groups/basic")
public class GroupsController {
    @Autowired
    GroupsService groupsService;
    @Autowired
    LogService logService;
    @Autowired
    InfosService infosService;

    @GetMapping("/")
    public RespPageBean getGroupsByPage(@RequestParam Integer keywords, @RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer size, Groups employee) {
        return groupsService.getActivitiesPage(keywords, page, size, employee);
    }

    @GetMapping("/score")
    public RespPageBean getGroupScore(@RequestParam Integer keywords, @RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer size, Groups employee){
        return groupsService.getGroupScore(keywords,page,size,employee);
    }

    @PostMapping("/insert")
    public RespBean addParticipates(@RequestBody Groups employee, @RequestParam Integer institutionID) {
//        System.out.println("insert");
        employee.setName("输入分组名称");
        if (groupsService.addParticipates(employee) != -1) {
            RespBean res = new RespBean(200, "添加成功!", groupsService.getMaxId());
            return res;
        }
        return RespBean.error("添加失败!");
    }

    @PostMapping("/delete")
    public String deleteInstitution(@RequestBody Groups company, @RequestParam Integer institutionID) throws ParseException {
        if (groupsService.deleteActivities(company) == 1) {
            Log log = new Log();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = sdf.parse(sdf.format(System.currentTimeMillis()));
            Timestamp nousedate = new Timestamp(date.getTime());
            log.setLog(nousedate, institutionID, "分组", "删除成功");
            logService.addLogs(log);
            return "删除成功!";
        }
        Log log = new Log();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = sdf.parse(sdf.format(System.currentTimeMillis()));
        Timestamp nousedate = new Timestamp(date.getTime());
        log.setLog(nousedate, institutionID, "分组", "删除失败");
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
    public String updateOrNewGroup(@RequestBody Groups group, @RequestParam Integer institutionID) throws ParseException {
        Integer ID = group.getID();

        if (ID == null) {
            if (groupsService.insertGroup(group) == 1) {
                Log log = new Log();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = sdf.parse(sdf.format(System.currentTimeMillis()));
                Timestamp nousedate = new Timestamp(date.getTime());
                log.setLog(nousedate, institutionID, "分组", "新增成功");
                logService.addLogs(log);
                return "新增成功!";
            }
        } else {
            if (groupsService.updateGroup(group) == 1) {
                Log log = new Log();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = sdf.parse(sdf.format(System.currentTimeMillis()));
                Timestamp nousedate = new Timestamp(date.getTime());
                log.setLog(nousedate, institutionID, "分组", "更新成功");
                logService.addLogs(log);
                return "更新成功!";
            }
        }
        Log log = new Log();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = sdf.parse(sdf.format(System.currentTimeMillis()));
        Timestamp nousedate = new Timestamp(date.getTime());
        log.setLog(nousedate, institutionID, "分组", "更新失败");
        logService.addLogs(log);
        return "更新失败!";
    }

    @PostMapping("/createGroups")
    @ResponseBody
    public List<Participates> createGroups(@RequestBody Map<String,Object> data) throws ParseException {
        Integer activityID = (Integer) data.get("activityID");
        Integer infoItemID = (Integer) data.get("infoItemID");
        Integer sortByItemID = (Integer) data.get("sortByItemID");
        List<Integer> arr = (List<Integer>) data.get("arr");
        Integer exchangeNums = (Integer) data.get("exchangeNums");
        Integer groupsNums = (Integer) data.get("groupsNums");
        List<String> infoContent = (List<String>) data.get("infoContent");
        //筛选出没有分组的选手
        List<Participates> participates = infosService.getPartipicantByActivityId(activityID,infoItemID,infoContent);
        if(data.get("sortByItemType").equals("评分项")){//先不考虑评分项
            groupsService.createGroupsByScore(arr,exchangeNums,groupsNums,participates,sortByItemID);
            return participates;
        }else if(data.get("sortByItemType").equals("信息项")){//信息项有可能是数字也有可能不是数字
            //插入groups数据，返回groups，传入
            for (int i = 0;i< arr.size();i++){//每组多少人
                participates = groupsService.creatGroups(arr.get(i),participates,activityID,0,arr.get(i),i);
            }
        }
        //返回分好组的选手信息
        return participates;
    }
}
