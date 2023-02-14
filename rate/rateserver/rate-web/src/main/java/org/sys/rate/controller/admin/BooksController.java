package org.sys.rate.controller.admin;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import org.sys.rate.config.JsonResult;
import org.sys.rate.model.Books;
import org.sys.rate.service.admin.BooksService;

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
    @PostMapping("/list")
    @ResponseBody
    public JsonResult list(Books book)
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
}
