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
    public Msg readStartThesisExcelData(String type, MultipartFile file) {
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

                Cell yearCell = null;
                Cell majorCell = null;
                Cell classCell = null;
                Cell teacherNameCell;
                Cell teacherJobNumberCell;

                if ("student".equals(type)) {
                    yearCell = row.getCell(2);
                    majorCell = row.getCell(3);
                    classCell = row.getCell(4);
                    teacherNameCell = row.getCell(5);
                    teacherJobNumberCell = row.getCell(6);
                } else {
                    teacherNameCell = row.getCell(2);
                    teacherJobNumberCell = row.getCell(3);
                    if (teacherJobNumberCell == null) {
                        msg.setMsg("教师学号不能为空！");
                        return msg.fail();
                    }
                }

                if (idCell == null) {
                    msg.setMsg("学生学号不能为空！");
                    return msg.fail();
                }

                String name = nameCell != null ? nameCell.getStringCellValue() : "";
                String major = majorCell != null ? majorCell.getStringCellValue() : "";
                String className = classCell != null ? classCell.getStringCellValue() : "";
                String teacherName = teacherNameCell != null ? teacherNameCell.getStringCellValue() : "";

                String studentNumber;
                try {
                    studentNumber = idCell.getCellType() == CellType.NUMERIC ? String.valueOf((int) idCell.getNumericCellValue()) : idCell.getStringCellValue();
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
                        teacherJobNumber = teacherJobNumberCell.getCellType() == CellType.NUMERIC ? String.valueOf((int) teacherJobNumberCell.getNumericCellValue()) : teacherJobNumberCell.getStringCellValue();
                    } catch (NumberFormatException e) {
                        msg.setMsg("无法获取导师编号！");
                        return msg.fail();
                    }
                } else {
                    teacherJobNumber = null;
                }

                Thesis thesis = new Thesis();
                if ("student".equals(type)) {
                    UnderGraduate student = new UnderGraduate();
                    Teachers teacher = new Teachers();
                    student.setName(name);
                    student.setStuNumber(studentNumber);
                    student.setYear(year);
                    student.setSpecialty(major);
                    student.setClassName(className);
                    teacher.setName(teacherName);
                    teacher.setJobnumber(teacherJobNumber);

                    studentList.add(student);
                    if (StringUtil.isNotEmpty(teacherJobNumber)) {
                        teachersSet.add(teacher);
                    }
                }
                thesis.setStudentNumber(studentNumber);
                thesis.setTutorNumber(teacherJobNumber);

                thesisList.add(thesis);
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
