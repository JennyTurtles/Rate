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
     * @param id 主键
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
     *
     * @param ids: paper的主键
     * @Return int
     */
    int deletePaperByIds(List<Integer> ids);

    /**
     * 修改paper的状态
     *
     * @param state:
     * @param id:
     * @Return int
     */
    int editState(String state, Integer id);

    /**
     * 分页返回该生的paper
     *
     * @param studentID:
     * @param page:
     * @param size:
     * @Return List<Paper>
     */
    List<Paper> selectListByIdWithPaging(@Param("studentID") Integer studentID, @Param("page") Integer page, @Param("size") Integer size);

    /**
     * 根据学号返回该生的paper
     * @param studentID:
     * @Return List<Paper>
     */
    @Select("select * from i_paper where student_id = #{studentID}")
    List<Paper> selectListById(Integer studentID);

    /**
     * 返回某学生已经被管理员通过的2分论文的主键，目的是判断该生是否已经提交过2分的论文
     * 1. 通过paper的state和student_id来确定paper的主键id
     * 2. 通过paper的id找到paper的publication_id
     * 3. 通过publication_id和paper.date在中间表中获取所有对应的indicator_id
     * 4. 通过score=2来筛选出唯一的indicator_id
     * @param stuID:
     * @Return Integer
     */
    @Select("SELECT p.id FROM i_paper p JOIN indicator_publication ip ON p.publication_id = ip.publication_id " +
            "WHERE p.state = 'admin_pass' AND p.student_id = #{stuID}  AND ip.flag = 0 AND YEAR(ip.date) = YEAR(p.date) " +
            "AND EXISTS(SELECT i.id FROM indicator i WHERE i.id = ip.indicator_id AND i.score = 2)")
    Integer checkHaveScore(Integer stuID);


    /**
     * 修改2分的论文状态
     *
     * @param state:
     * @param id:
     * @param valid:
     * @Return Integer
     */
    @Update("UPDATE i_paper SET state = #{state}, have_score = #{valid} WHERE id = #{id}")
    Integer editState2(String state, Integer id, Integer valid);

    /**
     * 更新研究生表的分数
     * @param stuID:
     * @param score:
     * @Return int
     */
    @Update("UPDATE graduatestudent SET point = point + #{score} WHERE studentID = #{stuID}")
    int updateScore(int stuID, int score);


}
