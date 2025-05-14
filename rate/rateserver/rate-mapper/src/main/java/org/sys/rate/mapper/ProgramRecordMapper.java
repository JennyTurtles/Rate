package org.sys.rate.mapper;

import org.apache.ibatis.annotations.*;
import org.sys.rate.model.GraduateStudent;
import org.sys.rate.model.ProgramRecord;
import org.sys.rate.model.ProgramResult;
import org.sys.rate.model.Project;

import java.util.List;

@Mapper
public interface ProgramRecordMapper {

    @Select("select * from programrecord pr, graduatestudent g where #{studentID} = g.studentID and pr.studentID = g.ID ORDER BY startDateStu")
    List<ProgramRecord> selectRecordListStu(Integer studentID);


    @Insert("insert into programrecord (studentID, startDateStu, endDateStu, dateTea, preSum, nextPlan, tutorComment, num, isPass, workHours) values(#{studentID},#{startDateStu},#{endDateStu},#{dateTea},#{preSum},#{nextPlan},#{tutorComment},#{num},#{isPass},#{workHours})")
    Integer add(ProgramRecord programRecord);

    @Delete("delete from programrecord where ID = #{ID} and studentID = #{studentID}")
    Integer deleteRecordById(int ID, int studentID);

    @Update("update programrecord set  preSum = #{preSum}, nextPlan = #{nextPlan},dateTea = #{dateTea}, tutorComment = #{tutorComment},isPass = ''  where studentID = #{studentID} and num = #{num}")
    int updateStuRecord(ProgramRecord programRecord);

    @Update("update graduatestudent set fillMiss = #{fillMiss} where ID = #{studentID}")
    int fillMissChange(Integer studentID, Integer fillMiss);

    List<GraduateStudent> getGraduateStudentsOfTutorID(Integer tutorID);

    @Select("SELECT ID FROM graduatestudent WHERE studentID = #{studentID}")
    Integer getIDByStudentID(Integer studentID);

    List<GraduateStudent> getStuByFilter(String searchNumber, String searchName, String searchSpecialty, String searchStuType, @Param("searchYears") List<Integer> searchYears, String searchTutorName, Integer minWorkHours, Integer maxWorkHours, Integer tutorID);

    @Select("SELECT fillMiss FROM graduatestudent WHERE studentID = #{studentID}")
    Integer getFillMiss(Integer studentID);

    @Select("select * from programrecord where studentID = #{studentID} ORDER BY startDateStu")
    List<ProgramRecord> selectRecordListTea(Integer studentID);

    @Update("update programrecord set dateTea = #{dateTea}, tutorComment = #{tutorComment}, isPass = #{isPass} where ID = #{ID}")
    int updateTeaComment(ProgramRecord programRecord);

    int addResult(ProgramResult programResult);

    ProgramResult selectProgramResultById(Long id);

    Integer updateResult(ProgramResult programResult);

    @Select("select count(*) from programresults where programresults.student_id = #{id}")
    int getDtaByStuID(int id);

    Integer selectHorizontalProjectNumberOfPendingMessing(String state,Integer ID);

    List<ProgramResult> searchHorizontalProjectByConditions(String studentName, String state, String projectName, String pointFront, String pointBack);

    int editState(String state, Long id);

    @Select("select * from programresults where ID = #{ID}")
    ProgramResult getById(Integer ID);

    Integer deleteById(Long id);

    @Select("SELECT id FROM programresults WHERE student_id = #{stuID} AND point = 3 AND state = 'adm_pass' LIMIT 1")
    public Integer checkScore(int stuID);

    @Update("UPDATE programresults SET state = #{state},have_score = #{valid} WHERE id = #{ID}")
    public Integer editState2(String state, Long ID, Integer valid);

    @Update("UPDATE graduatestudent SET point = point + #{score} WHERE studentID = #{stuID}")
    public int updateScore(Long stuID,Long score);

    List<ProgramResult> selectProgramResultListById(Integer studentId);
}
