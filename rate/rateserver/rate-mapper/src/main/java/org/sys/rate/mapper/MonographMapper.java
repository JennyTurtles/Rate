package org.sys.rate.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.sys.rate.model.Monograph;
import org.sys.rate.model.Patent;

import java.util.List;

/**
 * 专利成果Mapper接口
 *
 * @author system
 * @date 2022-03-13
 */
@Mapper
public interface MonographMapper
{
    /**
     * 通过ID获取Monograph
     *
     * @param ID
     * @return
     */
    @Select("select * from i_book where id = #{ID}")
    Monograph getById(Integer ID);

    /**
     * 查询专利成果
     *
     * @param ID 专利成果ID
     * @return 专利成果
     */
    public Monograph selectMonographById(Long ID);

    /**
     * 查询专利成果列表
     *
     * @param monograph 专利成果
     * @return 专利成果集合
     */
    public List<Monograph> selectMonographList(Monograph monograph);

    public int insertMonograph(Monograph monograph);

    public int updateMonograph(Monograph monograph);

    public int editState(String state, Long ID);
    /**
     * 删除专利成果
     *
     * @param ID 成果ID
     * @return 结果
     */
    public int deleteMonographById(Long ID);

    /**
     * 批量删除专利成果
     *
     * @param IDs 需要删除的数据ID
     * @return 结果
     */
    public int deleteMonographByIds(String[] IDs);

    public List<Monograph> selectAllMonographList();
    public List<Monograph> selectListById(@Param("studentID") Integer studentID, @Param("page") Integer page, @Param("size") Integer size);
    public List<Monograph> selectMonographListById(@Param("studentID") Integer studentID);


    @Select("SELECT id FROM i_book WHERE studentID = #{stuID} AND point = 2 AND state = 'adm_pass' LIMIT 1")
    public Integer checkScore(Long stuID);

    @Update("UPDATE i_book SET state = #{state},have_score = #{valid} WHERE id = #{ID}")
    public Integer editState2(String state, Long ID, Integer valid);

    @Update("UPDATE graduatestudent SET point = point + #{score} WHERE ID = #{stuID}")
    public int updateScore(Long stuID,Long score);

    @Select("SELECT * FROM i_book WHERE  id = #{ID}")
    public Monograph selectByID(Long ID);

    public List<Monograph> searchMonographByConditions(String studentName, String state, String monoName, String pointFront, String pointBack);
    public Integer selectMonographNumberOfPendingMessing(String state);

    @Update("UPDATE i_book SET have_score = #{have_score} WHERE id = #{id}")
    public Integer editPoint(Monograph monograph);

}
