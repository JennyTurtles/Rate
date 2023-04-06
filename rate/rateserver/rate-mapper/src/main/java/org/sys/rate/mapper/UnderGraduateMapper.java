package org.sys.rate.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.sys.rate.model.Student;
import org.sys.rate.model.UnderGraduate;

import java.util.List;

@Mapper
public interface UnderGraduateMapper {

    List<UnderGraduate> getUnderStudent();
    int insert(UnderGraduate record);
    int update(Student record);
    int deleteUnderStudent(UnderGraduate under);
    int editUnderStudent(UnderGraduate under);
    List<String> getTeaNamesBySelect(String teaName);
    List<UnderGraduate> getUnderStudentsBySelect(String teaName,Integer year);
    List<Integer> check(List<UnderGraduate> under);

    int insertFROMImport(List<UnderGraduate> record);

    int updateFROMImport(List<UnderGraduate> record);
    @Select("SELECT COUNT(*) from undergraduate WHERE studentID = #{stuId}")
    int isUndergraduate(Integer stuId);
}
