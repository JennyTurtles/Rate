package org.sys.rate.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.sys.rate.model.Decision;

import java.util.List;

/**
 * 专利成果Mapper接口
 *
 * @author system
 * @date 2022-03-13
 */
@Mapper
public interface DecisionMapper
{
    /**
     * 通过ID获取Decision
     *
     * @param ID
     * @return
     */
    @Select("select * from i_patent where ID = #{ID}")
    Decision getById(Integer ID);

    /**
     * 查询专利成果
     *
     * @param ID 专利成果ID
     * @return 专利成果
     */
    public Decision selectDecisionById(Long ID);

    /**
     * 查询成果列表
     *
     * @param
     * @return 专利成果集合
     */
    public List<Decision> selectDecisionList(Decision patent);

    public int insertDecision(Decision patent);

    public int updateDecision(Decision decision);

    public int editState(String state, Long ID);
    /**
     * 删除专利成果
     *
     * @param ID 成果ID
     * @return 结果
     */
    public int deleteDecisionById(Long ID);

    /**
     * 批量删除专利成果
     *
     * @param IDs 需要删除的数据ID
     * @return 结果
     */
    public int deleteDecisionByIds(String[] IDs);

    public List<Decision> selectAllDecisionList();
    public List<Decision> selectListById(@Param("studentID") Integer studentID, @Param("page") Integer page, @Param("size") Integer size);
    public List<Decision> selectDecisionListById(@Param("studentID") Integer studentID);


    @Select("SELECT ID FROM i_patent WHERE studentID = #{stuID} AND point = 2 AND state = 'adm_pass' LIMIT 1")
    public Integer checkScore(Long stuID);

    @Update("UPDATE i_patent SET state = #{state},have_score = #{valid} WHERE ID = #{ID}")
    public Integer editState2(String state, Long ID, Integer valid);

    @Update("UPDATE graduatestudent SET point = point + #{score} WHERE ID = #{stuID}")
    public int updateScore(Long stuID,Long score);

    @Select("SELECT * FROM i_patent WHERE  ID = #{ID}")
    public Decision selectByID(Long ID);

    public List<Decision> searchDecisionByConditions(String studentName, String state, String decisionName, String pointFront, String pointBack);
    public Integer selectDecisionNumberOfPendingMessing(String state);

}
