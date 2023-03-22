package org.sys.rate.model;

import lombok.Data;

//活动总分计算项表
@Data
public class TotalItem {
    private Integer ID;

    private Integer activityID;

    private String infoItemIDs;

    private Integer addParticipantScore;

    private Integer[] infos;

    private String[] infoName;

    private String totalItemIDs;

    private Integer[] total;

    private String[]calcName;

    private Integer displaySequence;

    private  String name;

    private Integer fullScore;

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public Integer getActivityID() {
        return activityID;
    }

    public void setActivityID(Integer activityID) {
        this.activityID = activityID;
    }

}
