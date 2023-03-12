package org.sys.rate.controller.expert;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.sys.rate.model.*;
import org.sys.rate.service.admin.*;
import org.sys.rate.service.expert.ExpertService;
import org.sys.rate.service.expert.TeacherService;
import org.sys.rate.service.expert.ExpertactivitiesService;
import org.sys.rate.utils.PDFUtils;
import org.sys.rate.utils.POIUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

//@RestController("ratesystemExperts")
@RestController
@RequestMapping("/system/Experts")
public class ExpertController {
    private static Lock reentrantLock = new ReentrantLock();// 锁对象
    @Autowired
    TeacherService teacherService;
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
    InfoItemService infoItemService;
    @Autowired
    InfosService infosService;
    @Autowired
    LogService logService;
    @Autowired
    ExpertService expertService;

    @ResponseBody
    @GetMapping(value = "/activities")
    public Msg getActivitiesById(@RequestParam(value = "expertID") Integer expertID) {
        List<Expertactivities> expertactivitiesLists = expertactivitiesService.selectActByExpertID(expertID);
        List<ActivityList> ac = new ArrayList<ActivityList>();
        for (Expertactivities e : expertactivitiesLists) {
            Integer activityID = e.getActivityid();
            Integer groupId = e.getGroupid();
            String groupName = expertactivitiesService.getgroupNameById(activityID, groupId);
            List<Activities> activitys= activitiesService.getActivity(activityID);
            ActivityList activityList = new ActivityList();
            activityList.setActivityID(activityID);
            activityList.setActivityLists(activitys);
            activityList.setGroupId(groupId);
            activityList.setGroupName(groupName);
            ac.add(activityList);
        }
        return Msg.success().add("activitiesList",ac).add("count",ac.size());
    }

    @ResponseBody
    @GetMapping("/score")
    public Msg getParticiants(@RequestParam(value = "activitiesID") Integer activitiesID,
                              @RequestParam(value = "expertID") Integer expertID,
                              @RequestParam(value = "groupId") Integer groupId) {
//        返回 participant的p.ID,p.displaySequence,p.code,学生姓名s.name,p.studentID 多个学生记录
        List<Participates> participatesL = participatesService.getParticipantsByGroupId(activitiesID,groupId);
//        返回分数 查找的expertid是null
        List<Scores> scoresListNoBy = scoresService.getScoreListNoExpert(activitiesID,groupId);
        //        返回分数 查找的expertid是null
        List<Scores> scoresListByExpert = scoresService.getScoreListByExpert(expertID,activitiesID,groupId);
        //获得专家活动评分状态 是否已经完成评分
        Boolean finished = expertactivitiesService.getState(activitiesID, expertID,groupId);
        //获得评分项的个数根据活动id 分数项
        Integer scoreItemCount = activitiesService.getScoreItemCount(activitiesID);
        //System.out.println(scoreItemCount);
        //获得该活动的评分项列表  byexpert是false
        List<ScoreItem> scoreitems = scoreItemService.getScoreItemByActivityId(activitiesID);
        //获得inforItem项列表
        List<InfoItem> infoItems = infoItemService.getInforItemByActivityIdAndPars(activitiesID);

        List<Infos> infosList = infosService.getInforsList(activitiesID,groupId);

        return Msg.success().add("finished", finished).add("scoreitems", scoreitems).add("scoreItemCount", scoreItemCount)
                .add("participatesList", participatesL).add("scoresListByExpert",scoresListByExpert)
                .add("scoresListNoExpert", scoresListNoBy ).add("infoItems",infoItems).add("infosList",infosList);
    }

    @ResponseBody
    @PostMapping("/save")
    public Msg saveScore(@RequestBody ScoreWithEP scoreWithEP) {
        Integer expertID = scoreWithEP.getExpertID();
        Integer participantID = scoreWithEP.getParticipantID();
        Integer AID = null;
        List<ScoreItem> scoreList = scoreWithEP.getScoreList();
        for (ScoreItem s : scoreList) {
            Integer activityID = s.getActivityid();
            AID=activityID;
            Integer scoreItemID = s.getId();
            Double score = s.getScore();
            //判断是否有该项，有就更新，没有就插入
            Integer t = scoresService.getScoreByP(expertID, activityID, participantID, scoreItemID);
//            System.out.println(t);
            if (t == 0) {
                Integer i = scoresService.saveScore(expertID, activityID, participantID, scoreItemID, score);
                if (i == 0) {
                    return Msg.fail().add("msg", "保存失败");
                }
            } else if (t != 0) {
                scoresService.updateScore(expertID, activityID, participantID, scoreItemID, score);
            }
        }
        reentrantLock.lock();
        try {//开始算
            Integer i =participatesService.saveAvgscore(participantID, AID);
            if(i==0){return Msg.fail().add("msg", "保存失败");}
        }
        catch(Exception e) {
            return Msg.fail().add("msg", "保存失败");
        }
        finally {
            reentrantLock.unlock();
            // "解锁了！！！！！！！！！！！！！！！！！！！！！" + Calendar.getInstance().getTime());
        }
        return Msg.success().add("msg", "保存成功");
    }

    @ResponseBody
    @PostMapping("/ChangeState")
    public Msg changeState(@RequestParam(value = "activityId") Integer activityId,
                           @RequestParam(value = "expertID") Integer expertID,
                           @RequestParam(value = "groupId") Integer groupId,
                           @RequestParam(value = "finished") Boolean finished) {
        expertactivitiesService.updateState(activityId, expertID,groupId, finished); //更新状态
        return Msg.success().add("msg", "更新成功");
    }

