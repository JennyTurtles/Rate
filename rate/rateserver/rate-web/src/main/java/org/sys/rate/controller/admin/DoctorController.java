package org.sys.rate.controller.admin;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.sys.rate.mapper.DoctorMapper;
import org.sys.rate.mapper.StudentMapper;
import org.sys.rate.model.Doctor;
import org.sys.rate.model.Msg;
import org.sys.rate.model.RespBean;
import org.sys.rate.service.admin.DoctorService;
import org.sys.rate.service.expert.ExpertService;
import org.sys.rate.utils.POIUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/doctorM/basic")
public class DoctorController {
    @Resource
    DoctorMapper doctorMapper;
    @Resource
    DoctorService doctorService;
    @Resource
    StudentMapper studentMapper;


    @GetMapping("/exportDoctor")
    public ResponseEntity<byte[]> downloadExample_Participants_exportMoPar_Group(HttpServletResponse response){
        return POIUtils.writeDoctorStudent();
    }
    @PostMapping("/update")
    public RespBean updateStudent(@RequestBody Doctor record) {
        if (doctorMapper.checkHaveStudentOfStuNumber(record.getInstitutionID(),record.getStuNumber(),record.getStudentID()) == 1){
            return RespBean.error("学号已存在，请重新修改或联系管理员！");
        }
        if (studentMapper.update(record) == 1){
            if (doctorMapper.update(record) == 1) {
                return RespBean.ok("更新成功!");
            }
        }
        return RespBean.error("更新失败!");
    }

