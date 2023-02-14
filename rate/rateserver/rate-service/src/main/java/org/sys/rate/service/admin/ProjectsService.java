package org.sys.rate.service.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.sys.rate.mapper.ProjectsMapper;
import org.sys.rate.model.Projects;

import java.util.List;

@Service
public class ProjectsService {
    @Autowired
    private ProjectsMapper projectsService;

    public Projects selectProjectsById(Long ID){
        return projectsService.selectProjectsById(ID);
    }

    /**
     * 查询项目列表
     *
     * @param projects 项目
     * @return 项目集合
     */
    public List<Projects> selectProjectsList(Projects projects){
        return projectsService.selectProjectsList(projects);
    }

    /**
     * 新增项目
     *
     * @param projects 项目
     * @return 结果
     */
    public int insertProjects(Projects projects){
        return projectsService.insertProjects(projects);
    }

    /**
     * 修改项目
     *
     * @param projects 项目
     * @return 结果
     */
    public int updateProjects(Projects projects){
        return projectsService.updateProjects(projects);
    }

    /**
     * 删除项目
     *
     * @param ID 项目ID
     * @return 结果
     */
    public int deleteProjectsById(Long ID){
       return projectsService.deleteProjectsById(ID);
    }

    public List<Projects> selectList(){
        return projectsService.selectList();
    }
}
