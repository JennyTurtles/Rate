package org.sys.rate.service.admin;

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

    public Integer getThesisID(int stuID) {
        return paperCommentMapper.getThesisID(stuID);
    }

    public int updateStuComment(PaperComment paperComment) {
        return paperCommentMapper.updateStuComment(paperComment);
    }


    public Thesis getThesis(int stuID, int year, int month) {
        return paperCommentMapper.getThesis(stuID, year, month);
    }

    public int updateTeaComment(PaperComment paperComment) {
        return paperCommentMapper.updateTeaComment(paperComment);
    }

    public List<Student> getStuThesis(int teaID) {
        return paperCommentMapper.getStuThesis(teaID);
    }

    public List<PaperComment> selectCommentListTea(int thesisID) {
        return paperCommentMapper.selectCommentList(thesisID);
    }

    public List<PaperComment> selectCommentListStu(int thesisID) {
        return paperCommentMapper.selectCommentListStu(thesisID);
    }


    public Thesis getThesisByTID(Integer thesisID) {
        return paperCommentMapper.getThesisByTID(thesisID);
    }

    public List<Student> getStuThesis(Integer tutorId, Integer year, Integer month) {
        return paperCommentMapper.getStuThesisWithDate(tutorId, year, month);
    }

    public List<PaperComment> selectCommentListStuOrderByNum(Integer thesisID) {
        return paperCommentMapper.selectCommentListStuOrderByNum(thesisID);
    }

    public int checkSign(Integer thesisID) {
        return paperCommentMapper.checkSign(thesisID);
    }
}
