package org.sys.rate.mapper;

import org.apache.ibatis.annotations.*;
import org.sys.rate.model.Student;

import java.util.List;

@Mapper
public interface StudentMapper {
    List<Student> getTotal();

    Student loadUserByUsername(String username);
//    List<Role> getAdminRolesById(Integer id);
    Student getStuByIDNumber(String IDNumber);
    Student getById(Integer ID);

    int updatePassword(Integer ID,String pass);
    int insert(Student record);

    int delete(Student record);

    int update(Student record);

    int check(@Param("IDNumber") String record);

    @Select("select ID from student where ID = #{ID}")
    Integer checkID(@Param("ID") Integer ID);

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

    @Select("select studentID from graduatestudent where stuNumber = #{studentNumber}")
    Integer getGraduateByStudentNumber(String studentNumber);

    @Insert("INSERT INTO graduatestudent(institutionID,studentID,stuNumber,year,studentType,point)\n" +
            "VALUES (#{institutionid},#{ID},#{studentnumber},#{year},#{gradType},0)")
    void registerGraduate(Student student);

    @Update("UPDATE graduatestudent SET studentID = #{newID}\n" +
            "WHERE ID = (SELECT ID2 FROM (SELECT ID ID2 FROM graduatestudent WHERE studentID = #{oldID}) AS a)")
    void updateGraduateStudentID(Integer oldID, Integer newID);

    @Update("UPDATE undergraduate SET studentID = #{newID}\n" +
            "WHERE ID = (SELECT ID2 FROM (SELECT ID ID2 FROM undergraduate WHERE studentID = #{oldID}) AS a)")
    void updateUndergraduateStudentID(Integer oldID, Integer newID);

    @Select("select studentID from undergraduate where stuNumber = #{studentNumber}")
    Integer getUndergraduateByStudentNumber(String studentNumber);

    @Insert("INSERT INTO undergraduate(institutionID,studentID,stuNumber,year)\n" +
            "VALUES (#{institutionid},#{ID},#{studentnumber},#{year})")
    void registerUndergraduate(Student student);


    @Select("select name from student where ID = #{studentID}")
    String getNameByID(Integer studentID);
}
