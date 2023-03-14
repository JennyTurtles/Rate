package org.sys.rate.model;

import lombok.Data;

//活动总分计算项表
@Data
public class TotalItem {
    private Integer ID;

    private Integer activityID;

    private String infoSumIDs;

    private String infoName;

    private String scoreSumIDs;

    private String scoreName;

    private String[] infos;

    private String[] scores;

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
