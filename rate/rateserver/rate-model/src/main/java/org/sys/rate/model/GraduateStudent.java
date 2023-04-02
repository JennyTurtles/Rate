package org.sys.rate.model;

public class GraduateStudent extends Account{
    private Integer ID;
    private Integer institutionID;
    private String stuNumber;
    private Integer studentID;
    private String year;
    private Integer tutorID;
    private String studentType;
    private String point;


    @Override
    public Integer getID() {
        return ID;
    }

    @Override
    public void setID(Integer ID) {
        this.ID = ID;
    }

    @Override
    public Integer getInstitutionID() {
        return institutionID;
    }

    @Override
    public void setInstitutionID(Integer institutionID) {
        this.institutionID = institutionID;
    }

    public Integer getStudentID() {
        return studentID;
    }

    public void setStudentID(Integer studentID) {
        this.studentID = studentID;
    }

    public String getStuNumber() {
        return stuNumber;
    }

    public void setStuNumber(String stuNumber) {
        this.stuNumber = stuNumber;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public Integer getTutorID() {
        return tutorID;
    }

    public void setTutorID(Integer tutorID) {
        this.tutorID = tutorID;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public String getStudentType() {
        return studentType;
    }

    public void setStudentType(String studentType) {
        this.studentType = studentType;
    }
}
