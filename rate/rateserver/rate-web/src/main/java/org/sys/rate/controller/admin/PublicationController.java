package org.sys.rate.controller.admin;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.sys.rate.config.JsonResult;
import org.sys.rate.mapper.PublicationMapper;
import org.sys.rate.model.Indicator;
import org.sys.rate.model.Publication;
import org.sys.rate.model.RespBean;
import org.sys.rate.service.admin.PublicationService;

import javax.annotation.Resource;


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

    @Resource
    private PublicationMapper publicationMapper;

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

    // 2.14 功能4
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

    // 文档2.14 功能1 用部分名字搜全称
    @GetMapping("/searchNamesByStr/{name}")
    @ResponseBody
    public RespBean getNamesByStr(@PathVariable String name){
        List<String> res = publicationMapper.getNamesByStr(name);
        return RespBean.ok("success",res);
    }

    // 文档2.14 功能2
    @PostMapping("/searchByYearName")
    @ResponseBody
    public RespBean getPubByYearName(@RequestBody Publication publication){
        Integer year = publication.getYear();
        String name = publication.getName();
        Publication p = publicationMapper.getPubByYearName(year,name);
        Integer year1 = p.getYear();
        Integer indicatorId = Math.toIntExact(p.getIndicatorID());
        Integer year2 = publicationMapper.getMaxYearByIdYear(indicatorId,year);
        if (year1 < year2)
            p.setScore(0);
        return RespBean.ok("success",p);
    }

    // 文档2.14 功能6
    @PostMapping("/delPubs")
    @ResponseBody
    public RespBean deleteByYearId(@RequestBody Publication publication){
        Integer year = publication.getYear();
        Integer indicatorID = Math.toIntExact(publication.getIndicatorID());
        int res = publicationMapper.deleteByYearId(year,indicatorID);
        return RespBean.ok("success", res);
    }

    // 文档2.14 功能7 用部分名字搜全称
    @PostMapping("searchNamesByIdName")
    @ResponseBody
    public RespBean getNamesByIdName(@RequestBody Publication publication){
        Integer indicatorID = Math.toIntExact(publication.getIndicatorID());
        String name = publication.getName();
        List<String> res = publicationMapper.getNamesByIdName(indicatorID,name);
        return RespBean.ok("success",res);
    }

    // 文档2.14 功能8 用部分名字搜全称
    @GetMapping("searchByName/{name}")
    @ResponseBody
    public RespBean getPubsByName(@PathVariable String name){
        List<Publication> res = publicationMapper.getPubsByName(name);
        return RespBean.ok("success",res);
    }

}
