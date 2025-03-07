package org.sys.rate.controller.admin;

import org.springframework.web.bind.annotation.*;
import org.sys.rate.config.JsonResult;
import org.sys.rate.mapper.GraduateStudentMapper;
import org.sys.rate.mapper.ProgramRecordMapper;
import org.sys.rate.model.GraduateStudent;
import org.sys.rate.model.PaperComment;
import org.sys.rate.model.ProgramRecord;
import org.sys.rate.model.RespBean;
import org.sys.rate.service.admin.ProgramRecordService;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/programRecord/basic")
public class ProgramRecordController {
    @Resource
    ProgramRecordMapper programRecordMapper;
    @Resource
    GraduateStudentMapper graduateStudentMapper;
    @Resource
    ProgramRecordService programRecordService;

    @GetMapping("/getAllRecordStu")
    public JsonResult<List> getAllRecordStu(Integer studentID) {
        List<ProgramRecord> data = programRecordMapper.selectRecordListStu(studentID);
        for (int i = 0; i < data.size(); i++) {
            data.get(i).setIndex(i+1);
        }
        List<ProgramRecord> list3 = data.stream().filter(s -> Objects.equals(s.getIsPass(), "tea_deny")).collect(Collectors.toList());
        List<ProgramRecord> list2 = data.stream().filter(s -> !(Objects.equals(s.getIsPass(), "tea_deny"))).collect(Collectors.toList());

        List<ProgramRecord> combinedList = new ArrayList<>();
        combinedList.addAll(list3);
        combinedList.addAll(list2);

        return new JsonResult(combinedList);
    }

    @GetMapping("/getBeforeAfterRecordStu")
    public JsonResult<List> getBeforeAfterRecordStu(ProgramRecord programRecord) {
        List<ProgramRecord> data = programRecordMapper.selectRecordListStu(programRecord.getStudentID());
        // 如果没有现有记录，直接返回空列表
        if (data.isEmpty()) {
            return new JsonResult(new ArrayList<>());
        }
        // 获取第一条和最后一条记录
        ProgramRecord firstRecord = data.get(0);
        ProgramRecord lastRecord = data.get(data.size() - 1);
        // 检查时间范围
        if (programRecord.getStartDateStu().compareTo(lastRecord.getEndDateStu()) > 0 ||
                programRecord.getEndDateStu().compareTo(firstRecord.getStartDateStu()) < 0) {
            return new JsonResult(new ArrayList<>());
        }
        List<Integer> combinedList = new ArrayList<>();
        // 初始化索引
        int minEndIndex = -1;
        int maxStartIndex = -1;

        // 查找符合条件的记录
        for (int i = 0; i < data.size(); i++) {
            ProgramRecord record = data.get(i);
            if (record.getEndDateStu().compareTo(programRecord.getStartDateStu()) > 0) {
                break;
            }
            minEndIndex =i;
        }
        for (int i = data.size()-1; i >=0 ; i--) {
            ProgramRecord record = data.get(i);
            if (record.getStartDateStu().compareTo(programRecord.getEndDateStu()) < 0) {
                break;
            }
            maxStartIndex = i;

        }
        combinedList.add(minEndIndex+1);
        combinedList.add(maxStartIndex+1);
        return new JsonResult(combinedList);
    }

    @GetMapping("/getStuByTea")
    public RespBean getStuByTea(Integer tutorID) {
        List<GraduateStudent> data = programRecordMapper.getGraduateStudentsOfTutorID(tutorID);
        return RespBean.ok("success",data);
    }

    @PostMapping("/add")
    public JsonResult addSave(ProgramRecord programRecord) throws MessagingException {
        Integer res = programRecordService.addSave(programRecord);
        return new JsonResult(res);
    }


    @DeleteMapping("/remove/{num}/{studentID}")
    public JsonResult deleteRecordById(@PathVariable("num") int num, @PathVariable("studentID") int studentID) {
        Integer res = programRecordService.deleteRecordById(num, studentID);
        return new JsonResult(res);
    }

    @PostMapping("/edit")
    public JsonResult editSave(ProgramRecord programRecord) throws MessagingException {
        return new JsonResult(programRecordService.updateStuRecord(programRecord));
    }

    @PostMapping("/fillMissChange")
    public JsonResult fillMissChange(Integer studentID, Integer fillMiss) throws MessagingException {
        return new JsonResult(programRecordService.fillMissChange(studentID, fillMiss));
    }

    @GetMapping("/getStuByFilter")
    public RespBean getStuByFilter(@RequestParam String searchNumber,
                                   @RequestParam String searchName,
                                   @RequestParam String searchSpecialty,
                                   @RequestParam String searchStuType,
                                   @RequestParam List<Integer> searchYears,
                                   @RequestParam(required = false) String searchTutorName,
                                   @RequestParam(required = false) Integer minWorkHours,
                                   @RequestParam(required = false) Integer maxWorkHours,
                                   @RequestParam(required = false) Integer tutorID) {
        List<GraduateStudent> data = programRecordMapper.getStuByFilter(searchNumber, searchName, searchSpecialty, searchStuType, searchYears, searchTutorName, minWorkHours, maxWorkHours, tutorID);
        return RespBean.ok("success",data);
    }

    @GetMapping("/getFillMiss")
    public RespBean getFillMiss(Integer studentID) {
        Integer data = programRecordMapper.getFillMiss(studentID);
        return RespBean.ok("success",data);
    }

    @GetMapping("/getAllRecordTea")
    public JsonResult<List> getAllRecordTea(Integer studentID) {
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

        return new JsonResult(combinedList);
    }

    @PostMapping("/updateTea")
    public JsonResult updateTeaComment(@RequestBody ProgramRecord programRecord) {
        return new JsonResult(programRecordMapper.updateTeaComment(programRecord));
    }
}
