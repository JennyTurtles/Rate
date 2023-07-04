package org.sys.rate.service.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import org.sys.rate.mapper.*;
import org.sys.rate.model.*;
import org.sys.rate.service.expert.ExpertService;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;


@Service
public class ParticipatesService {

    @Autowired
    ParticipatesMapper participatesMapper;
    @Autowired
    StudentMapper studentMapper;
    @Autowired
    ScoresMapper scoresMapper;
    @Autowired
    ScoreItemMapper scoreItemMapper;
    @Autowired
    InfosMapper infosMapper;
    @Autowired
    InfoItemMapper infoItemMapper;
    @Autowired
    ActivitiesMapper activitiesMapper;
    @Autowired
    GroupsMapper groupsMapper;
    @Autowired
    RabbitTemplate rabbitTemplate;

    public final static Logger logger = LoggerFactory.getLogger(ParticipatesService.class);
    SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
    SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
    DecimalFormat decimalFormat = new DecimalFormat("##.00");

    public List<Participates> setProptiesOfParticipants(List<Participates> par,Integer activitiesID){
        for(Participates participates: par)
        {
            HashMap<Integer, ScoreItemValue> map = new HashMap<>();
            //先通过activityID放入map<ID,<ID,NAME,>>
            List<ScoreItemValue> scoreItemValue_0=scoreItemMapper.getScoreItemAll(activitiesID,participates.getID());
            for(ScoreItemValue a:scoreItemValue_0)
            {
                map.put(a.getId(),a);
            }
            ScoreItemValue ItemValue=new ScoreItemValue();
            List<ScoreItemValue> scoreItemValue=scoreItemMapper.getScoreItemValue(activitiesID,participates.getID(),ItemValue);
            for(ScoreItemValue a:scoreItemValue)
            {
                map.put(a.getId(),a);
            }
            participates.setScoremap(map);
            //信息项
            HashMap<Integer, InfoItemValue> map_info = new HashMap<>();
            //先通过activityID放入map<ID,<ID,NAME,>>
            List<InfoItemValue> infoItemValue_0=infoItemMapper.getInfoItemAll(activitiesID,participates.getID());
            for(InfoItemValue b:infoItemValue_0)
            {
                map_info.put(b.getId(),b);
            }
            InfoItemValue ItemValue_info=new InfoItemValue();
            List<InfoItemValue> infoItemValue=infoItemMapper.getInfoItemValue(activitiesID,participates.getID(),ItemValue_info);
            for(InfoItemValue b:infoItemValue)
            {
                map_info.put(b.getId(),b);
            }
            participates.setInfomap(map_info);
            //信息项end
            if(participates.getGroupID()!=null){
                participates.setOldgroupname(groupsMapper.getEmployeeById(participates.getGroupID()).getName());
            }
        }
        return par;
    }

    public RespPageBean getParticipantsPage(Integer page, Integer size,Integer groupID,Integer activitiesID,Participates employee) {
        if (page != null && size != null) {
            page = (page - 1) * size;
        }
        List<Participates> data = participatesMapper.getActivitiesByPage(page, size,groupID,employee);
        setProptiesOfParticipants(data,activitiesID);
        Long total = participatesMapper.getTotal(groupID,employee);
        RespPageBean bean = new RespPageBean();
        bean.setData(data);
        bean.setTotal(total);
        return bean;
    }

