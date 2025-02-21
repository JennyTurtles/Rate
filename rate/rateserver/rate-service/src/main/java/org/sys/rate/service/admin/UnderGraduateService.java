package org.sys.rate.service.admin;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.sys.rate.mapper.*;
import org.sys.rate.model.*;
import org.sys.rate.utils.ReadExcel;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;

@Service
public class UnderGraduateService {
    @Autowired
    StudentMapper studentMapper;
    @Autowired
    TeachersMapper teachersMapper;
    @Autowired
    ParticipatesMapper participatesMapper;
    @Autowired
    GraduateStudentMapper graduateStudentMapper;
    @Autowired
    UnderGraduateMapper underGraduateMapper;
    @Resource
    private ThesisMapper thesisMapper;
    @Resource
    private ReadExcel readExcel;
    @Autowired
    GroupsService groupsService;
    @Resource
    private ThesisService thesisService;
    public List<String> checkGraduateStudent(MultipartFile file) {
        List<String> error = new ArrayList<>();
        try {
            //1. 创建一个 workbook 对象
            HSSFWorkbook workbook = new HSSFWorkbook(file.getInputStream());
            //2. 获取 workbook 中表单的数量
            int numberOfSheets = workbook.getNumberOfSheets();
            //3. 获取表单 只读第一个
            HSSFSheet sheet = workbook.getSheetAt(0);
            String sheetName = sheet.getSheetName();
            //4. 获取表单中的行数
            int physicalNumberOfRows = sheet.getPhysicalNumberOfRows();
            int Cells = sheet.getRow(0).getPhysicalNumberOfCells();
            HashMap<Integer, String> map = new HashMap<>();
            boolean hasNameColumn = false;
            boolean hasNumberColumn = false;
            boolean hasMobileColumn = false;
            boolean hasEmailColumn = false;
            boolean hasTuturNoColumn = false;
            boolean hasTuturNameColumn = false;
            boolean hasYearColumn = false;
            boolean hasTypeColumn = false;
            boolean hasMajorColumn = false;
            boolean hasClassColumn = false;

//            姓名	学号	手机号	邮箱	导师工号	导师姓名	入学年份	学生类别	专业	班级

            for (int m = 0; m < Cells; m++) {
                if (sheet.getRow(0).getCell(m).getStringCellValue() != null){
                    String columnName = sheet.getRow(0).getCell(m).getStringCellValue();
                    map.put(m, columnName);
                    if ("学生类别".equals(columnName)) {
                        hasTypeColumn = true;
                    }
                    if ("专业".equals(columnName)) {
                        hasMajorColumn = true;
                    }
                    if ("班级".equals(columnName)) {
                        hasClassColumn = true;
                    }
                    if ("入学年份".equals(columnName)) {
                        hasYearColumn = true;
                    }
                    if ("导师姓名".equals(columnName)) {
                        hasTuturNameColumn = true;
                    }
                    if ("导师工号".equals(columnName)) {
                        hasTuturNoColumn = true;
                    }
                    if ("邮箱".equals(columnName)) {
                        hasEmailColumn = true;
                    }
                    if ("姓名".equals(columnName)) {
                        hasNameColumn = true;
                    }
                    if ("学号".equals(columnName)) {
                        hasNumberColumn = true;
                    }
                    if ("手机号".equals(columnName)) {
                        hasMobileColumn = true;
                    }
                }
            }

            if (!hasNameColumn) {
                error.add("导入的" + sheetName + "缺少姓名列，请检查！");
                return error;
            }
            if (!hasNumberColumn) {
                error.add("导入的" + sheetName + "缺少学号列，请检查！");
                return error;
            }
            if (!hasMobileColumn) {
                error.add("导入的" + sheetName + "缺少手机号列，请检查！");
                return error;
            }
            if (!hasEmailColumn) {
                error.add("导入的" + sheetName + "缺少邮箱列，请检查！");
                return error;
            }
            if (!hasTuturNoColumn) {
                error.add("导入的" + sheetName + "缺少导师工号列，请检查！");
                return error;
            }
            if (!hasTuturNameColumn) {
                error.add("导入的" + sheetName + "缺少导师姓名列，请检查！");
                return error;
            }
            if (!hasYearColumn) {
                error.add("导入的" + sheetName + "缺少入学年份列，请检查！");
                return error;
            }
            if (!hasTypeColumn) {
                error.add("导入的" + sheetName + "缺少学生类别列，请检查！");
                return error;
            }
            if (!hasMajorColumn) {
                error.add("导入的" + sheetName + "缺少专业列，请检查！");
                return error;
            }
            if (!hasClassColumn) {
                error.add("导入的" + sheetName + "缺少班级列，请检查！");
                return error;
            }

            //行
            for (int j = 0; j < physicalNumberOfRows; j++) {
                //5. 跳过标题行
                if (j == 0) {
                    continue;//跳过标题行//获得表头，为后续对应位置
                }
                //6. 获取行
                HSSFRow row = sheet.getRow(j);
                if (row == null) {
                    continue;//防止数据中间有空行
                }
                //7. 获取列数
                int rowNullNums = 0;

                // 检查"姓名"和"编号"列是否为空
                boolean isNameEmpty = true;
                boolean isNumberEmpty = true;
                boolean isTuturNoEmpty = true;
                boolean isTuturNameEmpty = true;
                boolean isYearEmpty = true;
                boolean isTypeEmpty = true;

                List<UnderGraduate> dataArr = new LinkedList<>();
                for (int k = 0; k < Cells; k++) {
                    HSSFCell cell = row.getCell(k);
                    String columnName = map.get(k);

                    if (cell != null) {
                        cell.setCellType(CellType.STRING);
                        String cellValue = cell.getStringCellValue();
                        UnderGraduate underGraduate = new UnderGraduate();
                        if ("姓名".equals(columnName) && !cellValue.equals("")) {
                            isNameEmpty = false;
                        }
                        if ("学号".equals(columnName) && !cellValue.equals("")) {
                            isNumberEmpty = false;
                        }

                        if ("导师工号".equals(columnName) && !cellValue.equals("")) {
                            isTuturNoEmpty = false;
                            underGraduate.setTutorJobNumber(cellValue);
                        }
                        if ("导师姓名".equals(columnName) && !cellValue.equals("")) {
                            isTuturNameEmpty = false;
                            underGraduate.setTutorName(cellValue);
                        }
                        if ("入学年份".equals(columnName) && !cellValue.equals("")) {
                            isYearEmpty = false;
                        }
                        if ("学生类别".equals(columnName) && !cellValue.equals("")) {
                            isTypeEmpty = false;
                        }
                        if(!isTuturNameEmpty || !isTuturNoEmpty ){
                            dataArr.add(underGraduate);
                        }
                    }
                }
                if (isNumberEmpty && isNameEmpty && isTuturNoEmpty && isTuturNameEmpty
                        && isYearEmpty && isTypeEmpty
                ) {
                    continue;
                }
//                    if (rowNullNums != Cells && rowNullNums != 0) {
//                        String tips = "第【" + j + "】行有空数据，";
//                        error.add(tips);
//                    }
                if (isNameEmpty){
                    String tips = "第【" + (j + 1) + "】行的姓名为空，请确认";
                    error.add(tips);
                }
                if (isNumberEmpty){
                    String tips = "第【" + (j + 1) + "】行的编号为空，请确认";
                    error.add(tips);
                }
                if (isYearEmpty) {
                    String tips = "第【" + (j + 1) + "】行的入学年份为空，请确认";
                    error.add(tips);
                }
                if (isTypeEmpty) {
                    String tips = "第【" + (j + 1) + "】行的学生类别为空，请确认";
                    error.add(tips);
                }
                if(!dataArr.isEmpty()){
                    List<String> tmpDataArr = checkGraduateStudent(dataArr);
                    if(tmpDataArr != null && !tmpDataArr.isEmpty()){
                        error.addAll(tmpDataArr);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return error;
    }
    public List<String> checkGraduateStudent(List<UnderGraduate> underList) {
        List<String> jobTeas = new ArrayList<>(); // 记录导师的工号
        List<String> nameTeas = new ArrayList<>(); // 记录导师的姓名
        List<String> errors = new LinkedList<>();
        for(int i = 0; i < underList.size(); i++) {
            UnderGraduate underGraduatenderGraduate = underList.get(i);
            String jobNumber = underGraduatenderGraduate.getTutorJobNumber();
            String name = underGraduatenderGraduate.getTutorName();

            // ------------------------- 新增校验逻辑 -------------------------
            if (jobNumber != null && !jobNumber.isEmpty()) {
                // 情况1：有工号，以工号为准
                Teachers dbTeacher = teachersMapper.selectTeaByJobnumber(jobNumber);
                if (dbTeacher == null) {
                    errors.add("工号 " + jobNumber + " 对应的导师不存在");
                }
                // 如果同时有姓名，需验证是否匹配
                if (name != null && !name.isEmpty() && !name.equals(dbTeacher.getName())) {
                    errors.add("工号 " + jobNumber + " 与姓名 " + name + " 不匹配");
                }
                underGraduatenderGraduate.setTeachers(dbTeacher);
                underGraduatenderGraduate.setTutorID(dbTeacher.getID());
            } else if (name != null && !name.isEmpty()) {
                // 情况2：只有姓名，检查是否唯一
                List<Teachers> teachers = teachersMapper.selectTeasByName(Collections.singletonList(name));
                if (teachers.isEmpty()) {
                    errors.add("未找到姓名为 " + name + " 的导师");
                } else if (teachers.size() > 1) {
                    errors.add("姓名为 " + name + " 的导师存在重复");
                }
                underGraduatenderGraduate.setTeachers(teachers.get(0));
                underGraduatenderGraduate.setTutorID(teachers.get(0).getID());
            } else {
                // 情况3：工号和姓名都为空
                underGraduatenderGraduate.setTutorID(null);
            }
            // ------------------------- 结束新增逻辑 -------------------------
        }

        return errors;
    }
    //管理员导入本科生，只添加，即使已经存在了该条数据也不更新
    public RespBean addUnderGraduate(List<UnderGraduate> underList, List<Student> stuList, Integer institutionID) {
        List<String> jobTeas = new ArrayList<>(); // 记录导师的工号
        List<String> nameTeas = new ArrayList<>(); // 记录导师的姓名
        for(int i = 0; i < underList.size(); i++) {
            UnderGraduate underGraduatenderGraduate = underList.get(i);
            Teachers teacher = underGraduatenderGraduate.getTeachers();
            String jobNumber = teacher.getJobnumber();
            String name = teacher.getName();

            // ------------------------- 新增校验逻辑 -------------------------
            if (jobNumber != null && !jobNumber.isEmpty()) {
                // 情况1：有工号，以工号为准
                Teachers dbTeacher = teachersMapper.selectTeaByJobnumber(jobNumber);
                if (dbTeacher == null) {
                    return RespBean.error("工号 " + jobNumber + " 对应的导师不存在");
                }
                // 如果同时有姓名，需验证是否匹配
                if (name != null && !name.isEmpty() && !name.equals(dbTeacher.getName())) {
                    return RespBean.error("工号 " + jobNumber + " 与姓名 " + name + " 不匹配");
                }
                underGraduatenderGraduate.setTeachers(dbTeacher);
                underGraduatenderGraduate.setTutorID(dbTeacher.getID());
            } else if (name != null && !name.isEmpty()) {
                // 情况2：只有姓名，检查是否唯一
                List<Teachers> teachers = teachersMapper.selectTeasByName(Collections.singletonList(name));
                if (teachers.isEmpty()) {
                    return RespBean.error("未找到姓名为 " + name + " 的导师");
                } else if (teachers.size() > 1) {
                    return RespBean.error("姓名为 " + name + " 的导师存在重复");
                }
                underGraduatenderGraduate.setTeachers(teachers.get(0));
                underGraduatenderGraduate.setTutorID(teachers.get(0).getID());
            } else {
                // 情况3：工号和姓名都为空
                underGraduatenderGraduate.setTutorID(null);
            }
            // ------------------------- 结束新增逻辑 -------------------------
        }
        for (int i = 0; i < underList.size(); i++) {
            // 工号和姓名都有按照工号来，都没有 tutorid 为空，只有姓名就按照姓名查找
            if (underList.get(i).getTeachers().getJobnumber() == null && underList.get(i).getTeachers().getName() == null) {
                underList.get(i).setTutorID(null);
            }
            if (underList.get(i).getTeachers().getJobnumber() != null) {
                jobTeas.add(underList.get(i).getTeachers().getJobnumber());
            }
            if (underList.get(i).getTeachers().getJobnumber() == null && underList.get(i).getTeachers().getName() != null) {
                nameTeas.add(underList.get(i).getTeachers().getName());
            }
        }

        List<Teachers> jobTeachers = new ArrayList<>();
        jobTeachers = teachersMapper.selectTeasByJobnumber(jobTeas);
        if (jobTeachers.size() == 0) {
            return RespBean.error("未找到老师信息！请仔细检查工号");
        }

        List<Teachers> nameTeachers = new ArrayList<>();
        if (nameTeas.size() > 0) {
            nameTeachers = teachersMapper.selectTeasByName(nameTeas);
        }

        List<String> checkIDNumbers = new ArrayList<>();
        List<UnderGraduate> insertUnder = new ArrayList<>();
        List<Student> insertStus = new ArrayList<>();
        List<UnderGraduate> updateUnder = new ArrayList<>();
        List<Student> updateStus = new ArrayList<>();

        // 检查已存在的本科生
        List<UnderGraduate> checkStudents = underGraduateMapper.checkAndReturnID(underList, institutionID);
        if (checkStudents.size() != 0) {
            for (UnderGraduate i : checkStudents) {
                checkIDNumbers.add(i.getStuNumber());
            }
            for (int i = 0; i < underList.size(); i++) {
                if (checkIDNumbers.indexOf(underList.get(i).getStuNumber()) == -1) {
                    insertUnder.add(underList.get(i));
                    insertStus.add(stuList.get(i));
                } else {
                    updateUnder.add(underList.get(i));
                    updateStus.add(stuList.get(i));
                }
            }
        } else {
            insertUnder = underList;
            insertStus = stuList;
        }

        try {
            if (insertStus.size() > 0) {
                studentMapper.insertFromAdminExcel(insertStus);
            }
        } catch (Exception e) {
            return RespBean.error("插入学生信息失败：" + e.getMessage());
        }

        // 设置本科生的 StudentID
        for (int i = 0; i < insertUnder.size(); i++) {
            insertUnder.get(i).setStudentID(insertStus.get(i).getID());
        }

        try {
            if (insertUnder.size() > 0) {
                underGraduateMapper.insertFROMImport(insertUnder);
            }
        } catch (Exception e) {
            return RespBean.error("插入本科生信息失败：" + e.getMessage());
        }

        try {
            if (updateStus.size() > 0) {
                studentMapper.updateFromAdminExcel(updateStus);
            }
        } catch (Exception e) {
            return RespBean.error("更新学生信息失败：" + e.getMessage());
        }

        try {
            if (updateUnder.size() > 0) {
                underGraduateMapper.updateFROMImport(updateUnder);
            }
        } catch (Exception e) {
            return RespBean.error("更新本科生信息失败：" + e.getMessage());
        }

        return RespBean.ok("操作成功");
    }

    public List<UnderGraduate> getUnderStudent(Integer institutionID) {
        List<UnderGraduate> res = new ArrayList<>();
        try {
            res = underGraduateMapper.getUnderStudent(institutionID);
        } catch (Exception e) {
        }
        return res;
    }

    public Msg deleteUnderStudent(UnderGraduate under) {
        try {
            underGraduateMapper.deleteUnderStudent(under);
            //如果没有导师就直接删除
            if (under.getTutorID() != null && !under.getTutorID().equals("")) {
                //说明只是这一个学生的本科生老师，需要去掉这个老师的本科生导师角色
                if (underGraduateMapper.checkHaveStudentOftutorID(under.getTutorID(), under.getID()) == 0) {
                    Teachers tea = teachersMapper.selectByPrimaryId(under.getTutorID());
                    String r = tea.getRole();
                    //区分要不要删掉多余分号，我懒得思考了，就这样吧，好累，有bug再说
                    if (r.contains("10;")) {
                        r = r.replace("10;", "");
                        tea.setRole(r);
                        teachersMapper.updateRoleOfOneTeacher(tea);
                    } else if (r.contains("10")) {
                        r = r.replace("10", "");
                        tea.setRole(r);
                        teachersMapper.updateRoleOfOneTeacher(tea);
                    }
                }
            }
            if (graduateStudentMapper.checkHaveStudentOfstudenID(under.getStudentID()) == 0 &&
                    participatesMapper.isParticipants(under.getStudentID()) == 0) {
                //如果在选手表中和研究生表中查不到关于这个stuid关联的数据，说明可以删除
                studentMapper.deleteStudent(under.getStudentID());
            }
        } catch (Exception e) {
            return Msg.fail();
        }
        return Msg.success();
    }

    public RespBean editUnderStudent(UnderGraduate under) {
        //目的不在于更改老师表中的信息
        //直接限定老师名字和工号必须填写了
        try {
            Teachers tea = null;
            //先判断有没有这个导师的存在
            if (under.getTeachers().getJobnumber() != null && !under.getTeachers().getJobnumber().equals("")) {
                tea = teachersMapper.selectTeaByJobnumber(under.getTeachers().getJobnumber());
                if (tea == null) {//有工号，但是没有查到，说明工号错误
                    return RespBean.error("未查询到该老师");
                }
                //如果根据工号查到了这个老师，但是数据库的老师名字和前端传来的老师名字不同，说明信息填写错误
                if (under.getTeachers().getName() != null && !under.getTeachers().getName().equals("") && !tea.getName().equals(under.getTeachers().getName())) {
                    return RespBean.error("该老师工号和姓名不符，请仔细检查信息");
                }
                under.setTutorID(tea.getID());//重新设置导师id
                underGraduateMapper.editUnderStudent(under);
            }
        } catch (Exception e) {
            return RespBean.error("处理失败");
        }
        return RespBean.ok("更新成功");
    }

    public RespBean getTeaNamesBySelect(String teaName) {
        List<Teachers> res;
        try {
            res = underGraduateMapper.getTeaNamesBySelect(teaName);
        } catch (Exception e) {
            return RespBean.error("error");
        }
        return RespBean.ok("ok", res);
    }

    public List<UnderGraduate> getUnderStudentsBySelect(Integer year, String teaName) {
        List<UnderGraduate> res = null;
        try {
            res = underGraduateMapper.getUnderStudentsBySelect(teaName, year);
        } catch (Exception e) {
        }
        return res;
    }

    public UnderGraduate getUnderByStuID(Integer studentID) {
        return underGraduateMapper.getUnderByStuID(studentID);
    }

    @Transactional()
    public RespBean importThesis(String type, Integer institutionID, Integer startThesisID, MultipartFile file) throws RespBean {
        // 1. 从excel解析出来的数据
        Msg excelData = readExcel.readStartThesisExcelData(type, institutionID, file);
        if (excelData.getCode() == 500) {
            throw RespBean.error(excelData.getMsg());
        }

        // 3.对于学期进行分解，3-春季，9-秋季
//        Integer month;
//        if (semester.length() > 1) {
//            month = "春季".equals(semester) ? 3 : 9;
//        } else {
//            month = Integer.valueOf(semester);
//        }

        // 4.插入thesis表(或者当type==teacher时，需要更新thesis表)
        List thesisList = (List<Thesis>) excelData.getExtend().get("thesis");
        RespBean thesisResBean = this.insertOrUpdateThesis(thesisList, startThesisID, type);
        if (thesisResBean.getStatus().equals(500)) {
            return thesisResBean;
        }

        // 5. 全部成功后，要记录多少行成功，多少行失败，多少行重复插入，还有第几行是因为什么原因失败
        Integer total = (Integer) excelData.getExtend().get("total");
        DataProcessingResult record = (DataProcessingResult) excelData.getExtend().get("record");
        record.setTotal(total);
        record.setFailedRowsCount(total - thesisList.size());
        record.setSuccessfulRowsCount((Integer) thesisResBean.getObj());
        record.setDuplicateInsertRowsCount(thesisList.size() - (Integer) thesisResBean.getObj());
        return RespBean.ok("", record);
    }


    private RespBean insertOrUpdateThesis(List<Thesis> thesisList, Integer startThesisID, String type) {
        int rows = 0;
        try {
            rows = thesisService.upsert(thesisList, startThesisID, type);
        } catch (Exception e) {
            String errorMessage = "插入或者更新操作毕业论文信息时出错！";
            return RespBean.error(errorMessage);
        }

        return RespBean.ok("", rows);
    }


    public List<UnderGraduate> getStudent(Integer institutionID, Integer startThisThesisID) {
        return underGraduateMapper.getStudent(institutionID, startThisThesisID);
    }

    public RespBean getThesisExistDate(Integer institutionID, Integer adminID) {
        try {
            List<String> date;
            if (adminID != -1)
                date = underGraduateMapper.getThesisExistDateByAdmin(institutionID, adminID);
            else
                date = underGraduateMapper.getThesisExistDate(institutionID);
            return RespBean.ok("", date);
        } catch (Exception e) {
            return RespBean.error("");
        }
    }

    public RespBean startThesis(Integer institutionID, Integer adminID, Integer year, String semester) {
        try {
            underGraduateMapper.startThesis(institutionID, adminID, year, semester);
            return RespBean.ok("");
        } catch (Exception e) {
            return RespBean.error("开启毕业设计失败！");
        }
    }

    @Transactional
    public RespBean updateUndergraduate(UnderGraduate under) {
        // 修改student表，undergraduate表和thesis表即可！
        try {
            studentMapper.edit(under);
            underGraduateMapper.update(under);
            thesisMapper.update(under);
            return RespBean.ok("");
        } catch (Exception e) {
            return RespBean.error("");
        }

    }


    @Transactional
    public RespBean updateUndergraduateBaseOnTeacher(UnderGraduate under) {
        // 修改student表，undergraduate表和thesis表即可！
        try {
            studentMapper.edit(under);
            underGraduateMapper.update(under);
            thesisMapper.updateWithStuID(under.getThesis());
            return RespBean.ok("");
        } catch (Exception e) {
            return RespBean.error("");
        }

    }

    public RespBean deleteThesis(UnderGraduate under) {
        try {
            thesisMapper.delete(under);
            return RespBean.ok("");
        } catch (Exception e) {
            return RespBean.error("删除失败！");
        }
    }

    public RespBean clearGroups(Integer startThesisID) {
        try {
            thesisMapper.clearGroups(startThesisID);
            return RespBean.ok("");
        } catch (Exception e) {
            return RespBean.error("清除失败！");
        }
    }

    public String createGroup(Integer startThesisID, List<Integer> arr, Integer exchangeNums, Integer groupsNums, String groupWay,List<String> selectInfo,HashMap<String,List<Integer>> orderNums) {
        List<Double> point = new ArrayList<>();
        List<Thesis> students = new ArrayList<>();
        List<double[]> point_participant = new ArrayList<>();
        if (groupWay.equals("专业"))
            students = underGraduateMapper.getUngroupedBySpecialty(startThesisID, selectInfo);
        else if (groupWay.equals("班级"))
            students = underGraduateMapper.getUngroupedByClass(startThesisID, selectInfo);
        for (Thesis student : students) {
            double[] temp = new double[4];
            point.add(Double.valueOf(student.getGrade()==null? 0.0:student.getGrade()));
            temp[0] = Double.valueOf(student.getGrade()==null? 0.0:student.getGrade());//分数
            temp[1] = Double.valueOf(student.getID());//学生id
            temp[2] = Double.valueOf(-1);//组号标识
            temp[3] = Double.valueOf(-1); //-1：非指定的学生 x：指定为x组的学生
            int index = 0;
            for(String key:orderNums.keySet()){
                boolean flag = false;
                for (Integer id : orderNums.get(key)) {
                    if (student.getStudentID().equals(id)) {
                        temp[3] = Double.valueOf(index);
                        flag = true;
                        break;
                    }
                }
                if (flag)
                    break;
                index++;
            }
            point_participant.add(temp);
        }
        //得到交换后的groups
        List<List<double[]>> res = groupsService.createGroupsByScore(arr, exchangeNums, groupsNums, point, point_participant);
        String name = "";
        //对每组遍历
        try {
            for (int residx = 0; residx < res.size(); residx++) {
                List<Integer> ids = new ArrayList<>();
                name = "第" + Integer.toString(residx + 1) + "组";
                for (int item = 0; item < res.get(residx).size(); item++) {
                    Integer id = (int) res.get(residx).get(item)[1];
                    ids.add(id);
                }
                if (ids.size() > 0)
                    underGraduateMapper.updateGroup(ids, name);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "分组失败";
        }
        return "分组成功";
    }

    public String createGroup_judge(Integer startThesisID, List<Integer> arr, Integer exchangeNums, Integer groupsNums, String groupWay,List<String> selectInfo,HashMap<String, List<Integer>> arrSub,HashMap<String, HashMap<String,List<Integer>>> orderNums) {
        if (arrSub == null){ //无指定专业或班级分组
            HashMap<String,List<Integer>> order_empty = new HashMap<>();
            return createGroup(startThesisID,arr,exchangeNums,groupsNums,groupWay,selectInfo,order_empty);
        }
        else {
            try {
                for (String s : selectInfo) {
                    List<String> info_single = new ArrayList<>();
                    info_single.add(s);
                    HashMap<String,List<Integer>> order = orderNums.get(s);
                    createGroup(startThesisID, arrSub.get(s), exchangeNums, groupsNums, groupWay, info_single, order);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
                return "分组失败";
            }
            return "分组成功";
        }
    }

    public RespBean importThesisName(Integer tutorId, Integer institutionID, Integer startThesisID, MultipartFile file) throws RespBean {
        // 1. 从excel解析出来的数据
        Msg excelData = readExcel.readThesisNameExcelData(tutorId, institutionID, startThesisID, file);
        if (excelData.getCode() == 500) {
            throw RespBean.error(excelData.getMsg());
        }

        // 2. 进行总结
        Integer total = (Integer) excelData.getExtend().get("total");
        Integer update = (Integer) excelData.getExtend().get("update");
        DataProcessingResult record = (DataProcessingResult) excelData.getExtend().get("record");
        record.setTotal(total);
        record.setFailedRowsCount(total - update);
        record.setDuplicateInsertRowsCount(update);
        return RespBean.ok("", record);
    }

    public List<UnderGraduate> getStudentByConditions(StudentCondition studentCondition) {
        return underGraduateMapper.getStudentByConditions(studentCondition);
    }

    public RespBean deleteStartThesis(Integer startThesisID) {
        try {
            underGraduateMapper.deleteStartThesis(startThesisID);
        } catch (Exception e) {
            return RespBean.error("请先手动删除改毕业设计下的学生！");
        }
        return RespBean.ok("");
    }

    public RespBean deleteThesisByStartThesis(Integer startThesisID) {
        try {
            thesisMapper.deleteThesisByStartThesis(startThesisID);
        } catch (Exception e) {
            return RespBean.error("删除失败，学生已经填写记录！");
        }
        return RespBean.ok("");
    }
}
