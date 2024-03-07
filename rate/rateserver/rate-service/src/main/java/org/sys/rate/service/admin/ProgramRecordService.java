package org.sys.rate.service.admin;

import org.springframework.stereotype.Service;
import org.sys.rate.mapper.GraduateStudentMapper;
import org.sys.rate.mapper.ProgramRecordMapper;
import org.sys.rate.model.PaperComment;
import org.sys.rate.model.ProgramRecord;

import javax.annotation.Resource;

@Service
public class ProgramRecordService {
    @Resource
    ProgramRecordMapper programRecordMapper;
    @Resource
    GraduateStudentMapper graduateStudentMapper;

    // 添加记录
    public Integer addSave(ProgramRecord programRecord) {
        Integer studentID = programRecordMapper.getIDByStudentID(programRecord.getStudentID());
        programRecord.setStudentID(studentID);
        return programRecordMapper.add(programRecord);
    }

    //删除记录
    public Integer deleteRecordById(int num, int studentID) {
        return programRecordMapper.deleteRecordById(num, studentID);
    }

    public int updateStuRecord(ProgramRecord programRecord) {
        Integer studentID = programRecordMapper.getIDByStudentID(programRecord.getStudentID());
        programRecord.setStudentID(studentID);
        return programRecordMapper.updateStuRecord(programRecord);
    }

    public int fillMissChange(Integer studentID, Integer fillMiss) {
        return programRecordMapper.fillMissChange(studentID, fillMiss);
    }
}