    public RespPageBean getParticipantsPageByACID(Integer page, Integer size,Integer activitiesID,Participates employee) {
        if (page != null && size != null) {
            page = (page - 1) * size;
        }
        List<Participates> data = participatesMapper.getParticipantsPageByACID(page, size,activitiesID,employee);
        setProptiesOfParticipants(data,activitiesID);
//        for(Participates participates: data)
//        {
//            HashMap<Integer, ScoreItemValue> map = new HashMap<>();
//            //先通过activityID放入map<ID,<ID,NAME,>>
//            List<ScoreItemValue> scoreItemValue_0=scoreItemMapper.getScoreItemAll(activitiesID,participates.getID());
//            for(ScoreItemValue a:scoreItemValue_0)
//            {
//                map.put(a.getId(),a);
//            }
//            ScoreItemValue ItemValue=new ScoreItemValue();
//            List<ScoreItemValue> scoreItemValue=scoreItemMapper.getScoreItemValue(activitiesID,participates.getID(),ItemValue);
//            for(ScoreItemValue a:scoreItemValue)
//            {
//                map.put(a.getId(),a);
//            }
//            participates.setScoremap(map);
//            //信息项
//            HashMap<Integer, InfoItemValue> map_info = new HashMap<>();
//            //先通过activityID放入map<ID,<ID,NAME,>>
//            List<InfoItemValue> infoItemValue_0=infoItemMapper.getInfoItemAll(activitiesID,participates.getID());
//            for(InfoItemValue b:infoItemValue_0)
//            {
//                map_info.put(b.getId(),b);
//            }
//            InfoItemValue ItemValue_info=new InfoItemValue();
//            List<InfoItemValue> infoItemValue=infoItemMapper.getInfoItemValue(activitiesID,participates.getID(),ItemValue_info);
//            for(InfoItemValue b:infoItemValue)
//            {
//                map_info.put(b.getId(),b);
//            }
//            participates.setInfomap(map_info);
//            //信息项end
//            if(participates.getGroupID()!=null){
//                participates.setOldgroupname(groupsMapper.getEmployeeById(participates.getGroupID()).getName());
//            }
//        }
        Long total = participatesMapper.getTotalByACID(activitiesID,employee);
        RespPageBean bean = new RespPageBean();
        bean.setData(data);
        bean.setTotal(total);
        return bean;
    }
    //未分组的选手筛选
    public RespPageBean getParticipantsPageByACIDNoGroup(Integer page, Integer size,Integer activitiesID,Participates employee){
        if (page != null && size != null) {
            page = (page - 1) * size;
        }
        List<Participates> data = participatesMapper.getParticipantsPageByACIDNoGroup(page, size,activitiesID,employee);
        setProptiesOfParticipants(data,activitiesID);
        Long total = participatesMapper.getTotalByACIDNogroup(activitiesID,employee);
        RespPageBean bean = new RespPageBean();
        bean.setData(data);
        bean.setTotal(total);
        return bean;
    }
    //分过组的选手筛选
    public RespPageBean getParticipantsPageByACIDHaveGroup(Integer page, Integer size,Integer activitiesID,Participates employee){
        if (page != null && size != null) {
            page = (page - 1) * size;
        }
        List<Participates> data = participatesMapper.getParticipantsPageByACIDHaveGroup(page, size,activitiesID,employee);
        setProptiesOfParticipants(data,activitiesID);
        Long total = participatesMapper.getTotalByACIDHaveGroup(activitiesID,employee);
        RespPageBean bean = new RespPageBean();
        bean.setData(data);
        bean.setTotal(total);
        return bean;
    }

