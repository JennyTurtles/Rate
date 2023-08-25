package org.sys.rate.utils;/**
 * ClassName: ReadExcel
 * Package: org.sys.rate.utils
 * Description:
 *
 * @Author ZYK
 * @Create 2023/8/12 20:43
 * @Version 1.0
 */

import com.github.pagehelper.util.StringUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.sys.rate.mapper.StudentMapper;
import org.sys.rate.mapper.TeachersMapper;
import org.sys.rate.mapper.UnderGraduateMapper;
import org.sys.rate.model.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Description:
 * @param: $
 * @return: $
 */
@Service
public class ReadExcel {
    @Resource
    private TeachersMapper teachersMapper;
    @Resource
    private UnderGraduateMapper underGraduateMapper;
    @Resource
    private StudentMapper studentMapper;

    public Msg readStartThesisExcelData(String type, Integer institutionID, MultipartFile file) {
        Msg msg = new Msg();
        List<Thesis> thesisList = new ArrayList<>();
        // 还需要记录多少行成功，多少行失败，多少行重复插入，还有第几行是因为什么原因失败
        DataProcessingResult record = new DataProcessingResult();
        int rowIndex = 0;


        try (Workbook workbook = new HSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0); // 获取第一个工作表
            for (Row row : sheet) {
                ++rowIndex;
                if (rowIndex == 1) {
                    continue; // 跳过第一行
                }

                Cell nameCell = row.getCell(0);
                Cell idCell = row.getCell(1);

                Cell gradeCell = null;
                Cell yearCell = null;
                Cell majorCell = null;
                Cell classCell = null;
                Cell teacherJobNumberCell;

                if ("student".equals(type)) {
                    gradeCell = row.getCell(2);
                    yearCell = row.getCell(3);
                    majorCell = row.getCell(4);
                    classCell = row.getCell(5);
                    teacherJobNumberCell = row.getCell(7);
                } else {
                    teacherJobNumberCell = row.getCell(3);
                }

                if (idCell == null) {
                    record.getFailureReasons().put(rowIndex, "学生学号为空");
                    continue;
                }
                String name = nameCell != null ? nameCell.getStringCellValue() : "";
                String major = majorCell != null ? majorCell.getStringCellValue() : "";
                String className = classCell != null ? classCell.getStringCellValue() : "";

                Integer year = null;
                if (yearCell != null) {
                    try {
                        year = yearCell.getCellType() == CellType.NUMERIC ? (int) yearCell.getNumericCellValue() : Integer.parseInt(yearCell.getStringCellValue());
                        if (year < 2000 || year > LocalDateTime.now().getYear()) {
                            record.setFailReasonForRowIndex(rowIndex, "入学年份不合法");
                            continue;
                        }
                    } catch (NumberFormatException e) {
                        record.setFailReasonForRowIndex(rowIndex, "入学年份格式错误");
                        continue;
                    }
                }

                String studentNumber;
                Integer undergraduateId = null;
                try {
                    studentNumber = idCell.getCellType() == CellType.NUMERIC ? String.valueOf((int) idCell.getNumericCellValue()) : idCell.getStringCellValue();
                    UnderGraduate underGraduate = new UnderGraduate();
                    underGraduate.setInstitutionID(institutionID);
                    underGraduate.setName(name);
                    underGraduate.setSpecialty(major);
                    underGraduate.setClassName(className);
                    underGraduate.setStuNumber(studentNumber);
                    underGraduate.setYear(year);
                    RespBean existOrInsertResBean = updateExistOrInsertUndergraduate(underGraduate, type);
                    if (existOrInsertResBean.getStatus() == 500) {
                        record.setFailReasonForRowIndex(rowIndex, existOrInsertResBean.getMsg());
                        continue;
                    } else {
                        // 获取到undergraduate.id
                        undergraduateId = (Integer) existOrInsertResBean.getObj();
                    }
                } catch (NumberFormatException e) {
                    record.setFailReasonForRowIndex(rowIndex, "学生学号单元格式错误");
                    continue;
                }


                Double grade = 0.0;
                if (gradeCell != null) {
                    try {
                        grade = gradeCell.getCellType() == CellType.NUMERIC ? gradeCell.getNumericCellValue() : Double.parseDouble(gradeCell.getStringCellValue());
                    } catch (NumberFormatException e) {
                        record.setFailReasonForRowIndex(rowIndex, "绩点格式错误");
                        continue;
                    }
                }


                String teacherJobNumber = "";
                Integer tutorID = null;
                if (teacherJobNumberCell != null) {
                    try {
                        teacherJobNumber = teacherJobNumberCell.getCellType() == CellType.NUMERIC ? String.valueOf((int) teacherJobNumberCell.getNumericCellValue()) : teacherJobNumberCell.getStringCellValue();
                        tutorID = teachersMapper.getTutorIdByJobNumAndInstitutionID(teacherJobNumber, institutionID);
                        if (tutorID == null) {
                            record.setFailReasonForRowIndex(rowIndex, "教师工号不存在");
                            continue;
                        }
                    } catch (NumberFormatException e) {
                        record.setFailReasonForRowIndex(rowIndex, "教师工号格式错误");
                        continue;
                    }
                } else {
                    if ("teacher".equals(type)) {
                        record.setFailReasonForRowIndex(rowIndex, "教师工号为空");
                        continue;
                    }
                }


                Thesis thesis = new Thesis();
                thesis.setGrade(grade);
                thesis.setStudentID(undergraduateId);
                thesis.setTutorID(tutorID);

                thesisList.add(thesis);
            }
        } catch (IOException e) {
            e.printStackTrace();
            msg.setMsg("读取文件时发生错误！");
            return msg.fail();
        }
        Msg successMsg = Msg.success();
        // 这里获取的都是学生学号、教师工号合法的数据，已经不合法的数据。
        successMsg.add("thesis", thesisList).add("record", record).add("total", rowIndex - 1);
        return successMsg;
    }

    private RespBean updateExistOrInsertUndergraduate(UnderGraduate underGraduate, String type) {
        Integer id = underGraduateMapper.checkStudentExist(underGraduate.getStuNumber(), underGraduate.getInstitutionID());
        if (id != null && "student".equals(type)) {
            try {
                underGraduateMapper.updateWithInstitutionID(underGraduate);
                return RespBean.ok("exist", id);
            } catch (Exception e) {
                return RespBean.error("更新本科生表出现错误！");
            }
        } else if (id == null) {
            // 首先插入到student表，获取主键id，然后插入undergraduate表中
            Student student = new Student();
            student.setName(underGraduate.getName());
            student.setInstitutionid(underGraduate.getInstitutionID());
            student.setDeleteflag(0);
            student.setRole("10");
            try {
                studentMapper.insertReturnId(student);
            } catch (Exception e) {
                return RespBean.error("插入学生出错！");
            }

            underGraduate.setStudentID(student.getID());
            try {
                underGraduateMapper.addReturnId(underGraduate);
                return RespBean.ok("", underGraduate.getID());
            } catch (Exception e) {
                return RespBean.error("插入本科生出错！");
            }
        }
        return RespBean.ok("", id);
    }
}
