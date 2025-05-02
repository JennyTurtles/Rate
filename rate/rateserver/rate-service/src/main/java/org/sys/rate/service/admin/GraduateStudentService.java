package org.sys.rate.service.admin;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.multipart.MultipartFile;
import org.sys.rate.mapper.*;
import org.sys.rate.model.*;

import java.io.IOException;
import java.util.*;

@Service
public class GraduateStudentService {
    @Autowired
    StudentMapper studentMapper;
    @Autowired
    TeachersMapper teachersMapper;
    @Autowired
    TeacherMapper teacherMapper;
    @Autowired
    ParticipatesMapper participatesMapper;
    @Autowired
    UnderGraduateMapper underGraduateMapper;
    @Autowired
    GraduateStudentMapper graduateStudentMapper;



    public Msg checkGraduateStudent(MultipartFile file, int rowIndex, Integer groupid) {
        List<String> error = new ArrayList<>();
        Msg msg = new Msg();
        msg.setCode(200);
        msg.setMsg("校验成功");
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
                msg.setCode(500);
                msg.setMsg("导入的" + sheetName + "缺少姓名列，请检查！");
                return msg;
            }
            if (!hasNumberColumn) {
                error.add("导入的" + sheetName + "缺少学号列，请检查！");
                msg.setCode(500);
                msg.setMsg("导入的" + sheetName + "缺少学号列，请检查！");
                return msg;
            }
            if (!hasMobileColumn) {
                error.add("导入的" + sheetName + "缺少手机号列，请检查！");
                msg.setCode(500);
                msg.setMsg("导入的" + sheetName + "缺少手机号列，请检查！");
                return msg;
            }
            if (!hasEmailColumn) {
                error.add("导入的" + sheetName + "缺少邮箱列，请检查！");
                msg.setCode(500);
                msg.setMsg("导入的" + sheetName + "缺少邮箱列，请检查！");
                return msg;
            }
            if (!hasTuturNoColumn) {
                error.add("导入的" + sheetName + "缺少导师工号列，请检查！");

                msg.setCode(500);
                msg.setMsg("导入的" + sheetName + "缺少导师工号列，请检查！");
                return msg;
            }
            if (!hasTuturNameColumn) {
                error.add("导入的" + sheetName + "缺少导师姓名列，请检查！");
                msg.setCode(500);
                msg.setMsg("导入的" + sheetName + "缺少导师姓名列，请检查！");
                return msg;
            }
            if (!hasYearColumn) {
                error.add("导入的" + sheetName + "缺少入学年份列，请检查！");
                msg.setCode(500);
                msg.setMsg("导入的" + sheetName + "缺少入学年份列，请检查！");
                return msg;
            }
            if (!hasTypeColumn) {
                error.add("导入的" + sheetName + "缺少学生类别列，请检查！");
                msg.setCode(500);
                msg.setMsg("导入的" + sheetName + "缺少学生类别列，请检查！");
                return msg;
            }
            if (!hasMajorColumn) {
                error.add("导入的" + sheetName + "缺少专业列，请检查！");
                msg.setCode(500);
                msg.setMsg("导入的" + sheetName + "缺少专业列，请检查！");
                return msg;
            }
            if (!hasClassColumn) {
                error.add("导入的" + sheetName + "缺少班级列，请检查！");
                msg.setCode(500);
                msg.setMsg("导入的" + sheetName + "缺少班级列，请检查！");
                return msg;
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
                UnderGraduate underGraduate = new UnderGraduate();
                for (int k = 0; k < Cells; k++) {
                    HSSFCell cell = row.getCell(k);
                    String columnName = map.get(k);
                    boolean isTutorName = false;
                    if (cell != null) {
                        cell.setCellType(CellType.STRING);
                        String cellValue = cell.getStringCellValue();
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
                        if ("导师姓名".equals(columnName)) {
                            isTutorName = true;
                            if(!cellValue.equals("")){
                                isTuturNameEmpty = false;
                                underGraduate.setTutorName(cellValue);
                            }
                        }
                        if ("入学年份".equals(columnName) && !cellValue.equals("")) {
                            isYearEmpty = false;
                        }
                        if ("学生类别".equals(columnName) && !cellValue.equals("")) {
//                            isTypeEmpty = false;
                            if (groupid.equals(1)){
                                //研究生
                                if (cellValue.equals("专硕")||cellValue.equals("学硕")){
                                    isTypeEmpty = false;
                                }else {
                                    isTypeEmpty = true;
                                }
                            }else{
                                //博士
                                if (cellValue.equals("专博")||cellValue.equals("学博")){
                                    isTypeEmpty = false;
                                }else {
                                    isTypeEmpty = true;
                                }
                            }
                        }
                        if((!isTuturNameEmpty || !isTuturNoEmpty)&& isTutorName ){
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
                    msg.setCode(500);
                    msg.setMsg("第【" + (j + 1) + "】行的姓名为空，请确认");
                    return msg;
                }
                if (isNumberEmpty){
                    String tips = "第【" + (j + 1) + "】行的学号为空，请确认";
                    error.add(tips);
                    msg.setCode(500);
                    msg.setMsg("第【" + (j + 1) + "】行的学号为空，请确认");
                    return msg;
                }
                if (isYearEmpty) {
                    String tips = "第【" + (j + 1) + "】行的入学年份为空，请确认";
                    error.add(tips);
                    msg.setCode(500);
                    msg.setMsg("第【" + (j + 1) + "】行的入学年份为空，请确认");
                    return msg;
                }
                if (isTypeEmpty) {
                    String tips = "第【" + (j + 1) + "】行的学生类别为空，请确认";
                    error.add(tips);
                    msg.setCode(500);
                    msg.setMsg("第【" + (j + 1) + "】行的学生类别为空，请确认");
                    return msg;
                }
                if (isTuturNoEmpty) {
                    String tips = "第【" + (j + 1) + "】行的导师工号为空，请确认";
//                    msg.setCode(200);
//                    msg.setMsg("第【" + (j + 1) + "】行的导师工号为空，请确认");
//                    return msg;
                }
                if(!dataArr.isEmpty()){
                    List<String> tmpDataArr = checkGraduateStudent(dataArr,j+1);
                    if(tmpDataArr != null && !tmpDataArr.isEmpty()){
                        String s = tmpDataArr.toString();
                        String result = s.substring(1, s.length() - 1);
                        error.addAll(tmpDataArr);
                        msg.setCode(500);
                        msg.setMsg(result);
                        return msg;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return msg;
    }
    public RespBean updatePoint1(Integer studentID) {
        try {
            int res = graduateStudentMapper.updatePoint1(studentID);
            if (res > 0) {
                return RespBean.ok("更新成功");
            } else {
                return RespBean.error("更新失败");
            }
        } catch (Exception e) {
            return RespBean.error("更新失败");
        }
    }
    public List<String> checkGraduateStudent(List<UnderGraduate> underList, int rowIndex) {
        List<String> jobTeas = new ArrayList<>(); // 记录导师的工号
        List<String> nameTeas = new ArrayList<>(); // 记录导师的姓名
        List<String> errors = new LinkedList<>();
        for(int i = 0; i < underList.size(); i++) {
            UnderGraduate underGraduatenderGraduate = underList.get(i);
            String jobNumber = underGraduatenderGraduate.getTutorJobNumber();
            String name = underGraduatenderGraduate.getTutorName();
            if(name != null && !name.isEmpty())
                name = name.replaceAll("\\(.*?\\)", "");

            // ------------------------- 新增校验逻辑 -------------------------
            if (jobNumber != null && !jobNumber.isEmpty()) {
                // 情况1：有工号，以工号为准
                Teachers dbTeacher = teachersMapper.selectTeaByJobnumber(jobNumber);
                if (dbTeacher == null) {
                    errors.add("工号 " + jobNumber + " 对应的导师不存在");
                    return errors;
                }
                // 如果同时有姓名，需验证是否匹配
                if (name != null && !name.isEmpty() && !name.equals(dbTeacher.getName())) {
                    errors.add("工号 " + jobNumber + " 与姓名 " + name + " 不匹配");
                    return errors;
                }
                underGraduatenderGraduate.setTeachers(dbTeacher);
                underGraduatenderGraduate.setTutorID(dbTeacher.getID());
            } else if (name != null && !name.isEmpty()) {
                // 情况2：只有姓名，检查是否唯一
                List<Teachers> teachers = teachersMapper.selectTeasByName(Collections.singletonList(name));
                if (teachers.isEmpty()) {
                    errors.add("未找到姓名为 " + name + " 的导师");
                    return errors;
                } else if (teachers.size() > 1) {
                    errors.add("第【" + rowIndex + "】行的导师姓名\"" + name + "\"存在重名情况，请填写工号信息");
                    return errors;
                }
                if(!teachers.isEmpty()){
                underGraduatenderGraduate.setTeachers(teachers.get(0));
                underGraduatenderGraduate.setTutorID(teachers.get(0).getID());}
            } else {
                // 情况3：工号和姓名都为空
                underGraduatenderGraduate.setTutorID(null);
            }
            // ------------------------- 结束新增逻辑 -------------------------
        }

        return errors;
    }


    //管理员导入研究生，只添加，即使已经存在了该条数据也不更新
    @Transactional
    public RespBean addGraduate(List<GraduateStudent> graduateList) {

        List<String> jobTeas = new ArrayList<>(); //记录导师的工号`
        List<String> nameTeas = new ArrayList<>(); //记录导师的姓名

        for(int i = 0; i < graduateList.size(); i++) {
            GraduateStudent graduate = graduateList.get(i);
            Teachers teacher = graduate.getTeachers();
            String jobNumber = teacher.getJobnumber();
            String name = teacher.getName();

        }

        for(int i = 0;i < graduateList.size();i++){
            //工号和姓名都有按照工号来，都没有tutorid为空，只有姓名就按照姓名查找

            if (StringUtils.isBlank(graduateList.get(i).getTeachers().getJobnumber()))
                graduateList.get(i).getTeachers().setJobnumber(null);
            if(graduateList.get(i).getTeachers().getJobnumber() == null && graduateList.get(i).getTeachers().getName() == null){
                graduateList.get(i).setTutorID(null);
            }
            if(graduateList.get(i).getTeachers().getJobnumber() != null){
                jobTeas.add(graduateList.get(i).getTeachers().getJobnumber());
            }
            if(graduateList.get(i).getTeachers().getJobnumber() == null && graduateList.get(i).getTeachers().getName() != null){
                nameTeas.add(graduateList.get(i).getTeachers().getName());
            }
        }
        List<Teachers> jobTeachers = new ArrayList<>();
        if (jobTeas.size() > 0){
            jobTeachers = teachersMapper.selectTeasByJobnumber(jobTeas);
            if(jobTeachers.size() == 0) {
                return RespBean.error("未找到老师信息！请仔细检查工号");
            }
        }
        //改为根据机构id和学号判断在student表中是否已经存在
        Integer id = null;
        List<Student> updateStus = new ArrayList<>();
        List<GraduateStudent> insertGraduates = new ArrayList<>();
        List<GraduateStudent> updateGraduates = new ArrayList<>();
        for(int i = 0;i < graduateList.size(); i ++) {
            GraduateStudent graduateStudent = graduateList.get(i);
            id = graduateStudentMapper.checkStudentExist(graduateStudent.getStuNumber(), graduateStudent.getName(), graduateStudent.getInstitutionID());
            if (id != null) {
                if (id.equals(-1)) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    return RespBean.error("学生学号和姓名不匹配");
                }
                graduateStudent.setStudentID(id);
                try {
                    // 更新研究生表信息
                    Student student = new Student();
                    student.setID(graduateStudent.getStudentID());
                    student.setInstitutionID(graduateStudent.getInstitutionID());
                    student.setTelephone(graduateStudent.getTelephone());
                    student.setEmail(graduateStudent.getEmail());
                    student.setName(graduateStudent.getName());
                    updateStus.add(student);
                } catch (Exception e) {
                    return RespBean.error("更新研究生信息出现错误！");
                }
            } else { //需要插入新学生记录
                Student student = new Student();
                student.setInstitutionID(graduateStudent.getInstitutionID());
                student.setTelephone(graduateStudent.getTelephone());
                student.setEmail(graduateStudent.getEmail());
                student.setRole("11");
                student.setName(graduateStudent.getName());
                int studentId = 0;
                try {
                    studentId = studentMapper.insertStudentByImportGraduate(student);
                } catch (Exception e) {
                    return RespBean.error("插入学生出错！");
                }
                graduateStudent.setStudentID(student.getID());
            }
        }
        if(updateStus.size() > 0) {
            studentMapper.updateFromAdminExcel(updateStus); // 更新student表的信息
        }

        List<Teachers> nameTeachers = new ArrayList<>();
        List<Teachers> updateTeachers = new ArrayList<>();
        if(jobTeas.size() > 0){ //找到对应的导师信息，更新导师的role
            for(int i = 0;i < graduateList.size();i++) {
                if(graduateList.get(i).getTeachers().getJobnumber() == null || graduateList.get(i).getTeachers().getJobnumber().equals("")){
                    continue;
                }
                for (int j = 0; j < jobTeachers.size(); j++) {
                    if (graduateList.get(i).getTeachers().getJobnumber().equals(jobTeachers.get(j).getJobnumber())) {
                        String tearole = jobTeachers.get(j).getRole();
                        if(!tearole.contains("9")){
                            tearole += ";9";
                            jobTeachers.get(j).setRole(tearole);
                            updateTeachers.add(jobTeachers.get(j));
                        }
                        graduateList.get(i).setTeachers(jobTeachers.get(j));
                        graduateList.get(i).setTutorID(jobTeachers.get(j).getID());
                        break;
                    }
                }
            }
        }
        if(nameTeas.size() > 0){
            nameTeachers = teachersMapper.selectTeasByName(nameTeas);
            for(int i = 0;i < graduateList.size();i++) {
                if(graduateList.get(i).getTeachers().getName() == null || graduateList.get(i).getTeachers().getName().equals("")){
                    continue;
                }
                for (int j = 0; j < nameTeachers.size(); j++) {
                    if (graduateList.get(i).getTeachers().getName().equals(nameTeachers.get(j).getName())) {
                        String tearole = nameTeachers.get(j).getRole();
                        if(!tearole.contains("9")){
                            tearole += ";9";
                            nameTeachers.get(j).setRole(tearole);
                            updateTeachers.add(nameTeachers.get(j));
                        }
                        graduateList.get(i).setTeachers(nameTeachers.get(j));
                        graduateList.get(i).setTutorID(nameTeachers.get(j).getID());
                        break;
                    }
                }
            }
        }

        for(int i = 0;i < graduateList.size(); i ++) { //拿到需要插入研究生表的数据
            GraduateStudent graduateStudent = graduateList.get(i);
            id = graduateStudentMapper.checkStudentExist(graduateStudent.getStuNumber(), graduateStudent.getName(), graduateStudent.getInstitutionID());
            if (id == null) {
                insertGraduates.add(graduateStudent);
            } else {
                updateGraduates.add(graduateStudent);
            }
        }

        try {
            if(insertGraduates.size() > 0){
                graduateStudentMapper.insertFROMImport(insertGraduates);
            }
            if(updateGraduates.size() > 0){
                graduateStudentMapper.updateFROMImport(updateGraduates);
            }
            if(updateTeachers.size() > 0){//需要对老师的role字段进行更新
                teachersMapper.updateRoleOfTeachers(updateTeachers);
            }
        }catch (Exception e){
            return RespBean.error("error");
        }
        return RespBean.ok("ok");
    }

    public List<GraduateStudent> getGraduatesStudent(){
        List<GraduateStudent> res = new ArrayList<>();
        res = graduateStudentMapper.getGraduateStudent();
        return res;
    }
    public Msg deleteGraduateStudent(GraduateStudent grad){
        //点击删除也要删除student表中的学生信息，所以要判断这个student是不是还是选手或其他身份
        //同时也要删除对应老师的角色，所以也要判断这个老师是不是还是别的学生的导师
        try {
            graduateStudentMapper.deleteGraduateStudent(grad);
            //如果没有导师就直接删除
            if(grad.getTutorID() != null && !grad.getTutorID().equals("")){
                //说明只是这一个学生的研究生老师，需要去掉这个老师的研究生导师角色
                if(graduateStudentMapper.checkHaveStudentOftutorID(grad.getTutorID(),grad.getID()) == 0){
                    Teachers tea = teachersMapper.selectByPrimaryId(grad.getTutorID());
                    String r = tea.getRole();
                    //区分要不要删掉多余分号，我懒得思考了，有bug再说
                    if(r.contains("11;")){
                        r = r.replace("11;","");
                        tea.setRole(r);
                        teachersMapper.updateRoleOfOneTeacher(tea);
                    }else if(r.contains("11")){
                        r = r.replace("11","");
                        tea.setRole(r);
                        teachersMapper.updateRoleOfOneTeacher(tea);
                    }
                }
            }
            if(underGraduateMapper.checkHaveStudentOfstudenID(grad.getStudentID()) == 0 &&
                    participatesMapper.isParticipants(grad.getStudentID()) == 0){
                //如果在选手表中和本科生表中查不到关于这个stuid关联的数据，说明可以删除
                studentMapper.deleteStudent(grad.getStudentID());
            }
        }catch (Exception e){
            return Msg.fail();
        }return Msg.success();
    }
    public RespBean editgraduateStudent(GraduateStudent graduate){
        //目的不在于更改老师表中的信息
        //直接限定老师名字和工号必须填写了
        try {
            Teachers tea = null;
            //先判断有没有这个导师的存在
            if(graduate.getTeachers().getJobnumber() != null && !graduate.getTeachers().getJobnumber().equals("")){
                tea = teachersMapper.selectTeaByJobnumber(graduate.getTeachers().getJobnumber());
                if(tea == null){//有工号，但是没有查到，说明工号错误
                    return RespBean.error("未查询到该老师");
                }
                //如果根据工号查到了这个老师，但是数据库的老师名字和前端传来的老师名字不同，说明信息填写错误
                if(graduate.getTeachers().getName()!= null && !graduate.getTeachers().getName().equals("") && !tea.getName().equals(graduate.getTeachers().getName())){
                    return RespBean.error("该老师工号和姓名不符，请仔细检查信息");
                }
                graduate.setTutorID(tea.getID());//重新设置导师id
                graduateStudentMapper.editGraduateStudent(graduate);
            }
        }catch (Exception e){
            return RespBean.error("处理失败");
        }return RespBean.ok("更新成功");
    }
    public RespBean getTeaNamesBySelect(String teaName){
        List<Teachers> res;
        res = graduateStudentMapper.getTeaNamesBySelect(teaName);
        return RespBean.ok("ok",res);
    }
    //管理员段筛选
    public List<GraduateStudent> getGraduateStudentsBySelect(Integer year, String teaName){
        List<GraduateStudent> res = new ArrayList<>();
        res = graduateStudentMapper.getGraduateStudentsBySelect(teaName,year);
        return res;
    }
    //教师端筛选
    public List<GraduateStudent> getGraduateStudentsBySelectOfTeacher(Integer tutorID, Integer year){
        List<GraduateStudent> res = new ArrayList<>();
        res = graduateStudentMapper.getGraduateStudentsBySelectOfTeacher(tutorID,year);
        return res;
    }

    public GraduateStudent getGradByStuID(Integer studentID){
        return graduateStudentMapper.getGradByStuID(studentID);
    }
}
