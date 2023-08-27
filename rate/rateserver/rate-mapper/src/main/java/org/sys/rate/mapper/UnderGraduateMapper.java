package org.sys.rate.mapper;

import org.apache.ibatis.annotations.*;
import org.sys.rate.model.*;

import java.util.List;

@Mapper
public interface UnderGraduateMapper {

    List<UnderGraduate> getUnderStudent();
    UnderGraduate getUnderByStuID(Integer studentID);
    int insert(UnderGraduate record);
    int update(UnderGraduate record);
    int updateWithInstitutionID(UnderGraduate record);
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

    @Select("SELECT s.name, t.name thesisName,u.stuNumber,u.class className,u.specialty\n" +
            "FROM student s, undergraduate u, thesis t\n" +
            "WHERE s.ID = #{studentID} AND u.studentID = s.ID AND u.ID = t.studentID AND t.tutorID = #{tutorID} LIMIT 1")
    GradeForm getGradeFormByStuIdAndTutorId(Integer studentID, Integer tutorID);

    @Select("select id from undergraduate  where stuNumber =#{stuNumber} and institutionID = #{institutionID}")
    Integer checkStudentExist(String stuNumber, Integer institutionID);

    @Insert("insert into undergraduate (institutionID, studentID, stuNumber, year, specialty, class) " +
            "values (#{institutionID}, #{studentID}, #{stuNumber}, #{year}, #{specialty}, #{className})")
    @Options(useGeneratedKeys = true, keyProperty = "ID")
    Integer addReturnId(UnderGraduate underGraduate);

    @Select("SELECT u.institutionID, u.specialty, u.stuNumber, u.class as className, u.studentID, u.`year`, t.tutorID, t.`group`, s.email as email, s.telephone as telephone, s.NAME, tea.`name` AS tutorName, tea.jobnumber as tutorJobNumber  FROM thesis t " +
            "INNER JOIN undergraduate u ON t.studentID = u.ID INNER JOIN student s ON s.ID = u.studentID " +
            "LEFT JOIN teacher tea ON t.tutorID = tea.id " +
            "WHERE t.YEAR = #{year} AND t.`month` = #{month} and u.institutionID=#{institutionID} Order BY stuNumber")
    List<UnderGraduate> getStudent(Integer institutionID, Integer year, Integer month);

//    @Select("SELECT DISTINCT CONCAT(t.year, t.month) AS date FROM thesis t, undergraduate u " +
//            "WHERE u.institutionID = #{institutionID} AND t.studentID = u.ID")
    @Select("select concat(s.year,'年',s.semester) from startThesis s where s.institutionID = #{institutionID} order by id desc")
    List<String> getThesisExistDate(Integer institutionID);

    @Insert("insert ignore into startThesis (year, semester, institutionID) values (#{year}, #{semester}, #{institutionID})")
    void startThesis(Integer institutionID, Integer year, String semester);

    @Select("SELECT COUNT(*) > 0 FROM startThesis WHERE year = #{year} AND semester = #{semester} AND institutionID = #{institutionID}")
    boolean havingStartThisThesis(@Param("institutionID") Integer institutionID, @Param("year") Integer year, @Param("semester") String semester);

    @Select("SELECT * FROM thesis WHERE `group` is null AND year =#{year} AND month =#{month}")
    List<Thesis> getUngrouped(Integer year, Integer month);

    void updateGroup(Integer ID,String groupName);
    List<Thesis> getByGrade(Integer year,Integer month,Double grade);
    List<Thesis> getNoGrade(Integer year,Integer month);

}
