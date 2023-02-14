package org.sys.rate.model;

public class IndicatorSearchResult {
    private Integer ID;
    private String name;
    private Integer indicatorID;

    public IndicatorSearchResult() {
    }

    public IndicatorSearchResult(Integer ID, String name, Integer indicatorID) {
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
        return "IndicatorSearchResult{" +
                "ID=" + ID +
                ", name='" + name + '\'' +
                ", indicatorID=" + indicatorID +
                '}';
    }
}
