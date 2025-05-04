package org.sys.rate.service.admin;

import cn.hutool.core.util.RandomUtil;
import org.springframework.stereotype.Service;
import org.sys.rate.mapper.GraduateStudentMapper;
import org.sys.rate.mapper.ProgramRecordMapper;
import org.sys.rate.model.*;
import org.sys.rate.service.mail.MailToStuService;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ProgramRecordService {
    @Resource
    ProgramRecordMapper programRecordMapper;
    @Resource
    GraduateStudentMapper graduateStudentMapper;
    @Resource
    private MailToStuService mailToStuService;
    @Resource
    private XinProjectService xinProjectService;
    // 添加记录
    public Integer addSave(ProgramRecord programRecord) {
        Integer studentID = programRecordMapper.getIDByStudentID(programRecord.getStudentID());
        programRecord.setStudentID(studentID);
        return programRecordMapper.add(programRecord);
    }

    //删除记录
    public Integer deleteRecordById(int ID, int studentID) {
        return programRecordMapper.deleteRecordById(ID, studentID);
    }

    public int updateStuRecord(ProgramRecord programRecord) {
        Integer studentID = programRecordMapper.getIDByStudentID(programRecord.getStudentID());
        programRecord.setStudentID(studentID);
        return programRecordMapper.updateStuRecord(programRecord);
    }

    public int fillMissChange(Integer studentID, Integer fillMiss) {
        return programRecordMapper.fillMissChange(studentID, fillMiss);
    }

    public List<ProgramRecord> getAllRecordTea(Integer studentID) {
        List<ProgramRecord> data = programRecordMapper.selectRecordListTea(studentID);
        for (int i = 0; i < data.size(); i++) {
            data.get(i).setIndex(i+1);
        }
        List<ProgramRecord> list3 = data.stream().filter(s -> Objects.equals(s.getIsPass(), "tea_pass")).collect(Collectors.toList());
        List<ProgramRecord> list2 = data.stream().filter(s -> Objects.equals(s.getIsPass(), "tea_deny")).collect(Collectors.toList());
        List<ProgramRecord> list1 = data.stream().filter(s -> Objects.equals(s.getIsPass(), "")).collect(Collectors.toList());
        List<ProgramRecord> combinedList = new ArrayList<>();
        combinedList.addAll(list1);
        combinedList.addAll(list2);
        combinedList.addAll(list3);
        return  combinedList;
    }

    public List<ProgramRecord> getBeforeAfterRecordStu(ProgramRecord programRecord) {
        List<ProgramRecord> data = programRecordMapper.selectRecordListStu(programRecord.getStudentID());
        // 如果没有现有记录，直接返回空列表
        if (data.isEmpty()) {
            return new ArrayList<>();
        }
        // 获取第一条和最后一条记录
        ProgramRecord firstRecord = data.get(0);
        ProgramRecord lastRecord = data.get(data.size() - 1);
        // 检查时间范围--如果大于最后一条结束时间说明插入在最后，直接返回空列表
        if (programRecord.getStartDateStu().compareTo(lastRecord.getEndDateStu()) > 0) {
            return new ArrayList<>();
        }else if(programRecord.getEndDateStu().compareTo(firstRecord.getStartDateStu()) < 0){
            // 如果小于第一条开始时间说明插入在最前，返回第一条记录
            return Arrays.asList(firstRecord);
        }

        List<ProgramRecord> combinedList = new ArrayList<>();

        ProgramRecord before = new ProgramRecord();
        ProgramRecord after = new ProgramRecord();

        // 查找符合条件的记录
        for (int i = 0; i < data.size(); i++) {
            ProgramRecord record = data.get(i);
            if (record.getEndDateStu().compareTo(programRecord.getStartDateStu()) > 0) {
                break;
            }
            before =record;
        }
        combinedList.add(before);
        for (int i = data.size()-1; i >=0 ; i--) {
            ProgramRecord record = data.get(i);
            if (record.getStartDateStu().compareTo(programRecord.getEndDateStu()) < 0) {
                break;
            }
            after =record;

        }
        combinedList.add(after);
        return combinedList;
    }

    public List<ProgramRecord> getAllRecordStu(Integer studentID) {
        List<ProgramRecord> data = programRecordMapper.selectRecordListStu(studentID);
        for (int i = 0; i < data.size(); i++) {
            data.get(i).setIndex(i+1);
        }
        List<ProgramRecord> list3 = data.stream().filter(s -> Objects.equals(s.getIsPass(), "tea_deny")).collect(Collectors.toList());
        List<ProgramRecord> list2 = data.stream().filter(s -> !(Objects.equals(s.getIsPass(), "tea_deny"))).collect(Collectors.toList());

        List<ProgramRecord> combinedList = new ArrayList<>();
        combinedList.addAll(list3);
        combinedList.addAll(list2);
        return  combinedList;
    }

    public int addResult(ProgramResult programResult) {
        int rlt = programRecordMapper.addResult(programResult);

        XinProject xinProject = new XinProject("项目开发申报", 3, programResult.getAuthor(),
                programResult.getState(), programResult.getRemark(), programResult.getId(), "项目开发", programResult.getStudentId());
        xinProjectService.insertXinProject(xinProject);
        return rlt;
    }

    public ProgramResult selectProgramResultById(Long id) {
        return programRecordMapper.selectProgramResultById(id);
    }

    public Integer editResult(ProgramResult programResult) {
        XinProject xinProject = new XinProject("项目开发申报", 3, programResult.getAuthor(),
                "commit", programResult.getRemark(), programResult.getId(), "项目开发", programResult.getStudentId());
        xinProjectService.updateXinProject(xinProject);
        return programRecordMapper.updateResult(programResult);
    }

    public int getDtaByStuID(int id) {
        return programRecordMapper.getDtaByStuID(id);

    }

    public List<ProgramResult> searchHorizontalProjectByConditions(String studentName, String state, String projectName, String pointFront, String pointBack) {
        List<ProgramResult> list = programRecordMapper.searchHorizontalProjectByConditions(studentName, state, projectName, pointFront, pointBack);
        return list;
    }

    public int editState(String state, Long ID) throws MessagingException {
        ProgramResult programResult = programRecordMapper.getById(Math.toIntExact(ID));
//        mailToStuService.sendStuMail(state, project, null, "纵向科研项目");
        programResult.setState(state);
        XinProject xinProject = new XinProject("项目开发申报", 3, programResult.getAuthor(),
                programResult.getState(), programResult.getRemark(), programResult.getId(), "项目开发", programResult.getStudentId());
        xinProjectService.updateXinProject(xinProject);
        if (state.equals("adm_pass")) {
            int stuID = programResult.getStudentId();
            int score = programResult.getPoint();
            if (programRecordMapper.checkScore(stuID) != null) { // 已经申报过成果,将该论文的have_score设置为0
                return programRecordMapper.editState2(state, ID, 0);
            } else {
                programRecordMapper.editState2(state, ID, 1);
                return programRecordMapper.updateScore((long) stuID, (long) score);
            }
        }
        return programRecordMapper.editState(state, ID);
    }

    public Integer deleteById(Long id) {
        ProgramResult programResult = new ProgramResult();
        programResult.setId(id.intValue());
        xinProjectService.deleteXinProject(id.intValue(), "项目开发");
        return programRecordMapper.deleteById(id);
    }

    public List<ProgramResult> selectListByIds(Integer studentID) {
        return programRecordMapper.selectProgramResultListById(studentID);
    }
}
