package org.sys.rate.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.sys.rate.model.Student;
import org.sys.rate.model.GraduateStudent;
import org.sys.rate.model.UnderGraduate;

import java.util.List;

@Mapper
public interface GraduateStudentMapper {

    List<Integer> check(List<GraduateStudent> graduate);
    int insertFROMImport(List<GraduateStudent> record);

    int updateFROMImport(List<GraduateStudent> record);

    @Select("SELECT COUNT(*) from graduatestudent WHERE studentID = #{stuId}")
    int isGraduateStudent(Integer stuId);
}
