package org.sys.rate.model;


import java.util.List;

/**
 * 著作对象 patent
 *
 * @author system
 * @date 2022-03-13
 */
public class Patent extends Productions
{
    private static final long serialVersionUID = 1L;

    private Long ID ;
    private Long studentID ;
    private String typename ;
    private Long month;
    private int rank;
    private String rights;
    private String patentee;
    private int point ;
    private String state;
    private String url;

    private int total;

    private int patentTypeID;

    private Student student;
    private Publication publication;
    private PatentType patentType;
    private List<PatentOper> patentList;

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

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPatentTypeID() {
        return patentTypeID;
    }

    public void setPatentTypeID(int patentTypeID) {
        this.patentTypeID = patentTypeID;
    }

    public PatentType getPatentType() {
        return patentType;
    }

    public void setPatentType(PatentType patentType) {
        this.patentType = patentType;
    }

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Long getID() {
        return ID;
    }

    @Override
    public void setID(Long ID) {
        this.ID = ID;
    }

    @Override
    public Long getStudentID() {
        return studentID;
    }

    @Override
    public void setStudentID(Long studentID) {
        this.studentID = studentID;
    }

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }

    @Override
    public Long getMonth() {
        return month;
    }

    @Override
    public void setMonth(Long month) {
        this.month = month;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getRights() {
        return rights;
    }

    public void setRights(String rights) {
        this.rights = rights;
    }

    public String getPatentee() {
        return patentee;
    }

    public void setPatentee(String patentee) {
        this.patentee = patentee;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Publication getPublication() {
        return publication;
    }

    public void setPublication(Publication publication) {
        this.publication = publication;
    }

    public List<PatentOper> getPatentList() {
        return patentList;
    }

    public void setPatentList(List<PatentOper> patentList) {
        this.patentList = patentList;
    }
}
