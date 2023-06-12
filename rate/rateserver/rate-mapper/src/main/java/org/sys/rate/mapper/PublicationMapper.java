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
     * @param ID 刊物ID
     * @return 刊物
     */
    @Select("select id, name, abbr, publisher, url from i_publication")
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
     * @param IDs 需要删除的数据ID
     * @return 结果
     */
    Integer deletePublicationByIds(List<Integer> ids);

    /**
     * 模糊查询，返回相关的刊物全称
     *
     * @param name:
     * @Return List<String>
     */
    @Select("select name from i_publication where name LIKE CONCAT('%', #{name}, '%')")
    List<String> getPublicationNamesByName(String name);

    /**
     * 通过刊物全称和出版年份，返回最佳刊物实体
     *
     * @param name: 刊物全称
     * @param year: 刊物出版年份
     * @Return List<Publication>
     */
    @Select("SELECT p.id, p.`name`, p.abbr, p.publisher, p.url, i.id, i.`name`, i.score \n" +
            "FROM indicator_publication ip LEFT JOIN i_publication p ON ip.publication_id = p.id LEFT JOIN indicator i ON ip.indicator_id = i.id \n" +
            "WHERE p.`name` = #{name} AND YEAR ( ip.date )= #{year}  AND ip.flag !=- 1 \n" +
            "ORDER BY i.score DESC, i.`order` \n" +
            "LIMIT 1")
    Publication getPublicationByNameYear(String name, Integer year);


    /**
     * 获取期刊的详细信息，btw是每年没有删除的？
     *
     * @param name:
     * @Return List<Publication>
     */
    @Select("select p.id, p.name, p.abbr, p.publisher, p.url, YEAR(ip.date), i.id, i.name, i.type, i.score from indicator_publication ip " +
            "left join i_publication p on p.id = ip.publication_id left join indicator i on ip.indicator_id = i.id " +
            "where p.name = #{name} and ip.flag = 0 order by ip.date")
    List<Publication> getPublicationInfByName(String name);

    /**
     * 插入期刊时，也需要插入中间表中
     * @param id:
     * @Return Integer
     */
    @Insert("insert into indicator_publication(indicator_id, publication_id, date, flag) values(#{indicatorId},#{publicationId},#{date},0)")
    Integer insertIndicatorPublication(Integer indicatorId, Integer publicationId, Date date);
}
