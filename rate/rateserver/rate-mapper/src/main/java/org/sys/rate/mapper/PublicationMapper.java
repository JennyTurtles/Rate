package org.sys.rate.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.web.bind.annotation.PathVariable;
import org.sys.rate.model.Award;
import org.sys.rate.model.InfoItem;
import org.sys.rate.model.Publication;
import org.sys.rate.model.RespBean;

import java.util.List;

/**
 * 刊物Mapper接口
 * 
 * @author system
 * @date 2022-03-13
 */
@Mapper
public interface PublicationMapper 
{
    /**
     * 查询刊物
     * 
     * @param ID 刊物ID
     * @return 刊物
     */
    public Publication selectPublicationById(Long ID);

    /**
     * 查询刊物列表
     * 
     * @param publication 刊物
     * @return 刊物集合
     */
    public List<Publication> selectPublicationList(Publication publication);

    /**
     * 新增刊物
     * 
     * @param publication 刊物
     * @return 结果
     */
    public int insertPublication(Publication publication);

    /**
     * 修改刊物
     * 
     * @param publication 刊物
     * @return 结果
     */
    public int updatePublication(Publication publication);

    /**
     * 删除刊物
     * 
     * @param ID 刊物ID
     * @return 结果
     */
    public int deletePublicationById(Long ID);

    /**
     * 批量删除刊物
     * 
     * @param IDs 需要删除的数据ID
     * @return 结果
     */
    public int deletePublicationByIds(String[] IDs);

    public List<Publication> selectList();
    public List<Publication> selectListByPubName(String publicationName);

    @Select("select `score` from indicator where ID = #{id}")
    public Long selectScoreById(Long id);

    @Select("SELECT * FROM publication WHERE name = #{name} AND year <= #{year} ORDER BY ABS(#{year}-year) LIMIT 1")
    public Publication selectPublicationByNameYear(String name, Integer year);

//    @Select("SELECT publication.ID,publication.name,indicator.score FROM publication JOIN indicator ON indicator.ID = publication.indicatorID WHERE year = #{year}")
//    public List<Publication> selectPublicationByYear(Integer year);

    @Select("SELECT publication.ID,publication.name,indicator.score \n" +
            "FROM publication \n" +
            "JOIN indicator ON indicator.ID = publication.indicatorID \n" +
            "WHERE publication.ID in \n" +
            "(SELECT t1.ID\n" +
            "FROM publication t1\n" +
            "WHERE year = (SELECT MAX(year) FROM publication t2 WHERE t1.indicatorID = t2.indicatorID AND `year`<=#{year}))")
    public List<Publication> selectPublicationByYear(Integer year);

    @Select("SELECT DISTINCT name FROM publication WHERE `name` LIKE CONCAT('%', #{name}, '%') LIMIT 10")
    public List<String> getNamesByStr(String name);

    @Select("SELECT * FROM publication t1\n" +
            "WHERE year = (SELECT MAX(year) FROM publication t2 WHERE t1.indicatorID = t2.indicatorID AND `year`<=#{year}) AND name LIKE CONCAT('%', #{name}, '%') LIMIT 10")
    public List<Publication> getNamesByNameYear(String name, Integer year);

    @Select("SELECT score FROM indicator WHERE id = #{id}")
    public Integer getScore(Integer id);

//    @Select("SELECT publication.ID,publication.name,publication.indicatorID,indicator.score \n" +
//            "FROM publication,indicator\n" +
//            "WHERE `year` = #{year} AND publication.`name` = #{name} AND publication.indicatorID = indicator.ID ")
//    public Publication getNamesByYearName(Integer year,String name);

//    @Select("SELECT publication.ID,publication.name,publication.indicatorID,indicator.score FROM publication,indicator\n" +
//            "WHERE `year` = (SELECT MAX(year) FROM publication WHERE name = #{name} AND year <= #{year})\n" +
//            "AND publication.`name` = #{name}  AND publication.indicatorID = indicator.ID")
//    public Publication getNamesByYearName(Integer year,String name);

    // 获取最近年份和indicatorId
    @Select("SELECT publication.ID,publication.name,publication.indicatorID,indicator.score,publication.year FROM publication,indicator\n" +
            "WHERE `year` = (SELECT MAX(year) FROM publication WHERE publication.name = #{name} AND year <= #{year})\n" +
            "AND publication.name = #{name} AND publication.indicatorID = indicator.ID")
    public Publication getPubByYearName(Integer year,String name);




    @Select("SELECT MAX(`year`) FROM publication WHERE indicatorID = #{Id} AND `year` <= #{year}")
    public Integer getMaxYearByIdYear(Integer Id,Integer year);


    public int deleteByYearIndicatorNames(@Param("year") int year, @Param("IndicatorNames") List<String> IndicatorNames);

    @Select("SELECT DISTINCT `name` FROM publication WHERE indicatorID = #{indicatorID} AND name LIKE CONCAT('%', #{name}, '%') LIMIT 10")
    public List<String> getNamesByIdName(Integer indicatorID,String name);

    @Select("SELECT DISTINCT `name` FROM publication WHERE name LIKE CONCAT('%', #{name}, '%') LIMIT 10")
    public List<String> getNamesByName(String name);

    @Select("SELECT * FROM publication WHERE name = #{name}")
    public List<Publication> getPubsByName(String name);

    @Select("SELECT p.ID,p.`name`,p.abbr,p.publisher,p.url,p.`level`,p.indicatorID,p.year,i.score,i.`name` as indicatorName,i.type from publication p, indicator i \n" +
            "WHERE p.`name` = #{name} AND\n" +
            "i.ID = p.indicatorID")
    public List<Publication> getInfByName(String name);
}
