package org.sys.rate.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.sys.rate.model.GradeForm;
import org.sys.rate.model.Student;
import org.sys.rate.model.Teachers;
import org.sys.rate.model.UnderGraduate;

import java.util.List;

@Mapper
public interface UnderGraduateMapper {

    List<UnderGraduate> getUnderStudent();
    UnderGraduate getUnderByStuID(Integer studentID);
    int insert(UnderGraduate record);
    int update(UnderGraduate record);
    int deleteUnderStudent(UnderGraduate under);
    int editUnderStudent(UnderGraduate under);
    List<Teachers> getTeaNamesBySelect(String teaName);
    List<UnderGraduate> getUnderStudentsBySelect(String teaName,Integer year);
    List<Integer> check(List<UnderGraduate> under);
    int checkHaveStudentOfstudenID(Integer studentID);
    int checkHaveStudentOftutorID(Integer tutorID,Integer ID);
    int checkHaveStudentOfStuNumber(Integer institutionID,String stuNumber,Integer studentID);
    int insertFROMImport(List<UnderGraduate> record);

    int updateFROMImport(List<UnderGraduate> record);
    @Select("SELECT COUNT(*) from undergraduate WHERE studentID = #{stuId}")
    int isUndergraduate(Integer stuId);

    // 由于tID暂时不存在所以先删除了
//    @Select("SELECT u.studentID,stuNumber,specialty,class as className,s.name,t.name as thesisName FROM undergraduate u,student s WHERE u.studentID = #{studentID} AND s.ID = u.studentID  LIMIT 1")
//    @Select("SELECT u.studentID,stuNumber,specialty,class as className,s.name FROM undergraduate u,student s WHERE u.studentID = #{studentID} AND s.ID = u.studentID  LIMIT 1")
    @Select("SELECT name FROM student s WHERE s.ID = #{studentID} LIMIT 1")
    GradeForm getGradeFormByStuID(Integer studentID);

    @Select("select id from undergraduate  where stuNumber =#{stuNumber} and institutionID = #{institutionID}")
    Integer checkStudentExist(String stuNumber, Integer institutionID);

    @Insert("insert into undergraduate (institutionID, studentID, stuNumber, year, specialty, class) " +
            "values (#{institutionID}, #{studentID}, #{stuNumber}, #{year}, #{specialty}, #{className})")
    void add(UnderGraduate underGraduate);
}
