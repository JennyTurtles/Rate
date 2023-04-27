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
    //private Boolean deleteFlag;
    private String status;
    private Institution institution;
    private Integer groupID;
    private String groupName;
    private Integer parentID;
    private Integer requireGroup;
    private Integer haveSub;
    private Integer adminID;

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
