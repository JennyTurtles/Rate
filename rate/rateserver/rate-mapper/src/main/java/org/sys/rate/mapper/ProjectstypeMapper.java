package org.sys.rate.mapper;

import java.util.List;

import org.sys.rate.model.Award;
import org.sys.rate.model.Projects;
import org.sys.rate.model.Projectstype;

/**
 * 著作类型Mapper接口
 * 
 * @author system
 * @date 2022-03-13
 */
public interface ProjectstypeMapper 
{
    /**
     * 查询著作类型
     * 
     * @param ID 著作类型ID
     * @return 著作类型
     */
    public Projectstype selectProjectstypeById(Long ID);

    /**
     * 查询著作类型列表
     * 
     * @param projectstype 著作类型
     * @return 著作类型集合
     */
    public List<Projectstype> selectProjectstypeList(Projectstype projectstype);

    /**
     * 新增著作类型
     * 
     * @param projectstype 著作类型
     * @return 结果
     */
    public int insertProjectstype(Projectstype projectstype);

    /**
     * 修改著作类型
     * 
     * @param projectstype 著作类型
     * @return 结果
     */
    public int updateProjectstype(Projectstype projectstype);

    /**
     * 删除著作类型
     * 
     * @param ID 著作类型ID
     * @return 结果
     */
    public int deleteProjectstypeById(Long ID);

    /**
     * 批量删除著作类型
     * 
     * @param IDs 需要删除的数据ID
     * @return 结果
     */
    public int deleteProjectstypeByIds(String[] IDs);

    public List<Projectstype> selectList();
}
