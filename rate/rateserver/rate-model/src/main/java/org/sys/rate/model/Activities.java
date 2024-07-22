package org.sys.rate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

import java.util.List;

@Data
public class Activities {

    private Integer id;
    private Integer institutionID;
    private String name;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startDate;
    private  Double score;
    private  Integer scoreItemCount;
    private Integer groupCount;
    private Integer expertCount;
    private Integer participantCount;
    private String comment;
    private Boolean deleteFlag;
    private String status;
    private Institution institution;
    private Integer groupID;
    private String groupName;
    private Integer parentID;
    private String parentName;//父活动名字
    private Integer requireGroup;
    private Integer haveSub;
    private boolean isSub; // 是否为子活动
    private Integer adminID;//这个应该是和管理员-活动表相关联
    private Integer creatorID;//活动创建者id
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date enterDate;//专家能进入活动打分页面的时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date visibleDate;//专家能看到该活动的时间
    private Integer scoreSetByself; //成绩查看设置方式
    private Integer gradeFormType;


    public void cleanCount(){
        this.groupCount = 0;
        this.expertCount = 0;
        this.participantCount = 0;
    }

    public void fillNewInfo(Activities newAct){
        this.name = newAct.getName() != null ? newAct.getName() : this.name;
        this.startDate = newAct.getStartDate() != null ? newAct.getStartDate() : this.startDate;
        this.comment = newAct.getComment() != null ? newAct.getComment() : this.comment;
        this.enterDate = newAct.getEnterDate() != null ? newAct.getEnterDate() : this.enterDate;
        this.visibleDate = newAct.getVisibleDate() != null ? newAct.getVisibleDate() : this.visibleDate;
        this.parentID = newAct.getParentID() != null ? newAct.getParentID() : this.parentID;
        this.adminID = newAct.getAdminID() != null ? newAct.getAdminID() : this.adminID;
    }

    public Integer getGradeFormType() {
        return gradeFormType;
    }

    public void setGradeFormType(Integer gradeFormType) {
        this.gradeFormType = gradeFormType;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public Integer getAdminID() {
        return adminID;
    }

    public void setAdminID(Integer adminID) {
        this.adminID = adminID;
    }
    private Integer haveComment;

    public Integer getRequireGroup() {
        return requireGroup;
    }

    public void setRequireGroup(Integer requireGroup) {
        this.requireGroup = requireGroup;
    }

    public Integer getHaveComment() {
        return haveComment;
    }

    public void setHaveComment(Integer haveComment) {
        this.haveComment = haveComment;
    }

    public Integer getHaveSub() {
        return haveSub;
    }

    public void setHaveSub(Integer haveSub) {
        this.haveSub = haveSub;
    }

    public Integer getGroupID() {
        return groupID;
    }

    public void setGroupID(Integer groupID) {
        this.groupID = groupID;
    }
    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    private List<ScoreItem> scoreitemList;

    public List<ScoreItem> getScoreitemList() {
        return scoreitemList;
    }

    public void setScoreitemList(List<ScoreItem> scoreitemList) {
        this.scoreitemList = scoreitemList;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Institution getInstitution() {
        return institution;
    }

    public void setInstitution(Institution institution) {
        this.institution = institution;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public Integer getinstitutionID() {
        return institutionID;
    }

    public void setinstitutionID(Integer institutionID) {
        this.institutionID = institutionID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public Integer getScoreItemCount() {
        return scoreItemCount;
    }

    public void setScoreItemCount(Integer scoreItemCount) {
        this.scoreItemCount = scoreItemCount;
    }


    public Integer getGroupCount() {
        return groupCount;
    }

    public void setGroupCount(Integer groupCount) {
        this.groupCount = groupCount;
    }

    public Integer getExpertCount() {
        return expertCount;
    }

    public void setExpertCount(Integer expertCount) {
        this.expertCount = expertCount;
    }

    public Integer getParticipantCount() {
        return participantCount;
    }

    public void setParticipantCount(Integer participantCount) {
        this.participantCount = participantCount;
    }


    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    /*public void setDeleteFlag(Boolean deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public boolean isDeleteFlag() {
        return deleteFlag;
    }*/

    @Override
    public String toString() {
        return "Activities{" +
                "id=" + id +
                ", institutionID=" + institutionID +
                ", name='" + name + '\'' +
                ", startDate=" + startDate +
                ", score=" + score +
                ", scoreItemCount=" + scoreItemCount +
                ", groupCount=" + groupCount +
                ", expertCount=" + expertCount +
                ", participantCount=" + participantCount +
                ", comment='" + comment + '\'' +
                ", status='" + status + '\'' +
                ", institution=" + institution +
                '}';
    }
}
