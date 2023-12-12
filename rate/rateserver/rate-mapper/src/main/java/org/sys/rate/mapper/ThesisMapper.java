package org.sys.rate.mapper;

import org.apache.ibatis.annotations.*;
import org.sys.rate.model.Thesis;
import org.sys.rate.model.UnderGraduate;

import java.util.List;
import java.util.Map;

@Mapper
public interface ThesisMapper {
    // 通过学生ID获取学生所有的毕业论文或设计
    @Select("select id, studentid, name, url, year, month, tutorid, grade from thesis where studentID=#{studentID}")
    List<Thesis> getAll(int studentID);

    // 通过thesisID获取thesis
    @Select("select id, studentid, name, year, url, month from thesis where ID =  #{thesisID}")
    Thesis getByID(int thesisID);

    @Insert("insert into thesis (studentID, year, month, tutorID) VALUES (#{studentID}, #{year}, #{month}, #{tutorID}) ")
    Integer add(Integer studentID, Integer year, Integer month, Integer tutorID);


    Integer addBatch(List<Thesis> thesisList, Integer year, Integer month);

    Integer updateBatch(List<Thesis> thesisList, Integer year, Integer month);


    @Update("update thesis set tutorID = #{tutorID} where studentID=#{studentID} and year=#{year} and month=#{month}")
    void update(UnderGraduate underGraduate);

    @Delete("DELETE FROM thesis " +
            "WHERE studentID IN (SELECT ID FROM undergraduate WHERE studentID = #{studentID}) " +
            "AND (tutorID = #{tutorID} OR tutorID IS NULL) AND start_thesis_id = #{startThesisID}")
    void delete(UnderGraduate under);

    @Select("select count(id)>0 from thesis where studentID=#{studentID} and year =#{year} and month=#{month}")
    boolean ifExist(Thesis thesis);

    @Update("update thesis set tutorID = #{tutorID} where studentID=#{studentID} and year=#{year} and month=#{month}")
    void edit(Thesis thesis);

    @Insert("insert into thesis (studentID, year, month, tutorID, grade) VALUES (#{studentID}, #{year}, #{month}, #{tutorID}, #{grade})")
    void insert(Thesis thesis);

    @Insert("INSERT INTO thesis (studentID, start_thesis_id, tutorID, grade) " +
            "VALUES (#{studentID}, #{startThesisID}, #{tutorID, jdbcType=NUMERIC}, #{grade}) " +
            "ON DUPLICATE KEY UPDATE tutorID = VALUES(tutorID), grade = VALUES(grade)")
    @Options(useGeneratedKeys = true, keyProperty = "ID", keyColumn = "ID")
    int upsert(Thesis thesis);


    @Insert("INSERT INTO thesis (studentID, tutorID) " +
            "VALUES (#{studentID}, #{tutorID, jdbcType=NUMERIC}) " +
            "ON DUPLICATE KEY UPDATE tutorID = VALUES(tutorID)")
    @Options(useGeneratedKeys = true, keyProperty = "ID", keyColumn = "ID")
    int upsertTutorId(Thesis thesis);


//

    Integer batchUpsert(Map<String, Object> paramMap);

    @Update("update thesis set name = #{thesisName} where ID = #{thesisId}")
    void editThesisName(Integer thesisId, String thesisName);


    @Update("UPDATE thesis SET name = #{name} WHERE studentID = #{studentID} AND start_thesis_id = #{startThesisID} AND tutorID = #{tutorID}")
    Integer notExistOrUpdate(Thesis thesis);

    @Select("select studentID from undergraduate where ID=#{studentID}")
    Integer getStuId(Integer studentID);

    @Update("update thesis t,undergraduate u, student s, teacher tea " +
            "set t.tutorID = #{tutorID} " +
            "where s.ID = #{studentID} and t.year=#{year} and t.month=#{month} and s.ID = u.studentID and u.ID = t.studentID")
    void updateWithStuID(Thesis thesis);
}
