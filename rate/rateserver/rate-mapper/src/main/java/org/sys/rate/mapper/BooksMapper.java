package org.sys.rate.mapper;

import org.sys.rate.model.Books;

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
}
