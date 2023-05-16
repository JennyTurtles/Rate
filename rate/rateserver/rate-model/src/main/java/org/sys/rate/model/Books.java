package org.sys.rate.model;


import java.util.List;

/**
 * 著作对象 book
 *
 * @author system
 * @date 2022-03-13
 */
public class Books
{
    private static final long serialVersionUID = 1L;

    /**  */
    private int ID;

    /** 著作名称 */
    private String name;



    /** 作者 */
    private String member;

    /** 排名 */
    private int rank;

    /** 版次 */
    private int version;

    /** 出版年 */
    private int year;

    /** 出版月 */
    private int month;



    /** 著作还是教材 */
    private String type;

    private int point;

    private String state;

    private String bookTypeID;

    private String url;

    private Student student;

    private Bookstype bookstype;

    private int studentID;

    private List<BookOper> bookList;

    /** 出版社 */
    private String place;

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

    public List<BookOper> getBookList() {
        return bookList;
    }

    public void setBookList(List<BookOper> bookList) {
        this.bookList = bookList;
    }

    public int getStudentID() {
        return studentID;
    }

    public void setStudentID(int studentID) {
        this.studentID = studentID;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMember() {
        return member;
    }

    public void setMember(String member) {
        this.member = member;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getBookTypeID() {
        return bookTypeID;
    }

    public void setBookTypeID(String bookTypeID) {
        this.bookTypeID = bookTypeID;
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

    public Bookstype getBookstype() {
        return bookstype;
    }

    public void setBookstype(Bookstype bookstype) {
        this.bookstype = bookstype;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }
}
