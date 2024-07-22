package org.sys.rate.model;

import lombok.Data;

import java.util.List;

//专家活动表
@Data
public class Expertactivities {
    private Integer id;

    private Integer teacherID;

    private Integer activityid;

    private Integer groupid;

    private Boolean finished;
    private String role;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getExpertid() {
        return teacherID;
    }

    public void setExpertid(Integer expertid) {
        this.teacherID = expertid;
    }

    public Integer getActivityid() {
        return activityid;
    }

    public void setActivityid(Integer activityid) {
        this.activityid = activityid;
    }

    public Integer getGroupid() {
        return groupid;
    }

    public void setGroupid(Integer groupid) {
        this.groupid = groupid;
    }

    public Boolean getFinished() {
        return finished;
    }

    public void setFinished(Boolean finished) {
        this.finished = finished;
    }
}
