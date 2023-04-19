package org.sys.rate.model;

import java.sql.Timestamp;
import java.util.List;

/**
 * 论文成果对象 paper
 *
 * @author system
 * @date 2022-03-13
 */
public class Paper extends Productions {
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Long ID;

    /**
     * 学号
     */
    private Long studentID;

    /**
     * 论文题目
     */
    private String name;

    /**
     * 出版年
     */
    private String year;

    /**
     * 出版月
     */
    private Long month;
    private String author;

    /**
     * 排名
     */
    private Long rank;

    /**
     * 总人数
     */
    private Long total;

    /**
     * 积分
     */
    private Long point;

    // 用于判断2积分论文是否生效
    private Integer have_score;

    public Integer getHave_score() {
        return have_score;
    }

    public void setHave_score(Integer have_score) {
        this.have_score = have_score;
    }

    /**
     * 刊物id
     */
    private Long publicationID;

    //    private String content;
    private Timestamp time;//提交时间

    //    private List<String> remark;
    private String remark;
    private String url;
    private String state;
    private String pubPage;
    private Publication publication;
    private List<PaperOper> paperoperList;
    private Student student;
    //    private String stuname;
    private String pubName;
    private Long pubid;

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public Timestamp getTime() {
        return time;
    }

    public List<PaperOper> getPaperoperList() {
        return paperoperList;
    }

    public void setPaperoperList(List<PaperOper> paperoperList) {
        this.paperoperList = paperoperList;
    }

    public Publication getPublication() {
        return publication;
    }

    public void setPublication(Publication publication) {
        this.publication = publication;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

//    public String getContent() {
//        return content;
//    }

    //    public void setContent(String content) {
//        this.content = content;
//    }
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {

        this.remark = remark;
    }
//    public List<String> getRemark() {
//        return remark;
//    }
//
//    public void setRemark(List<String> remark) {
//
//        this.remark = remark;
//    }
//    public String getStuname() {
//        return stuname;
//    }

    //    public void setStuname(String stuname) {
//        this.stuname = stuname;
//    }
    public String getPubName() {
        return pubName;
    }

    public void setPubName(String pubname) {
        this.pubName = pubname;
    }

    @Override
    public String getAuthor() {
        return author;
    }

    @Override
    public void setAuthor(String author) {
        this.author = author;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getpubPage() {
        return pubPage;
    }

    public void setpubPage(String pubPage) {
        this.pubPage = pubPage;
    }

    @Override
    public String getState() {
        return state;
    }

    @Override
    public void setState(String state) {
        this.state = state;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public Long getPubid() {
        return pubid;
    }

    public void setPubid(Long pubid) {
        this.pubid = pubid;
    }

    public Long getID() {
        return ID;
    }

    @Override
    public void setStudentID(Long studentID) {
        this.studentID = studentID;
    }

    @Override
    public Long getStudentID() {
        return studentID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public void setYear(String year) {
        this.year = year;
    }

    @Override
    public String getYear() {
        return year;
    }

    @Override
    public void setMonth(Long month) {
        this.month = month;
    }

    @Override
    public Long getMonth() {
        return month;
    }

    public void setRank(Long rank) {
        this.rank = rank;
    }

    public Long getRank() {
        return rank;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Long getTotal() {
        return total;
    }

    public void setPoint(Long point) {
        this.point = point;
    }

    public Long getPoint() {
        return point;
    }

    @Override
    public void setPublicationID(Long publicationID) {
        this.publicationID = publicationID;
    }

    @Override
    public Long getPublicationID() {
        return publicationID;
    }

    public Paper() {
    }

}
