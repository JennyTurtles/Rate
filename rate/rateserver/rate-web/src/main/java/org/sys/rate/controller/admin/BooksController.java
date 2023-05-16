package org.sys.rate.controller.admin;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import org.sys.rate.config.JsonResult;
import org.sys.rate.model.BookOper;
import org.sys.rate.model.Books;
import org.sys.rate.model.ProjectsOper;
import org.sys.rate.service.admin.BooksService;

import javax.mail.MessagingException;

/**
 * 著作Controller
 * 
 * @author system
 * @date 2022-03-13
 */
@RestController
@RequestMapping("/book/basic")
public class BooksController
{

    @Autowired
    private BooksService bookService;

    @GetMapping("/List")
    public JsonResult<List> getCollect(){
        return new JsonResult<>(bookService.selectList());
    }

    /**
     * 查询著作列表
     */
    @GetMapping("/list")
    @ResponseBody
    public JsonResult list(Books book)
    {
        List<Books> list = bookService.selectBookList(book);
        return new JsonResult(list);
    }


    /**
     * 查询著作列表
     */
    @PostMapping("/list")
    @ResponseBody
    public JsonResult postList(@RequestBody Books book)
    {
        List<Books> list = bookService.selectBookList(book);
        return new JsonResult(list);
    }



    /**
     * 新增保存著作
     */
    @PostMapping("/add")
    @ResponseBody
    public JsonResult addSave(Books book)
    {
        return new JsonResult(bookService.insertBook(book));
    }

    /**
     * 修改保存著作
     */
    @PostMapping("/edit")
    @ResponseBody
    public JsonResult editSave(Books book)
    {
        return new JsonResult(bookService.updateBook(book));
    }

    /**
     * 删除著作
     */
    @PostMapping( "/remove")
    @ResponseBody
    public JsonResult remove(Long ids)
    {
        return new JsonResult(bookService.deleteBookById(ids));
    }


    //    修改论文状态
    @GetMapping("/edit_state")
    public JsonResult getById(String state, Long ID) throws MessagingException {
        return new JsonResult(bookService.editState(state, ID));
    }

    @PostMapping("/addOper")
    @ResponseBody
    public JsonResult addSave(BookOper paperoper)
    {
        return new JsonResult(bookService.insertPaperOper(paperoper));
//        return new JsonResult();
    }
}
