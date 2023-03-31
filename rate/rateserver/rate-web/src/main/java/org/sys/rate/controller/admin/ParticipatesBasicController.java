package org.sys.rate.controller.admin;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import com.csvreader.CsvWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.sys.rate.config.CSVReader;
import org.sys.rate.model.*;
import org.sys.rate.service.admin.*;
import org.sys.rate.service.expert.ExpertService;
import org.sys.rate.utils.POIUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


@RestController
@RequestMapping("/participants/basic")
public class ParticipatesBasicController {
    @Autowired
    ParticipatesService participatesService;
    @Autowired
    ScoreItemService scoreItemService;
    @Autowired
    InfoItemService infoItemService;
    @Autowired
    GroupsService groupsService;
    @Autowired
    ExpertService expertService;
    @Autowired
    LogService logService;
    @Resource
    TotalItemService totalItemService;

    @GetMapping("/")
    public RespPageBean getActivitiesByPage(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer size, @RequestParam Integer groupID, @RequestParam Integer activitiesID, Participates employee) {
        RespPageBean res;
        if(groupID!=0)
        {
            res=participatesService.getParticipantsPage(page, size,groupID,activitiesID, employee);
        }
        else {
            res=participatesService.getParticipantsPageByACID(page, size,activitiesID, employee);
        }

        return res;
    }
    @GetMapping("/filterByState")
    public RespPageBean getActivitiesByPage(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer size, @RequestParam String groupState, @RequestParam Integer activitiesID, Participates employee) {
        RespPageBean res;
        if(groupState.equals("未分组"))
        {
            res = participatesService.getParticipantsPageByACIDNoGroup(page, size,activitiesID, employee);
        }else{//已分组
            res = participatesService.getParticipantsPageByACIDHaveGroup(page, size,activitiesID, employee);
        }
        return res;
    }

    /*@PostMapping("/insert")
    public RespBean addParticipates(@RequestBody Participates employee) {
        System.out.println("insert");
        if (participatesService.addParticipates(employee) == 1) {
            return RespBean.ok("添加成功!");
        }
        return RespBean.error("添加失败!");
    }*/

    @PostMapping("/delete")
    public RespBean deleteParticipant(@RequestParam Integer groupID,@RequestBody Participates company) throws ParseException {
        if (participatesService.deleteParticipant(groupID,company) == 1) {
            Log log=new Log();
            SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = sdf.parse(sdf.format(System.currentTimeMillis()));
            Timestamp nousedate = new Timestamp(date.getTime());
            log.setLog(nousedate,company.getInstitutionid(),"选手管理","删除成功");
            logService.addLogs(log);
            return RespBean.ok("删除成功!");
        }
        Log log=new Log();
        SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = sdf.parse(sdf.format(System.currentTimeMillis()));
        Timestamp nousedate = new Timestamp(date.getTime());
        log.setLog(nousedate,company.getInstitutionid(),"选手管理","删除失败");
        logService.addLogs(log);
        return RespBean.error("删除失败!");
    }


    @PostMapping("/update")
    public RespBean updateActivities(@RequestBody Participates company) throws ParseException {
        if (participatesService.updateActivities(company) == 1) {//first, update participants and student
            //second update scoreitem
            participatesService.updatescoreitem(company);
            participatesService.updateinfoitem(company);
            Log log=new Log();
            SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = sdf.parse(sdf.format(System.currentTimeMillis()));
            Timestamp nousedate = new Timestamp(date.getTime());
            log.setLog(nousedate,company.getInstitutionid(),"选手管理","更新成功");
            logService.addLogs(log);
            return RespBean.ok("更新成功!");
        }
        Log log=new Log();
        SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = sdf.parse(sdf.format(System.currentTimeMillis()));
        Timestamp nousedate = new Timestamp(date.getTime());
        log.setLog(nousedate,company.getInstitutionid(),"选手管理","更新失败");
        logService.addLogs(log);
        return RespBean.error("更新失败!");
    }

