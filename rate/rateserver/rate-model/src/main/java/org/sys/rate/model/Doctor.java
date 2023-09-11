package org.sys.rate.model;

import lombok.Data;

@Data
public class Doctor extends Student{
    private Integer ID;
    private Integer institutionID;
    private String stuNumber;
    private Integer studentID;
    private Integer year;
    private Integer tutorID;
    private String studentType;
    private String point;
    private String specialty;
    private String className;

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

    @Override
    public Integer getYear() {
        return year;
    }

    @Override
    public void setYear(Integer year) {
        this.year = year;
    }

    @Override
    public Integer getTutorID() {
        return tutorID;
    }

    @Override
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
