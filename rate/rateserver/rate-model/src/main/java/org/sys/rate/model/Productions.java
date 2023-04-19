package org.sys.rate.model;

/**
 * @projectName: Rate
 * @package: org.sys.rate.model
 * @className: Productions
 * @author: ZYK
 * @description: TODO
 * @date: 2023/4/15 12:41
 * @version: 1.0
 */
public class Productions {
    /** ID */
    private Long ID;

    /** 学号 */
    private Long studentID;

    /** 论文题目 */
    private String name;

    /** 出版年 */
    private String year;

    /** 出版月 */
    private Long month;

    /** 作者 */
    private String author;

    /** 状态 */
    private String state;

    /**
     * 刊物id
     */
    private Long publicationID;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Long getPublicationID() {
        return publicationID;
    }

    public void setPublicationID(Long publicationID) {
        this.publicationID = publicationID;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public Long getStudentID() {
        return studentID;
    }

    public void setStudentID(Long studentID) {
        this.studentID = studentID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public Long getMonth() {
        return month;
    }

    public void setMonth(Long month) {
        this.month = month;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
