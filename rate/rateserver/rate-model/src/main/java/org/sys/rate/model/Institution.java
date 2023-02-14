package org.sys.rate.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

public class Institution implements Serializable {
    private Integer id;
    private String company;
    private Integer activityCount;
    private Integer uplimit;
    private String comment;

    @Override
    public String toString() {
        return "Institution{" +
                ", id=" + id +
                ", company='" + company + '\'' +
                ", activityCount='" + activityCount +
                ", uplimit=" + uplimit +
                ", comment='" + comment + '\'' +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public String getCompany() {
        return company;
    }
    public void setCompany(String company) {
        this.company = company;
    }
    public Integer getActivityCount() {
        return activityCount;
    }

    public void setActivityCount(Integer activityCount) {
        this.activityCount = activityCount;
    }

    public Integer getUplimit() {
        return uplimit;
    }

    public void setUplimit(Integer uplimit) {
        this.uplimit = uplimit;
    }
    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }

}
