package org.sys.rate.service.underFunction;

import org.springframework.stereotype.Service;
import org.sys.rate.mapper.PaperCommentMapper;
import org.sys.rate.model.PaperComment;
import org.sys.rate.model.Student;
import org.sys.rate.model.Thesis;

import javax.annotation.Resource;
import java.util.List;

@Service
public class PaperCommentService {
    @Resource
    private PaperCommentMapper paperCommentMapper;

    // 根据stuID和thesisID和num获取某一次评论
    public PaperComment selectCommentOne(int stuID, int num) {
        return paperCommentMapper.selectCommentOne(stuID, num);
    }

    // 根据stuID和thesisID获取papercomment
    public List<PaperComment> selectCommentList(int thesisID) {
        return paperCommentMapper.selectCommentList(thesisID);
    }

    // 插入一条评论记录
    public Integer insertComment(PaperComment paperComment) {
        return paperCommentMapper.insertComment(paperComment);
    }

    public Integer deleteCommentById(int num, int thesisID) {
        return paperCommentMapper.deleteCommentById(num, thesisID);
    }

    public int getTIDbySID(int stuID) {
        return paperCommentMapper.getTIDbySID(stuID);
    }

    public int updateStuComment(PaperComment paperComment) {
        return paperCommentMapper.updateStuComment(paperComment);
    }

    public List<Student>getStuIDbyTeaID(int teaID) {
        return paperCommentMapper.getStuIDbyTeaID(teaID);
    }

    public Thesis getThesis(int stuID) {
        return paperCommentMapper.getThesis(stuID);
    }

    public int updateTeaComment(PaperComment paperComment) {
        return  paperCommentMapper.updateTeaComment(paperComment);
    }

    public List<Student> getStuThesis(int teaID) {
        return paperCommentMapper.getStuThesis(teaID);
    }

    public List<PaperComment> selectCommentListStu(int thesisID) {
        return paperCommentMapper.selectCommentListStu(thesisID);
    }


    public Thesis getThesisByTID(Integer thesisID) {
        return paperCommentMapper.getThesisByTID(thesisID);
    }
}
