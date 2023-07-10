package org.sys.rate.mapper;

import org.apache.ibatis.annotations.*;
import org.sys.rate.model.Indicator;
import org.sys.rate.model.Publication;

import java.util.Date;
import java.util.List;

/**
 * 刊物Mapper接口
 *
 * @author system
 * @date 2022-03-13
 */
@Mapper
public interface PublicationMapper {
    /**
     * 查询刊物
     *
     * @param id 刊物id
     * @return 刊物
     */
    @Select("select id, name, abbr, publisher, url from i_publication where id= #{id}")
    Publication selectPublicationById(Integer id);

    /**
     * 新增刊物
     *
     * @param publication 刊物
     * @return 返回新增刊物的主键，然后加入到中间表
     */
    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("insert into i_publication(name, abbr, publisher, url) values(#{name}, #{abbr}, #{publisher}, #{url})")
    int insertPublication(Publication publication);

    /**
     * 修改刊物
     *
     * @param publication 刊物
     * @return 结果
     */
    Integer updatePublication(Publication publication);

    /**
     * 批量删除刊物
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    Integer deletePublicationByIds(List<Integer> ids);

    /**
     * 根据name和year模糊查询，返回相关的刊物全称
     *
     * @param name:
     * @Return List<String>
     */
//    @Select("SELECT DISTINCT NAME FROM i_publication p LEFT JOIN indicator_publication ip on p.id = ip.publication_id WHERE NAME LIKE CONCAT('%',#{name},'%') and ip.year=#{year}")
//    List<String> getPublicationNamesByNameYear(String name, Integer year);

    @Select("SELECT DISTINCT `name` FROM i_publication p, indicator_publication ip \n" +
            "WHERE p.id = ip.publication_id AND NAME LIKE CONCAT('%',#{name},'%')")
    List<String> getPublicationNamesByNameYear(String name);

    /**
     * 根据name模糊查询，返回相关的刊物全称
     *
     * @param name:
     * @Return List<String>
     */
    @Select("select distinct name from i_publication where name LIKE CONCAT('%', #{name}, '%')")
    List<String> getPublicationNamesByName(String name);


//    @Select("SELECT p.name,i.name indicatorName,i.order,i.score,ip.year,i.type FROM i_publication p, indicator_publication ip,indicator i\n" +
//            "WHERE p.id = ip.publication_id AND i.id = ip.indicator_id AND p.`name` = #{name} AND year <= #{year}\n" +
//            "ORDER BY i.score DESC, i.`order`")
//    List<Publication> getPublicationByNameYear(String name, String year);

    Publication getPublicationByNameYear(String name,Integer year);


    /**
     * 获取期刊的详细信息，btw是每年没有删除的？
     *
     * @param name:
     * @Return List<Publication>
     */

    Publication getPublicationInfByName(String name);

    /**
     * 插入期刊时，也需要插入中间表中
     */
    @Insert("insert into indicator_publication(indicator_id, publication_id, year) values(#{indicatorId},#{publicationId},#{date})")
    Integer insertIndicatorPublication(Integer indicatorId, Integer publicationId, Integer date);
}
