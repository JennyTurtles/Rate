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
import org.sys.rate.model.*;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * @Description:
 * @param: $
 * @return: $
 */
@Service
public class ReadExcel {
    public Msg readExcelData(MultipartFile file) {
        Msg msg = new Msg();
        Set<UnderGraduate> studentList = new HashSet<>();
        Set<Thesis> thesisList = new HashSet<>();
        Set<Teachers> teachersSet = new HashSet<>();


        try (Workbook workbook = new HSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0); // 获取第一个工作表
            // ?若遇到备注信息，可以直接忽略！不要报错，但是感觉也没有必要。
            int rowIndex = 0;
            for (Row row : sheet) {
                if (rowIndex == 0) {
                    rowIndex++;
                    continue; // 跳过第一行
                }
                Cell nameCell = row.getCell(0);
                Cell idCell = row.getCell(1);
                Cell yearCell = row.getCell(2);
                Cell majorCell = row.getCell(3);
                Cell classCell = row.getCell(4);
                Cell teacherNameCell = row.getCell(5);
                Cell teacherJobNumberCell = row.getCell(6);

                if (idCell != null && teacherJobNumberCell != null) {
                    String name = nameCell != null ? nameCell.getStringCellValue() : "";
                    String major = majorCell != null ? majorCell.getStringCellValue() : "";
                    String className = classCell != null ? classCell.getStringCellValue() : "";
                    String teacherName = teacherNameCell != null ? teacherNameCell.getStringCellValue() : "";

                    String studentNumber;
                    try {
                        if (idCell.getCellType() == CellType.NUMERIC) {
                            studentNumber = String.valueOf(idCell.getNumericCellValue());
                        } else if (idCell.getCellType() == CellType.STRING) {
                            studentNumber = idCell.getStringCellValue();
                        } else {
                            msg.setMsg("无法获取学生学号！");
                            return msg.fail();
                        }
                    } catch (NumberFormatException e) {
                        msg.setMsg("无法获取学生学号！");
                        return msg.fail();
                    }


                    int year;
                    try {
                        if (yearCell != null) {
                            if (yearCell.getCellType() == CellType.NUMERIC) {
                                year = (int) yearCell.getNumericCellValue();
                            } else if (yearCell.getCellType() == CellType.STRING) {
                                year = Integer.parseInt(yearCell.getStringCellValue());
                            } else {
                                msg.setMsg("无法获取年份！");
                                return msg.fail();
                            }
                        } else {
                            year = 0;
                        }
                    } catch (NumberFormatException e) {
                        msg.setMsg("无法获取年份！");
                        return msg.fail();
                    }

                    String teacherJobNumber;
                    try {
                        if (teacherJobNumberCell.getCellType() == CellType.NUMERIC) {
                            teacherJobNumber = String.valueOf(teacherJobNumberCell.getNumericCellValue());
                        } else if (teacherJobNumberCell.getCellType() == CellType.STRING) {
                            teacherJobNumber = teacherJobNumberCell.getStringCellValue();
                        } else {
                            msg.setMsg("无法获取导师编号！");
                            return msg.fail();
                        }
                    } catch (NumberFormatException e) {
                        msg.setMsg("无法获取导师编号！");
                        return msg.fail();
                    }


                    UnderGraduate student = new UnderGraduate();
                    Teachers teacher = new Teachers();
                    Thesis thesis = new Thesis();
                    student.setName(name);
                    student.setStuNumber(studentNumber);
                    student.setYear(year == 0 ? null : year);
                    student.setSpecialty(major);
                    student.setClassName(className);
                    teacher.setName(teacherName);
                    teacher.setJobnumber(teacherJobNumber);
                    thesis.setStudentNumber(studentNumber);
                    thesis.setTutorNumber(teacherJobNumber);

                    studentList.add(student);
                    teachersSet.add(teacher);
                    thesisList.add(thesis);
                } else {
                    msg.setMsg("学生学号和导师编号不能为空！");
                    return msg.fail();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            msg.setMsg("读取文件时发生错误！");
            return msg.fail();
        }
        Msg successMsg = Msg.success();
        successMsg.add("student", studentList).add("teacher", teachersSet).add("thesis", thesisList);
        return successMsg;
    }

    public Msg readStartThesisExcelData(MultipartFile file) {
        Msg msg = new Msg();
        Set<UnderGraduate> studentList = new HashSet<>();
        Set<Thesis> thesisList = new HashSet<>();
        Set<Teachers> teachersSet = new HashSet<>();


        try (Workbook workbook = new HSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0); // 获取第一个工作表
            int rowIndex = 0;
            for (Row row : sheet) {
                if (rowIndex == 0) {
                    rowIndex++;
                    continue; // 跳过第一行
                }
                Cell nameCell = row.getCell(0);
                Cell idCell = row.getCell(1);
                Cell yearCell = row.getCell(2);
                Cell majorCell = row.getCell(3);
                Cell classCell = row.getCell(4);
                Cell teacherNameCell = row.getCell(5);
                Cell teacherJobNumberCell = row.getCell(6);

                if (idCell != null) {
                    String name = nameCell != null ? nameCell.getStringCellValue() : "";
                    String major = majorCell != null ? majorCell.getStringCellValue() : "";
                    String className = classCell != null ? classCell.getStringCellValue() : "";
                    String teacherName = teacherNameCell != null ? teacherNameCell.getStringCellValue() : "";

                    String studentNumber;
                    try {
                        studentNumber = idCell.getCellType() == CellType.NUMERIC ? String.valueOf((int)idCell.getNumericCellValue()) : idCell.getStringCellValue();
                    } catch (NumberFormatException e) {
                        msg.setMsg("无法获取学生学号！");
                        return msg.fail();
                    }


                    Integer year;
                    if (yearCell != null) {
                        try {
                            year = yearCell.getCellType() == CellType.NUMERIC ? (int) yearCell.getNumericCellValue() : Integer.parseInt(yearCell.getStringCellValue());
                        } catch (NumberFormatException e) {
                            msg.setMsg("无法获取年份！");
                            return msg.fail();
                        }
                    } else {
                        year = null;
                    }

                    String teacherJobNumber;
                    if (teacherJobNumberCell != null) {
                        try {
                            teacherJobNumber = teacherJobNumberCell.getCellType() == CellType.NUMERIC ? String.valueOf((int)teacherJobNumberCell.getNumericCellValue()) : teacherJobNumberCell.getStringCellValue();
                        } catch (NumberFormatException e) {
                            msg.setMsg("无法获取导师编号！");
                            return msg.fail();
                        }
                    } else {
                        teacherJobNumber = null;
                    }


                    UnderGraduate student = new UnderGraduate();
                    Teachers teacher = new Teachers();
                    Thesis thesis = new Thesis();
                    student.setName(name);
                    student.setStuNumber(studentNumber);
                    student.setYear(year);
                    student.setSpecialty(major);
                    student.setClassName(className);
                    teacher.setName(teacherName);
                    teacher.setJobnumber(teacherJobNumber);
                    thesis.setStudentNumber(studentNumber);
                    thesis.setTutorNumber(teacherJobNumber);

                    studentList.add(student);
                    if(StringUtil.isNotEmpty(teacherJobNumber)) {
                        teachersSet.add(teacher);
                    }
                    thesisList.add(thesis);
                } else {
                    msg.setMsg("学生学号不能为空！");
                    return msg.fail();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            msg.setMsg("读取文件时发生错误！");
            return msg.fail();
        }
        Msg successMsg = Msg.success();
        successMsg.add("student", studentList).add("teacher", teachersSet).add("thesis", thesisList);
        return successMsg;
    }
}
