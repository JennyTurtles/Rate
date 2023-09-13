package org.sys.rate.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.sys.rate.model.Competition;

import java.util.List;

/**
 * 学科竞赛成果Mapper接口
 *
 * @author system
 * @date 2022-03-13
 */
@Mapper
public interface CompetitionMapper
{
    /**
     * 通过ID获取Competition
     *
     * @param ID
     * @return
     */
    @Select("select * from i_competition where ID = #{ID}")
    Competition getById(Integer ID);

    /**
     * 查询学科竞赛成果
     *
     * @param ID 学科竞赛成果ID
     * @return 学科竞赛成果
     */
    public Competition selectCompetitionById(Long ID);

    /**
     * 查询学科竞赛成果列表
     *
     * @param competition 学科竞赛成果
     * @return 学科竞赛成果集合
     */
    public List<Competition> selectCompetitionList(Competition competition);

    public int insertCompetition(Competition competition);

    public int updateCompetition(Competition competition);

    public int editState(String state, Long ID);
    /**
     * 删除学科竞赛成果
     *
     * @param ID 成果ID
     * @return 结果
     */
    public int deleteCompetitionById(Long ID);

    /**
     * 批量删除学科竞赛成果
     *
     * @param IDs 需要删除的数据ID
     * @return 结果
     */
    public int deleteCompetitionByIds(String[] IDs);

    public List<Competition> selectAllCompetitionList();
    public List<Competition> selectListById(@Param("studentID") Integer studentID, @Param("page") Integer page, @Param("size") Integer size);
    public List<Competition> selectCompetitionListById(Integer studentID);


    @Select("SELECT ID FROM i_competition WHERE studentID = #{stuID} AND point = 2 AND state = 'adm_pass' LIMIT 1")
    public Integer checkScore(Long stuID);

    @Update("UPDATE i_competition SET state = #{state},have_score = #{valid} WHERE ID = #{ID}")
    public Integer editState2(String state, Long ID, Integer valid);

    @Update("UPDATE graduatestudent SET point = point + #{score} WHERE ID = #{stuID}")
    public int updateScore(Long stuID,Long score);

    @Select("SELECT * FROM i_competition WHERE  ID = #{ID}")
    public Competition selectByID(Long ID);

    public List<Competition> searchCompetitionByConditions(String studentName, String state, String competitionName, String pointFront, String pointBack);
    public Integer selectCompetitionNumberOfPendingMessing(String state);


}
