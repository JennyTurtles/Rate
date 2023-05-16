package org.sys.rate.model;

import java.sql.Timestamp;

public class AwardOper {
//    private static final long serialVersionUID = 1L;

    /** ID */
    private Long ID;

    private String operatorRole;
    private Long operatorID;
    private String operatorName;
    private Long awardID;
    /** 论文题目 */
    private String awardName;
    private int awardTypeID;
    private int awardTypeName;
    private Timestamp time;
    private String operation;
    private String state;
    private String remark;

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public String getOperatorRole() {
        return operatorRole;
    }

    public void setOperatorRole(String operatorRole) {
        this.operatorRole = operatorRole;
    }

    public Long getOperatorID() {
        return operatorID;
    }

    public void setOperatorID(Long operatorID) {
        this.operatorID = operatorID;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public Long getAwardID() {
        return awardID;
    }

    public void setAwardID(Long awardID) {
        this.awardID = awardID;
    }

    public String getAwardName() {
        return awardName;
    }

    public void setAwardName(String awardName) {
        this.awardName = awardName;
    }

    public int getAwardTypeID() {
        return awardTypeID;
    }

    public void setAwardTypeID(int awardTypeID) {
        this.awardTypeID = awardTypeID;
    }

    public int getAwardTypeName() {
        return awardTypeName;
    }

    public void setAwardTypeName(int awardTypeName) {
        this.awardTypeName = awardTypeName;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
