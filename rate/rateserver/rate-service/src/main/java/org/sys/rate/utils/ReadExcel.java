package org.sys.rate.utils;/**
 * ClassName: ReadExcel
 * Package: org.sys.rate.utils
 * Description:
 *
 * @Author ZYK
 * @Create 2023/8/12 20:43
 * @Version 1.0
 */

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.sys.rate.mapper.StudentMapper;
import org.sys.rate.mapper.UnderGraduateMapper;
import org.sys.rate.model.*;
import org.sys.rate.service.admin.TeachersService;
import org.sys.rate.service.admin.ThesisService;

import javax.annotation.Resource;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @Description:
 * @param: $
 * @return: $
 */
@Service
public class ReadExcel {
    @Resource
    private TeachersService teachersService;
    @Resource
    private UnderGraduateMapper underGraduateMapper;
    @Resource
    private StudentMapper studentMapper;
    @Resource
    private ThesisService thesisService;

    public Msg readStartThesisExcelData(String type, Integer institutionID, MultipartFile file) {
        Msg msg = new Msg();
        List<Thesis> thesisList = new ArrayList<>();
        // 还需要记录多少行成功，多少行失败，多少行重复插入，还有第几行是因为什么原因失败
        DataProcessingResult record = new DataProcessingResult();
        int rowIndex = 0;


        try (Workbook workbook = new HSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0); // 获取第一个工作表
            int Cells = sheet.getRow(0).getPhysicalNumberOfCells();
            HashMap<Integer, String> map = new HashMap<>();
            for (int m = 0; m < Cells; m++) {
                if (sheet.getRow(0).getCell(m).getStringCellValue() != null)
                    map.put(m, sheet.getRow(0).getCell(m).getStringCellValue());
            }
            for (Row row : sheet) {
                ++rowIndex;
                if (rowIndex == 1) {
                    continue; // 跳过第一行
                }
                if (row.getLastCellNum() < 3) {
                    --rowIndex;
                    continue;
                }


                Cell nameCell = null;
                Cell idCell = null;

                Cell gradeCell = null;
                Cell yearCell = null;
                Cell majorCell = null;
                Cell classCell = null;
                Cell teacherJobNumberCell = null;
                Cell teacherNameCell = null;


                for (int k = 0; k < Cells; k++) {
                    Cell cell = row.getCell(k);
                    if (cell != null) {
                        cell.setCellType(CellType.STRING);
                        switch (map.get(k)) {
                            case "学号":
                                idCell = cell;
                                break;
                            case "姓名":
                                nameCell = cell;
                                break;
                            case "导师工号":
                                teacherJobNumberCell = cell;
                                break;
                            case "导师姓名":
                                teacherNameCell = cell;
                                break;
                            case "绩点":
                                gradeCell = cell;
                                break;
                            case "入学年份":
                                yearCell = cell;
                                break;
                            case "专业":
                                majorCell = cell;
                                break;
                            case "班级":
                                classCell = cell;
                                break;
                            default:
                                break;
                        }
                        //break;
                    }
                }

//                if ("student".equals(type)) {
//                    gradeCell = row.getCell(2);
//                    yearCell = row.getCell(3);
//                    majorCell = row.getCell(4);
//                    classCell = row.getCell(5);
//                    teacherNameCell = row.getCell(7);
//                    teacherJobNumberCell = row.getCell(6);
//                } else {
//                    teacherJobNumberCell = row.getCell(2);
//                    teacherNameCell = row.getCell(3);
//                }
                if (idCell == null) {
                    record.setFailReasonForRowIndex(rowIndex, "学生学号为空");
                    continue;
                }
                if (nameCell == null) {
                    record.setFailReasonForRowIndex(rowIndex, "学生姓名为空");
                    continue;
                }


                String name = nameCell.getCellType() == CellType.STRING ? nameCell.getStringCellValue() : "";
                if ("".equals(name)) {
                    record.setFailReasonForRowIndex(rowIndex, "学生姓名为空");
                    continue;
                }
                String major = majorCell != null ? (majorCell.getCellType() == CellType.STRING ? majorCell.getStringCellValue() : "") : "";
                String className = classCell != null ? (classCell.getCellType() == CellType.STRING ? classCell.getStringCellValue() : "") : "";

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

                // 姓名和学号成对出现，且合理
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

                // ?工号可以不存在，但是若是重名必须提供工号！
                if (teacherNameCell == null) {
                    if ("teacher".equals(type)) {
                        record.setFailReasonForRowIndex(rowIndex, "指导教师姓名为空");
                        continue;
                    } else {
                        Thesis thesis = new Thesis();
                        thesis.setGrade(grade);
                        thesis.setStudentID(undergraduateId);
                        thesis.setTutorID(null);

                        thesisList.add(thesis);
                        continue;
                    }
                }

                // 1. 姓名
                // 1.1 姓名为空，教师端出错，学生端就不做判断
                // 1.2 姓名不为空，教师端和学生端都做判断


                String teacherJobNumber = "";
                String teacherName = "";
                Integer tutorID = null;

                try {
                    if (teacherJobNumberCell == null) {
                        teacherJobNumber = null;
                    } else {
                        teacherJobNumber = teacherJobNumberCell.getCellType() == CellType.NUMERIC ? String.valueOf((int) teacherJobNumberCell.getNumericCellValue()) : teacherJobNumberCell.getStringCellValue();
                    }

                    teacherName = teacherNameCell.getStringCellValue();
                    tutorID = teachersService.checkTeacherExist(teacherJobNumber, teacherName, institutionID);
                    if (tutorID == null) {
                        record.setFailReasonForRowIndex(rowIndex, "指导教师姓名不存在");
                        continue;
                    } else if (tutorID.equals(-1)) {
                        record.setFailReasonForRowIndex(rowIndex, "指导教师姓名存在重名，但是未提供工号");
                        continue;
                    } else if (tutorID.equals(-2)) {
                        record.setFailReasonForRowIndex(rowIndex, "指导教师姓名和工号不匹配");
                        continue;
                    }
                } catch (NumberFormatException e) {
                    record.setFailReasonForRowIndex(rowIndex, "指导教师工号或者姓名格式错误");
                    continue;
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
        // 这里获取的都是合法的数据，安心插入或者更新
        successMsg.add("thesis", thesisList).add("record", record).add("total", rowIndex - 1);
        return successMsg;
    }

    private RespBean updateExistOrInsertUndergraduate(UnderGraduate underGraduate, String type) {
        // 检查这个单位这个学号这个姓名是否存在，若不存在则插入，但是若是学号和姓名不匹配应该怎么做？——报错
        // 若学号存在且姓名匹配成功，则返回本科生的id；若学号不存在，则返回null；若学号存在且姓名匹配不成功，则返回-1
        Integer id = underGraduateMapper.checkStudentExist(underGraduate.getStuNumber(), underGraduate.getName(), underGraduate.getInstitutionID());
        if (id != null) {
            if (id.equals(-1)) {
                return RespBean.error("学生学号和姓名不匹配");
            }
            try {
                if ("student".equals(type)) {
                    underGraduateMapper.updateWithInstitutionID(underGraduate);
                }
                return RespBean.ok("exist", id);
            } catch (Exception e) {
                return RespBean.error("更新本科生表出现错误！");
            }
        }
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

    private RespBean updateExistOrInsertUndergraduate(UnderGraduate underGraduate) {
        Integer id = underGraduateMapper.checkStudentExist(underGraduate.getStuNumber(), underGraduate.getName(), underGraduate.getInstitutionID());
        if (id == null) {
            return RespBean.error("学生学号和姓名在本单位中不存在");
        } else if (id.equals(-1)) {
            return RespBean.error("学生学号和姓名不匹配");
        }
        return RespBean.ok("", id);

    }

    public Msg readThesisNameExcelData(Integer tutorId, Integer institutionID, Integer startThesisID, MultipartFile file) {
        Msg msg = new Msg();
        // 还需要记录多少行成功，多少行失败，多少行重复插入，还有第几行是因为什么原因失败
        DataProcessingResult record = new DataProcessingResult();
        int rowIndex = 0;
        int updateRows = 0;


        try (Workbook workbook = new HSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0); // 获取第一个工作表
            for (Row row : sheet) {
                ++rowIndex;
                if (rowIndex == 1) {
                    continue; // 跳过第一行
                }

                Cell idCell = row.getCell(0);
                Cell nameCell = row.getCell(1);
                Cell thesisNameCell = row.getCell(2);


                if (idCell == null) {
                    record.setFailReasonForRowIndex(rowIndex, "第一列学生学号为空");
                    continue;
                }
                if (nameCell == null) {
                    record.setFailReasonForRowIndex(rowIndex, "第二列学生姓名为空");
                    continue;
                }


                String name = nameCell.getCellType() == CellType.STRING ? nameCell.getStringCellValue() : "";
                String thesisName = thesisNameCell.getCellType() == CellType.STRING ? thesisNameCell.getStringCellValue() : "";


                // ?姓名和学号是否匹配
                String studentNumber;
                Integer undergraduateId = null;
                try {
                    studentNumber = idCell.getCellType() == CellType.NUMERIC ? String.valueOf((int) idCell.getNumericCellValue()) : idCell.getStringCellValue();
                    UnderGraduate underGraduate = new UnderGraduate();
                    underGraduate.setInstitutionID(institutionID);
                    underGraduate.setName(name);
                    underGraduate.setStuNumber(studentNumber);
                    RespBean existOrInsertResBean = updateExistOrInsertUndergraduate(underGraduate);
                    if (existOrInsertResBean.getStatus() == 500) {
                        record.setFailReasonForRowIndex(rowIndex, existOrInsertResBean.getMsg());
                        continue;
                    } else {
                        // 获取到undergraduate.id
                        undergraduateId = (Integer) existOrInsertResBean.getObj();
                    }
                } catch (NumberFormatException e) {
                    record.setFailReasonForRowIndex(rowIndex, "第一列学生学号单元格式错误");
                    continue;
                }

                // ?该学生属于此次毕业设计吗？若属于则进行更新，否则就不需要更新，并且返回错误信息
                Thesis thesis = new Thesis();
                thesis.setName(thesisName);
                thesis.setStudentID(undergraduateId);
                thesis.setTutorID(tutorId);
                thesis.setStartThesisID(startThesisID);

                RespBean existOrUpdate = notExistOrUpdate(thesis);

                if (existOrUpdate.getStatus().equals(500)) {
                    record.setFailReasonForRowIndex(rowIndex, "更新毕业论文题目失败");
                    continue;
                }
                // 若该学生不属于本次毕业论文
                if (existOrUpdate.getObj().equals(0)) {
                    record.setFailReasonForRowIndex(rowIndex, "该学生不属于本次毕业设计");
                    continue;
                }
                ++updateRows;
            }
        } catch (IOException e) {
            e.printStackTrace();
            msg.setMsg("读取文件时发生错误！");
            return msg.fail();
        }
        Msg successMsg = Msg.success();
        // 这里获取的都是合法的数据，安心插入或者更新
        successMsg.add("record", record).add("total", rowIndex - 1).add("update", updateRows);
        return successMsg;
    }

    private RespBean notExistOrUpdate(Thesis thesis) {
        try {
            Integer ifExist = thesisService.notExistOrUpdate(thesis);
            return RespBean.ok("", ifExist);
        } catch (Exception e) {
            return RespBean.error("");
        }
    }


}
