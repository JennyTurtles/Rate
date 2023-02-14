package org.sys.rate.service.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.sys.rate.mapper.BooksMapper;
import org.sys.rate.model.Books;

import java.util.List;

@Service
public class BooksService {

    @Autowired
    private BooksMapper bookMapper;

    public Books selectBookById(Long ID){
        return bookMapper.selectBookById(ID);
    }

    /**
     * 查询著作列表
     *
     * @param book 著作
     * @return 著作集合
     */
    public List<Books> selectBookList(Books book){
        return bookMapper.selectBookList(book);
    }

    /**
     * 新增著作
     *
     * @param book 著作
     * @return 结果
     */
    public int insertBook(Books book){
        return bookMapper.insertBook(book);
    }

    /**
     * 修改著作
     *
     * @param book 著作
     * @return 结果
     */
    public int updateBook(Books book){
        return bookMapper.updateBook(book);
    }

    /**
     * 删除著作
     *
     * @param ID 著作ID
     * @return 结果
     */
    public int deleteBookById(Long ID){
        return bookMapper.deleteBookById(ID);
    }

    public List<Books> selectList(){
        return bookMapper.selectList();
    }

}
