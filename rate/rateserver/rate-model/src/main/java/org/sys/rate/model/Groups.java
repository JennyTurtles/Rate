package org.sys.rate.model;

public class Groups {

    private Integer expertCount;
    private Integer participantCount;
    private Integer activityID;
    private Integer ID;
    private String name;
    private Integer parentID;

    @Override
    public String toString() {
        return "Institution{" +
                ", ID=" + ID +
                ", activityID='" + activityID + '\'' +
                ", participantCount='" + participantCount +
                ", expertCount=" + expertCount +
                ", name='" + name + '\'' +
                '}';
    }


    public Integer getParentID() {
        return parentID;
    }

    public void setParentID(Integer parentID) {
        this.parentID = parentID;
    }

    public Integer getExpertCount() {
        return expertCount;
    }

    public void setExpertCount(Integer expertCount) {
        this.expertCount = expertCount;
    }


    public Integer getactivityID() {
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

    public Integer getParticipantCount() {
        return participantCount;
    }

    public void setParticipantCount(Integer participantCount) {
        this.participantCount = participantCount;
    }

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public Groups() {
    }

    public Groups(Integer activityID, Integer parentID, String name) {
        this.participantCount = 0;
        this.expertCount = 0;
        this.activityID = activityID;
        this.name = name;
        this.parentID = parentID;
    }
}
