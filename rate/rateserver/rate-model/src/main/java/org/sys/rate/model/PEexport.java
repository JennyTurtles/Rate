package org.sys.rate.model;

import lombok.Data;

import java.util.HashMap;
import java.util.List;

@Data
public class PEexport {
    private String expertName;
    private Integer expertID;
    private String groupName;
    private Integer groupID;
    private String participantName;
    private Integer participantID;
    private String scoreItemName;
    private Integer scoreItemID;
    private Double score;
    private String code;
    private Integer displaySequence;
    private HashMap<Integer, String> map;//评分项，名字和id
    private HashMap<Integer, String> Gmap;
    private HashMap<Integer, String> Emap;
    private HashMap<Integer, String> Pmap;


    public String getExpertName() {
        return expertName;
    }

    public void setExpertName(String expertName) {
        this.expertName = expertName == null ? null : expertName.trim();
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String expertName) {
        this.groupName = expertName == null ? null : expertName.trim();
    }

    public Integer getExpertID() {
        return expertID;
    }

    public void setExpertID(Integer eID) {
        this.expertID = eID;
    }

    public Integer getParticipantID() {
        return participantID;
    }

    public void setParticipantID(Integer participantID) {
        this.participantID = participantID;
    }

    public Integer getGroupID() {
        return groupID;
    }

    public void setGroupID(Integer groupID) {
        this.groupID = groupID;
    }

    public Integer getScoreItemID() {
        return scoreItemID;
    }

    public void setScoreItemID(Integer scoreItemID) {
        this.scoreItemID = scoreItemID;
    }

    public String getScoreItemName() {
        return scoreItemName;
    }

    public void setScoreItemName(String scoreItemName) {
        this.scoreItemName = scoreItemName == null ? null : scoreItemName.trim();
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double scoreItemName) {
        this.score = scoreItemName;
    }

    public String getParticipantName() {
        return participantName;
    }

    public void setParticipantName(String scoreItemName) {
        this.participantName = scoreItemName == null ? null : scoreItemName.trim();
    }

    /*public List<Participates> getParticipatesList() {
        return participatesList;
    }

    public void setParticipatesList(List<Participates> participatesList) {
        this.participatesList = participatesList;
    }

    public HashMap<Integer, String> getMap() {
        return map;
    }

    public void setMap(HashMap<Integer, String> map) {
        this.map = map;
    }*/

}
