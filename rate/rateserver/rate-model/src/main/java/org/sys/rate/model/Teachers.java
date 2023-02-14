package org.sys.rate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * 老师对象 teachers
 * 
 * @author system
 * @date 2022-03-13
 */
public class Teachers extends Account
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Integer ID;
    private String sex;
    private String department;
    private String IDNumber;
    private Integer institutionid;
    /** 姓名 */
    private String name;

    /** 工号 */
    private String jobnumber;

    /** 邮箱 */
    private String email;

    /** 账号 */
    private String username;

    /** 密码 */
    private String password;
    private String phone;
    private String role;
    /** 删除标志 */
    private Long deleteflag;

    public void setID(Integer ID)
    {
        this.ID = ID;
    }

    public Integer getID()
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
    public void setSex(String sex)
    {
        this.sex = sex;
    }

    public String getSex()
    {
        return sex;
    }
    public void setDepartment(String department)
    {
        this.department = department;
    }

    public String getDepartment()
    {
        return department;
    }
    public void setIDNumber(String IDNumber)
    {
        this.IDNumber = IDNumber;
    }

    public String getIDNumber()
    {
        return IDNumber;
    }
    public void setJobnumber(String jobnumber)
    {
        this.jobnumber = jobnumber;
    }

    public String getJobnumber()
    {
        return jobnumber;
    }
    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public String getPhone()
    {
        return phone;
    }
    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getEmail()
    {
        return email;
    }
    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getUsername()
    {
        return username;
    }
    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getPassword()
    {
        return password;
    }
    public void setDeleteflag(Long deleteflag)
    {
        this.deleteflag = deleteflag;
    }

    public Long getDeleteflag()
    {
        return deleteflag;
    }
    public Integer getInstitutionid() {
        return institutionid;
    }

    public void setInstitutionid(Integer institutionid) {
        this.institutionid = institutionid;
    }
//    public void setInstitutionID(Integer institutionID)
//    {
//        this.institutionid = institutionID;
//    }
//
//    public Integer getInstitutionID()
//    {
//        return institutionid;
//    }
    public void setRole(String role)
    {
        this.role = role;
    }

    public String getRole()
    {
        return role;
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
