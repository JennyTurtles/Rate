package org.sys.rate.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.sys.rate.model.GraduateStudent;
import org.sys.rate.model.Teachers;

import java.util.List;

@Mapper
public interface GraduateStudentMapper {
    List<GraduateStudent> getGraduateStudent();
    GraduateStudent getGradByStuID(Integer studentID);

    int deleteGraduateStudent(GraduateStudent grad);
    int editGraduateStudent(GraduateStudent grad);
    int insert(GraduateStudent record);
    int update(GraduateStudent record);

    List<Integer> check(List<GraduateStudent> graduate);
    int insertFROMImport(List<GraduateStudent> record);

    int updateFROMImport(List<GraduateStudent> record);
    int checkHaveStudentOfstudenID(Integer studentID);
    int checkHaveStudentOftutorID(Integer tutorID,Integer ID);
    int checkHaveStudentOfStuNumber(Integer institutionID,String stuNumber,Integer studentID);

    @Select("SELECT COUNT(*) from graduatestudent WHERE studentID = #{stuId}")
    int isGraduateStudent(Integer stuId);
    List<Teachers> getTeaNamesBySelect(String teaName);
    List<GraduateStudent> getGraduateStudentsBySelect(String teaName,Integer year);

    // 获取研究生列表
    List<GraduateStudent> getGraduateListByTutorID(Integer tutorID);

    @Select("SELECT CASE WHEN s.name = #{stuName} THEN s.id ELSE -1 END AS result " +
            "FROM graduatestudent u " +
            "JOIN student s ON u.studentID = s.ID " +
            "WHERE u.stuNumber = #{stuNumber} AND u.institutionID = #{institutionID}")
    Integer checkStudentExist(String stuNumber, String stuName, Integer institutionID);

    int updateWithInstitutionID(GraduateStudent graduateStudent);

    @Update("UPDATE graduatestudent SET point = point + #{score} WHERE studentID = #{stuID}")
    public int updateScore(Long stuID,Long score);
}
