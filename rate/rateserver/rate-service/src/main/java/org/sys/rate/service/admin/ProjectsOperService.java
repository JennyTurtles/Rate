package org.sys.rate.service.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.sys.rate.mapper.ProjectsOperMapper;
import org.sys.rate.model.ProjectsOper;

import java.util.List;

@Service
public class ProjectsOperService {
    @Autowired
    private ProjectsOperMapper projectoperMapper;


    public int insertProjectOper(ProjectsOper projectsoper){

        System.out.println(projectsoper.getProjectName());
        return projectoperMapper.insertProjectOper(projectsoper);
    }
    public List<ProjectsOper> selectList(Long ID){
        return projectoperMapper.selectList(ID);
    }
}
