package org.sys.rate.service.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.sys.rate.mapper.BooksMapper;
import org.sys.rate.model.BookOper;
import org.sys.rate.model.Books;
import org.sys.rate.model.Projects;
import org.sys.rate.model.ProjectsOper;

import javax.mail.MessagingException;
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


    //    修改论文状态
    public int editState(String state, Long ID) throws MessagingException {
        Books paper = bookMapper.selectBookById(ID);
//        mailToStuService.sendStuMail(state, paper);
        // 管理员通过的时候需要处理2分论文的情况，还要计算student的活动总分
        if (state.equals("adm_pass")){
            int stuID = paper.getStudentID();
//            int score = paper.getPoint();

//            if (score == 2){ // 2分的时候检查是否已经发表过2分的论文
//                if (projectsMapper.checkScore(Long.parseLong(String.valueOf(stuID))) != null){ // 已经发表过2分论文,将该论文的have_score设置为0
//                    return projectsMapper.editState2(state,ID,0);
//                }else {
//                    projectsMapper.editState2(state,ID,1);
//                    return projectsMapper.updateScore(stuID,score);
//                }
//            }
//            projectsMapper.updateScore(stuID,score);
        }
        return bookMapper.editState(state,ID);
    }

    public int insertPaperOper(BookOper paperoper){
        return bookMapper.insertPaperoper(paperoper);
    }

}
