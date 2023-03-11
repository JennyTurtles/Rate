package org.sys.rate.model;

public class ScoreDetail {
    private Integer activityID;
    private Integer groupID;
    private String activityname;
    private String groupname;
    private Double maxscore;
    private Double minscore;
    private Double avescore;
    private Double varscore;

    public Integer getActivityID() {
        return activityID;
    }

    public void setActivityID(Integer activityID) {
        this.activityID = activityID;
    }

    public Integer getGroupID() {
        return groupID;
    }

    public String getActivityname() {
        return activityname;
    }

    public void setActivityname(String activityname) {
        this.activityname = activityname;
    }

    public void setGroupID(Integer groupID) {
        this.groupID = groupID;
    }

    public String getGroupname() {
        return groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }

    public Double getMaxscore() {
        return maxscore;
    }

    public void setMaxscore(Double maxscore) {
        this.maxscore = maxscore;
    }

    public Double getMinscore() {
        return minscore;
    }

    public void setMinscore(Double minscore) {
        this.minscore = minscore;
    }

    public Double getAvescore() {
        return avescore;
    }

    public void setAvescore(Double avescore) {
        this.avescore = avescore;
    }

    public Double getVarscore() {
        return varscore;
    }

    public void setVarscore(Double varscore) {
        this.varscore = varscore;
    }
}
