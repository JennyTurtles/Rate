package org.sys.rate.model;


/**
 * 本科生毕业设计或论文，一个学生可以有多个毕业设计或者论文
 */
public class Thesis {
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
     * 上传材料的地址
     */
    private String url;
    /**
     * 通过ID获取学生
     */
    private Student student;

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
