package org.sys.rate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class Student extends Account {
    //按照老师的说法，student表中要丢弃很多内容，所以先把这个类当作一个共同属性类（本科生 研究生 选手）
    //数据库中更改但是这里不能改
    private Integer ID;
//    private Integer sid;

    private String sname;
    private String IDNumber;
    private Integer institutionid;
    private String studentnumber;
    private String telephone;
    private String email;
    private String username;
    private String password;
    private Integer year;
    private Integer tutorID;
    private Teachers teachers;//老师对象
    private String gradType;//专硕还是学硕
    private String stuType;//本科生、研究生、选手
    private String registerQuestion;
    private String registerAnswer;
    private Integer deleteflag;
    private Integer role;
    private Thesis thesis;

    public String getStuType() {
        return stuType;
    }

    public void setStuType(String stuType) {
        this.stuType = stuType;
    }

    public String getRegisterQuestion() {
        return registerQuestion;
    }

    public void setRegisterQuestion(String registerQuestion) {
        this.registerQuestion = registerQuestion;
    }

    public String getRegisterAnswer() {
        return registerAnswer;
    }

    public void setRegisterAnswer(String registerAnswer) {
        this.registerAnswer = registerAnswer;
    }

    public Thesis getThesis() {
        return thesis;
    }

    public String getGradType() {
        return gradType;
    }

    public void setGradType(String gradType) {
        this.gradType = gradType;
    }

    public void setThesis(Thesis thesis) {
        this.thesis = thesis;
    }

    public Teachers getTeachers(){
        return teachers;
    }
    public void setTeachers(Teachers teachers){
        this.teachers=teachers;
    }

    public Integer getDeleteflag(){ return  deleteflag;}
    public void setDeleteflag(Integer deleteflag){ this.deleteflag = deleteflag;}

//    public Integer getRole(){ return role;}
//    public void setRole(Integer role){this.role = role;}

    public Integer getID() {
        return ID;
    }

    public Student() {

    }
//
    public void setID(Integer ID) {
        this.ID = ID;
    }
    public String getSname() {
        return sname;
    }
    public void setSname(String sname) {
        this.sname = sname;
    }

    public String getStudentnumber() {
        return studentnumber;
    }

    public void setStudentnumber(String studentnumber) {
        this.studentnumber = studentnumber;
    }

    public String getIDNumber() {
        return IDNumber;
    }

    public void setIDNumber(String IDNumber) {
        this.IDNumber = IDNumber;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getTutorID() {
        return tutorID;
    }

    public void setTutorID(Integer tutorID) {
        this.tutorID = tutorID;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getInstitutionid() {
        return institutionid;
    }

    public void setInstitutionid(Integer institutionid) {
        this.institutionid = institutionid;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }


}
