package org.sys.rate.mapper;

import org.apache.ibatis.annotations.*;
import org.sys.rate.model.PaperComment;
import org.sys.rate.model.Student;
import org.sys.rate.model.Thesis;

import java.util.List;

@Mapper
public interface PaperCommentMapper {
    // 根据thesisID获取papercomment
    @Select("select p.dateStu,p.dateTea,p.preSum,p.nextPlan,p.tutorComment,p.num,p.thesisID,p.isPass \n" +
            "            from papercomment p \n" +
            "            where #{thesis.ID} = p.thesisID" +
            " ORDER BY p.isPass, p.num")
    List<PaperComment> selectCommentList(int thesisID);

    @Select("select p.dateStu,p.dateTea,p.preSum,p.nextPlan,p.tutorComment,p.num,p.thesisID,p.isPass \n" +
            "            from papercomment p \n" +
            "            where #{thesis.ID} = p.thesisID")
    List<PaperComment> selectCommentListStu(int thesisID);

    // 根据stuID和thesisID和num获取某一次评论
    @Select("select p.dateStu,p.dateTea,p.preSum,p.nextPlan,p.tutorComment,p.num \n" +
            "            from papercomment p,thesis \n" +
            "            where #{stuID} = thesis.studentID and thesis.ID = p.thesisID and p.num=#{num}")
    PaperComment selectCommentOne(int stuID, int num);

    @Insert("insert into papercomment values(#{ID},#{thesisID},#{dateStu},#{dateTea},#{preSum},#{nextPlan},#{tutorComment},#{num},#{isPass})")
    Integer insertComment(PaperComment paperComment);

    @Delete("delete from papercomment where num = #{num} and thesisID = #{thesisID}")
    Integer deleteCommentById(int num, int thesisID);

    // 通过stuID获取最新的毕设ID
    @Select("SELECT ID FROM thesis WHERE studentID = #{stuID} ORDER BY year DESC, month DESC LIMIT 1;")
    Integer getTIDbySID(int stuID);

    // 更新的话，会刷新导师的一些评价什么的
    @Update("update papercomment set dateStu = #{dateStu}, preSum = #{preSum}, nextPlan = #{nextPlan},dateTea = #{dateTea}, tutorComment = #{tutorComment}, isPass = #{isPass} where thesisID = #{thesisID} and num = #{num}")
    int updateStuComment(PaperComment paperComment);

//    @Select("SELECT s.ID as stuID,s.name as stuName, t.ID as tID, t.name as tName FROM student s, thesis t WHERE s.tutorID = 1044 and s.ID = t.studentID ")
//    public List<String> getStuIDbyTeaID(int teaID);

    @Select("SELECT ID, name FROM student WHERE tutorID = #{teaID}")
    public List<Student> getStuIDbyTeaID(int teaID);

    @Select("SELECT ID,name,studentID,year,`month`,url FROM thesis WHERE studentID = #{stuID} ORDER BY year DESC, month DESC LIMIT 1;")
    Thesis getThesis(int stuID);

    @Update("update papercomment set dateTea = #{dateTea}, tutorComment = #{tutorComment}, isPass = #{isPass} where thesisID = #{thesisID} and num = #{num}")
    int updateTeaComment(PaperComment paperComment);

//    @Select("SELECT s.ID as stuID, t.ID as theisID, s.name AS stuName, t.name AS thesisName\n" +
//            "FROM student s\n" +
//            "JOIN thesis t ON s.ID = t.studentID\n" +
//            "WHERE s.tutorID = #{teaID}\n" +
//            "AND NOT EXISTS (\n" +
//            "  SELECT 1\n" +
//            "  FROM thesis t2\n" +
//            "  WHERE t.studentID = t2.studentID\n" +
//            "  AND (t.year < t2.year OR (t.year = t2.year AND t.month < t2.month))\n" +
//            ")\n" +
//            "ORDER BY s.id ASC;\n")
//    @Select("select s.ID , s.name, t.ID , t.name  from student s, thesis t where #{teaID} = s.tutorID and s.ID = t.studentID")
    List<Student> getStuThesis(int teaID);

    @Select("SELECT ID,name,studentID,year, month, url FROM thesis WHERE ID = #{ID}")
    Thesis getThesisByTID(Integer thesisID);
}