    @PostMapping("/exportdoctorData")
    public ResponseEntity<byte[]> exportdoctorData(@RequestBody List<Doctor> doctorStudents) {

        Workbook workbook = null;
        try {
            workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("doctor Students");

            // 创建表头
            Row headerRow = sheet.createRow(0);
            String[] headers = {"学号", "姓名", "用户名", "电话", "邮箱", "入学年份", "学生类别", "积分", "达标", "导师姓名"};
            for (int i = 0; i < headers.length; i++) {
                headerRow.createCell(i).setCellValue(headers[i]);
            }

            // 填充数据
            int rowNum = 1;
            for (Doctor student : doctorStudents) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(student.getStuNumber() != null ? student.getStuNumber() : "");
                row.createCell(1).setCellValue(student.getName() != null ? student.getName() : "");
                row.createCell(2).setCellValue(student.getUsername() != null ? student.getUsername() : "");
                row.createCell(3).setCellValue(student.getTelephone() != null ? student.getTelephone() : "");
                row.createCell(4).setCellValue(student.getEmail() != null ? student.getEmail() : "");
                row.createCell(5).setCellValue(student.getYear() != null ? student.getYear() + "" : "");
                row.createCell(6).setCellValue(student.getStudentType() != null ? student.getStudentType() : "");
                row.createCell(7).setCellValue(student.getPoint());
                row.createCell(8).setCellValue(student.getPoint1());
                String teacherName = (student.getTeachers() != null && student.getTeachers().getName() != null)
                        ? student.getTeachers().getName() : "";
                row.createCell(9).setCellValue(teacherName);
            }

            // 自动调整列宽
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // 写入流并关闭资源
            try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
                workbook.write(byteArrayOutputStream);

                // 设置响应头
                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
                String headerValue = "attachment; filename=\"doctor_students.xlsx\"; filename*=UTF-8''doctor_students.xlsx";
                httpHeaders.add("Content-Disposition", headerValue);

                return new ResponseEntity<>(byteArrayOutputStream.toByteArray(), httpHeaders, HttpStatus.OK);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            try {
                if (workbook != null) {
                    workbook.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    @GetMapping("/getDoctorStudents")
    public Msg getDoctorStudents(@RequestParam("pageNum") Integer pageNum, @RequestParam("pageSize") Integer pageSize) {
        Page page = PageHelper.startPage(pageNum, pageSize); // 设置当前所在页和每页显示的条数
        List<Doctor> t = doctorMapper.getDoctorStudents();
        PageInfo info = new PageInfo<>(page.getResult());
        Object[] res = {t, info.getTotal()}; // res是分页后的数据，info.getTotal()是总条数
        return Msg.success().add("res", res);
    }
    @GetMapping("/getDoctorsBySelect")
    public RespBean getDoctorsBySelect(@RequestParam("year")Integer year,@RequestParam("teaName")String teaName,@RequestParam("pageNum")Integer pageNum,@RequestParam("pageSize")Integer pageSize){
        Page page = PageHelper.startPage(pageNum, pageSize); // 设置当前所在页和每页显示的条数
        List<Doctor> t = doctorService.getDoctorsBySelect(year,teaName);
        PageInfo info = new PageInfo<>(page.getResult());
        Object[] res = {t, info.getTotal()}; // res是分页后的数据，info.getTotal()是总条数
        return RespBean.ok("ok", res);
    }
    @GetMapping("/getDoctorStudentsBySelectOfTeacher") //教师端学生列表的筛选
    public RespBean getDoctorStudentsBySelectOfTeacher(@RequestParam("tutorID") Integer tutorID, @RequestParam("year")Integer year,@RequestParam("pageNum")Integer pageNum,@RequestParam("pageSize")Integer pageSize){
        Page page = PageHelper.startPage(pageNum, pageSize); // 设置当前所在页和每页显示的条数
        List<Doctor> t = doctorService.getDoctorStudentsBySelectOfTeacher(tutorID, year);
        PageInfo info = new PageInfo<>(page.getResult());
        Object[] res = {t, info.getTotal()}; // res是分页后的数据，info.getTotal()是总条数
        return RespBean.ok("ok", res);
    }
    @PostMapping("/importDoctors")
    public RespBean importDoctors(Integer institutionID, MultipartFile file) throws ParseException {
        Map<String, List> mm = POIUtils.readExcel_doctrstudent(institutionID, file);
        List<Doctor> doctors = mm.get("doctorlist");
        if (doctors.size() == 0) { //先将excel中读取到的数据行拿出来，student和graduate列表是同样的数量才对
            return RespBean.error("未读取到有效导入数据");
        }
        RespBean res = doctorService.addDoctor(doctors);
        return res;
    }
    @PostMapping("/deleteDoctorStudent")
    public Msg deleteDoctorStudent(@RequestBody Doctor doctor){
        return doctorService.deleteDoctorStudent(doctor);
    }
    @PostMapping("/editDoctorStudent")
    public RespBean editDoctorStudent(@RequestBody Doctor doctor){
        return doctorService.editDoctorStudent(doctor);
    }

    @PostMapping("/updateScore") //加法
    public RespBean updateScoreAdd(@RequestBody Doctor record) {
        Integer res = doctorMapper.updateScore(Long.valueOf(record.getStudentID().intValue()),Long.parseLong(record.getPoint()));
        if (res > 0) {
            doctorService.updatePoint1(record.getStudentID());
            return RespBean.ok("ok", res);
        }

        return RespBean.error("error");
    }
    @PostMapping("/updateScoreSub") //减法
    public RespBean updateScoreSub(@RequestBody Doctor record) {
        Integer res = doctorMapper.updateScoreSub(Long.valueOf(record.getStudentID().intValue()),Long.parseLong(record.getPoint()));
        if (res > 0) {
            doctorService.updatePoint1(record.getStudentID());
            return RespBean.ok("ok", res);
        }

        return RespBean.error("error");
    }
    @PostMapping("/resetUnderPassword")
    public RespBean resetUnderPassword(@RequestBody Doctor doctor){
        Integer res = 0;
        try{
            String p = ExpertService.sh1(doctor.getPassword());
            res = studentMapper.updatePassword(doctor.getStudentID(),p);
        }catch (Exception e){
            return RespBean.error("error",null);
        }
        if(res > 0){
            return RespBean.ok("ok",null);
        }
        return RespBean.error("error",null);
    }
}
