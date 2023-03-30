package org.sys.rate.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.sys.rate.config.JsonResult;
import org.sys.rate.mapper.InfoItemMapper;
import org.sys.rate.mapper.InfosMapper;
import org.sys.rate.mapper.ParticipatesMapper;
import org.sys.rate.mapper.ScoresMapper;
import org.sys.rate.model.*;
import org.sys.rate.service.admin.*;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


@RestController
@RequestMapping("/infoItem/basic")
public class InfoItemController {
    @Autowired
    InfoItemService infoItemService;
    @Autowired
    InfosMapper infosMapper;
    @Autowired
    LogService logService;
    @Resource
    InfoItemMapper infoItemMapper;
    @Resource
    ScoreItemService scoreItemService;
    @Resource
    ParticipatesMapper participatesMapper;
    @Resource
    TotalItemService totalItemService;

    @GetMapping("/")
    public RespPageBean getActivitiesByPage(@RequestParam Integer keywords, @RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer size, InfoItem employee) {
        return infoItemService.getActivitiesPage(keywords, page, size, employee);
    }

    @PostMapping("/delete")
    public RespBean deleteInfo(@RequestBody InfoItem company,@RequestParam Integer institutionID) throws ParseException {
        if (infoItemService.deleteInfo(company) == 1) {
            Log log=new Log();
            SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = sdf.parse(sdf.format(System.currentTimeMillis()));
            Timestamp nousedate = new Timestamp(date.getTime());
            log.setLog(nousedate,institutionID,"信息项","删除成功");
            logService.addLogs(log);
            return RespBean.ok("删除成功!");
        }
        Log log=new Log();
        SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = sdf.parse(sdf.format(System.currentTimeMillis()));
        Timestamp nousedate = new Timestamp(date.getTime());
        log.setLog(nousedate,institutionID,"信息项","删除失败");
        logService.addLogs(log);
        return RespBean.error("删除失败!");
    }

    @PostMapping("/UpdateOrNew")
    public RespBean updateInfoItem(@RequestBody InfoItem infoItem,@RequestParam Integer institutionID) throws ParseException {
        Integer ID = infoItem.getID();
        StringBuffer str5 = new StringBuffer();
        for (String s : infoItem.getShuZuType()) {
            str5.append(s).append(',');
        }
        infoItem.setContentType(str5.toString());
        if (ID == null) {
            if (infoItemService.addParticipates(infoItem) == 1) {
                Log log=new Log();
                SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = sdf.parse(sdf.format(System.currentTimeMillis()));
                Timestamp nousedate = new Timestamp(date.getTime());
                log.setLog(nousedate,institutionID,"信息项","新增成功");
                logService.addLogs(log);
                return RespBean.ok("新增成功!");
            }
        } else {
            if (infoItemService.updateInfoItem(infoItem) == 1) {
                Log log=new Log();
                SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = sdf.parse(sdf.format(System.currentTimeMillis()));
                Timestamp nousedate = new Timestamp(date.getTime());
                log.setLog(nousedate,institutionID,"信息项","更新成功");
                logService.addLogs(log);
                return RespBean.ok("更新成功!");
            }
        }
        Log log=new Log();
        SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = sdf.parse(sdf.format(System.currentTimeMillis()));
        Timestamp nousedate = new Timestamp(date.getTime());
        log.setLog(nousedate,institutionID,"信息项","新增/更新失败");
        logService.addLogs(log);
        return RespBean.error("更新失败!");
    }
    //通过学生id等3个id获得participantID
    public Infos getParticipantIDByStu(Integer activityID,Integer studentID,Integer infoItemID){
        Integer participantID = infosMapper.selectStudent(studentID,activityID);
        Infos infos = new Infos();
        infos.setParticipantID(participantID);
        infos.setActivityID(activityID);
        infos.setInfoItemID(infoItemID);
        return infos;
    }
    //上传文件 学生界面
    @PostMapping("/upload")
    public JsonResult upload(@RequestParam MultipartFile file,@RequestParam Integer activityID,@RequestParam Integer studentID,@RequestParam Integer infoItemID) throws IOException {
        String filename = file.getOriginalFilename();
//        System.out.println(filename);
        String fPath=new File("upload").getAbsolutePath() + "/" + filename;
//        String fPath_=new File("upload").getAbsolutePath();
        File newFile = new File(fPath);
        file.transferTo(newFile);
        //将文件的保存路径存入数据库的content中
        Infos infos = getParticipantIDByStu(activityID,studentID,infoItemID);
        infos.setContent(fPath);
        Integer res = infosMapper.selectStudent(studentID,activityID);
        infos.setParticipantID(res);
        if (infosMapper.UpdateScore1(infos) == 0){
            infosMapper.insertScore1(infos);
        }
        //返回文件存储路径
        return new JsonResult(fPath);
    }
    @PostMapping("/deleteFile")//删除文件 学生界面
    @ResponseBody
    public JsonResult delete(String filepath,Integer activityID,Integer studentID,Integer infoItemID){
        boolean flag = false;
        File file = new File(filepath);
        if( file.isFile() && file.exists()){
            flag = file.delete();
        }
        if(flag){
            Infos infos = getParticipantIDByStu(activityID,studentID,infoItemID);
            infos.setContent("");
            infosMapper.UpdateScore1(infos);
        }
        return new JsonResult(flag);
    }

