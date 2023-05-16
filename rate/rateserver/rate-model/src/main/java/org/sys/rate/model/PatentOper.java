package org.sys.rate.model;

import java.sql.Timestamp;

public class PatentOper {
//    private static final long serialVersionUID = 1L;

    /** ID */
    private Long ID;

    private String operatorRole;
    private Long operatorID;
    private String operatorName;
    private Long patentID;
    /** 论文题目 */
    private String patentName;
    private int patentTypeID;
    private int patentTypeName;
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

    public Long getPatentID() {
        return patentID;
    }

    public void setPatentID(Long patentID) {
        this.patentID = patentID;
    }

    public String getPatentName() {
        return patentName;
    }

    public void setPatentName(String patentName) {
        this.patentName = patentName;
    }

    public int getPatentTypeID() {
        return patentTypeID;
    }

    public void setPatentTypeID(int patentTypeID) {
        this.patentTypeID = patentTypeID;
    }

    public int getPatentTypeName() {
        return patentTypeName;
    }

    public void setPatentTypeName(int patentTypeName) {
        this.patentTypeName = patentTypeName;
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
