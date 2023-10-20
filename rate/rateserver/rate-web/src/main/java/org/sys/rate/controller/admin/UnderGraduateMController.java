package org.sys.rate.controller.admin;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.sys.rate.mapper.StudentMapper;
import org.sys.rate.mapper.UnderGraduateMapper;
import org.sys.rate.model.*;
import org.sys.rate.service.admin.LogService;
import org.sys.rate.service.admin.UnderGraduateService;
import org.sys.rate.service.expert.ExpertService;
import org.sys.rate.utils.POIUtils;
import org.sys.rate.utils.ReadExcel;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.*;
import java.nio.file.Files;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/undergraduateM/basic")
public class UnderGraduateMController {
    @Autowired
    LogService logService;
    @Autowired
    UnderGraduateService underGraduateService;
    @Autowired
    StudentMapper studentMapper;
    @Autowired
    UnderGraduateMapper underGraduateMapper;

    @GetMapping("/exportUnderGraduate")
    public ResponseEntity<byte[]> downloadExample_UnderGraduateStudents(@RequestParam("type") String type, HttpServletResponse response) {
        if ("thesisName".equals(type)) {
            return POIUtils.thesisNameExcelTemplate();
        }
        return "student".equals(type) ? POIUtils.writeUnderGraduate(type) : POIUtils.undergraduateExcelTemplate(type);
    }

    @PostMapping("/importUnderGraduate")
    public RespBean importUnderGraduate(Integer institutionID, MultipartFile file) throws ParseException {
        Map<String, List> mm = POIUtils.readExcel_undergraduate(institutionID, file);
        List<UnderGraduate> under = mm.get("underlist");
        List<Student> stu = mm.get("studentlist");
        if (under.size() == 0 || stu.size() == 0 || under.size() != stu.size()) {
            return RespBean.error("未读取到有效导入数据");
        }
        RespBean res = underGraduateService.addUnderGraduate(under, stu, institutionID);
        return res;
    }

    @GetMapping("/getUnderGraduateStudents")
    public Msg getUnderStudent(@RequestParam("pageNum") Integer pageNum, @RequestParam("pageSize") Integer pageSize) {
        Page page = PageHelper.startPage(pageNum, pageSize); // 设置当前所在页和每页显示的条数
        List<UnderGraduate> t = underGraduateService.getUnderStudent();
        PageInfo info = new PageInfo<>(page.getResult());
        Object[] res = {t, info.getTotal()}; // res是分页后的数据，info.getTotal()是总条数
        return Msg.success().add("res", res);
    }

    @PostMapping("/deleteUnderGraduateStudent")
    public Msg deleteUnderStudent(@RequestBody UnderGraduate under) {
        return underGraduateService.deleteUnderStudent(under);
    }

    @PostMapping("/editUnderGraduateStudent")
    public RespBean editUnderStudent(@RequestBody UnderGraduate under) {
        return underGraduateService.editUnderStudent(under);
    }

    //根据页面的筛选框进行查找本科生信息
    @GetMapping("/getTeaNamesBySelect")
    public RespBean getTeaNamesBySelect(String teaName) {
        return underGraduateService.getTeaNamesBySelect(teaName);
    }

    @GetMapping("/getUnderStudentsBySelect")
    public RespBean getUnderStudentsBySelect(@RequestParam("year") Integer year, @RequestParam("teaName") String teaName, @RequestParam("pageNum") Integer pageNum, @RequestParam("pageSize") Integer pageSize) {
        Page page = PageHelper.startPage(pageNum, pageSize); // 设置当前所在页和每页显示的条数
        List<UnderGraduate> t = underGraduateService.getUnderStudentsBySelect(year, teaName);
        PageInfo info = new PageInfo<>(page.getResult());
        Object[] res = {t, info.getTotal()}; // res是分页后的数据，info.getTotal()是总条数
        return RespBean.ok("ok", res);
    }

    @PostMapping("/resetUnderPassword")
    public RespBean resetUnderPassword(@RequestBody UnderGraduate under) {
        Integer res = 0;
        try {
            String p = ExpertService.sh1(under.getPassword());
            res = studentMapper.updatePassword(under.getStudentID(), p);
        } catch (Exception e) {
            return RespBean.error("error", null);
        }
        if (res > 0) {
            return RespBean.ok("ok", null);
        }
        return RespBean.error("error", null);
    }

