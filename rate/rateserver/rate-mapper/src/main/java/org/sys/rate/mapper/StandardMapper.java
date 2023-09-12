package org.sys.rate.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.sys.rate.model.Standard;

import java.util.List;

/**
 * 专利成果Mapper接口
 *
 * @author system
 * @date 2022-03-13
 */
@Mapper
public interface StandardMapper
{
    /**
     * 通过ID获取Standard
     *
     * @param ID
     * @return
     */
    @Select("select * from i_standard where ID = #{ID}")
    Standard getById(Integer ID);

    /**
     * 查询专利成果
     *
     * @param ID 专利成果ID
     * @return 专利成果
     */
    public Standard selectStandardById(Long ID);

    /**
     * 查询专利成果列表
     *
     * @param i_standard 专利成果
     * @return 专利成果集合
     */
    public List<Standard> selectStandardList(Standard standard);

    /**
     * 新增专利成果
     *
     * @param standard 专利成果
     * @return 结果
     */
    public int insertStandard(Standard standard);

    /**
     * 修改专利成果
     */
    public int updateStandard(Standard standard);

    public int editState(String state, Long ID);
    /**
     * 删除专利成果
     *
     * @param ID 专利成果ID
     * @return 结果
     */
    public int deleteStandardById(Long ID);

    /**
     * 批量删除专利成果
     *
     * @param IDs 需要删除的数据ID
     * @return 结果
     */
    public int deleteStandardByIds(String[] IDs);

    public List<Standard> selectList();
    public List<Standard> selectListById(@Param("studentID") Integer studentID, @Param("page") Integer page, @Param("size") Integer size);
    public List<Standard> selectListByIds(@Param("studentID") Integer studentID);


    @Select("SELECT ID FROM i_standard WHERE studentID = #{stuID} AND point = 2 AND state = 'adm_pass' LIMIT 1")
    public Integer checkScore(Long stuID);

    @Update("UPDATE i_standard SET state = #{state},have_score = #{valid} WHERE ID = #{ID}")
    public Integer editState2(String state, Long ID, Integer valid);

    @Update("UPDATE graduatestudent SET point = point + #{score} WHERE ID = #{stuID}")
    public int updateScore(Long stuID,Long score);

    @Select("SELECT * FROM i_standard WHERE  ID = #{ID}")
    public Standard selectByID(Long ID);

    public List<Standard> searchStandardByConditions(String studentName, String state, String projectName, String pointFront, String pointBack);
    public Integer selectStandardNumberOfPendingMessing(String state);

}
