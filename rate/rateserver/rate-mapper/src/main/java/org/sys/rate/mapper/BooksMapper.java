package org.sys.rate.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.sys.rate.model.BookOper;
import org.sys.rate.model.Books;
import org.sys.rate.model.ProjectsOper;

import java.util.List;

/**
 * 著作Mapper接口
 * 
 * @author system
 * @date 2022-03-13
 */
public interface BooksMapper
{
    /**
     * 查询著作
     * 
     * @param ID 著作ID
     * @return 著作
     */
    public Books selectBookById(Long ID);

    /**
     * 查询著作列表
     * 
     * @param book 著作
     * @return 著作集合
     */
    public List<Books> selectBookList(Books book);

    /**
     * 新增著作
     * 
     * @param book 著作
     * @return 结果
     */
    public int insertBook(Books book);

    /**
     * 修改著作
     * 
     * @param book 著作
     * @return 结果
     */
    public int updateBook(Books book);

    /**
     * 删除著作
     * 
     * @param ID 著作ID
     * @return 结果
     */
    public int deleteBookById(Long ID);

    /**
     * 批量删除著作
     * 
     * @param IDs 需要删除的数据ID
     * @return 结果
     */
    public int deleteBookByIds(String[] IDs);

    public List<Books> selectList();


    @Select("SELECT ID FROM book WHERE studentID = #{stuID} AND point = 2 AND state = 'adm_pass' LIMIT 1")
    public Integer checkScore(Long stuID);

    @Update("UPDATE book SET state = #{state},have_score = #{valid} WHERE ID = #{ID}")
    public Integer editState2(String state, Long ID, Integer valid);

    int editState(@Param("state") String state, @Param("ID") Long id);

    @Update("UPDATE student SET score = score + #{score} WHERE ID = #{stuID}")
    public int updateScore(int stuID,int score);

    int insertPaperoper(BookOper paperoper);
}
