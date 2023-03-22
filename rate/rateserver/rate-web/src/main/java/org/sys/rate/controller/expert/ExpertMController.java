package org.sys.rate.controller.expert;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.sys.rate.config.CSVReader;
import org.sys.rate.model.*;
import org.sys.rate.service.admin.*;
import org.sys.rate.service.expert.ExpertService;
import org.sys.rate.service.expert.ExpertactivitiesService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.sys.rate.utils.POIUtils;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@RestController("ratesystemExperts")
@RequestMapping("/systemM/Experts")
public class ExpertMController {
    @Autowired
    ExpertService expertService;
    @Autowired
    ExpertactivitiesService expertactivitiesService;
    @Autowired
    ActivitiesService activitiesService;
    @Autowired
    ScoreItemService scoreItemService;
    @Autowired
    ParticipatesService participatesService;
    @Autowired
    ScoresService scoresService;
    @Autowired
    LogService logService;

    @GetMapping("/")
    @ResponseBody
    public List<Experts> getExpertsByPage(@RequestParam Integer keywords, @RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer size, Experts employee) {
        List<Experts> res = expertService.getExpertsByPage(keywords,page, size, employee);
        System.out.println(res);
        return res;
    }

    @GetMapping("/show_situ")
    public RespPageBean getExpertSituationByExpertID(@RequestParam Integer activityID,@RequestParam Integer groupID, @RequestParam Integer expertID,@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer size) {
        return expertService.getExpertSituationByExpertID(activityID,groupID,expertID,page, size);
    }

    @PostMapping("/update")
    public String updateActivities(@RequestBody Experts teacher) throws ParseException {
        if (expertService.updateTeacherCount(teacher) == 1) {
            Log log=new Log();
            SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = sdf.parse(sdf.format(System.currentTimeMillis()));
            Timestamp nousedate = new Timestamp(date.getTime());
            log.setLog(nousedate,teacher.getInstitutionid(),"专家管理","更新成功");
            logService.addLogs(log);
            return "更新成功!";
        }
        Log log=new Log();
        SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = sdf.parse(sdf.format(System.currentTimeMillis()));
        Timestamp nousedate = new Timestamp(date.getTime());
        log.setLog(nousedate,teacher.getInstitutionid(),"专家管理","更新失败");
        logService.addLogs(log);
        return "更新失败!";
    }

    @PostMapping("/withdraw")
    public String withdrawScore(@RequestParam Integer activityID,@RequestParam Integer groupID,@RequestParam Integer expertID) {
        if(expertService.withdrawScore(activityID,groupID,expertID))
            return "撤回成功!";
        else
            return "撤回失败";
    }

    @PostMapping("/updateGroupId")
    public RespBean updateGroupId(@RequestParam Integer groupid,@RequestParam Integer activityid,@RequestBody Experts teacher) throws ParseException {
        if (expertService.updateExActivityTable(groupid,activityid,teacher) == 1) {
            Log log=new Log();
            SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = sdf.parse(sdf.format(System.currentTimeMillis()));
            Timestamp nousedate = new Timestamp(date.getTime());
            log.setLog(nousedate,teacher.getInstitutionid(),"专家管理","更新成功");
            logService.addLogs(log);
            return RespBean.ok("更新成功!");
        }
        Log log=new Log();
        SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = sdf.parse(sdf.format(System.currentTimeMillis()));
        Timestamp nousedate = new Timestamp(date.getTime());
        log.setLog(nousedate,teacher.getInstitutionid(),"专家管理","更新失败");
        logService.addLogs(log);
        return RespBean.error("更新失败!");
    }
    //3.22 对导入功能的改进和完善
    @PostMapping("/import")
    public RespBean importData(@RequestParam Integer groupid,@RequestParam Integer activityid,@RequestParam Integer insititutionID,MultipartFile file) throws IOException, ParseException {
        RespPageBean bean= POIUtils.readExcel_expert(file);
        List<Experts> list= (List<Experts>) bean.getData();
        if(list==null){
            Log log=new Log();
            SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = sdf.parse(sdf.format(System.currentTimeMillis()));
            Timestamp nousedate = new Timestamp(date.getTime());
            log.setLog(nousedate,insititutionID,"专家管理","未读取到有效导入数据，可能第"+bean.getTotal()+"行csv编码或者表头或者内容错误！");
            logService.addLogs(log);
            return RespBean.error("未读取到有效导入数据，可能第"+bean.getTotal()+"行csv编码或者表头或者内容错误！");
        }else {
            List<String> res=expertService.addTeachers(groupid,activityid,list);
            if (res.size()==0) {
                Log log=new Log();
                SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = sdf.parse(sdf.format(System.currentTimeMillis()));
                Timestamp nousedate = new Timestamp(date.getTime());
                log.setLog(nousedate,insititutionID,"专家管理","导入成功");
                logService.addLogs(log);
                return RespBean.ok("success");
            }
            Log log=new Log();
            SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = sdf.parse(sdf.format(System.currentTimeMillis()));
            Timestamp nousedate = new Timestamp(date.getTime());
            log.setLog(nousedate,insititutionID,"专家管理","导入失败");
            logService.addLogs(log);
            return RespBean.error("fail",res);
        }
    }

