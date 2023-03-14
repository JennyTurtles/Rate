package org.sys.rate.model;

import lombok.Data;

//评分项表
@Data
public class ScoreItemValue {
    private Integer ID;

    private String name;

    private Double score;

    private Integer expertID;

    private String expertName;

    public Integer getId() {
        return ID;
    }

    public void setId(Integer ID) {
        this.ID = ID;
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

    public Integer getExpertID() {
        return expertID;
    }

    public void setExpertID(Integer expertID) {
        this.expertID = expertID;
    }

}
