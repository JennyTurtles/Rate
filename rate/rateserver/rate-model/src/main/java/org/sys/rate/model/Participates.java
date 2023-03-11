package org.sys.rate.model;

import lombok.Data;

import java.sql.Date;
import java.util.HashMap;

@Data
public class Participates{
    private Integer ID;//student的id
    private String IDNumber;//from stu_IDNumber
    private String studentnumber;//new from stu
    private String telephone;//new from stu
    private String email;//new from stu
    //private Integer institutionID;
    private String name;//from stu
    private  Double score;//p
    private Integer groupID;//p
    private String groupName;//g
    private Integer activityID;//p
    private Integer studentID;//p
    private String code;//p
    private String school;
    private String major;
    private String sex;
    private String examscore;
    private Integer displaySequence;//p
    private String department;//p
    private Integer oldGroupID;
    private String oldgroupname;
    private String username;
    private Student student;
    private Expertactivities expertactivities;
    private String password;
    private HashMap<Integer, String> map;
    private HashMap<Integer, String> map_info;
    private Integer institutionid;//from stu
    private HashMap<Integer, ScoreItemValue> scoremap;
    private HashMap<Integer, InfoItemValue> infomap;
    private String totalscorewithdot;
    /*private Date startDate;

    private  Integer scoreItemCount;
    private Integer groupCount;
    private Integer expertCount;
    private Integer participantCount;
    private String comment;*/
    //private Boolean deleteFlag;
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }
    public Integer getGroupID() {
        return groupID;
    }

    public void setGroupID(Integer groupID) {
        this.groupID = groupID;
    }

    public Integer getActivityID() {
        return activityID;
    }

    public void setActivityID(Integer activityID) {
        this.activityID = activityID;
    }

    public Integer getStudentID() {
        return studentID;
    }

    public void setStudentID(Integer studentID) {
        this.studentID = studentID;
    }

    public String getSchool() { return school; }

    public void setSchool(String shool) {
        this.school = shool;
    }

    public String getStudentNumber() {
        return studentnumber;
    }

    public void setStudentNumber(String studentnumber) {
        this.studentnumber = studentnumber;
    }



    public String getIDNumber() {
        return IDNumber;
    }

    public void setIDNumber(String IDNumber) {
        this.IDNumber = IDNumber;
    }


    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getOldGroupId() {
        return oldGroupID;
    }

    public void setOldGroupId(Integer oldGroupID) {
        this.oldGroupID = oldGroupID;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public Integer getDisplaySequence() {
        return displaySequence;
    }

    public void setDisplaySequence(Integer displaySequence) {
        this.displaySequence = displaySequence;
    }

    public HashMap<Integer, String> getScoreItemMap() {
        return map;
    }

    public void setScoreItemMap(HashMap<Integer, String> map) {
        this.map = map;
    }

    public HashMap<Integer, String> getInfoItemMap() {
        return map_info;
    }

    public void setInfoItemMap(HashMap<Integer, String> map) {
        this.map_info = map;
    }

    public Integer getInstitutionid() {
        return institutionid;
    }

    public void setInstitutionid(Integer institutionid) {
        this.institutionid = institutionid;
    }

    public HashMap<Integer, ScoreItemValue> getScoremap() {
        return scoremap;
    }

    public void setScoremap(HashMap<Integer, ScoreItemValue> map) {
        this.scoremap = map;
    }

    public HashMap<Integer, InfoItemValue> getInfomap() {
        return infomap;
    }

    public void setInfomap(HashMap<Integer, InfoItemValue> map) {
        this.infomap = map;
    }

    public String getTotalscorewithdot() {
        return totalscorewithdot;
    }

    public void setTotalscorewithdot(String totalscorewithdot) {
        this.totalscorewithdot = totalscorewithdot;
    }

}
