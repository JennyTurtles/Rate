package org.sys.rate.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.sys.rate.model.Paper;

import java.util.List;

/**
 * 论文成果Mapper接口
 *
 * @author system
 * @date 2022-03-13
 */
@Mapper
public interface PaperMapper
{
    /**
     * 通过ID获取Paper
     *
     * @param ID
     * @return
     */
    @Select("select * from paper where ID = #{ID}")
    Paper getById(Integer ID);

    /**
     * 查询论文成果
     *
     * @param ID 论文成果ID
     * @return 论文成果
     */
    public Paper selectPaperById(Long ID);

    /**
     * 查询论文成果列表
     *
     * @param paper 论文成果
     * @return 论文成果集合
     */
    public List<Paper> selectPaperList(Paper paper);

    /**
     * 新增论文成果
     *
     * @param paper 论文成果
     * @return 结果
     */
    public int insertPaper(Paper paper);

    /**
     * 修改论文成果
     *
     * @param paper 论文成果
     * @return 结果
     */
    public int updatePaper(Paper paper);

    public int editState(String state, Long ID);
    /**
     * 删除论文成果
     *
     * @param ID 论文成果ID
     * @return 结果
     */
    public int deletePaperById(Long ID);

    /**
     * 批量删除论文成果
     *
     * @param IDs 需要删除的数据ID
     * @return 结果
     */
    public int deletePaperByIds(String[] IDs);

    public List<Paper> selectList();
    public List<Paper> selectListById(@Param("studentID") Integer studentID, @Param("page") Integer page, @Param("size") Integer size);
    public List<Paper> selectListByIds(@Param("studentID") Integer studentID);


    @Select("SELECT ID FROM paper WHERE studentID = #{stuID} AND point = 2 AND state = 'adm_pass' LIMIT 1")
    public Integer checkScore(Long stuID);

    @Update("UPDATE paper SET state = #{state},have_score = #{valid} WHERE ID = #{ID}")
    public Integer editState2(String state, Long ID, Integer valid);

    @Update("UPDATE graduatestudent SET point = point + #{score} WHERE ID = #{stuID}")
    public int updateScore(Long stuID,Long score);

    @Select("SELECT * FROM paper WHERE  ID = #{ID}")
    public Paper selectByID(Long ID);

    public List<Paper> searchPaperByConditions (String studentName, String state, String name, String pointFront, String pointBack, String pub, Integer pageNum, Integer pageSize);
}
