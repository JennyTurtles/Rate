package org.sys.rate.service.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.sys.rate.mapper.ProjectstypeMapper;
import org.sys.rate.model.Projectstype;

import java.util.List;

@Service
public class ProjectstypeService {
    @Autowired
    private ProjectstypeMapper projectsMapper;

    /**
     * 查询著作类型
     *
     * @param ID 著作类型ID
     * @return 著作类型
     */
    public Projectstype selectProjectstypeById(Long ID){
        return projectsMapper.selectProjectstypeById(ID);
    }

    /**
     * 查询著作类型列表
     *
     * @param projectstype 著作类型
     * @return 著作类型集合
     */
    public List<Projectstype> selectProjectstypeList(Projectstype projectstype){
        return projectsMapper.selectProjectstypeList(projectstype);
    }

    /**
     * 新增著作类型
     *
     * @param projectstype 著作类型
     * @return 结果
     */
    public int insertProjectstype(Projectstype projectstype){
        return projectsMapper.insertProjectstype(projectstype);
    }

    /**
     * 修改著作类型
     *
     * @param projectstype 著作类型
     * @return 结果
     */
    public int updateProjectstype(Projectstype projectstype){
        return projectsMapper.updateProjectstype(projectstype);
    }

    /**
     * 删除著作类型
     *
     * @param ID 著作类型ID
     * @return 结果
     */
    public int deleteProjectstypeById(Long ID){
        return projectsMapper.deleteProjectstypeById(ID);
    }

    public List<Projectstype> selectList(){
        return projectsMapper.selectList();
    }
}