    @ResponseBody
    @PostMapping("/revert")
    public Msg revert(@RequestParam(value = "activityId") Integer activityId,
                           @RequestParam(value = "expertID") Integer expertID,
                           @RequestParam(value = "groupId") Integer groupId,
                      @RequestParam(value = "institutionID") Integer institutionID,
                           @RequestParam(value = "finished") Boolean finished) throws ParseException {
        expertactivitiesService.updateState(activityId, expertID,groupId, finished); //更新状态
        Log log=new Log();
        SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = sdf.parse(sdf.format(System.currentTimeMillis()));
        Timestamp nousedate = new Timestamp(date.getTime());
        log.setLog(nousedate,institutionID,"专家评分状态","撤回成功");
        logService.addLogs(log);
        return Msg.success().add("msg", "更新成功");
    }

    @ResponseBody
    @GetMapping("/exportRate")
    public ResponseEntity<byte[]> exportData_group(@RequestParam Integer groupId,
                                                   @RequestParam Integer expertID,
                                                   @RequestParam Integer activitiesID) {
        List<Activities> Act = activitiesService.getActivity(activitiesID);
        List<Participates> participatesL = participatesService.getParticipantsByGroupId(activitiesID,groupId);

        List<Scores> scoresList = scoresService.getScoreListExpert(expertID,activitiesID,groupId);

        //获得该活动的评分项列表
        List<ScoreItem> scoreItems = scoreItemService.getScoreItemByActivityId(activitiesID);
        //获得inforItem项列表
        List<InfoItem> infoItemsShow = infoItemService.getInforItemByActivityIdIsShow(activitiesID);
        List<Infos> infosList = infosService.getInforsList(activitiesID,groupId);
        return POIUtils.ExportRate(Act,participatesL,scoresList,scoreItems,infoItemsShow,infosList);
    }

    @PostMapping("/importRate")
    public RespBean importData(@RequestParam Integer groupId,
                               @RequestParam Integer expertID,
                               @RequestParam Integer activitiesID, MultipartFile file) throws IOException, ParseException {
        System.out.println("POIUtils.check(file)");
        //获得该活动的评分项列表
        List<ScoreItem> scoreItemsByE = scoreItemService.getScoreItemByEActivityId(activitiesID);
        List<Participates> participatesL = participatesService.getParticipantsByGroupId(activitiesID,groupId);

        List<Scores> scoresList = new ArrayList<>();
        Scores scoreOne;
        RespPageBean bean= POIUtils.readExcel_rate(file);
        List<Map<String, String>> rateList = (List<Map<String, String>>) bean.getData();
        if(rateList == null){
            return RespBean.error("未读取到有效导入数据！");
        }else {
            for(int i=0;i<rateList.size();i++){
                Map<String, String> list = rateList.get(i);
                for(String k: list.keySet()){
                    Integer pID = null;
                    for(Participates p:participatesL){
                        if(p.getCode().equals(list.get("编号"))){
                            pID = p.getID();
                        }
                    }
                    for(ScoreItem s:scoreItemsByE){
                        if(s.getName().equals(k)){
                            scoreOne = new Scores();
                            if(list.get(k)!=null) {
                                scoreOne.setScore(new Double(list.get(k)));
                                scoreOne.setExpertID(expertID);
                                scoreOne.setActivityID(activitiesID);
                                scoreOne.setScoreItemID(s.getId());
                                scoreOne.setParticipantID(pID);
                                scoresList.add(scoreOne);
                            }
                        }
                    }
                }
            }
            List<String> res = teacherService.addScores(expertID,activitiesID,scoresList);
            if (res.size()==0) {
                System.out.println("导入成功");
                return RespBean.ok("success");
            }
            return RespBean.error("fail",res);
        }
    }

    @ResponseBody
    @GetMapping("/exportPDF")
    public void exportDataPDF(HttpServletResponse response,
                              @RequestParam Integer groupId,
                              @RequestParam Integer expertID,
                              @RequestParam Integer activitiesID) throws Exception {
        //专家名
        String expertName = teacherService.getExpertNameById(expertID);
        //活动信息
        List<Activities> Act = activitiesService.getActivity(activitiesID);
        //人员信息
        List<Participates> participatesL = participatesService.getParticipantsByGroupId(activitiesID,groupId);
        //评分
        List<Scores> scoresList = scoresService.getScoreListExpert(expertID,activitiesID,groupId);

        //获得该活动的评分项列表
        List<ScoreItem> scoreItems = scoreItemService.getScoreItemByActivityId(activitiesID);
        //获得inforItem项列表
        List<InfoItem> infoItemsShow = infoItemService.getInforItemByActivityIdIsShow(activitiesID);

        List<Infos> infosList = infosService.getInforsList(activitiesID,groupId);

        Map<String, Object> model = new HashMap<>();
        model.put("name",expertName);
        model.put("Act", Act);
        model.put("participatesL", participatesL);
        model.put("scoresList", scoresList);
        model.put("scoreItems", scoreItems);
        model.put("infoItemsShow", infoItemsShow);
        model.put("infosList", infosList);
        try{
            // 设置response方式,使执行此controller时候自动出现下载页面,而非直接使用excel打开
            String fileName =Act.get(0).getName()+"评分表"+".pdf";
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/pdf");
            //打开浏览器窗口预览文件
            response.setHeader("Content-Disposition","filename=" + new String(fileName.getBytes(), "iso8859-1"));
            //直接下载
            //response.setHeader("Content-Disposition","attachment;filename=" + new String(fileName.getBytes(), "iso8859-1"));
            PDFUtils.ExportPDF(response,model);
        }catch (Exception e){
            e.printStackTrace();
        }

//        PDFUtils.ExportPDF(Act,participatesL,scoresList,scoreItems,infoItemsShow,infosList);
//        ExportFileUtils.createPdf(document, map);
//        return new ModelAndView(new ViewPDF(), model);

    }
}



