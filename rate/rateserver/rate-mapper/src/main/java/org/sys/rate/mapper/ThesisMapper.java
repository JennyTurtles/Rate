package org.sys.rate.mapper;

import org.apache.ibatis.annotations.*;
import org.sys.rate.model.Thesis;
import org.sys.rate.model.UnderGraduate;

import java.util.List;

@Mapper
public interface ThesisMapper {
    // 通过学生ID获取学生所有的毕业论文或设计
    @Select("select id, studentid, name, url, year, month, tutorid from thesis where studentID=#{studentID}")
    List<Thesis> getAll(int studentID);

    // 通过thesisID获取thesis
    @Select("select id, studentid, name, year, url, month from thesis where ID =  #{thesisID}")
    Thesis getByID(int thesisID);

    @Insert("insert into thesis (studentID, year, month, tutorID) VALUES (#{studentID}, #{year}, #{month}, #{tutorID}) ")
    Integer add(Integer studentID, Integer year, Integer month, Integer tutorID);


    Integer addBatch(List<Thesis> thesisList, Integer year, Integer month);

    void updateBatch(List<Thesis> thesisList, Integer year, Integer month);

    @Update("update thesis set tutorID = #{tutorID} where studentID=#{studentID} and year=#{year} and month=#{month}")
    void update(UnderGraduate underGraduate);

    @Delete("DELETE FROM thesis " +
            "WHERE studentID IN (SELECT ID FROM undergraduate WHERE studentID = #{studentID}) " +
            "AND (tutorID = #{tutorID} OR tutorID IS NULL) AND YEAR = #{year} AND MONTH = #{month}")
    void delete(UnderGraduate under);
}
