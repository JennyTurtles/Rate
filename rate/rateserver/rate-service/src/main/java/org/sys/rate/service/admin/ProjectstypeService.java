package org.sys.rate.service.admin;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.sys.rate.mapper.ProjectstypeMapper;
import org.sys.rate.model.Projectstype;
import org.sys.rate.model.Publication;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ProjectstypeService {
    @Resource
    private ProjectstypeMapper projectstypeMapper;

    /**
     * 查询著作类型
     *
     * @param ID 著作类型ID
     * @return 著作类型
     */
    public Projectstype selectProjectstypeById(Long ID){
        return projectstypeMapper.selectProjectstypeById(ID);
    }

    /**
     * 查询著作类型列表
     *
     * @param projectstype 著作类型
     * @return 著作类型集合
     */
    public List<Projectstype> selectProjectstypeList(Projectstype projectstype){
        return projectstypeMapper.selectProjectstypeList(projectstype);
    }

    //模糊查询 返回type
    public List<Projectstype> selectTypeListByName(@Param("TypeName") String TypeName){
        System.out.println("调用了ProjectstypeService");
        System.out.println(TypeName);
        List<Projectstype> res=projectstypeMapper.selectListByTypeName(TypeName);
//        List<String> res=paperMapper.selectListByPubName(publicationName);
//        System.out.println("res:");
//        System.out.println(res);
        return res;
    }

    /**
     * 新增著作类型
     *
     * @param projectstype 著作类型
     * @return 结果
     */
    public int insertProjectstype(Projectstype projectstype){
        return projectstypeMapper.insertProjectstype(projectstype);
    }

    /**
     * 修改著作类型
     *
     * @param projectstype 著作类型
     * @return 结果
     */
    public int updateProjectstype(Projectstype projectstype){
        return projectstypeMapper.updateProjectstype(projectstype);
    }

    /**
     * 删除著作类型
     *
     * @param ID 著作类型ID
     * @return 结果
     */
    public int deleteProjectstypeById(Long ID){
        return projectstypeMapper.deleteProjectstypeById(ID);
    }

    public List<Projectstype> selectList(){
        return projectstypeMapper.selectList();
    }

 //   public Long selectScoreById(long id)
 //   {
 //       return projectstypeMapper.selectScoreById(id);
 //   }

    public Projectstype selectTypeByName(String name){return projectstypeMapper.selectTypeByName(name);}

 //   public List<Projectstype> selectTypeListBy(Integer year){
 //       return projectstypeMapper.selectTypeByYear(year);
 //   }
}