    // 写得实在太差了，洋洋洒洒写了200多行，写的人完全没有考虑后期维护和扩展性的问题，这是一种极其不负责任的态度，看得我血压高！💢 by grz
    // 问题1：代码啰嗦，创建student对象，先是new，然后疯狂set，不知道用构造函数的吗？
    // 问题2：命名混乱，ID、Id、id三种写法一种意思。方法名，一会儿驼峰命名一会儿下划线命名一会儿insertFROMImport。选手名叫employee，连名字都懒得改一下是吧？
    // 问题3：逻辑混乱，200多行几乎都是if-else，注释写了跟没写一样也是nb。
    // 举例：checkByIDandActivityID通过活动ID和选手ID查找，后面checkGroupIDExists又用同样的方式查找了一遍，注释写的是：真的不存在。请问还有假的不存在吗？？
    // 举例：前面已经通过if-else确保了选手表里没有记录，写的sql为什么用ON DUPLICATE KEY UPDATE呢？
    // 建议：将此代码布置为JAVA程序设计课的课后作业，让同学们改BUG，作为反面教材引以为鉴
    public String addParticipatess(Integer groupid,Integer activityid,Integer insititutionID,List<Participates> list) {
        //先获得顺序的总数，order取top，然后循环的时候插入++插入。
        Integer last=participatesMapper.getlastDisplaySequence(groupid);
        if(last==null)
        {
            last=0;
        }
        last++;
        for (Participates participants : list) {
            Boolean flag=false;
            Student student=new Student();
            student.setName(participants.getName());
            student.setTelephone(participants.getTelephone());
            student.setIDNumber(participants.getIDNumber());
            student.setEmail(participants.getEmail());
            student.setInstitutionid(insititutionID);
//            student.setRole("7");
            //如果是1，则为本单位，再设置为管理员的instituteId，否则为null
//            if(participants.getInstitutionid()==1){
//                int instituteId = activitiesMapper.selectByActivityId(activityid);//通过活动号查找管理员组织号，新增的ActivitiesMapper方法
//                student.setInstitutionid(instituteId);
//            }else if(participants.getInstitutionid()==-1){
//                student.setInstitutionid(-1);
//            }else {
//                student.setInstitutionid(null);
//            }
            if (studentMapper.check(participants.getIDNumber())!=0) {
                if(participants.getUsername()!=null)
                {//不为空
                    student.setUsername(participants.getUsername());
                }
                else
                {//如果没有用户名，更新的时候会被忽略，插入时在下面专门if-else里面
                    student.setUsername(null);
                }
                if(participants.getPassword()!=null)
                {//不为空
                    String encodePass = participants.getPassword();
                    student.setPassword(ExpertService.sh1(encodePass));
                }
                else
                {//如果没有密码，更新的时候会被忽略
                    student.setPassword(null);
                }
                System.out.println("student表中已经有该选手信息。");
                //更新name和instituteId，如果不是null,新增username和password
                int insert0=studentMapper.updateFROMImport(student);
                if (insert0 > 0) {
                    System.out.println("insert->" + student.getName() + " 的信息更新成功");
                } else {
                    System.out.println("insert->" + student.getName() + " 的信息更新失败");
                }
                flag=true;
            } else {
                if(participants.getUsername()!=null)
                {//不为空
                    student.setUsername(participants.getUsername());
                }
                else
                {//默认用户名为编号
                    System.out.println("participants.getCode()" +participants.getCode()+ " 的信息更新成功");
                    student.setUsername(participants.getCode());
                }
//                BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
                String encodePass;
                if(participants.getPassword()!=null)
                {//不为空
                    encodePass = ExpertService.sh1(participants.getPassword());
                }
                else
                {//默认密码为手机号
                    encodePass = ExpertService.sh1(participants.getTelephone());
                }
                student.setPassword(encodePass);
                //如果没有就插入选手信息
                int insert0 = studentMapper.insertFROMImport(student);
                if (insert0 > 0) {
                    System.out.println("insert->" + student.getName() + " 的信息插入成功");
                    flag=true;
                } else {
                    System.out.println("insert->" + student.getName() + " 的信息插入失败");
                }
            }
            if(flag)//student表里有
            {
                //System.out.println(student);
                //在participants中加入专家
                //先获得顺序的总数，order取top，然后循环的时候插入++插入。
                participants.setStudentID(participatesMapper.getID(participants.getIDNumber()));
                Integer studentID =participatesMapper.getID(participants.getIDNumber());
                Integer groupid_temp=0;
                if(groupid!=0)
                {//组内选手管理
                    participants.setGroupID(groupid);
                    /*if(participants.getDisplaySequence()==null)
                        participants.setDisplaySequence(last);*/
                }
                else if(participants.getGroupID()!=null)//全活动
                {//从活动中导入,且不是groupid=null
                    groupid_temp=participants.getGroupID();
                    /*Integer last_uniq=participatesMapper.getlastDisplaySequence(groupid_temp);
                    System.out.println("name->last_uniq"+last_uniq);
                    if(last_uniq==null)
                    {
                        last_uniq=0;
                    }*/
                    //participants.setDisplaySequence(last_uniq+1);
                }
                participants.setActivityID(activityid);
                Integer pend = participatesMapper.checkByIDandActivityID(studentID, activityid);//返回的是groupid，null有可能是groupid=null也有可能是不存在
                if ((pend != null && pend.equals(groupid)&&groupid!=0&&groupid!=-1)||(pend != null && pend.equals(groupid_temp)&&groupid==0)||(pend != null &&groupid==-1)) {
                    System.out.println("该组已经有选手： " + participants.getName()+"进行更新");
                    //participants.setDisplaySequence(null);//此时不需要更新顺序，只需要更新Code//new:如果display不为空，则更新
                    //if(participants.getCode()!=null)
                    participatesMapper.update_relationship(participants);
                } else {
                    int insert=0;
                    if(participatesMapper.checkGroupIDExists(studentID, activityid)==0)//真的不存在
                    {
                        participants.setID(null); // mark：如果其他地方的导入选手出bug，先检查此处
                        insert= participatesMapper.insert_relationship(participants);
                        last++;
                    }
                    else
                    {//之前groupID为空或者groupID存在，这里还可以区分，activity是应该update还是insert
                        if(participants.getCode()!=null||participants.getDisplaySequence()!=-1||participants.getGroupID()!=-1)
                        {insert= participatesMapper.update_relationship(participants);}
                        else{insert=1;}
                        last++;
                    }
                    if (insert > 0) {
                        System.out.println("insert->" + participants.getName() + " 的信息插入成功");
                    } else {
                        System.out.println("insert->" + participants.getName() + " 的信息插入失败");
                        flag=false;
                    }
                }
                if(flag)
                {//insert score
//                    if(participants.getGroupID()==-1)
//                    {
                        participants.setGroupID(null);
//                    }
                    Integer participatesID =participatesMapper.getPID(participants);
                    HashMap<Integer, String> map = participants.getScoreItemMap() == null ? new HashMap<>() : participants.getScoreItemMap();
                    Set<Integer> keys = map.keySet(); // 遍历键集 得到 每一个键//键是scoreItemID值是分数
                    for (Integer key : keys) {
                        //key 就是键 //获取对应值
                        String value = map.get(key);
                        if(!value.equals(""))
                        {Double value_double;
                            if(value.equals("-1"))
                            {value_double=null;}
                            else
                            {value_double=Double.parseDouble(value);}
                            //scoresMapper.insertScore(0,activityid,participatesID,key,value_double);
                            Scores scores=new Scores();
                            scores.setActivityID(activityid);
                            scores.setParticipantID(participatesID);
                            scores.setScoreItemID(key);
                            scores.setScore(value_double);
                            Integer check=scoresMapper.CheckExists(scores);
                            if(check==0)//insert
                            {
                                scoresMapper.insertScore1(scores);//这里还需要添加同外键update
                            }
                            else//update
                            {
                                scoresMapper.UpdateScore1(scores);//这里还需要添加同外键update
                            }
                        }

                    }
                    //insert content
                    //Integer participatesID =participatesMapper.getPID(participants);
                    HashMap<Integer, String> mapinfo = participants.getInfoItemMap() == null ? new HashMap<>() : participants.getInfoItemMap();
                    Set<Integer> keys_info = mapinfo.keySet(); // 遍历键集 得到 每一个键//键是infoItemID值是content
                    for (Integer key_info : keys_info) {
                        //key 就是键 //获取对应值
                        String value_info = mapinfo.get(key_info);
                        if(!value_info.equals(""))
                        {
                            if(value_info.equals("-1"))
                            {value_info="";}
                            Infos infos=new Infos();
                            infos.setActivityID(activityid);
                            infos.setParticipantID(participatesID);
                            infos.setInfoItemID(key_info);
                            infos.setContent(value_info);
                            Integer check=infosMapper.CheckExists(infos);
                            if(check==0)//insert
                            {
                                infosMapper.insertScore1(infos);//这里还需要添加同外键update
                            }
                            else//update
                            {
                                infosMapper.UpdateScore1(infos);//这里还需要添加同外键update
                            }
                        }
                    }

                }
            }

        }
        return "operate successfully";
    }