    @PostMapping("/update")
    public RespBean updateStudent(@RequestBody UnderGraduate record) {
        if (underGraduateMapper.checkHaveStudentOfStuNumber(record.getInstitutionID(), record.getStuNumber(), record.getStudentID()) == 1) {
            return RespBean.error("学号已存在，请重新修改或联系管理员！");
        }
        if (studentMapper.update(record) == 1) {
            if (underGraduateMapper.update(record) == 1) {
                return RespBean.ok("更新成功!");
            }
        }
        return RespBean.error("更新失败!");
    }

    @PostMapping("/uploadSign")
    public RespBean uploadSign(@RequestParam("id") String id, @RequestParam("file") MultipartFile file) {
        if (!file.isEmpty()) {
            try {
                String filePath = new File("files").getAbsolutePath() + "\\template\\signs\\" + UUID.randomUUID() + "-";
                String fileName = file.getOriginalFilename();

                // 在指定路径下创建文件
                File dest = new File(filePath + fileName);
                file.transferTo(dest);

                UnderGraduate underGraduate = new UnderGraduate();
                underGraduate.setStudentID(Integer.valueOf(id));
                underGraduate.setSign(dest.getAbsolutePath());
                underGraduateMapper.save(underGraduate);

                return RespBean.ok("");
            } catch (IOException e) {
                return RespBean.error("");
            }
        } else {
            return RespBean.error("");
        }
    }

    @GetMapping("/downloadSign")
    public void downloadSign(@RequestParam("id") String studentId,
                             @RequestParam(value = "isOnLine", defaultValue = "false") boolean isOnLine,
                             HttpServletResponse response) {
        String signUrl = underGraduateMapper.getSignUrl(studentId);
        File sign = new File(signUrl);

        if (sign.exists()) {
            try (FileInputStream fis = new FileInputStream(sign);
                 BufferedInputStream bis = new BufferedInputStream(fis);
                 OutputStream os = response.getOutputStream()) {

                // 设置响应头信息
                response.reset(); // 非常重要

//                String filename = signUrl.substring(signUrl.length() - 3).equals("jpg") ? "个人签名.jpg" : "个人签名.png";
                String filename = "个人签名.jpg";

                if (isOnLine) {
                    // 在线打开方式
                    response.setContentType("application/octet-stream");
                    response.setHeader("Content-Disposition", "inline; filename=" + java.net.URLEncoder.encode(filename, "UTF-8"));
                } else {
                    // 纯下载方式
                    response.setContentType("application/octet-stream");
                    response.setHeader("Content-Disposition", "attachment; filename=" + java.net.URLEncoder.encode(filename, "UTF-8"));
                }

                // 读取文件内容并写入响应流
                byte[] buffer = new byte[1024];
                int len;
                while ((len = bis.read(buffer)) != -1) {
                    os.write(buffer, 0, len);
                }
                os.flush();

            } catch (IOException e) {
                // 在实际应用中，你可能会采取适当的措施来处理异常，如记录日志或返回适当的错误信息
                e.printStackTrace();
            }
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            // 在实际应用中，你可能会在日志中记录文件不存在的情况
        }
    }


    @PostMapping("/importThesis")
    public RespBean importThesis(@RequestParam("type") String type,
                                 @RequestParam("institutionID") Integer institutionID,
                                 @RequestParam("year") Integer year,
                                 @RequestParam("semester") String semester, MultipartFile file) throws RespBean {
        try {
            return underGraduateService.importThesis(type, institutionID, year, semester, file);
        } catch (RespBean res) {
            return RespBean.error(res.getMsg());
        }
    }

    @PostMapping("/importThesisName")
    public RespBean importThesis(@RequestParam("tutorId") Integer tutorId,
                                 @RequestParam("institutionId") Integer institutionId,
                                 @RequestParam("year") Integer year,
                                 @RequestParam("semester") Integer semester, MultipartFile file) throws RespBean {
        try {
            return underGraduateService.importThesisName(tutorId, institutionId, year, semester, file);
        } catch (RespBean res) {
            return RespBean.error(res.getMsg());
        }
    }

