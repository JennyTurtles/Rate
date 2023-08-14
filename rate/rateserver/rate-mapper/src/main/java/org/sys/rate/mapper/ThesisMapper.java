package org.sys.rate.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.sys.rate.model.Thesis;

import java.util.List;
import java.util.Set;

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
}