    public String alterDisplay(Integer total,Integer groupID,Participates NewPar) {
        //Participates old=participatesMapper.getEmployeeById(company.getID());
        Integer maxDisplaySequence = total;
        if (NewPar.getID() != null
                && NewPar.getDisplaySequence() > 0) {
            Participates old=participatesMapper.getEmployeeById(NewPar.getID());
            if (old.getDisplaySequence() == null)
                old.setDisplaySequence(0);

            // id确实存在
            if (old != null) {
                // 显示顺序没有变化
                if (old.getDisplaySequence() == NewPar
                        .getDisplaySequence())
                    return "success";
                // 修改的显示序号不能大于最大的显示序号
                if (NewPar.getDisplaySequence() <= maxDisplaySequence) {
                    if (NewPar.getDisplaySequence() > old.getDisplaySequence()) {
                        // 修正显示顺序
                        participatesMapper.subDisplaySequence(groupID,old.getDisplaySequence(),NewPar
                                .getDisplaySequence());
                        // 保存
                        participatesMapper.saveDisplaySequence(groupID,NewPar.getDisplaySequence(),NewPar.getID());
                        return "success";
                    }
                    if (NewPar.getDisplaySequence() < old.getDisplaySequence()) {
                        // 修正显示顺序
                        participatesMapper.addDisplaySequence(groupID,NewPar.getDisplaySequence(),old
                                .getDisplaySequence());
                        // 保存
                        participatesMapper.saveDisplaySequence(groupID,NewPar.getDisplaySequence(),NewPar.getID());
                        return "success";
                    }
                }
            }
        }
        return "false";
    }

