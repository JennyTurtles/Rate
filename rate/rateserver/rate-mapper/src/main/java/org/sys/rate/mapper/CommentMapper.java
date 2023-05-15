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

    @Select("SELECT t.name teacherName,content,date,e.role\n" +
            "FROM participants p,`comment` c,teacher t,expertactivities e\n" +
            "WHERE p.activityID = #{activityID} AND p.studentID = #{studentID} AND c.participantID = p.ID AND c.teacherID = t.ID\n" +
            "AND e.activityID = p.activityID AND e.groupID = p.groupID AND e.teacherID = t.ID")
    List<Comment> getGradeFormComment(Integer activityID,Integer studentID);

}
