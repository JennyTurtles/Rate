package org.sys.rate.mapper;

import org.apache.ibatis.annotations.*;
import org.sys.rate.model.GraduateStudent;
import org.sys.rate.model.PaperComment;
import org.sys.rate.model.ProgramRecord;

import java.util.List;

@Mapper
public interface ProgramRecordMapper {

    @Select("select * from programrecord pr, graduatestudent g where #{studentID} = g.studentID and pr.studentID = g.ID ORDER BY num")
    List<ProgramRecord> selectRecordListStu(Integer studentID);

    @Insert("insert into programrecord (studentID, startDateStu, endDateStu, dateTea, preSum, nextPlan, tutorComment, num, isPass, workHours) values(#{studentID},#{startDateStu},#{endDateStu},#{dateTea},#{preSum},#{nextPlan},#{tutorComment},#{num},#{isPass},#{workHours})")
    Integer add(ProgramRecord programRecord);

    @Delete("delete from programrecord where num = #{num} and studentID = #{studentID}")
    Integer deleteRecordById(int num, int studentID);

    @Update("update programrecord set startDateStu = #{startDateStu}, endDateStu = #{endDateStu}, preSum = #{preSum}, nextPlan = #{nextPlan},dateTea = #{dateTea}, tutorComment = #{tutorComment}, isPass = #{isPass}, workHours = #{workHours} where studentID = #{studentID} and num = #{num}")
    int updateStuRecord(ProgramRecord programRecord);

    @Update("update graduatestudent set fillMiss = #{fillMiss} where ID = #{studentID}")
    int fillMissChange(Integer studentID, Integer fillMiss);

    List<GraduateStudent> getGraduateStudentsOfTutorID(Integer tutorID);

    @Select("SELECT ID FROM graduatestudent WHERE studentID = #{studentID}")
    Integer getIDByStudentID(Integer studentID);

    List<GraduateStudent> getStuByFilter(String searchNumber, String searchName, String searchSpecialty, String searchStuType, @Param("searchYears") List<Integer> searchYears, String searchTutorName, Integer minWorkHours, Integer maxWorkHours, Integer tutorID);

    @Select("SELECT fillMiss FROM graduatestudent WHERE studentID = #{studentID}")
    Integer getFillMiss(Integer studentID);

    @Select("select * from programrecord where studentID = #{studentID} ORDER BY isPass, num")
    List<ProgramRecord> selectRecordListTea(Integer studentID);

    @Update("update programrecord set dateTea = #{dateTea}, tutorComment = #{tutorComment}, isPass = #{isPass} where ID = #{ID}")
    int updateTeaComment(ProgramRecord programRecord);

}
