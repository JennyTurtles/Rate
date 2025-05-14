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
import org.sys.rate.config.RsaUtil;
import org.sys.rate.mapper.GraduateStudentMapper;
import org.sys.rate.mapper.StudentMapper;
import org.sys.rate.model.GraduateStudent;
import org.sys.rate.model.Msg;
import org.sys.rate.model.RespBean;
import org.sys.rate.service.admin.GraduateStudentService;
import org.sys.rate.service.admin.LogService;
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
@RequestMapping("/graduatestudentM/basic")
public class GraduateStudentMController {
    @Resource
    LogService logService;
    @Resource
    GraduateStudentService graduateStudentService;
    @Resource
    StudentMapper studentMapper;
    @Resource
    RsaUtil rsaUtil;
    @Resource
    GraduateStudentMapper graduateStudentMapper;

    @RequestMapping("test")
    public String test(){
        return "ok";
    }

    @GetMapping("/exportGraduate")
    public ResponseEntity<byte[]> downloadExample_Participants_exportMoPar_Group(HttpServletResponse response){
        return POIUtils.writeGraduateStudent();
    }
//    @PostMapping("/exportGraduateData")
//    public ResponseEntity<byte[]> exportGraduateData(@RequestBody List<GraduateStudent> graduateStudents) {
//        Workbook workbook = new XSSFWorkbook();
//        Sheet sheet = workbook.createSheet("Graduate Students");
//
//        // 创建表头
//        Row headerRow = sheet.createRow(0);
//        String[] headers = {"学号", "姓名", "用户名", "电话", "邮箱", "入学年份", "学生类别", "积分", "差的积分", "导师姓名"};
//        for (int i = 0; i < headers.length; i++) {
//            Cell cell = headerRow.createCell(i);
//            cell.setCellValue(headers[i]);
//        }
//
//        // 填充数据
//        int rowNum = 1;
//        for (GraduateStudent student : graduateStudents) {
//            Row row = sheet.createRow(rowNum++);
//            row.createCell(0).setCellValue(student.getStuNumber());
//            row.createCell(1).setCellValue(student.getName());
//            row.createCell(2).setCellValue(student.getUsername());
//            row.createCell(3).setCellValue(student.getTelephone());
//            row.createCell(4).setCellValue(student.getEmail());
//            row.createCell(5).setCellValue(student.getYear());
//            row.createCell(6).setCellValue(student.getStudentType());
//            row.createCell(7).setCellValue(student.getPoint());
//            row.createCell(8).setCellValue(student.getPoint1());
//            row.createCell(9).setCellValue(student.getTeachers().getName());
//        }
//
//        // 自动调整列宽
//        for (int i = 0; i < headers.length; i++) {
//            sheet.autoSizeColumn(i);
//        }
//
//        // 将工作簿写入字节数组输出流
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        try {
//            workbook.write(byteArrayOutputStream);
//            workbook.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        // 设置响应头
//        HttpHeaders responseHeaders = new HttpHeaders();
//        responseHeaders.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
//        responseHeaders.setContentDispositionFormData("attachment", "graduate_students.xlsx");
//
//        return new ResponseEntity<>(byteArrayOutputStream.toByteArray(), responseHeaders, HttpStatus.OK);
//    }


    @PostMapping("/importGraduate")
    public RespBean importUnderGraduate(Integer institutionID, MultipartFile file) throws ParseException {
//        List<String> checkResult = POIUtils.check(file);
//        if (checkResult.size() > 0) {
//            return RespBean.error("导入数据存在问题", checkResult);
//        }
        Map<String, List> mm = POIUtils.readExcel_graduatestudent(institutionID, file);
        List<GraduateStudent> graduate = mm.get("graduatelist");

        if (graduate.size() == 0) { //先将excel中读取到的数据行拿出来，student和graduate列表是同样的数量才对
            return RespBean.error("未读取到有效导入数据");
        }
        RespBean res = graduateStudentService.addGraduate(graduate);

        return res;
    }

    @GetMapping("/getGraduateStudents")
    public Msg getGraduateStudents(@RequestParam("pageNum") Integer pageNum, @RequestParam("pageSize") Integer pageSize) {
        Page page = PageHelper.startPage(pageNum, pageSize); // 设置当前所在页和每页显示的条数
        List<GraduateStudent> t = graduateStudentService.getGraduatesStudent();
        PageInfo info = new PageInfo<>(page.getResult());
        Object[] res = {t, info.getTotal()}; // res是分页后的数据，info.getTotal()是总条数
        return Msg.success().add("res", res);
    }

    @PostMapping("/deleteGraduateStudent")
    public Msg deleteGraduateStudent(@RequestBody GraduateStudent grad){
        return graduateStudentService.deleteGraduateStudent(grad);
    }
    @PostMapping("/editGraduateStudent")
    public RespBean editGraduateStudent(@RequestBody GraduateStudent graduate){
        return graduateStudentService.editgraduateStudent(graduate);
    }
    //根据页面的筛选框进行查找老师姓名
    @GetMapping("/getTeaNamesBySelect")
    public RespBean getTeaNamesBySelect(String teaName){
        return graduateStudentService.getTeaNamesBySelect(teaName);
    }

    @GetMapping("/getGraduateStudentsBySelect") //管理员段学生列表的筛选
    public RespBean getGraduateStudentsBySelect(@RequestParam("year")Integer year,@RequestParam("teaName")String teaName,@RequestParam("pageNum")Integer pageNum,@RequestParam("pageSize")Integer pageSize){
        Page page = PageHelper.startPage(pageNum, pageSize); // 设置当前所在页和每页显示的条数
        List<GraduateStudent> t = graduateStudentService.getGraduateStudentsBySelect(year,teaName);
        PageInfo info = new PageInfo<>(page.getResult());
        Object[] res = {t, info.getTotal()}; // res是分页后的数据，info.getTotal()是总条数
        return RespBean.ok("ok", res);
    }

    @GetMapping("/getGraduateStudentsBySelectOfTeacher") //教师端学生列表的筛选
    public RespBean getGraduateStudentsBySelectOfTeacher(@RequestParam("tutorID") Integer tutorID, @RequestParam("year")Integer year,@RequestParam("pageNum")Integer pageNum,@RequestParam("pageSize")Integer pageSize){
        Page page = PageHelper.startPage(pageNum, pageSize); // 设置当前所在页和每页显示的条数
        List<GraduateStudent> t = graduateStudentService.getGraduateStudentsBySelectOfTeacher(tutorID, year);
        PageInfo info = new PageInfo<>(page.getResult());
        Object[] res = {t, info.getTotal()}; // res是分页后的数据，info.getTotal()是总条数
        return RespBean.ok("ok", res);
    }

    @PostMapping("/resetUnderPassword")
    public RespBean resetUnderPassword(@RequestBody GraduateStudent grad){
        Integer res = 0;
        try{
            String p = ExpertService.sh1(grad.getPassword());
            res = studentMapper.updatePassword(grad.getStudentID(),p);
        }catch (Exception e){
            return RespBean.error("error",null);
        }
        if(res > 0){
            return RespBean.ok("ok",null);
        }
        return RespBean.error("error",null);
    }

    @PostMapping("/updateScore") //加法
    public RespBean updateScoreAdd(@RequestBody GraduateStudent record) {
        Integer res = graduateStudentMapper.updateScore(Long.valueOf(record.getStudentID().intValue()),Long.parseLong(record.getPoint()));
        if (res > 0) {
            graduateStudentService.updatePoint1(record.getStudentID());
            return RespBean.ok("ok", res);
        }

        return RespBean.error("error");
    }

    @PostMapping("/exportGraduateData")
    public ResponseEntity<byte[]> exportGraduateData(@RequestBody List<GraduateStudent> graduateStudents) {


        Workbook workbook = null;
        try {
            workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Graduate Students");

            // 创建表头
            Row headerRow = sheet.createRow(0);
            String[] headers = {"学号", "姓名", "用户名", "电话", "邮箱", "入学年份", "学生类别", "积分", "达标", "导师姓名"};
            for (int i = 0; i < headers.length; i++) {
                headerRow.createCell(i).setCellValue(headers[i]);
            }

            // 填充数据
            int rowNum = 1;
            for (GraduateStudent student : graduateStudents) {
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
                String headerValue = "attachment; filename=\"graduate_students.xlsx\"; filename*=UTF-8''graduate_students.xlsx";
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


//    @PostMapping("/updateScoreSub") //减法
//    public RespBean updateScoreSub(@RequestBody GraduateStudent record) {
//        Integer res = graduateStudentMapper.updateScoreSub(Long.valueOf(record.getStudentID().intValue()),Long.parseLong(record.getPoint()));
//        if (res > 0) {
//            graduateStudentService.updatePoint1(record.getStudentID());
//            return RespBean.ok("ok", res);
//        }
//
//        return RespBean.error("error");
//    }
//    @PostMapping("/exportGraduateData")
//    public ResponseEntity<byte[]> exportGraduateData(@RequestBody List<GraduateStudent> graduateStudents) {
//        Workbook workbook = new XSSFWorkbook();
//        Sheet sheet = workbook.createSheet("Graduate Students");
//
//        // 创建表头
//        Row headerRow = sheet.createRow(0);
//        String[] headers = {"学号", "姓名", "用户名", "电话", "邮箱", "入学年份", "学生类别", "积分", "达标", "导师姓名"};
//        for (int i = 0; i < headers.length; i++) {
//            Cell cell = headerRow.createCell(i);
//            cell.setCellValue(headers[i]);
//        }
//
//        // 填充数据
//        int rowNum = 1;
//        for (GraduateStudent student : graduateStudents) {
//            Row row = sheet.createRow(rowNum++);
//            row.createCell(0).setCellValue(student.getStuNumber());
//            row.createCell(1).setCellValue(student.getName());
//            row.createCell(2).setCellValue(student.getUsername());
//            row.createCell(3).setCellValue(student.getTelephone());
//            row.createCell(4).setCellValue(student.getEmail());
//            row.createCell(5).setCellValue(student.getYear());
//            row.createCell(6).setCellValue(student.getStudentType());
//            row.createCell(7).setCellValue(student.getPoint());
//            row.createCell(8).setCellValue(student.getPoint1());
//            row.createCell(9).setCellValue(student.getTeachers().getName());
//        }
//
//        // 自动调整列宽
//        for (int i = 0; i < headers.length; i++) {
//            sheet.autoSizeColumn(i);
//        }
//
//        // 将工作簿写入字节数组输出流
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        try {
//            workbook.write(byteArrayOutputStream);
//            workbook.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        // 设置响应头
//        HttpHeaders responseHeaders = new HttpHeaders();
//        responseHeaders.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
//        responseHeaders.setContentDispositionFormData("attachment", "graduate_students.xlsx");
//
//        // Java Spring 添加这句
//        responseHeaders.set("Access-Control-Expose-Headers", "Content-Disposition, Content-Type");
//
//        // 后端返回 Base64
//        return new ResponseEntity<>(byteArrayOutputStream.toByteArray(), responseHeaders, HttpStatus.OK);
//    }


    @PostMapping("/update")
    public RespBean updateStudent(@RequestBody GraduateStudent record) {
        if (graduateStudentMapper.checkHaveStudentOfStuNumber(record.getInstitutionID(), record.getStuNumber(), record.getStudentID()) == 1) {
            return RespBean.error("学号已存在，请重新修改或联系管理员！");
        }
        if (studentMapper.update(record) == 1) {
            if (graduateStudentMapper.update(record) == 1) {
                return RespBean.ok("更新成功!");
            }
        }
        return RespBean.error("更新失败!");
    }
}
