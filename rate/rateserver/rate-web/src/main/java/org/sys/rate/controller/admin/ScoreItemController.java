package org.sys.rate.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.sys.rate.config.CSVReader;
import org.sys.rate.model.*;
import org.sys.rate.service.admin.LogService;
import org.sys.rate.service.admin.ScoreItemService;
import org.sys.rate.utils.POIUtils;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


@RestController
@RequestMapping("/scoreItem/basic")
public class ScoreItemController {
    @Autowired
    ScoreItemService scoreItemService;

    @Autowired
    LogService logService;


    @GetMapping("/")
    public RespPageBean getActivitiesByPage(@RequestParam Integer keywords, @RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer size, ScoreItem employee) {
        return scoreItemService.getActivitiesPage(keywords, page, size, employee);
    }

    @PostMapping("/insert")
    public RespBean addParticipates(@RequestBody ScoreItem employee) {
        if (scoreItemService.addParticipates(employee) == 1) {
            return RespBean.ok("添加成功!");
        }
        return RespBean.error("添加失败!");
    }

    @PostMapping("/delete")
    public RespBean deleteInstitution(@RequestBody ScoreItem company,@RequestParam Integer institutionID) throws ParseException {
        if (scoreItemService.deleteActivities(company) == 1) {
            Log log=new Log();
            SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = sdf.parse(sdf.format(System.currentTimeMillis()));
            Timestamp nousedate = new Timestamp(date.getTime());
            log.setLog(nousedate,institutionID,"评分项","删除成功");
            logService.addLogs(log);
            return RespBean.ok("删除成功!");
        }
        Log log=new Log();
        SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = sdf.parse(sdf.format(System.currentTimeMillis()));
        Timestamp nousedate = new Timestamp(date.getTime());
        log.setLog(nousedate,institutionID,"评分项","删除失败");
        logService.addLogs(log);
        return RespBean.error("删除失败!");
    }


    @PostMapping("/update")
    public String updateActivities(@RequestBody List<ScoreItem> scoreItem) {
        int activityID = scoreItem.get(0).getActivityid();
        int total = scoreItemService.getactivityCount(activityID);
        //System.out.println("total->" + total);
        if (scoreItem.size() == total) {
            if (scoreItemService.updateActivities(scoreItem) == 1) {
                return "更新成功!";
            }
        } else if (scoreItem.size() > total && total != 0) {
            List<ScoreItem> scoreItem_up = scoreItem.subList(0, total - 1);
            List<ScoreItem> scoreItem_insert = scoreItem.subList(total, scoreItem.size());
            if (scoreItemService.updateActivities(scoreItem_up) == 1 && scoreItemService.insertScoreItem(scoreItem_insert) == 1) {
                return "更新成功!";
            }
        } else if (total == 0) {
            if (scoreItemService.insertScoreItem(scoreItem) == 1) {
                return "更新成功!";
            }
        }
        return "更新失败!";
    }

    @PostMapping("/UpdateOrNew")
    public String updateScoreItem(@RequestBody ScoreItem scoreItem,@RequestParam Integer institutionID) throws ParseException {
        Integer ID = scoreItem.getId();

         if (ID == null) {
             if (scoreItemService.addParticipates(scoreItem) == 1) {
                 Log log=new Log();
                 SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                 Date date = sdf.parse(sdf.format(System.currentTimeMillis()));
                 Timestamp nousedate = new Timestamp(date.getTime());
                 log.setLog(nousedate,institutionID,"评分项","新增成功");
                 logService.addLogs(log);
                 return "新增成功!";
             }
         } else {
            if (scoreItemService.updateScoreItem(scoreItem) == 1) {
                Log log=new Log();
                SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = sdf.parse(sdf.format(System.currentTimeMillis()));
                Timestamp nousedate = new Timestamp(date.getTime());
                log.setLog(nousedate,institutionID,"评分项","更新成功");
                logService.addLogs(log);
                return "更新成功!";
            }
        }
        Log log=new Log();
        SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = sdf.parse(sdf.format(System.currentTimeMillis()));
        Timestamp nousedate = new Timestamp(date.getTime());
        log.setLog(nousedate,institutionID,"评分项","新增/更新失败");
        logService.addLogs(log);
        return "更新失败!";
    }

    @PostMapping("/modify")
    public RespBean modifyScoreItem(@RequestBody ScoreItem scoreItem) {
        if (scoreItemService.updateScoreItem(scoreItem) == 1)
            return RespBean.ok("更新成功!");
        return RespBean.error("更新失败!");
    }

    @GetMapping("/latestId")
    public RespBean latestId(){
        RespBean res = new RespBean(200,null,scoreItemService.latestId());
        return res;
    }

    @GetMapping("/getall")
    public RespBean getAll(@RequestParam Integer activityID){
        List<ScoreItem> scoreItems = scoreItemService.getScoreItemByActivityId(activityID);
        return RespBean.ok("success",scoreItems);
    }

}