    @PutMapping("/pass")
    public String updateHrPasswd(@RequestBody  Experts teacher) throws ParseException {
        Integer id = teacher.getId();
        String pass = teacher.getIdnumber();
        if (expertService.updatePasswd(id,pass)) {
            Log log=new Log();
            SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = sdf.parse(sdf.format(System.currentTimeMillis()));
            Timestamp nousedate = new Timestamp(date.getTime());
            log.setLog(nousedate,teacher.getInstitutionid(),"专家管理","已将密码设置为证件号码后6位");
            logService.addLogs(log);
            return "已将密码设置为证件号码后6位!";
        }
        Log log=new Log();
        SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = sdf.parse(sdf.format(System.currentTimeMillis()));
        Timestamp nousedate = new Timestamp(date.getTime());
        log.setLog(nousedate,teacher.getInstitutionid(),"专家管理","更新失败");
        logService.addLogs(log);
        return "更新失败!";
    }

    @PostMapping("/delete")
    public RespBean delete(@RequestParam Integer groupid,@RequestParam Integer activityid,@RequestBody Experts teacher) throws ParseException {
        if (expertService.deleteExActivityTable(groupid,activityid,teacher) == 1) {
            Log log=new Log();
            SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = sdf.parse(sdf.format(System.currentTimeMillis()));
            Timestamp nousedate = new Timestamp(date.getTime());
            log.setLog(nousedate,teacher.getInstitutionid(),"专家管理","删除成功");
            logService.addLogs(log);
            return RespBean.ok("删除成功!");
        }
        Log log=new Log();
        SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = sdf.parse(sdf.format(System.currentTimeMillis()));
        Timestamp nousedate = new Timestamp(date.getTime());
        log.setLog(nousedate,teacher.getInstitutionid(),"专家管理","删除失败");
        logService.addLogs(log);
        return RespBean.error("删除失败!");
    }
    @ResponseBody
    @GetMapping("/score")
    public Msg getParticiants(@RequestParam(value = "activitiesId")Integer activitiesId){
        List<Scores> scoresList;
            scoresList=scoresService.getScoreList_null(activitiesId,null);
        //System.out.println(scoresList);
        //获得评分项的个数根据活动id
        Integer scoreItemCount =scoreItemService.ScoreItemByActivityIdWhereNotByExpertCount(activitiesId);
        //System.out.println(scoreItemCount);
        //获得该活动的评分项列表
        List<ScoreItem> scoreitems=scoreItemService.selectScoreItemByActivityIdWhereNotByExpert(activitiesId);
        //System.out.println(scoreitems);
        return Msg.success().add("scoreitems",scoreitems).add("scoreItemCount",scoreItemCount)
                .add("scoresList",scoresList);
    }


}
