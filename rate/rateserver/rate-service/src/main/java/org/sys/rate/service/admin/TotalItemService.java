package org.sys.rate.service.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.sys.rate.mapper.InfoItemMapper;
import org.sys.rate.mapper.ScoreItemMapper;
import org.sys.rate.mapper.TotalItemMapper;
import org.sys.rate.model.InfoItem;
import org.sys.rate.model.RespPageBean;
import org.sys.rate.model.TotalItem;

import java.util.List;

@Service
public class TotalItemService {
    @Autowired
    TotalItemMapper totalItemMapper;
    @Autowired
    ScoreItemMapper scoreItemMapper;
    @Autowired
    InfoItemMapper infoItemMapper;

    public RespPageBean getByActivityID(Integer activityID) {
        List<TotalItem> data = totalItemMapper.findbyActivityID(activityID);
        for (TotalItem solo : data) {
            if (solo.getInfoSumIDs() != null) {
                String[] info = solo.getInfoSumIDs().split(",");
                String[] name = new String[info.length];
                Integer count = 0;
                for (String s : info){
                    Integer value = Integer.valueOf(s);
                    name[count++] = infoItemMapper.getNameByID(value);
                }
                solo.setInfos(name);
            }
            if (solo.getScoreSumIDs() != null) {
                String[] score = solo.getScoreSumIDs().split(",");
                String[] name = new String[score.length];
                Integer count = 0;
                for (String s : score){
                    Integer value = Integer.valueOf(s);
                    name[count++] = scoreItemMapper.getNameByID(value);
                }
                solo.setScores(name);
            }
        }
        Long total = totalItemMapper.getTotal(activityID);
        RespPageBean bean = new RespPageBean();
        bean.setTotal(total);
        bean.setData(data);
        return bean;
    }
}
