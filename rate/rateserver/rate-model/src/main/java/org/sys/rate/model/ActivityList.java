package org.sys.rate.model;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ActivityList {
    private String groupName;
    private String ActName;
    private List<Activities> activityLists;
    private Integer activityID;
    private Integer groupId;

    public List<Activities> getScoreitemList() {
        return activityLists;
    }

    public void setScoreitemList(List<Activities> activityLists) {
        this.activityLists = activityLists;
    }
}
