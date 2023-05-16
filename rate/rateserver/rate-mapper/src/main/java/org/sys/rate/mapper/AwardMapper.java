package org.sys.rate.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.sys.rate.model.Award;
import org.sys.rate.model.AwardOper;
import org.sys.rate.model.PatentOper;

import java.util.List;

/**
 * 回答Mapper接口
 *
 * @author system
 * @date 2022-03-13
 */
public interface AwardMapper
{
    /**
     * 查询回答
     *
     * @param ID 回答ID
     * @return 回答
     */
    public Award selectAwardById(Long ID);

    /**
     * 查询回答列表
     *
     * @param award 回答
     * @return 回答集合
     */
    public List<Award> selectAwardList(Award award);

    /**
     * 新增回答
     *
     * @param award 回答
     * @return 结果
     */
    public int insertAward(Award award);

    /**
     * 修改回答
     *
     * @param award 回答
     * @return 结果
     */
    public int updateAward(Award award);

    /**
     * 删除回答
     *
     * @param ID 回答ID
     * @return 结果
     */
    public int deleteAwardById(Long ID);

    /**
     * 批量删除回答
     *
     * @param IDs 需要删除的数据ID
     * @return 结果
     */
    public int deleteAwardByIds(String[] IDs);

    public List<Award> selectList();

    @Select("SELECT ID FROM award WHERE studentID = #{stuID} AND point = 2 AND state = 'adm_pass' LIMIT 1")
    public Integer checkScore(Long stuID);

    @Update("UPDATE award SET state = #{state},have_score = #{valid} WHERE ID = #{ID}")
    public Integer editState2(String state, Long ID, Integer valid);

    int editState(@Param("state") String state, @Param("ID") Long id);

    @Update("UPDATE student SET score = score + #{score} WHERE ID = #{stuID}")
    public int updateScore(int stuID,int score);

    int insertPaperoper(AwardOper paperoper);
}
