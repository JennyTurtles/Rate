package org.sys.rate.service.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.sys.rate.mapper.*;
import org.sys.rate.model.*;
import org.sys.rate.utils.ReadExcel;

import javax.annotation.Resource;
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

    //管理员导入本科生，只添加，即使已经存在了该条数据也不更新
    public RespBean addUnderGraduate(List<UnderGraduate> underList, List<Student> stuList,Integer institutionID) {
        //根据学号和单位得到已经存在的本科生
        List<UnderGraduate> checkStudents = underGraduateMapper.checkAndReturnID(underList,institutionID);
        List<String> checkIDNumbers = new ArrayList<>();
        List<UnderGraduate> insertUnder = new ArrayList<>();
        List<Student> insertStus = new ArrayList<>();
        if (checkStudents.size() != 0) {
            for (UnderGraduate i : checkStudents) {//在本单位已经存在的学号
                checkIDNumbers.add(i.getStuNumber());
            }
            for (int i = 0; i < underList.size(); i++) {
                //不在更新列表中，说明表里没有这个数据
                if (checkIDNumbers.indexOf(underList.get(i).getStuNumber()) == -1) {
                    insertUnder.add(underList.get(i));
                    insertStus.add(stuList.get(i));
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
            return RespBean.error("error");
        }
        //对本科生循环 设置stuid
        for (int i = 0; i < insertUnder.size(); i++) {
            insertUnder.get(i).setStudentID(insertStus.get(i).getID());
        }
        try {
            if (insertUnder.size() > 0) {
                underGraduateMapper.insertFROMImport(insertUnder);
            }
        } catch (Exception e) {
            return RespBean.error("error");
        }
        return RespBean.ok("ok");
    }

    public List<UnderGraduate> getUnderStudent() {
        List<UnderGraduate> res = new ArrayList<>();
        try {
            res = underGraduateMapper.getUnderStudent();
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
    public RespBean importThesis(String type, Integer institutionID, Integer year, String semester, MultipartFile file) throws RespBean {
        // 1. 从excel解析出来的数据
        Msg excelData = readExcel.readStartThesisExcelData(type, institutionID, file);
        if (excelData.getCode() == 500) {
            throw RespBean.error(excelData.getMsg());
        }

        // 3.对于学期进行分解，3-春季，9-秋季
        Integer month;
        if (semester.length() > 1) {
            month = "春季".equals(semester) ? 3 : 9;
        } else {
            month = Integer.valueOf(semester);
        }

        // 4.插入thesis表(或者当type==teacher时，需要更新thesis表)
        List thesisList = (List<Thesis>) excelData.getExtend().get("thesis");
        RespBean thesisResBean = this.insertOrUpdateThesis(thesisList, year, month, type);
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


    private RespBean insertOrUpdateThesis(List<Thesis> thesisList, Integer year, Integer month, String type) {
        int rows = 0;
        try {
            rows = thesisService.upsert(thesisList, year, month, type);
        } catch (Exception e) {
            String errorMessage = "插入或者更新操作毕业论文信息时出错！";
            return RespBean.error(errorMessage);
        }

        return RespBean.ok("", rows);
    }


    public List<UnderGraduate> getStudent(Integer institutionID, Integer year, Integer month) {
        return underGraduateMapper.getStudent(institutionID, year, month);
    }

    public RespBean getThesisExistDate(Integer institutionID) {
        try {
            List<String> date = underGraduateMapper.getThesisExistDate(institutionID);
            return RespBean.ok("", date);
        } catch (Exception e) {
            return RespBean.error("");
        }
    }

    public RespBean startThesis(Integer institutionID, Integer year, String semester) {
        try {
            underGraduateMapper.startThesis(institutionID, year, semester);
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
            return RespBean.error("");
        }
    }

    public String createGroup(Integer year, Integer month, List<Integer> arr, Integer exchangeNums, Integer groupsNums, String groupWay,List<String> selectInfo,HashMap<String,List<Integer>> orderNums) {
        List<Double> point = new ArrayList<>();
        List<Thesis> students = new ArrayList<>();
        List<double[]> point_participant = new ArrayList<>();
        if (groupWay.equals("专业"))
            students = underGraduateMapper.getUngroupedBySpecialty(year, month, selectInfo);
        else if (groupWay.equals("班级"))
            students = underGraduateMapper.getUngroupedByClass(year, month, selectInfo);
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
                underGraduateMapper.updateGroup(ids, name);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "分组失败";
        }
        return "分组成功";
    }

    public String createGroup_judge(Integer year, Integer month, List<Integer> arr, Integer exchangeNums, Integer groupsNums, String groupWay,List<String> selectInfo,HashMap<String, List<Integer>> arrSub,HashMap<String, HashMap<String,List<Integer>>> orderNums) {
        if (arrSub.size() == 0){ //无指定专业或班级分组
            HashMap<String,List<Integer>> order_empty = new HashMap<>();
            return createGroup(year,month,arr,exchangeNums,groupsNums,groupWay,selectInfo,order_empty);
        }
        else {
            try {
                for (String s : selectInfo) {
                    List<String> info_single = new ArrayList<>();
                    info_single.add(s);
                    HashMap<String,List<Integer>> order = orderNums.get(s);
                    createGroup(year, month, arrSub.get(s), exchangeNums, groupsNums, groupWay, info_single, order);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
                return "分组失败";
            }
            return "分组成功";
        }
    }

    public RespBean importThesisName(Integer tutorId, Integer institutionID, Integer year, Integer month, MultipartFile file) throws RespBean {
        // 1. 从excel解析出来的数据
        Msg excelData = readExcel.readThesisNameExcelData(tutorId, institutionID, year, month, file);
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
}