    @GetMapping("/stu")
    public RespPageBean getActivitiesByStudent(@RequestParam Integer keywords, @RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer size, @RequestParam Integer studentID,InfoItem employee) {
        return infoItemService.getActivitiesByStudent(keywords, page, size, studentID,employee);
    }

    // 获得未分组选手所有的信息项
    @GetMapping("/getAll/{id}")
    public Msg getAll(@PathVariable("id") Integer id){
        List<InfoItem> infoItems = infoItemService.getInforItemByActivityId(id);
        List<ScoreItem> scoreItems = scoreItemService.getAllByActicityID(id);
        return Msg.success().add("infoItems",infoItems).add("scoreItems",scoreItems);
    }

    @GetMapping("/all")
    public RespBean getAllByActivityID(@RequestParam Integer activityID){
        List<InfoItem> infoItems = infoItemService.getAll(activityID);
        return RespBean.ok("success",infoItems);
    }

    // 用于选手分数页面的筛选，基于信息项
    @GetMapping("/getAllInf")
    public Msg getAllInf(@RequestParam("ID") Integer ID,@RequestParam("groupID") Integer groupID){
        List<Integer> participatesID;
        if (groupID == 0) // 所有选手
            participatesID = participatesMapper.getParticipantsIDByAId(ID);
        else // 某个分组的选手
            participatesID = participatesMapper.getParticipantsIDByAIdAndGId(ID,groupID);
        if (participatesID.size() == 0)
            return Msg.success();
        List<InfoItem> infoItems = infoItemMapper.selectInfoItemByActivityId(ID,participatesID);
        return Msg.success().add("infoItems",infoItems);
    }

    // 待修改，计划在前端直接筛选
    // 通过信息项类型和信息项值对finalGroup筛选
    @GetMapping("/getFilteredFianlGroup")
    public RespPageBean getFilteredFianlGroup(@RequestParam("infoItemName") String infoItemName,
                                              @RequestParam("infoItemContent") List<String> infoItemValue,
                                              @RequestParam("activityID") Integer activityID){
        RespPageBean all = totalItemService.getFinalScore(activityID,1,1000);
        Integer infoItemID = infoItemMapper.getIDByNameAndActivityID(infoItemName,activityID);
        HashFianlScore hashFianlScore = (HashFianlScore) all.getData().get(0);
        HashMap<Integer,Participates> newHash = new HashMap<>();
        hashFianlScore.getMap().forEach((k,v)->{
           int participatesID = v.getID();
           // 通过participatesID，activityID，infoItemID在infos表中查询content
            String content = infosMapper.getContentByParticipatesIDAndActivityIDAndInfoItemID(participatesID,activityID,infoItemID);
            if (infoItemValue.contains(content)){
                newHash.put(k,v);
            }
        });
        hashFianlScore.setMap(newHash);
        all.setTotal(Long.valueOf((Integer)newHash.size()));
        return all;
    }
}
