package org.sys.rate.service.admin;

import org.springframework.stereotype.Service;
import org.sys.rate.mapper.PaperCommentMapper;
import org.sys.rate.model.PaperComment;
import org.sys.rate.model.ProgramRecord;
import org.sys.rate.model.Student;
import org.sys.rate.model.Thesis;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
        List<PaperComment> data = paperCommentMapper.selectCommentList(thesisID);
        for (int i = 0; i < data.size(); i++) {
            data.get(i).setIndex(i+1);
        }
        List<PaperComment> list3 = data.stream().filter(s -> Objects.equals(s.getIsPass(), "tea_pass")).collect(Collectors.toList());
        List<PaperComment> list2 = data.stream().filter(s -> Objects.equals(s.getIsPass(), "tea_deny")).collect(Collectors.toList());
        List<PaperComment> list1 = data.stream().filter(s -> Objects.equals(s.getIsPass(), "")).collect(Collectors.toList());
        List<PaperComment> combinedList = new ArrayList<>();
        combinedList.addAll(list1);
        combinedList.addAll(list2);
        combinedList.addAll(list3);
        return  combinedList;
    }

    public List<PaperComment> selectCommentListStu(int thesisID) {
        List<PaperComment> data = paperCommentMapper.selectCommentListStuOrderByNum(thesisID);
        for (int i = 0; i < data.size(); i++) {
            data.get(i).setIndex(i+1);
        }
        List<PaperComment> list3 = data.stream().filter(s -> Objects.equals(s.getIsPass(), "tea_deny")).collect(Collectors.toList());
        List<PaperComment> list2 = data.stream().filter(s -> !(Objects.equals(s.getIsPass(), "tea_deny"))).collect(Collectors.toList());
        List<PaperComment> combinedList = new ArrayList<>();
        combinedList.addAll(list3);
        combinedList.addAll(list2);
        return  combinedList;
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

    public List<PaperComment> getBeforeAfterRecordStu(PaperComment paperComment) {
        List<PaperComment> data = paperCommentMapper.selectRecordListStu(paperComment.getThesisID());
        // 如果没有现有记录，直接返回空列表
        if (data.isEmpty()) {
            return new ArrayList<>();
        }
        // 获取第一条和最后一条记录
        PaperComment firstComment = data.get(0);
        PaperComment lastComment = data.get(data.size() - 1);
        // 检查时间范围--如果大于最后一条结束时间说明插入在最后，直接返回空列表
        if (paperComment.getDateStu().compareTo(lastComment.getDateStu()) > 0) {
            return new ArrayList<>();
        }else if(paperComment.getDateStu().compareTo(firstComment.getDateStu()) < 0){
            // 如果小于第一条开始时间说明插入在最前，返回第一条记录
            return Arrays.asList(firstComment);
        }

        List<PaperComment> combinedList = new ArrayList<>();

        PaperComment before = new PaperComment();
        PaperComment after = new PaperComment();

        // 查找符合条件的记录
        for (int i = 0; i < data.size(); i++) {
            PaperComment comment = data.get(i);
            if (comment.getDateStu().compareTo(paperComment.getDateStu()) > 0) {
                break;
            }
            before =comment;
        }
        combinedList.add(before);
        for (int i = data.size()-1; i >=0 ; i--) {
            PaperComment comment = data.get(i);
            if (comment.getDateStu().compareTo(paperComment.getDateStu()) < 0) {
                break;
            }
            after =comment;

        }
        combinedList.add(after);
        return combinedList;
    }
}