    @GetMapping("/search")
    public RespPageBean searchActivities(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer size, @RequestParam String company) {
        return participatesService.searchActivities(page, size, company);
    }
//    @GetMapping("/export_group")
//    public ResponseEntity<byte[]> exportGroup(@RequestParam Integer groupID) {
//        List<Participates> list = (List<Participates>) participatesService.getparticipantsByPage(groupID,null, null, new Participates(),null).getData();
//        return POIUtils.Exceltest(list);
//    }

    // 不使用基于后端的导出
//    @GetMapping("/export_ac")
//    public ResponseEntity<byte[]> exportActivity(@RequestParam Integer activityID) {
//        HashFianlScore data = totalItemService.getHashFinalScore(activityID);
////        List<Participates> list = (List<Participates>) participatesService.getAc_participantsByPage(activityID,null, null,null).getData();
//        return POIUtils.ExcelExport(data);
//    }
//
//    // 不使用基于后端的导出
//    @GetMapping("/export_ac_group")
//    public ResponseEntity<byte[]> exportActivity_group(@RequestParam Integer activityID,@RequestParam String groupName) {
//        HashFianlScore data = totalItemService.getHashFinalScoreGroup(activityID, groupName);
//        return POIUtils.ExcelExport(data);
//    }


    @GetMapping("/export")
    public ResponseEntity<byte[]> exportData(@RequestParam Integer activityID) {
        //先get评分项item
        HashPEexport list = expertService.getExpertList("activityID",activityID);
        //List<ScoreItem> ScoreItem=scoreItemService.selectScoreItemByActivityIdWhereNotByExpert(activityID);
        //Integer ScoreItemFinal=scoreItemService.selectScoreItemFinal(activityID);
        return POIUtils.employee2Excel(list);
    }
    @GetMapping("/exportTG")
    public ResponseEntity<byte[]> exportData_group(@RequestParam Integer groupID) {
        //先get评分项item
        HashPEexport list = expertService.getExpertList("groupID",groupID);
        return POIUtils.employee2Excel(list);
    }
    @GetMapping("/parscore")
    public RespPageBean getExpertScore(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer size, @RequestParam Integer groupID, @RequestParam Integer activityID) {
        return expertService.getExpertGroupScore(page, size,groupID,activityID);
    }
    @GetMapping("/exportMo")
    public ResponseEntity<byte[]> downloadExample(HttpServletResponse response) {
        return POIUtils.writeMo();
    }

    @GetMapping("/exportMoPar_Group")
    public ResponseEntity<byte[]> downloadExample_Participants_exportMoPar_Group(@RequestParam Integer activityid,@RequestParam List<String> dymatic_list,@RequestParam List<String> scoreitem,@RequestParam List<String> infoitem){
        return POIUtils.writeExcel_group(dymatic_list,scoreitem,infoitem);
    }

