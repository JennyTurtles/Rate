package org.sys.rate.service.admin;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.sys.rate.mapper.OperationMapper;
import org.sys.rate.mapper.ProjectMapper;
import org.sys.rate.mapper.ProjectTypeMapper;
import org.sys.rate.mapper.PublicationMapper;
import org.sys.rate.model.Operation;
import org.sys.rate.model.Project;
import org.sys.rate.model.ProjectType;
import org.sys.rate.model.Publication;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ProjectTypeService {

    @Autowired
    private ProjectTypeMapper projectTypeMapper;

    public List<ProjectType> selectProjectTypeListByYear(Integer indicatorID, Integer year, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        List<ProjectType> publications = projectTypeMapper.selectProjectTypeListByYear(indicatorID, year);

        // 使用 PageInfo 包装查询结果，获取分页信息
        PageInfo<ProjectType> pageInfo = new PageInfo<>(publications);
        return pageInfo.getList();
    }

    public Integer addProjectType(ProjectType projectType) {
        return projectTypeMapper.addProjectType(projectType);
    }

    public Integer editProjectType(ProjectType projectType) {
        try {
            return projectTypeMapper.editProjectType(projectType);
        }catch (DuplicateKeyException e){
            return 0;
        }
    }


    public Integer deleteProjectTypeById(Integer id) {
        return projectTypeMapper.deleteById(id);
    }
}
