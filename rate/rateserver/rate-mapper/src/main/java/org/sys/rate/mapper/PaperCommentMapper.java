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

    @Select("SELECT ID FROM thesis WHERE studentID = #{stuID} ORDER BY year DESC, month DESC LIMIT 1;")
    Integer getThesisID(int stuID);

    @Update("update papercomment set dateStu = #{dateStu}, preSum = #{preSum}, nextPlan = #{nextPlan},dateTea = #{dateTea}, tutorComment = #{tutorComment}, isPass = #{isPass} where thesisID = #{thesisID} and num = #{num}")
    int updateStuComment(PaperComment paperComment);



    @Select("SELECT ID, studentID, name, url, year, month, tutorID FROM thesis WHERE studentID = #{stuID} ORDER BY year DESC, month DESC LIMIT 1;")
    Thesis getThesis(int stuID);

    @Update("update papercomment set dateTea = #{dateTea}, tutorComment = #{tutorComment}, isPass = #{isPass} where thesisID = #{thesisID} and num = #{num}")
    int updateTeaComment(PaperComment paperComment);

    List<Student> getStuThesis(int teaID);

    @Select("SELECT id, studentid, name, url, year, month, tutorid FROM thesis WHERE ID = #{ID}")
    Thesis getThesisByTID(Integer thesisID);
}