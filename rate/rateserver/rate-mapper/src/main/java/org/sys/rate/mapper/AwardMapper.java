package org.sys.rate.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.sys.rate.model.Award;
import org.sys.rate.model.Award;

import java.util.List;

/**
 * 专利成果Mapper接口
 *
 * @author system
 * @date 2022-03-13
 */
@Mapper
public interface AwardMapper
{
    /**
     * 通过ID获取Award
     *
     * @param ID
     * @return
     */
    @Select("select * from i_patent where ID = #{ID}")
    Award getById(Integer ID);

    /**
     * 查询专利成果
     *
     * @param ID 专利成果ID
     * @return 专利成果
     */
    public Award selectAwardById(Long ID);

    /**
     * 查询专利成果列表
     *
     * @param i_patent 专利成果
     * @return 专利成果集合
     */
    public List<Award> selectAwardList(Award patent);

    public int insertAward(Award patent);

    public int updateAward(Award award);

    public int editState(String state, Long ID);
    /**
     * 删除专利成果
     *
     * @param ID 成果ID
     * @return 结果
     */
    public int deleteAwardById(Long ID);

    /**
     * 批量删除专利成果
     *
     * @param IDs 需要删除的数据ID
     * @return 结果
     */
    public int deleteAwardByIds(String[] IDs);

    public List<Award> selectAllAwardList();
    public List<Award> selectListById(@Param("studentID") Integer studentID, @Param("page") Integer page, @Param("size") Integer size);
    public List<Award> selectAwardListById(@Param("studentID") Integer studentID);


    @Select("SELECT ID FROM i_patent WHERE studentID = #{stuID} AND point = 2 AND state = 'adm_pass' LIMIT 1")
    public Integer checkScore(Long stuID);

    @Update("UPDATE i_patent SET state = #{state},have_score = #{valid} WHERE ID = #{ID}")
    public Integer editState2(String state, Long ID, Integer valid);

    @Update("UPDATE graduatestudent SET point = point + #{score} WHERE ID = #{stuID}")
    public int updateScore(Long stuID,Long score);

    @Select("SELECT * FROM i_patent WHERE  ID = #{ID}")
    public Award selectByID(Long ID);
}
