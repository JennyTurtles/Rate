package org.sys.rate.model;


import com.baomidou.mybatisplus.annotation.TableField;

/**
 * 本科生毕业设计或论文，一个学生可以有多个毕业设计或者论文
 */
public class Thesis {
    @TableField(exist = true)
    private int comment_pass;

    @TableField(exist = true)
    private int comment_deny;

    @TableField(exist = true)
    private int comment_total;

    public int getComment_pass() {
        return comment_pass;
    }

    public void setComment_pass(int comment_pass) {
        this.comment_pass = comment_pass;
    }

    public int getComment_deny() {
        return comment_deny;
    }

    public void setComment_deny(int comment_deny) {
        this.comment_deny = comment_deny;
    }

    public int getComment_total() {
        return comment_total;
    }

    public void setComment_total(int comment_total) {
        this.comment_total = comment_total;
    }

    /**
     * 毕业论文或设计的ID
     */
    private int ID;
    /**
     * 学生学号
     */
    private int studentID;
    /**
     * 毕业论文或设计的标题
     */
    private String name;
    /**
     * 上传年份
     */
    private int year;
    /**
     * 上传月份
     */
    private int month;
    /**
     * 上传材料的地址
     */
    private String url;
    /**
     * 通过ID获取学生
     */
    private Student student;

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getStudentID() {
        return studentID;
    }

    public void setStudentID(int studentID) {
        this.studentID = studentID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
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

    public Thesis(){

    }
}
