package org.sys.rate.model;

import java.util.List;

public class IndicatorPublication {
    private Integer ID;
    private String name;
    private String abbr;
    private String publisher;
    private String url;
    private String level;
    private Integer indicatorID;
    private Integer year;
    private List<Integer> indicatorIDs;
    private List<String> indicatorNames;
    private String indicatorName;

    public IndicatorPublication() {
    }

    public IndicatorPublication(Integer ID, String name, String abbr, String publisher, String url, String level, Integer indicatorID,Integer year,List<Integer> indicatorIDs,List<String> indicatorNames,String indicatorName) {
        this.ID = ID;
        this.name = name;
        this.abbr = abbr;
        this.publisher = publisher;
        this.url = url;
        this.level = level;
        this.indicatorID = indicatorID;
        this.year = year;
        this.indicatorIDs = indicatorIDs;
        this.indicatorNames = indicatorNames;
        this.indicatorName = indicatorName;
    }

    public String getIndicatorName() {
        return indicatorName;
    }

    public void setIndicatorName(String indicatorName) {
        this.indicatorName = indicatorName;
    }

    public List<String> getIndicatorNames() {
        return indicatorNames;
    }

    public void setIndicatorNames(List<String> indicatorNames) {
        this.indicatorNames = indicatorNames;
    }

    public List<Integer> getIndicatorIDs() {
        return indicatorIDs;
    }

    public void setIndicatorIDs(List<Integer> indicatorIDs) {
        this.indicatorIDs = indicatorIDs;
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

    public String getAbbr() {
        return abbr;
    }

    public void setAbbr(String abbr) {
        this.abbr = abbr;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public Integer getIndicatorID() {
        return indicatorID;
    }

    public void setIndicatorID(Integer indicatorID) {
        this.indicatorID = indicatorID;
    }

    @Override
    public String toString() {
        return "IndicatorPublication{" +
                "ID=" + ID +
                ", name='" + name + '\'' +
                ", abbr='" + abbr + '\'' +
                ", publisher='" + publisher + '\'' +
                ", url='" + url + '\'' +
                ", level='" + level + '\'' +
                ", indicatorID=" + indicatorID +
                ", year=" + year +
                '}';
    }
}