    @PostMapping("/getStudentsByConditions")
    public Msg getStudents(@RequestBody StudentCondition studentCondition) {

        Page page = PageHelper.startPage(studentCondition.getPageNum(), studentCondition.getPageSize());

        List<UnderGraduate> student = underGraduateService.getStudentByConditions(studentCondition);
        PageInfo info = new PageInfo<>(page.getResult());
        Object[] res = {student, info.getTotal()}; // res是分页后的数据，info.getTotal()是总条数
        return Msg.success().add("res", res);
    }


    @GetMapping("/getStudents")
    public Msg getStudents(@RequestParam("institutionID") Integer institutionID,
                           @RequestParam("year") Integer year,
                           @RequestParam("semester") String semester,
                           @RequestParam("pageNum") Integer pageNum,
                           @RequestParam("pageSize") Integer pageSize) {
        Page page = PageHelper.startPage(pageNum, pageSize); // 设置当前所在页和每页显示的条数
        Integer month;
        if (semester.length() > 1) {
            month = "春季".equals(semester) ? 3 : 9;
        } else {
            month = Integer.valueOf(semester);
            semester = 3 == month ? "春季" : "秋季";
        }
        List<UnderGraduate> student = underGraduateService.getStudent(institutionID, year, month);
        PageInfo info = new PageInfo<>(page.getResult());
        Object[] res = {student, info.getTotal()}; // res是分页后的数据，info.getTotal()是总条数
        // 再加上一个判断，该年该月该单位是否开启
        boolean havingStart = underGraduateMapper.havingStartThisThesis(institutionID, year, semester);
        return Msg.success().add("res", res).add("havingStart", havingStart);
    }

    @GetMapping("/getThesisExistDate")
    public RespBean getThesisExistDate(@RequestParam("institutionID") Integer institutionID) {
        return underGraduateService.getThesisExistDate(institutionID);
    }

    @PostMapping("/startThesis")
    public RespBean startThesis(@RequestParam("institutionID") @NotNull Integer institutionID,
                                @RequestParam("year") @NotNull Integer year,
                                @RequestParam("semester") @NotNull String semester) {
        return underGraduateService.startThesis(institutionID, year, semester);
    }

    @PutMapping("/updateUndergraduate")
    public RespBean updateUndergraduate(@RequestBody UnderGraduate under) {
        return underGraduateService.updateUndergraduate(under);
    }

    @PutMapping("/updateUndergraduateBaseOnTeacher")
    public RespBean updateUndergraduateBaseOnTeacher(@RequestBody UnderGraduate under) {
        return underGraduateService.updateUndergraduateBaseOnTeacher(under);
    }

    @PostMapping("/deleteThesis")
    public RespBean deleteThesis(@RequestParam("studentID") @NotNull Integer studentID,
                                 @RequestParam("tutorID") String tutorIDStr,
                                 @RequestParam("year") @NotNull Integer year,
                                 @RequestParam("month") @NotNull Integer month) {
        Integer tutorID = tutorIDStr.equals("none") ? null : Integer.parseInt(tutorIDStr);

        UnderGraduate underGraduate = new UnderGraduate();
        underGraduate.setStudentID(studentID);
        underGraduate.setTutorID(tutorID);
        underGraduate.setYear(year);
        underGraduate.setMonth(month);

        return underGraduateService.deleteThesis(underGraduate);
    }

    @GetMapping("/getUngrouped")
    public RespBean getUngrouped(@RequestParam("year") Integer year, @RequestParam("month") Integer month) {
        List<UnderGraduate> res = underGraduateMapper.getUngrouped(year, month);
        return RespBean.ok("success", res);
    }

    @PostMapping("/createGroups")
    public String createGroup(@RequestBody Map<String, Object> data) {
        Integer year = (Integer) data.get("year");
        Integer month = (Integer) data.get("month");
        List<Integer> arr = (List<Integer>) data.get("arr");
        Integer exchangeNums = (Integer) data.get("exchangeNums");
        Integer groupsNums = (Integer) data.get("groupsNums");
        //List<Thesis> students = (List<Thesis>) data.get("students");
        String groupWay = (String) data.get("groupWay");
        List<String> selectInfo = (List<String>) data.get("selectInfo");
        return underGraduateService.createGroup(year, month, arr, exchangeNums, groupsNums, groupWay, selectInfo);
        //返回分好组的选手信息
    }
}
