package org.sys.rate.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.sys.rate.model.Doctor;
@Mapper
public interface DoctorMapper {

    int update(Doctor record);

    @Select("select d.ID,d.institutionID,d.studentID,d.stuNumber,d.year,d.studentType,d.point,d.specialty,d.class as className,d.tutorID,s.name,s.telephone,s.email,s.username,s.password,s.registerQuestion,s.registerAnswer " +
            "from doctor d,student s where d.studentID=#{studentID} and s.ID = d.studentID")
    Doctor getDocByStuID(Integer studentID);

    @Select("select exists (select * from doctor where institutionID = #{institutionID} and stuNumber = #{stuNumber} and studentID != #{studentID} )")
    int checkHaveStudentOfStuNumber(Integer institutionID,String stuNumber,Integer studentID);
}
