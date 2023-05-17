package org.sys.rate.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.sys.rate.model.Paper;
import org.sys.rate.model.Patent;
import org.sys.rate.model.PatentOper;

import java.util.List;

/**
 * 著作Mapper接口
 *
 * @author system
 * @date 2022-03-13
 */
public interface PatentMapper
{
    /**
     * 查询著作
     *
     * @param ID 著作ID
     * @return 著作
     */
    public Patent selectPatentById(Long ID);

    /**
     * 查询著作列表
     *
     * @param patent 著作
     * @return 著作集合
     */
    public List<Patent> selectPatentList(Patent patent);

    /**
     * 新增著作
     *
     * @param patent 著作
     * @return 结果
     */
    public int insertPatent(Patent patent);

    /**
     * 修改著作
     *
     * @param patent 著作
     * @return 结果
     */
    public int updatePatent(Patent patent);

    /**
     * 删除著作
     *
     * @param ID 著作ID
     * @return 结果
     */
    public int deletePatentById(Long ID);

    /**
     * 批量删除著作
     *
     * @param IDs 需要删除的数据ID
     * @return 结果
     */
    public int deletePatentByIds(String[] IDs);

    public List<Patent> selectList(Integer id);

    @Select("SELECT * FROM patent WHERE  ID = #{ID}")
    public Patent selectByID(Long ID);

    @Select("SELECT ID FROM patent WHERE studentID = #{stuID} AND point = 2 AND state = 'adm_pass' LIMIT 1")
    public Integer checkScore(Long stuID);

    @Update("UPDATE patent SET state = #{state},have_score = #{valid} WHERE ID = #{ID}")
    public Integer editState2(String state, Long ID, Integer valid);

    @Update("UPDATE student SET score = score + #{score} WHERE ID = #{stuID}")
    public int updateScore(Long stuID,Long score);

    int editState(@Param("state") String state, @Param("ID") Long id);

    int insertPaper(Paper paper);

    int insertPaperoper(PatentOper paperoper);
}
