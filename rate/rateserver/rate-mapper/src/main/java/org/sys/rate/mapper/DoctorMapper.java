package org.sys.rate.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.sys.rate.model.Doctor;
import org.sys.rate.model.GraduateStudent;

import javax.print.Doc;
import java.util.List;

@Mapper
public interface DoctorMapper {

    int update(Doctor record);

    @Select("select d.ID,d.institutionID,d.studentID,d.stuNumber,d.year,d.studentType,d.point,d.specialty,d.class as className,d.tutorID,s.name,s.telephone,s.email,s.username,s.password,s.role,s.registerQuestion,s.registerAnswer " +
            "from doctor d,student s where d.studentID=#{studentID} and s.ID = d.studentID")
    Doctor getDocByStuID(Integer studentID);

    @Select("select exists (select * from doctor where institutionID = #{institutionID} and stuNumber = #{stuNumber} and studentID != #{studentID} )")
    int checkHaveStudentOfStuNumber(Integer institutionID,String stuNumber,Integer studentID);

    List<Doctor> getDoctorStudents();

    @Select("SELECT CASE WHEN s.name = #{stuName} THEN s.id ELSE -1 END AS result " +
            "FROM doctor u " +
            "JOIN student s ON u.studentID = s.ID " +
            "WHERE u.stuNumber = #{stuNumber} AND u.institutionID = #{institutionID}")
    Integer checkStudentExist(String stuNumber, String stuName, Integer institutionID);

    int insertFROMImport(List<Doctor> record);
    int updateFROMImport(List<Doctor> record);
    List<Doctor> getDoctorsBySelect(String teaName,Integer year);
    int deleteDoctorStudent(Doctor doctor);

    int editDoctorStudent(Doctor doctor);

    @Update("UPDATE doctor SET point = point + #{score} WHERE studentID = #{stuID}")
    public int updateScore(Long stuID,Long score);

    @Update("UPDATE doctor SET point = point - #{score} WHERE studentID = #{stuID}")
    public int updateScoreSub(Long stuID,Long score);

}
