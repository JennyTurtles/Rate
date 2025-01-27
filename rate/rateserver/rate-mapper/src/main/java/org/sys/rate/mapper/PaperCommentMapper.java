package org.sys.rate.mapper;

import org.apache.ibatis.annotations.*;
import org.sys.rate.model.PaperComment;
import org.sys.rate.model.Student;
import org.sys.rate.model.Thesis;

import java.util.List;

@Mapper
public interface PaperCommentMapper {
    @Select("select ID, thesisID, dateStu, dateTea, preSum, nextPlan, tutorComment, num, isPass from papercomment  where #{thesisID} = thesisID ORDER BY isPass, dateStu")
    List<PaperComment> selectCommentList(Integer thesisID);


    @Select("select p.dateStu,p.dateTea,p.preSum,p.nextPlan,p.tutorComment,p.num \n" +
            "            from papercomment p,thesis \n" +
            "            where #{stuID} = thesis.studentID and thesis.ID = p.thesisID and p.num=#{num}")
    PaperComment selectCommentOne(int stuID, int num);

    @Insert("insert into papercomment (thesisid, datestu, datetea, presum, nextplan, tutorcomment, num, ispass) values(#{thesisID},#{dateStu},#{dateTea},#{preSum},#{nextPlan},#{tutorComment},#{num},#{isPass})")
    Integer insertComment(PaperComment paperComment);

    @Delete("delete from papercomment where num = #{num} and thesisID = #{thesisID}")
    Integer deleteCommentById(int num, int thesisID);


    @Select("SELECT t.ID, st.year, st.semester FROM thesis t, student s, undergraduate u, startthesis st \n" +
            "WHERE s.ID = #{stuID} AND s.ID = u.studentID AND u.ID = t.studentID AND st.id = t.start_thesis_id \n" +
            "ORDER BY st.year DESC, st.semester DESC LIMIT 1;")
    Integer getThesisID(int stuID);

    @Update("update papercomment set dateStu = #{dateStu}, preSum = #{preSum}, nextPlan = #{nextPlan},dateTea = #{dateTea}, tutorComment = #{tutorComment}, isPass = #{isPass}, num = #{num} where thesisID = #{thesisID} and ID = #{ID}")
    int updateStuComment(PaperComment paperComment);

    @Select("SELECT t.id, t.studentid, t.NAME, t.url, t.YEAR, t.MONTH, t.tutorid, t.grade, t.start_thesis_id FROM thesis t, student s, undergraduate u WHERE u.studentID = #{stuID} and t.studentID = u.ID and u.studentID = s.ID and t.start_thesis_id = #{startThesisID}")
    Thesis getThesis(int stuID, int startThesisID);

    @Update("update papercomment set dateTea = #{dateTea}, tutorComment = #{tutorComment}, isPass = #{isPass} where thesisID = #{thesisID} and num = #{num}")
    int updateTeaComment(PaperComment paperComment);

    List<Student> getStuThesis(int teaID);

    @Select("SELECT t.id, t.studentid, t.name, t.url, t.year, t.month, t.tutorid, t.grade, t.start_thesis_id, u.stuNumber studentNumber FROM thesis t,undergraduate u WHERE t.ID = #{ID} and t.studentID = u.ID")
    Thesis getThesisByTID(Integer thesisID);

    List<Student> getStuThesisWithDate(Integer tutorId, Integer startThesisID);

    List<Student> getStuThesisWithNoDate(Integer tutorId);

    @Select("select ID, thesisID, dateStu, dateTea, preSum, nextPlan, tutorComment, num, isPass from papercomment where #{thesisID} = thesisID ORDER BY dateStu")
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

    @Update("update thesis set fillMiss = #{fillMiss} where ID = #{thesisID}")
    int fillMissChange(Integer thesisID, Integer fillMiss);

    @Select("SELECT fillMiss FROM thesis WHERE ID = #{thesisID}")
    Integer getFillMiss(Integer thesisID);

    @Update("UPDATE papercomment set num = num + 1 " +
            "where thesisID=#{thesisID} and num >= #{num} and num < #{total}")
    Integer addNum(@Param("thesisID") Integer thesisID, @Param("num") Integer num,@Param("total") Integer total);

    @Update("UPDATE papercomment set num = num - 1 " +
            "where thesisID=#{thesisID} and num > #{oldNum} and num <= #{newNum}")
    Integer subNum(@Param("thesisID") Integer thesisID, @Param("oldNum") Integer oldNum,@Param("newNum") Integer newNum);

    @Select("select * from papercomment where ID = #{ID}")
    PaperComment selectByID(Integer ID);
}