    @PostMapping("/import")
    public RespBean importData(@RequestParam Integer groupid,@RequestParam Integer activityid,@RequestParam Integer insititutionID,MultipartFile file) throws IOException, ParseException {
        List<Groups> groups = groupsService.getActivitiesName(activityid);
        Map<String,Integer>map=new HashMap<>();
        for(Groups g:groups)
        {
            map.put(g.getName(),g.getID());
        }
        //infoItemService.selectInfoItemByActivityIdWhereNotByExpert(activityid);
        RespPageBean bean=POIUtils.excel2p(groupid,map,file,scoreItemService.selectScoreItemByActivityIdWhereNotByExpert(activityid), infoItemService.selectInfoItemByActivityIdWhereNotByExpert(activityid));
        List<Participates> list= (List<Participates>) bean.getData();
        if(list==null)
        {
            Log log=new Log();
            SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = sdf.parse(sdf.format(System.currentTimeMillis()));
            Timestamp nousedate = new Timestamp(date.getTime());
            log.setLog(nousedate,insititutionID,"选手管理","未读取到有效导入数据，可能第"+bean.getTotal()+"行csv编码或者表头或者内容错误！");
            logService.addLogs(log);
            return RespBean.error("未读取到有效导入数据，可能第"+bean.getTotal()+"行csv编码或者表头或者内容错误！");
        }
        String res=participatesService.addParticipatess(groupid,activityid,list);
        if (res.equals("operate successfully")) {
            Log log=new Log();
            SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = sdf.parse(sdf.format(System.currentTimeMillis()));
            Timestamp nousedate = new Timestamp(date.getTime());
            log.setLog(nousedate,insititutionID,"选手管理","导入完成，无姓名行已经被忽略");
            logService.addLogs(log);
            return RespBean.ok("导入完成，无姓名行已经被忽略");
        }
        Log log=new Log();
        SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = sdf.parse(sdf.format(System.currentTimeMillis()));
        Timestamp nousedate = new Timestamp(date.getTime());
        log.setLog(nousedate,insititutionID,"选手管理","上传失败");
        logService.addLogs(log);
        return RespBean.error("上传失败");
    }
    //alterDisplay
    @PostMapping("/alterDisplay")
    public RespBean alterDisplay(@RequestParam Integer total,@RequestParam Integer groupID,@RequestBody Participates company) throws ParseException {
        //System.out.println("import123"+total+groupID+company);
        if(participatesService.alterDisplay(total, groupID, company).equals("success"))
        {
            Log log=new Log();
            SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = sdf.parse(sdf.format(System.currentTimeMillis()));
            Timestamp nousedate = new Timestamp(date.getTime());
            log.setLog(nousedate,company.getInstitutionid(),"选手管理","顺序更改成功");
            logService.addLogs(log);
            return RespBean.ok("更改成功");}
        Log log=new Log();
        SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = sdf.parse(sdf.format(System.currentTimeMillis()));
        Timestamp nousedate = new Timestamp(date.getTime());
        log.setLog(nousedate,company.getInstitutionid(),"选手管理","顺序更新失败");
        logService.addLogs(log);
        return RespBean.error("更改失败");
    }
    @GetMapping("/getItem")
    public Msg getItem(@RequestParam Integer activityid){
        List<ScoreItem> ScoreItem=scoreItemService.selectScoreItemByActivityIdWhereNotByExpert(activityid);
        List<InfoItem> infoItem=infoItemService.selectInfoItemByActivityIdWhereNotByExpert(activityid);
        return Msg.success().add("ScoreItem",ScoreItem).add("InfoItem",infoItem);
    }
    //@PostMapping("/check")如果模板里有这一列，但是管理员空在那里没填数据，则提示管理员，该列数据会被置空，是否确认继续
    @PostMapping("/check")
    public List<String> check_null(@RequestParam Integer groupid,MultipartFile file){
        return POIUtils.check(file);
    }

    @GetMapping("/getParticipantIDByIdNumber")
    @ResponseBody
    public Msg getParticipantIDByIdNumber(@RequestParam Integer activityID,@RequestParam String IDNumber){
        //通过IDNumber大拿到选手id，通过三个id去找到infos，包括了content
        Participates participant = participatesService.getParticipantIDByIDNumber(activityID,IDNumber);
        List<InfoItem> infoItems = infoItemService.getInforItemAndContentByActivityIdAndPIdAndInfoId(activityID,participant.getID());
        return Msg.success().add("infoitems",infoItems).add("name",participant.getName());
    }
    //删除分组
    @GetMapping("/deleteGroups")
    public RespBean deleteGroups(@RequestParam Integer activityID){
        String res = participatesService.deleteGroups(activityID);
        if(res.equals("选手无分组信息！")){
            return RespBean.error(res);
        }else {
            return RespBean.ok(res);
        }
    }
}
