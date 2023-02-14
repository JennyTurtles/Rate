package org.sys.rate.model;

import java.sql.Timestamp;

public class PaperOper {
//    private static final long serialVersionUID = 1L;

    /** ID */
    private Long ID;

    private String operatorRole;
    private Long operatorID;
    private String operatorName;
    private Long paperID;
    /** 论文题目 */
    private String paperName;
    private Long pubID;
    private String pubName;

    private Timestamp time;
    private String operation;
    private String state;
    private String remark;


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

    public Long getPaperID() {
        return paperID;
    }
    public void setPaperID(Long paperID) {
        this.paperID = paperID;
    }

    public String getState() {
        return state;
    }
    public void setState(String state) {
        this.state = state;
    }

    public void setID(Long ID)
    {
        this.ID = ID;
    }
    public Long getID()
    {
        return ID;
    }

    public void setPaperName(String paperName)
    {
        this.paperName = paperName;
    }
    public String getPaperName()
    {
        return paperName;
    }

    public void setPubID(Long pubID)
    {
        this.pubID = pubID;
    }
    public Long getPubID()
    {
        return pubID;
    }

    public void setPubName(String pubName)
    {
        this.pubName = pubName;
    }
    public String getPubName()
    {
        return pubName;
    }

    public void setTime(Timestamp time)
    {
        this.time = time;
    }
    public Timestamp getTime()
    {
        return time;
    }

    public void setOperation(String operation)
    {
        this.operation = operation;
    }
    public String getOperation()
    {
        return operation;
    }

    public void setRemark(String remark)
    {
        this.remark = remark;
    }
    public String getRemark()
    {
        return remark;
    }

}
