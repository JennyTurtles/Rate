package org.sys.rate.mapper;

import org.apache.ibatis.annotations.*;
import org.sys.rate.model.PaperComment;
import org.sys.rate.model.Student;
import org.sys.rate.model.Thesis;

import java.util.List;

@Mapper
public interface PaperCommentMapper {
    @Select("select ID, thesisID, dateStu, dateTea, preSum, nextPlan, tutorComment, num, isPass from papercomment  where #{thesisID} = thesisID ORDER BY isPass, num")
    List<PaperComment> selectCommentList(Integer thesisID);


    @Select("select p.dateStu,p.dateTea,p.preSum,p.nextPlan,p.tutorComment,p.num \n" +
            "            from papercomment p,thesis \n" +
            "            where #{stuID} = thesis.studentID and thesis.ID = p.thesisID and p.num=#{num}")
    PaperComment selectCommentOne(int stuID, int num);

    @Insert("insert into papercomment (thesisid, datestu, datetea, presum, nextplan, tutorcomment, num, ispass) values(#{thesisID},#{dateStu},#{dateTea},#{preSum},#{nextPlan},#{tutorComment},#{num},#{isPass})")
    Integer insertComment(PaperComment paperComment);

    @Delete("delete from papercomment where num = #{num} and thesisID = #{thesisID}")
    Integer deleteCommentById(int num, int thesisID);


    @Select("SELECT t.ID FROM thesis t, student s, undergraduate u WHERE s.ID = #{stuID} and s.ID = u.studentID and u.ID = t.studentID ORDER BY t.year DESC, t.month DESC LIMIT 1;")
    Integer getThesisID(int stuID);

    @Update("update papercomment set dateStu = #{dateStu}, preSum = #{preSum}, nextPlan = #{nextPlan},dateTea = #{dateTea}, tutorComment = #{tutorComment}, isPass = #{isPass} where thesisID = #{thesisID} and num = #{num}")
    int updateStuComment(PaperComment paperComment);

    @Select("SELECT t.id, t.studentid, t.NAME, t.url, t.YEAR, t.MONTH, t.tutorid, t.grade, t.start_thesis_id FROM thesis t, student s, undergraduate u WHERE u.studentID = #{stuID} and t.studentID = u.ID and u.studentID = s.ID and t.year = #{year} and t.`month` = #{month};")
    Thesis getThesis(int stuID, int year, int month);

    @Update("update papercomment set dateTea = #{dateTea}, tutorComment = #{tutorComment}, isPass = #{isPass} where thesisID = #{thesisID} and num = #{num}")
    int updateTeaComment(PaperComment paperComment);

    List<Student> getStuThesis(int teaID);

    @Select("SELECT t.id, t.studentid, t.name, t.url, t.year, t.month, t.tutorid, t.grade, t.start_thesis_id, u.stuNumber studentNumber FROM thesis t,undergraduate u WHERE t.ID = #{ID} and t.studentID = u.ID")
    Thesis getThesisByTID(Integer thesisID);

    List<Student> getStuThesisWithDate(Integer tutorId, Integer year, Integer month);

    @Select("select ID, thesisID, dateStu, dateTea, preSum, nextPlan, tutorComment, num, isPass from papercomment where #{thesisID} = thesisID ORDER BY num")
    List<PaperComment> selectCommentListStuOrderByNum(Integer thesisID);

    @Select("select ID, thesisID, dateStu, dateTea, preSum, nextPlan, tutorComment, num, isPass from papercomment  where #{thesisID} = thesisID ORDER BY num desc")
    List<PaperComment> selectCommentListStu(int thesisID);

    //    @Select("SELECT IFNULL(u.sign, \"1\") , IFNULL(tea.sign, \"1\")  FROM `thesis` t, undergraduate u, teacher tea where t.ID = #{thesisID} and t.studentID = u.ID and t.tutorID = tea.ID")
    @Select("SELECT CASE WHEN u.sign IS NULL AND tea.sign IS NULL THEN -2 " +
            "            WHEN u.sign IS NULL THEN 0 " +
            "            WHEN tea.sign IS NULL THEN -1 " +
            "            ELSE 1 END " +
            "FROM thesis t, undergraduate u, teacher tea " +
            "WHERE t.ID = #{thesisID} AND t.studentID = u.ID AND t.tutorID = tea.ID")
    int checkSign(Integer thesisID);


}