package org.sys.rate.mapper;

import org.sys.rate.model.Comment;

import java.util.List;

public interface CommentMapper {
    int insertCommentByExpert(Comment comment);
    Comment selectHaveComment(Comment comment);
    int updateCommentByExpert(Comment comment);
}
