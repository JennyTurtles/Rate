package org.sys.rate.model;

public class IndicatorDecision {
    Integer ID;
    String name;
    Integer indicatorID;

    public IndicatorDecision() {
    }

    public IndicatorDecision(Integer ID, String name, Integer indicatorID) {
        this.ID = ID;
        this.name = name;
        this.indicatorID = indicatorID;
    }

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getIndicatorID() {
        return indicatorID;
    }

    public void setIndicatorID(Integer indicatorID) {
        this.indicatorID = indicatorID;
    }

    @Override
    public String toString() {
        return "AwardType{" +
                "ID=" + ID +
                ", name='" + name + '\'' +
                ", indicatorId=" + indicatorID +
                '}';
    }
}
