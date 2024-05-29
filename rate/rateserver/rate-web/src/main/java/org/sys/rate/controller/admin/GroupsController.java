package org.sys.rate.controller.admin;

import cn.hutool.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.sys.rate.mapper.GroupsMapper;
import org.sys.rate.model.*;
import org.sys.rate.service.admin.GroupsService;
import org.sys.rate.service.admin.InfosService;
import org.sys.rate.service.admin.LogService;
import org.sys.rate.service.admin.ParticipatesService;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


@RestController
@RequestMapping("/groups/basic")
public class GroupsController {
    @Autowired
    GroupsService groupsService;
    @Autowired
    LogService logService;
    @Autowired
    InfosService infosService;
    @Resource
    GroupsMapper groupsMapper;
    @Resource
    ParticipatesService participatesService;

    @GetMapping("/")
    public RespPageBean getGroupsByPage(@RequestParam Integer keywords, @RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer size, Groups employee) {
        return groupsService.getActivitiesPage(keywords, page, size, employee);
    }

    // 秘书获取当前活动中自己的组
    @GetMapping("/sec")
    public RespBean getGroupsSec(@RequestParam Integer groupID) {
        return RespBean.ok("success", groupsMapper.getGroup(groupID));
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
        if (groupsService.deleteActivities(company) >= 0) {
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
    public String createGroups(@RequestBody Map<String,Object> data) throws ParseException {
        Integer activityID = (Integer) data.get("activityID");
        Integer infoItemID = (Integer) data.get("infoItemID");
        Integer sortByItemID = (Integer) data.get("sortByItemID");
        List<Integer> arr = (List<Integer>) data.get("arr");
        Integer exchangeNums = (Integer) data.get("exchangeNums");
        Integer groupsNums = (Integer) data.get("groupsNums");
        List<String> infoContent = (List<String>) data.get("infoContent");
        return groupsService.judgeNumber(activityID,infoItemID,arr,exchangeNums,groupsNums,infoContent,sortByItemID);
        //返回分好组的选手信息
    }

    @GetMapping("/pars")
    public RespBean getGroupPars(@RequestParam Integer groupID) {
        return RespBean.ok("success", groupsMapper.getGroupPars(groupID));
    }

    @GetMapping("/parsByActID")
    public RespBean getParsByID(@RequestParam Integer activityID) {
        return RespBean.ok("success", groupsMapper.getParsByID(activityID));
    }

    @GetMapping("/experts")
    public RespBean getGroupExperts(@RequestParam Integer groupID) {
        return RespBean.ok("success", groupsMapper.getGroupExperts(groupID));
    }

    @GetMapping("/expertsByActID")
    public RespBean getGroupExpertsByActID(@RequestParam Integer activityID) {
        return RespBean.ok("success", groupsMapper.getGroupExpertsByActID(activityID));
    }

    @GetMapping("/subGroups")
    public RespBean getSubGroups(@RequestParam Integer activityID,@RequestParam Integer groupID) {
        return RespBean.ok("success", groupsMapper.getSubGroups(activityID,groupID));
    }

    // 处理不进行分组的子活动小组，该类型子活动只会存在一个小组。
    // 如果存在则返回组内的选手信息，不存在则创建一个小组并返回空列表。
    // 返回形式：[小组ID，小组内选手信息]
    @Transactional
    @GetMapping("/parsForUniqueGroupSubActivity")
    public RespBean getParsForUniqueGroupSubActivity(@RequestParam Integer activityID,@RequestParam Integer groupIDParent) {
        Integer groupID = groupsMapper.getSubGroupID(activityID,groupIDParent);
        if (groupID == null) { // 创建子活动分组
            Groups group = new Groups(activityID,groupIDParent,"默认子活动小组");
            groupsMapper.insertGroup(group); // 插入并返回ID
            participatesService.copyParticipates(groupIDParent,activityID,group.getID());
            return RespBean.ok("success", group.getID());
        }
        return RespBean.ok("success", groupID);
    }

    @Transactional
    @GetMapping("/expertsForUniqueGroupSubActivity")
    public RespBean getExpertsForUniqueGroupSubActivity(@RequestParam Integer activityID,@RequestParam Integer groupIDParent) {
        Integer groupID = groupsMapper.getSubGroupID(activityID,groupIDParent);
        if (groupID == null) { // 创建子活动分组
            Groups group = new Groups(activityID,groupIDParent,"默认子活动小组");
            groupsMapper.insertGroup(group); // 插入并返回ID
            return RespBean.ok("success", new ArrayList<>(Arrays.asList(group.getID(),new ArrayList<>())));
        }
        return RespBean.ok("success", new ArrayList<>(Arrays.asList(groupID,groupsMapper.getGroupExperts(groupID))));
    }

    @GetMapping("/getByActivityID")
    public RespBean getGroupsByActivityID(@RequestParam Integer activityID) {
        return RespBean.ok("success", groupsMapper.getGroupsByActivityID(activityID));
    }

    @GetMapping("/getAllByActivityID")
    public RespBean getAllGroupsByActivityID(@RequestParam Integer activityID) {
        return RespBean.ok("success", groupsMapper.getAllGroupsByActivityID(activityID));
    }
}
