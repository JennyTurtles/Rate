package org.sys.rate.model;

import lombok.Data;

//评分项表
@Data
public class InfoItem {
    private Integer ID;
    private Integer participantID;
    private Integer activityID;

    private String name;

    private String sizelimit;

    private String contentType;
    private String[] shuZuType;

    private Boolean byParticipant;

    private Boolean display;
    private String content;

    public String getContent() {
        return content;
    }

    public Integer getParticipantID() {
        return participantID;
    }

    public void setParticipantID(Integer participantID) {
        this.participantID = participantID;
    }

    public void setContent(String content) {
        this.content = content;
    }

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getSizelimit() {
        return sizelimit;
    }

    public void setSizelimit(String sizelimit) {
        this.sizelimit = sizelimit;
    }

    public Boolean getByParticipant() {
        return byParticipant;
    }

    public void setByParticipant(Boolean byParticipant) {
        this.byParticipant = byParticipant;
    }

    public Boolean getDisplay() {
        return display;
    }

    public void setDisplay(Boolean display) {
        this.display = display;
    }

}
