package org.sys.rate.mapper;

import org.apache.ibatis.annotations.*;
import org.sys.rate.model.Student;
import org.sys.rate.model.UnderGraduate;

import java.util.List;

@Mapper
public interface StudentMapper {
    List<Student> getTotal();

    Student loadUserByUsername(String username);

    //    List<Role> getAdminRolesById(Integer id);
    Student getStuByIDNumber(String IDNumber);

    Student getById(Integer ID);

    int updatePassword(Integer ID, String pass);

    int insert(Student record);

    int delete(Student record);

    int update(Student record);

    int check(@Param("IDNumber") String record);

    @Select("select ID from student where ID = #{ID}")
    Integer checkID(@Param("ID") Integer ID);

    @Select("select studentID from graduatestudent where studentID = #{ID}")
    Integer checkIDInGraduate(@Param("ID") Integer ID);

    @Select("select studentID from undergraduate where studentID = #{ID}")
    Integer checkIDInUndergraduate(@Param("ID") Integer ID);

    List<Student> checkAndReturnID(List<Student> stuList);

    int updatePasswordAndUsername(Student record);

    int insertStuFromRegister(Student record);//注册添加

    int insertFROMImport(Student record);

    int insertStuFromExcel(Student record);

    int updateFROMImport(Student record);

    public List<Student> selectList();

    int deleteStudent(Integer ID);

    int insertFromAdminExcel(List<Student> record);

    int updateFromAdminExcel(List<Student> record);


    @Select("select id from student where id = #{id}")
    Integer isStudent(Integer id);

    @Select("select name,s.ID as ID from graduatestudent g, student s\n" +
            "where stuNumber = #{studentNumber} AND s.ID = g.studentID")
    Student getGraduateByStudentNumber(String studentNumber);

    @Select("select name,s.ID as ID from undergraduate g, student s\n" +
            "where stuNumber = #{studentNumber} AND s.ID = g.studentID")
    Student getUndergraduateByStudentNumber(String studentNumber);

    @Insert("INSERT INTO graduatestudent(institutionID,studentID,stuNumber,year,studentType,point,tutorID)\n" +
            "VALUES (#{institutionid},#{ID},#{studentnumber},#{year},#{gradType},0,#{tutorID})")
    void registerGraduate(Student student);

    @Update("UPDATE graduatestudent SET studentID = #{newID}\n" +
            "WHERE ID = (SELECT ID2 FROM (SELECT ID ID2 FROM graduatestudent WHERE studentID = #{oldID}) AS a)")
    void updateGraduateStudentID(Integer oldID, Integer newID);

    @Update("UPDATE undergraduate SET studentID = #{newID}\n" +
            "WHERE ID = (SELECT ID2 FROM (SELECT ID ID2 FROM undergraduate WHERE studentID = #{oldID}) AS a)")
    void updateUndergraduateStudentID(Integer oldID, Integer newID);

    @Insert("INSERT INTO undergraduate(institutionID,studentID,stuNumber,year)\n" +
            "VALUES (#{institutionid},#{ID},#{studentnumber},#{year})")
    void registerUndergraduate(Student student);


    @Select("select name from student where ID = #{studentID}")
    String getNameByID(Integer studentID);

    @Update("UPDATE graduatestudent SET year = #{year}, tutorID = #{tutorID} ,studentType = #{gradType} WHERE studentID = #{ID}")
    void updateGraduate(Student student);

    @Update("UPDATE undergraduate SET year = #{year} WHERE studentID = #{ID}")
    void updateUnderGraduate(Student student);

    @Select("select distinct id from undergraduate where stuNumber = #{stuNumber} and institutionID = #{institutionID}")
    Integer getStuIdByStuNumAndInstitutionID(String stuNumber, Integer institutionID);

    @Insert("INSERT INTO student (name, institutionid, deleteflag, role) " +
            "VALUES (#{name}, #{institutionid}, #{deleteflag}, #{role})")
    @Options(useGeneratedKeys = true, keyProperty = "ID")
    void insertReturnId(Student student);

    @Update("update student set name = #{name}, email=#{email}, telephone=#{telephone} where ID=#{studentID}")
    void edit(UnderGraduate under);
}