    @Transactional
    public Integer deleteParticipant(Integer groupID,Participates par) {
        participatesMapper.delete(par);
        updateParCount(par.getActivityID(),groupID);
        int result = participatesMapper.deletePar(par.getID(),par.getActivityID());
        // 不在学生表中删除
//        if (participatesMapper.existPar(par.getStudentID()) == null){   // 该选手可能有其他活动，只有在选手表内无该选手时候，才能在student表删除
//            studentMapper.deleteStudent(par.getStudentID());
//        }
        if(result>0)
        {
            if (groupID != -1)
                participatesMapper.subDisplaySequence(groupID,par.getDisplaySequence(),null);
            return 1;
        }
        return 0;
    }

    public Integer updateActivities(Participates company) {
        int result = participatesMapper.update(company);
        return result;
    }

    public Integer updatescoreitem(Participates company) {
        HashMap<Integer, ScoreItemValue> mapinfo =company.getScoremap();
        //HashMap<Integer, String> mapinfo = participants.getInfoItemMap();
        Scores scores=new Scores();
        scores.setActivityID(company.getActivityID());
        scores.setParticipantID(company.getID());
        Set<Integer> keys_info = mapinfo.keySet(); // 遍历键集 得到 每一个键//键是infoItemID值是content
        for (Integer key_info : keys_info) {
            //key 就是键 //获取对应值
            ScoreItemValue value_info = mapinfo.get(key_info);
            if(value_info.getScore()!=null)
            {
                scores.setScoreItemID(value_info.getId());
                scores.setScore(value_info.getScore());
                Integer check=scoresMapper.CheckExists(scores);
                if(check==0)//insert
                {
                    scoresMapper.insertScore1(scores);//这里还需要添加同外键update
                }
                else//update
                {
                    scoresMapper.UpdateScore1(scores);//这里还需要添加同外键update
                }
            }
        }
        return 1;
    }

    public Integer updateinfoitem(Participates participates) {
        HashMap<Integer, InfoItemValue> mapinfo =participates.getInfomap();
        //HashMap<Integer, String> mapinfo = participants.getInfoItemMap();
        Infos infos=new Infos();
        infos.setActivityID(participates.getActivityID());
        infos.setParticipantID(participates.getID());
        Set<Integer> keys_info = mapinfo.keySet(); // 遍历键集 得到 每一个键//键是infoItemID值是content
        for (Integer key_info : keys_info) {
            //key 就是键 //获取对应值
            InfoItemValue value_info = mapinfo.get(key_info);
            if(value_info.getContent()!=null)
            {
                infos.setInfoItemID(value_info.getId());
                infos.setContent(value_info.getContent());
                Integer check=infosMapper.CheckExists(infos);
                if(check==0)//insert
                {
                    infosMapper.insertScore1(infos);//这里还需要添加同外键update
                }
                else//update
                {
                    infosMapper.UpdateScore1(infos);//这里还需要添加同外键update
                }
            }
        }
        return 1;
    }

    public RespPageBean searchActivities(Integer page, Integer size,String company) {
        if (page != null && size != null) {
            page = (page - 1) * size;
        }
        List<Participates> data = participatesMapper.getActivitiesByPageAndCompany(page, size, company);
        Long total = participatesMapper.getTotalByCompany(company);
        RespPageBean bean = new RespPageBean();
        bean.setData(data);
        bean.setTotal(total);
        return bean;
    }
    public Participates getEmployeeById(Integer empId) {
        return participatesMapper.getEmployeeById(empId);
    }

