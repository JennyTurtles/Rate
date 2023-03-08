package org.sys.rate.mapper;

import org.apache.ibatis.annotations.*;
import org.sys.rate.model.Paper;
import org.sys.rate.model.PaperComment;

import java.util.List;

@Mapper
public interface PaperCommentMapper {
    // 根据stuID和thesisID获取papercomment
    // TODO：目前先试着搞一个，后期再选择thesisID
    @Select("select p.dateStu,p.dateTea,p.preSum,p.nextPlan,p.tutorComment,p.num,p.thesisID \n" +
            "            from papercomment p \n" +
            "            where #{thesis.ID} = p.thesisID")
    List<PaperComment> selectCommentList(int thesisID);

    // 根据stuID和thesisID和num获取某一次评论
    @Select("select p.dateStu,p.dateTea,p.preSum,p.nextPlan,p.tutorComment,p.num \n" +
            "            from papercomment p,thesis \n" +
            "            where #{stuID} = thesis.studentID and thesis.ID = p.thesisID and p.num=#{num}")
    PaperComment selectCommentOne(int stuID, int num);

    @Insert("insert into papercomment values(#{ID},#{thesisID},#{dateStu},#{dateTea},#{preSum},#{nextPlan},#{tutorComment},#{num})")
    Integer insertComment(PaperComment paperComment);

    @Delete("delete from papercomment where num = #{num} and thesisID = #{thesisID}")
    Integer deleteCommentById(int num, int thesisID);

    @Select("SELECT ID FROM thesis WHERE studentID = #{stuID} ORDER BY year DESC LIMIT 1;")
    Integer getTIDbySID(int stuID);


    @Update("update papercomment set dateStu = #{dateStu}, preSum = #{preSum}, nextPlan = #{nextPlan} where thesisID = #{thesisID} and num = #{num}")
    int updateStuComment(PaperComment paperComment);


}