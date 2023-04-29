package org.sys.rate.model;

import lombok.Data;

//评分项表
@Data
public class ScoreItem {
    private Integer id;

    private Integer activityid;

    private String name;

    private Double score;

    private Double coef;

    private String comment;

    private Boolean byexpert;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getActivityid() {
        return activityid;
    }

    public void setActivityid(Integer activityid) {
        this.activityid = activityid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public Double getCoef() {
        return coef;
    }

    public void setCoef(Double coef) {
        this.coef = coef;
    }

    public Boolean getByexpert() {
        return byexpert;
    }

    public void setByexpert(Boolean byexpert) {
        this.byexpert = byexpert;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

}
