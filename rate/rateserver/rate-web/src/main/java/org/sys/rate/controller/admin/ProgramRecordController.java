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
import java.util.Arrays;
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
        List<ProgramRecord> combinedList = programRecordService.getAllRecordStu(studentID);
        return new JsonResult(combinedList);
    }

    //插入在中间的记录返回前后两条记录
    @GetMapping("/getBeforeAfterRecordStu")
    public JsonResult<List> getBeforeAfterRecordStu(ProgramRecord programRecord) {
        List<ProgramRecord> combinedList = programRecordService.getBeforeAfterRecordStu(programRecord);
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


    @DeleteMapping("/remove/{ID}/{studentID}")
    public JsonResult deleteRecordById(@PathVariable("ID") int ID, @PathVariable("studentID") int studentID) {
        Integer res = programRecordService.deleteRecordById(ID, studentID);
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
        List<ProgramRecord> allRecordTea = programRecordService.getAllRecordTea(studentID);
        return new JsonResult(allRecordTea);
    }

    @PostMapping("/updateTea")
    public JsonResult updateTeaComment(@RequestBody ProgramRecord programRecord) {
        return new JsonResult(programRecordMapper.updateTeaComment(programRecord));
    }
}