    public RespPageBean getparticipantsByPage(Integer groupID,Integer page, Integer size, Participates employee, Date[] beginDateScope) {
        if (page != null && size != null) {
            page = (page - 1) * size;
        }
        List<Participates> data = participatesMapper.getPByGID(page, size,groupID, employee);
        //Long total = participatesMapper.getTotaloutput(employee, beginDateScope);
        for(Participates participates:data)
        {
            List<ScoreItemValue> scoreItemValueList=scoreItemMapper.getScoreTotalValue(participates.getActivityID(),participates.getID());
            String totalscorewithdot= new String();
            for(ScoreItemValue scoreItemValue:scoreItemValueList)
            {
                if(scoreItemValue.getScore()!=null)
                {
                    DecimalFormat decimalFormat = new DecimalFormat("###################.###########");
                totalscorewithdot=totalscorewithdot+decimalFormat.format(scoreItemValue.getScore())+";";
                }
            }
            participates.setTotalscorewithdot(totalscorewithdot);
        }

        RespPageBean bean = new RespPageBean();
        bean.setData(data);
        //bean.setTotal(total);
        return bean;
    }

    public String getTotalscorewithdot(Integer activityID,Integer participateID)  {
        List<ScoreItemValue> scoreItemValueList=scoreItemMapper.getScoreTotalValue(activityID,participateID);
        String totalscorewithdot= new String();
        for(ScoreItemValue scoreItemValue:scoreItemValueList)
        {
            if(scoreItemValue.getScore()!=null)
            {
                DecimalFormat decimalFormat = new DecimalFormat("###################.###########");
                totalscorewithdot=totalscorewithdot+decimalFormat.format(scoreItemValue.getScore())+";";
            }
        }
        return totalscorewithdot;
    }

    public RespPageBean getAc_participantsByPage(Integer acID,Integer page, Integer size, Date[] beginDateScope) {
        List<Participates> data = participatesMapper.getPByACID(page, size,acID);
        for(Participates participates:data)
        {
            String totalscorewithdot=getTotalscorewithdot(participates.getActivityID(),participates.getID());
            participates.setTotalscorewithdot(totalscorewithdot);
        }
        RespPageBean bean = new RespPageBean();
        bean.setData(data);
        //bean.setTotal(total);
        return bean;
    }

    public List<Participates> getParticipantsByGroupId(Integer activitiesID,Integer groupId) {
        List<Participates> participants=participatesMapper.getPartByGroupId(activitiesID,groupId);
        return participants;
    }

    public List<Participates> selectAllParticipants(Integer activityId,Integer groupId){
        List<Participates>  ParticipantsList=participatesMapper.selectAllParticipants(activityId,groupId);
        return ParticipantsList;
    }

    public Integer saveScore(Integer participateId, Double score) {
        Integer i= participatesMapper.saveScore(participateId,score);
        return i;
    }

    public void saveLscore(Integer participantID,Double sum){
        participatesMapper.saveLscore(participantID,sum);
    }

    public Integer saveAvgscore(Integer participantID,Integer activityID){
        return participatesMapper.saveAvgscore(participantID,activityID);
    }
    public Integer saveAvgScores(Integer participantID,Integer activityID){
        return participatesMapper.saveAvgScores(participantID,activityID);
    }
    public Participates getParticipantIDByIDNumber(Integer activityID,String IDNumber){
        return participatesMapper.getParticipantIDByIDNumber(activityID,IDNumber);
    }
    public String deleteGroups(Integer activityID){
        Integer res = groupsMapper.isGroupsExit(activityID);
        if(res > 0){
            participatesMapper.deleteGroups(activityID);//删除groups表中的数据
            participatesMapper.deleteGroupsOfParticipantsAnaAcitivity(activityID);//删除选手表和活动表中的groupid
            return "删除成功";
        }else {
            return "无删除数据";
        }
    }
    //更新选手的分组id
    public void updateGroupID(Integer activityID,Integer groupID,List<Integer> parID){
        participatesMapper.updateGroupID(activityID,groupID,parID);
    }
    public List<Participates> getParticipantIDByStudentID(Integer studentID){
        return participatesMapper.getParticipantsBySID(studentID);
    }

    @Transactional
    public void updateParCount(Integer activityID, Integer groupID){
        participatesMapper.updateGroupParCount(groupID);
        participatesMapper.updateActParCount(activityID);
    }
}
