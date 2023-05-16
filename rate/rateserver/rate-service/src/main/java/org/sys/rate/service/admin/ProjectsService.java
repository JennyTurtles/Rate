package org.sys.rate.service.admin;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.sys.rate.mapper.ProjectsMapper;
import org.sys.rate.model.*;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import java.util.List;

@Service
public class ProjectsService {

    @Resource
    private ProjectsMapper projectsMapper;

    public Projects getById(Integer ID) {
        Projects projects = projectsMapper.getById(ID);
        if (projects != null) {
            return projects;
        }
        return null;
    }

    public List<Projects> selectListById(@Param("projectTypeId") Integer studentID){
//        System.out.println(studentID);
//        System.out.println("...");
        List<Projects> p = projectsMapper.selectListById(studentID);
//        System.out.println(p);
        return p;
    }

    public List<Projects> selectListByIds(@Param("studentID") Integer studentID) {
        return projectsMapper.selectListByIds(studentID);
    }
    /**
     * 查询项目列表
     *
     * @param projects 项目
     * @return 项目集合
     */
    public List<Projects> selectProjectsList(Projects projects){
        return projectsMapper.selectProjectList(projects);
    }

    /**
     * 新增项目
     *
     * @param projects 项目
     * @return 结果
     */
    public int insertProjects(Projects projects){
        return projectsMapper.insertProjects(projects);
    }

    /**
     * 修改项目
     *
     * @param projects 项目
     * @return 结果
     */
    public int updateProjects(Projects projects){
        return projectsMapper.updateProjects(projects);
    }

    /**
     * 删除项目
     *
     * @param ID 项目ID
     * @return 结果
     */
    public int deleteProjectsById(Long ID){
       return projectsMapper.deleteProjectsById(ID);
    }

    public List<Projects> selectList(Integer id){
        return projectsMapper.selectList(id);
    }

    //    修改论文状态
    public int editState(String state, Long ID) throws MessagingException {
        Projects paper = projectsMapper.selectProjectById(ID);
//        mailToStuService.sendStuMail(state, paper);
        // 管理员通过的时候需要处理2分论文的情况，还要计算student的活动总分
        if (state.equals("adm_pass")){
            int stuID = paper.getStudentID();
//            int score = paper.getPoint();

//            if (score == 2){ // 2分的时候检查是否已经发表过2分的论文
//                if (projectsMapper.checkScore(Long.parseLong(String.valueOf(stuID))) != null){ // 已经发表过2分论文,将该论文的have_score设置为0
//                    return projectsMapper.editState2(state,ID,0);
//                }else {
//                    projectsMapper.editState2(state,ID,1);
//                    return projectsMapper.updateScore(stuID,score);
//                }
//            }
//            projectsMapper.updateScore(stuID,score);
        }
        return projectsMapper.editState(state,ID);
    }

    public int insertPaperOper(ProjectsOper paperoper){
        return projectsMapper.insertPaperoper(paperoper);
    }
}
