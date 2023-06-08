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
public interface PaperMapper {
    /**
     * 通过ID获取Paper
     *
     * @param id
     * @return
     */
    @Select("select id, name, student_id, date, author, state, url, have_score, pub_page, publication_id from i_paper where id = #{id}")
    Paper getById(Integer id);

    /**
     * 新增paper
     *
     * @param paper
     * @return 新增paper的主键id
     */
    int insertPaper(Paper paper);

    /**
     * 修改paper
     *
     * @param paper 论文成果
     * @return 结果
     */
    int updatePaper(Paper paper);

    /**
     * 批量删除paper
     * @param ids: paper的主键
     * @Return int
     */
    int deletePaperByIds(List<Integer> ids);

    public int editState(String state, Integer ID);
    /**
     * 删除论文成果
     *
     * @param ID 论文成果ID
     * @return 结果
     */
    public int deletePaperById(Long ID);



    public List<Paper> selectList();

    public List<Paper> selectListById(@Param("studentID") Integer studentID, @Param("page") Integer page, @Param("size") Integer size);

    public List<Paper> selectListByIds(@Param("studentID") Integer studentID);


    @Select("SELECT ID FROM paper WHERE studentID = #{stuID} AND point = 2 AND state = 'adm_pass' LIMIT 1")
    public Integer checkScore(int stuID);

    @Update("UPDATE paper SET state = #{state},have_score = #{valid} WHERE ID = #{ID}")
    public Integer editState2(String state, int ID, Integer valid);

    @Update("UPDATE student SET score = score + #{score} WHERE ID = #{stuID}")
    public int updateScore(int stuID, int score);

    @Select("SELECT * FROM paper WHERE  ID = #{ID}")
    public Paper selectByID(int ID);
}
