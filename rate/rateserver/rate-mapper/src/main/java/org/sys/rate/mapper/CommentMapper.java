package org.sys.rate.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.sys.rate.model.Comment;

import java.util.List;

@Mapper
public interface CommentMapper {
    int insertCommentByExpert(Comment comment);
    Comment selectHaveComment(Comment comment);
    int updateCommentByExpert(Comment comment);

    @Select("SELECT t.name teacherName,content,date \n" +
            "FROM participants p,`comment` c,teacher t\n" +
            "WHERE p.activityID = #{activityID} AND p.studentID = #{studentID} AND c.participantID = p.ID AND c.teacherID = t.ID")
    List<Comment> getGradeFormComment(Integer activityID,Integer studentID);

}
