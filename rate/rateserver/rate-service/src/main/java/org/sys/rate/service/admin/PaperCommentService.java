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
    public Integer insertComment(Integer total, PaperComment paperComment) {
        if (paperComment.getNum() < total)
            paperCommentMapper.addNum(paperComment.getThesisID(),paperComment.getNum(),total);
        return paperCommentMapper.insertComment(paperComment);
    }

    public Integer deleteCommentById(int num, int thesisID) {
        return paperCommentMapper.deleteCommentById(num, thesisID);
    }

    public Integer getThesisID(int stuID) {
        return paperCommentMapper.getThesisID(stuID);
    }

    public int updateStuComment(PaperComment paperComment) {
        PaperComment old = paperCommentMapper.selectByID(paperComment.getID());
        Integer oldNum = old.getNum();
        Integer newNum = paperComment.getNum();
        if (newNum < oldNum)
            paperCommentMapper.addNum(paperComment.getThesisID(), newNum, oldNum);
        else if (newNum > oldNum)
            paperCommentMapper.subNum(paperComment.getThesisID(), oldNum, newNum);
        return paperCommentMapper.updateStuComment(paperComment);
    }


    public Thesis getThesis(int stuID, int startThesisID) {
        return paperCommentMapper.getThesis(stuID, startThesisID);
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
        return paperCommentMapper.selectCommentListStuOrderByNum(thesisID);
    }


    public Thesis getThesisByTID(Integer thesisID) {
        return paperCommentMapper.getThesisByTID(thesisID);
    }

    public List<Student> getStuThesis(Integer tutorId, Integer startThesisID) {
        return paperCommentMapper.getStuThesisWithDate(tutorId, startThesisID);
    }

    public List<Student> getStuAllThesesByTutor(Integer tutorId) {
        return paperCommentMapper.getStuThesisWithNoDate(tutorId);
    }

    public List<PaperComment> selectCommentListStuOrderByNum(Integer thesisID) {
        return paperCommentMapper.selectCommentListStuOrderByNum(thesisID);
    }

    public int checkSign(Integer thesisID) {
        return paperCommentMapper.checkSign(thesisID);
    }

    public int fillMissChange(Integer thesisID, Integer fillMiss) {
        return paperCommentMapper.fillMissChange(thesisID, fillMiss);
    }

    public int getFillMiss(Integer thesisID) {
        return paperCommentMapper.getFillMiss(thesisID);
    }
}
