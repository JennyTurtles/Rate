package org.sys.rate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;


public class PaperComment {
    /**
     * 毕业论文或设计评论的ID
     */
    private int ID;



    /**
     * 毕业论文或设计的ID
     */
    private int thesisID;
    /**
     * 学生评价上传的时间
     */
    // format="yyyy 年 MM 月 dd 日"
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern="yyyy-MM-dd", timezone = "GMT+8")
    private Date dateStu;
    /**
     * 导师评价上传的时间
     */

    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date dateTea;
    /**
     * 本期工作
     */
    private String preSum;
    /**
     * 下期安排
     */
    private String nextPlan;
    /**
     * 导师评价
     */
    private String tutorComment;

    /**
     * 获取毕业论文或设计的实体
     */
    private Thesis thesis;

    /**
     * 第几次评论
     */
    private int num;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getThesisID() {
        return thesisID;
    }

    public void setThesisID(int thesisID) {
        this.thesisID = thesisID;
    }

    public Date getDateStu() {
        return dateStu;
    }

    public void setDateStu(Date dateStu) {
        this.dateStu = dateStu;
    }

    public Date getDateTea() {
        return dateTea;
    }

    public void setDateTea(Date dateTea) {
        this.dateTea = dateTea;
    }

    public String getPreSum() {
        return preSum;
    }

    public void setPreSum(String preSum) {
        this.preSum = preSum;
    }

    public String getNextPlan() {
        return nextPlan;
    }

    public void setNextPlan(String nextPlan) {
        this.nextPlan = nextPlan;
    }

    public String getTutorComment() {
        return tutorComment;
    }

    public void setTutorComment(String tutorComment) {
        this.tutorComment = tutorComment;
    }

    public Thesis getThesis() {
        return thesis;
    }

    public void setThesis(Thesis thesis) {
        this.thesis = thesis;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public PaperComment(){

    }

}
