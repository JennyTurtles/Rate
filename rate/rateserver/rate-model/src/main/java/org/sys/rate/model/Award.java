package org.sys.rate.model;


import java.util.List;

/**
 * 回答对象 award
 *
 * @author system
 * @date 2022-03-13
 */
public class Award
{
    private static final long serialVersionUID = 1L;

    /**  */
    private Long ID;

    /** 成果名称 */
    private String name;

    /** 奖项名称 */
    private String awardname;

    /** 获奖年度 */
    private Long year;

    /** 所有完成人 */
    private String member;

    /** 所属单位 */
    private String unit;

    /** 人数 */
    private Long total;

    /** 排名 */
    private Long rank;

    /** 奖项id */
    private Long awardID;

    private int point;

    private int awardTypeID;
    private String state;
    private String url;

    private int studentID;

    private Student student;

    private Awardtype awardtype;

    private List<AwardOper> awardOperList;

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

    public List<AwardOper> getAwardOperList() {
        return awardOperList;
    }

    public void setAwardOperList(List<AwardOper> awardOperList) {
        this.awardOperList = awardOperList;
    }

    public int getStudentID() {
        return studentID;
    }

    public void setStudentID(int studentID) {
        this.studentID = studentID;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Awardtype getAwardtype() {
        return awardtype;
    }

    public void setAwardtype(Awardtype awardtype) {
        this.awardtype = awardtype;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public int getAwardTypeID() {
        return awardTypeID;
    }

    public void setAwardTypeID(int awardTypeID) {
        this.awardTypeID = awardTypeID;
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

    public void setID(Long ID)
    {
        this.ID = ID;
    }

    public Long getID()
    {
        return ID;
    }
    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }
    public void setAwardname(String awardname)
    {
        this.awardname = awardname;
    }

    public String getAwardname()
    {
        return awardname;
    }
    public void setYear(Long year)
    {
        this.year = year;
    }

    public Long getYear()
    {
        return year;
    }
    public void setMember(String member)
    {
        this.member = member;
    }

    public String getMember()
    {
        return member;
    }
    public void setUnit(String unit)
    {
        this.unit = unit;
    }

    public String getUnit()
    {
        return unit;
    }
    public void setTotal(Long total)
    {
        this.total = total;
    }

    public Long getTotal()
    {
        return total;
    }
    public void setRank(Long rank)
    {
        this.rank = rank;
    }

    public Long getRank()
    {
        return rank;
    }
    public void setAwardID(Long awardID)
    {
        this.awardID = awardID;
    }

    public Long getAwardID()
    {
        return awardID;
    }


}
