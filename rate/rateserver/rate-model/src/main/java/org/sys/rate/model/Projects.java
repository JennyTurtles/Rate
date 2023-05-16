package org.sys.rate.model;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

//项目
public class Projects {
    private Integer ID;
    private String name;
    private Integer studentID;
    private String source;
    private String sub;
    private String head;
    private String division;
    private Integer year;
    private String way;
    private String starttime;
    private String endtime;
    private Integer projectTypeId;
    private String state;
    private String url;

    private Timestamp time;//提交时间
    private Projectstype Type;
    private List<ProjectsOper> projectsOperList;
    private Student student;

    private String stuName;

    private String zcName;

    private String sState;

    private String sScore;
    private String eScore;

    public String getStuName() {
        return stuName;
    }

    public void setStuName(String stuName) {
        this.stuName = stuName;
    }

    public String getZcName() {
        return zcName;
    }

    public void setZcName(String zcName) {
        this.zcName = zcName;
    }

    public String getsState() {
        return sState;
    }

    public void setsState(String sState) {
        this.sState = sState;
    }

    public String getsScore() {
        return sScore;
    }

    public void setsScore(String sScore) {
        this.sScore = sScore;
    }

    public String geteScore() {
        return eScore;
    }

    public void seteScore(String eScore) {
        this.eScore = eScore;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public Projectstype getType() {
        return Type;
    }

    public void setType(Projectstype type) {
        Type = type;
    }

    public List<ProjectsOper> getProjectsOperList() {
        return projectsOperList;
    }

    public void setProjectsOperList(List<ProjectsOper> projectsOperList) {
        this.projectsOperList = projectsOperList;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Integer getStudentID() {
        return studentID;
    }

    public void setStudentID(Integer studentID) {
        this.studentID = studentID;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSub() {
        return sub;
    }

    public void setSub(String sub) {
        this.sub = sub;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getWay() {
        return way;
    }

    public void setWay(String way) {
        this.way = way;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public Integer getTypeId() {
        return projectTypeId;
    }

    public void setTypeId(Integer projectTypeId) {
        this.projectTypeId = projectTypeId;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public Integer getProjectTypeId() {
        return projectTypeId;
    }

    public void setProjectTypeId(Integer projectTypeId) {
        this.projectTypeId = projectTypeId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
