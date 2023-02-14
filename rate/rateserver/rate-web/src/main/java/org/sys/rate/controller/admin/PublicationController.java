package org.sys.rate.controller.admin;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.sys.rate.config.JsonResult;
import org.sys.rate.model.Publication;
import org.sys.rate.service.admin.PublicationService;


/**
 * 刊物Controller
 * 
 * @author system
 * @date 2022-03-13
 */
@RestController
@RequestMapping("/publication/basic")
public class PublicationController
{

    @Autowired
    private PublicationService publicationService;

    @GetMapping("/List")
    public JsonResult<List> getCollect(){
//        不传递参数 查找所有刊物
        List<Publication> publications = publicationService.selectList();
        List<Long> scores = new ArrayList<>();
        for (Publication publication : publications)
            scores.add(publicationService.selectScoreById(publication.getIndicatorID()));
        List<Object> res = new ArrayList<>();
        res.add(publications);
        res.add(scores);
        return new JsonResult<>(res);
    }

    @GetMapping("/listByName")
    public JsonResult listByName(String publicationName){
        List<Publication> list = publicationService.selectPublicationListByName(publicationName);
//        System.out.println(list.get(0).getIndicator().getScore());
        return new JsonResult(list);
    }
    /**
     * 查询刊物列表
     */
    @PostMapping("/list")
    @ResponseBody
    public JsonResult list(Publication publication)
    {
//        按照特定参数查找
        List<Publication> list = publicationService.selectPublicationList(publication);
        return new JsonResult(list);
    }

    // 按照相同年份和名称进行匹配
//    @GetMapping("/list/{year}")
//    public JsonResult listByYear(@PathVariable Integer year){
//        return new JsonResult(publicationService.selectPublicationListByYear(year));
//    }

    // 按照indicatorId进行分类，每类中对年份进行筛选，取小于目标年份且最大的年份的所有期刊
    @GetMapping("/list/{year}")
    public JsonResult listByYear(@PathVariable Integer year){
        return new JsonResult(publicationService.selectPublicationListByYear(year));
    }

    /**
     * 新增保存刊物
     */
    @PostMapping("/add")
    @ResponseBody
    public JsonResult addSave(Publication publication)
    {
        return new JsonResult(publicationService.insertPublication(publication));
    }


    /**
     * 修改保存刊物
     */
    @PostMapping("/edit")
    @ResponseBody
    public JsonResult editSave(Publication publication)
    {
        return new JsonResult(publicationService.updatePublication(publication));
    }

    /**
     * 删除刊物
     */
    @PostMapping( "/remove")
    @ResponseBody
    public JsonResult remove(Long ids)
    {
        return new JsonResult(publicationService.deletePublicationById(ids));
    }
}
