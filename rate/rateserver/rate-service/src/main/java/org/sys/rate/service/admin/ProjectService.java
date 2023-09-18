package org.sys.rate.service.admin;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import org.sys.rate.mapper.OperationMapper;
import org.sys.rate.mapper.ProjectMapper;
import org.sys.rate.mapper.ProjectTypeMapper;
import org.sys.rate.model.*;
import org.sys.rate.model.Project;
import org.sys.rate.service.mail.MailToStuService;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ProjectService {
    @Resource
    private ProjectMapper projectMapper;
    @Resource
    private ProjectTypeMapper projectTypeMapper;
    @Resource
    private OperationMapper operationMapper;
    @Resource
    private MailToStuService mailToStuService;

    public List<Project> selectProjectListById(Integer studentID) {
        List<Project> list = projectMapper.selectProjectListById(studentID);
        return list;
    }
    public List<Project> selectHorizontalProjectListById(Integer studentID) {
        List<Project> list = projectMapper.selectHorizontalProjectListById(studentID);
        return list;
    }

    /**
     * 新增科研专著教材成果
     *
     * @param project 科研专著教材成果
     * @return 结果
     */
    public int insertProject(Project project) {
        return projectMapper.insertProject(project);
    }

    public int updateProject(Project project) {
        return projectMapper.updateProject(project);
    }

    /**
     * 删除科研专著教材成果
     *
     * @param ID 科研专著教材成果ID
     * @return 结果
     */
    public int deleteProjectById(Long ID) {
        return projectMapper.deleteProjectById(ID);
    }

    public List<Project> selectAllProjectList() {
        List<Project> list = projectMapper.selectAllProjectList();
        return list;
    }

    public List<Project> setProjectOperation(List<Project> list) {
        for (int i = 0; i < list.size(); i++) {
            Project project = list.get(i);
            List<Operation> projectoperList = project.getOperationList();
            if (projectoperList == null || projectoperList.size() == 0) {
                continue;
            }
            if (projectoperList.size() == 1) {//只有一个提交
                project.setDate(projectoperList.get(0).getTime());
                continue;
            }
            int indexReject = -1;
            Timestamp timeCommit = new Timestamp(new Date().getTime());
            Timestamp timeReject = projectoperList.get(0).getTime();
            for (int projectOper = 0; projectOper < projectoperList.size(); projectOper++) {
                if (projectoperList.get(projectOper).getOperationName().equals("提交著作") && projectoperList.get(projectOper).getOperatorRole().equals("student")) {
//                    因为可能有多次提交（如老师驳回、再次提交），所以找到时间最早的一条记录
                    if (timeCommit.getTime() > projectoperList.get(projectOper).getTime().getTime()) {
                        timeCommit = projectoperList.get(projectOper).getTime();
                    }
                }
                if (projectoperList.get(projectOper).getOperationName().equals("审核驳回") && (projectoperList.get(projectOper).getOperatorRole().equals("teacher") || projectoperList.get(projectOper).getOperatorRole().equals("admin"))) {
//                    可能有多次驳回，所以找到时间最晚的一条记录
                    if (timeReject.getTime() < projectoperList.get(projectOper).getTime().getTime()) {
                        indexReject = projectOper; //有可能该专利没有被驳回过，所以后续通过indexReject判断是否为初始值-1
                        timeReject = projectoperList.get(projectOper).getTime();
                    }
                }
            }
            if (indexReject != -1) { //说明有驳回记录
                if (project.getState().equals("commit") || project.getState().equals("tea_pass") || project.getState().equals("adm_pass")) {
                    //但是如果当前的专利处于通过或再次被提交状态就不返回驳回理由
                    project.setRemark("");
                } else {
                    project.setRemark(projectoperList.get(indexReject).getRemark());
                }
            }
//            这里是返回里最早提交时间，显示详情中应该是申请的时间
//            project.setDate(timeCommit);
        }
        return list;
    }

    //    修改科研专著教材状态
    public int editState(String state, Long ID) throws MessagingException {
        Project project = projectMapper.getById(Math.toIntExact(ID));
        mailToStuService.sendStuMail(state, project, null, "科研项目");
        return projectMapper.editState(state, ID);
    }

    //    public List<Project> searchProjectByConditions(String studentName, String state, String monoName, String pointFront, String pointBack) {
//        return projectMapper.searchProjectByConditions(studentName, state, monoName, pointFront, pointBack);
//    }
    public List<ProjectType> getIndicatorByYearAndType(String year, String type) {
        List<ProjectType> list = projectTypeMapper.getIndicatorByYearAndType(year, type);
        return list;
    }

    public List<Project> searchProjectByConditions(String studentName, String state, String projectName, String pointFront, String pointBack) {
        List<Project> list = projectMapper.searchProjectByConditions(studentName, state, projectName, pointFront, pointBack);
        return list;
    }
    public List<Project> searchHorizontalProjectByConditions(String studentName, String state, String projectName, String pointFront, String pointBack) {
        List<Project> list = projectMapper.searchHorizontalProjectByConditions(studentName, state, projectName, pointFront, pointBack);
        return list;
    }
}
