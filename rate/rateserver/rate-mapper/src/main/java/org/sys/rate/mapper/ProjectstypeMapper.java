package org.sys.rate.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.sys.rate.model.Award;
import org.sys.rate.model.Projects;
import org.sys.rate.model.Projectstype;
import org.sys.rate.model.Publication;

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
    public List<Projectstype> selectListByTypeName(String TypeName);

//    @Select("select `score` from indicator where ID = #{id}")
//   public Long selectScoreById(Long id);

    @Select("SELECT * FROM projecttype WHERE name = #{name}")
    public Projectstype selectTypeByName(String name);

//    @Select("SELECT publication.ID,publication.name,indicator.score FROM publication JOIN indicator ON indicator.ID = publication.indicatorID WHERE year = #{year}")
//    public List<Publication> selectPublicationByYear(Integer year);

    @Select("SELECT DISTINCT name FROM projecttype WHERE `name` LIKE CONCAT('%', #{name}, '%') LIMIT 10")
    public List<String> getNamesByStr(String name);

//    @Select("SELECT publication.ID,publication.name,publication.indicatorID,indicator.score \n" +
//            "FROM publication,indicator\n" +
//            "WHERE `year` = #{year} AND publication.`name` = #{name} AND publication.indicatorID = indicator.ID ")
//    public Publication getNamesByYearName(Integer year,String name);

    @Select("SELECT p.ID,p.name,p.indicatorID FROM projecttype p " +
            "WHERE p.name = #{name}")
    public Projectstype getTypeByName(String name);

    @Delete("DELETE FROM projecttype WHERE indicatorID = #{indicatorID}")
    public int deleteById(@Param("indicatorID") Integer indicatorID);

    @Select("SELECT DISTINCT `name` FROM projecttype WHERE indicatorID = #{indicatorID} AND name LIKE CONCAT('%', #{name}, '%') LIMIT 10")
    public List<String> getNamesByIdName(Integer indicatorID,String name);

    @Select("SELECT * FROM projecttype WHERE name = #{name}")
    public List<Projectstype> getTypesByName(String name);
}
