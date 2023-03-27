package org.sys.rate.service.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.sys.rate.mapper.InfoItemMapper;
import org.sys.rate.mapper.InfosMapper;
import org.sys.rate.mapper.ParticipatesMapper;
import org.sys.rate.model.Participates;
import org.sys.rate.model.RespPageBean;
import org.sys.rate.model.InfoItem;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


@Service
public class InfoItemService {
    @Autowired
    InfoItemMapper infoItemMapper;
    @Autowired
    InfosMapper infosMapper;
    @Autowired
    ParticipatesMapper participatesMapper;
    public final static Logger logger = LoggerFactory.getLogger(InfoItemService.class);
    SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
    SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
    DecimalFormat decimalFormat = new DecimalFormat("##.00");

    public RespPageBean getActivitiesPage(Integer keywords, Integer page, Integer size, InfoItem employee) {
        if (page != null && size != null) {
            page = (page - 1) * size;
        }
        List<InfoItem> data = infoItemMapper.getActivitiesByPage(keywords,page, size, employee);
        for(InfoItem solo:data)
        {
            if(!solo.getContentType().isEmpty())
            {String[] s = solo.getContentType().split(",");
            solo.setShuZuType(s);}
        }
        Long total = infoItemMapper.getTotal(keywords,employee);
        RespPageBean bean = new RespPageBean();
        bean.setData(data);
        bean.setTotal(total);
        return bean;
    }

    public RespPageBean getActivitiesByStudent(Integer keywords, Integer page, Integer size,Integer studentID, InfoItem employee) {
        if (page != null && size != null) {
            page = (page - 1) * size;
        }
        List<InfoItem> data = infoItemMapper.getActivitiesByPage(keywords,page, size, employee);
        Integer participantID = infosMapper.selectStudent(studentID,keywords);
        for(InfoItem solo:data)
        {
            if(solo.getContentType()!=null){
                String[] s = solo.getContentType().split(",");
                solo.setShuZuType(s);
            }
            solo.setContent(infosMapper.selectInfosContent(keywords,participantID,solo.getID()));
        }
        Long total = infoItemMapper.getTotal(keywords,employee);
        RespPageBean bean = new RespPageBean();
        bean.setData(data);
        bean.setTotal(total);
        return bean;
    }

    public Integer addParticipates(InfoItem employee) {
        int result = infoItemMapper.insert(employee);
        /*if(result==1)
            infoItemMapper.insert_update(employee);*/
        return result;
    }

    public Integer getactivityCount(int id) {
        int result = infoItemMapper.getTotalforactivityCount(id);
        return result;
    }

    public Integer insertScoreItem(List<InfoItem> list) {
        for (int i = 0; i < list.size(); i++) {
            infoItemMapper.insert(list.get(i));
        }
        return 1;
    }

    public Integer deleteInfo(InfoItem company) {
        int result = infoItemMapper.delete(company);
        return result;
    }

    public Integer deleteByActivityId(Integer activityId){
        return infoItemMapper.deleteByPrimaryKey(activityId);
    }

    public Integer updateActivities(List<InfoItem> company) {
        for(int i=0;i<company.size();i++)
        {int result = infoItemMapper.update(company.get(i));
        if(result!=1)
            return 0;

        }
        return 1;
    }

    public Integer updateInfoItem(InfoItem company) {

        int result = infoItemMapper.update(company);
            if(result!=1)
                return 0;
        return 1;
    }
    public List<InfoItem> getScoreItemByActivityId(Integer activityId){
        List<InfoItem> scoreitems= infoItemMapper.selectScoreItemByActivityId(activityId);
        return scoreitems;
    }
    public List<InfoItem> selectInfoItemByActivityIdWhereNotByExpert(Integer activityId){
        return infoItemMapper.selectInfoItemByActivityIdWhereNotByExpert(activityId);
    }

    public Integer ScoreItemByActivityIdWhereNotByExpertCount(Integer activityId) {
        return infoItemMapper.ScoreItemByActivityIdWhereNotByExpertCount(activityId);
    }
    //对选手进行筛选
    public List<InfoItem>  getInforItemByActivityId(Integer activitiesID){
        List<Participates> participates = participatesMapper.getParticipantByAId(activitiesID); // 得到所有未分组的选手
        List<InfoItem> infoItems = new ArrayList<>();
        //没有不分组的选手
        if(participates.size() == 0){
            return infoItems;
        }
        List<Integer> participantID = new ArrayList<>();
        for(int i = 0;i < participates.size(); i++){
            participantID.add(participates.get(i).getID());
        }
        infoItems = infoItemMapper.selectInfoItemByActivityId(activitiesID,participantID);
        return infoItems;
    }
    //对选手不筛选，只靠acid获得
    public List<InfoItem>  getInforItemByActivityIdAndPars(Integer activitiesID){
        List<InfoItem> infoItems = infoItemMapper.selectInfoItemsByActivityId(activitiesID);
        return infoItems;
    }
    public List<InfoItem>  getInforItemByActivityIdIsShow(Integer activitiesID){
        List<InfoItem> infoItems = infoItemMapper.selectInfoItemByActivityIdIsShow(activitiesID);
        return infoItems;
    }
    public List<InfoItem>  getInforItemAndContentByActivityIdAndPIdAndInfoId(Integer activitiesID,Integer participantID){
        List<InfoItem> infoItems = infoItemMapper.selectInforItemAndContentByActivityIdAndPIdAndInfoId(activitiesID,participantID);
        return infoItems;
    }
    public List<InfoItem> getAll(Integer activityID){
        List<InfoItem> infoItems = infoItemMapper.selectInfoItemsByActivityId(activityID);
        return infoItems;
    }
}
