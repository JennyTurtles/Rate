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
import org.sys.rate.model.IndicatorPublication;
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
public class PublicationController
{

    @Autowired
    private PublicationService publicationService;

    @Resource
    private PublicationMapper publicationMapper;

    @GetMapping("/publication/basic/List")
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

    @GetMapping("/publication/basic/listByName")
    public JsonResult listByName(String publicationName){
        List<Publication> list = publicationService.selectPublicationListByName(publicationName);
//        System.out.println(list.get(0).getIndicator().getScore());
        return new JsonResult(list);
    }
    /**
     * 查询刊物列表
     */
    @PostMapping("/publication/basic/list")
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
    @GetMapping("/publication/basic/list/{year}")
    public JsonResult listByYear(@PathVariable Integer year){
        return new JsonResult(publicationService.selectPublicationListByYear(year));
    }

    /**
     * 新增保存刊物
     */
    @PostMapping("/publication/basic/add")
    @ResponseBody
    public JsonResult addSave(Publication publication)
    {
        return new JsonResult(publicationService.insertPublication(publication));
    }


    /**
     * 修改保存刊物
     */
    @PostMapping("/publication/basic/edit")
    @ResponseBody
    public JsonResult editSave(Publication publication)
    {
        return new JsonResult(publicationService.updatePublication(publication));
    }

    /**
     * 删除刊物
     */
    @PostMapping( "/publication/basic/remove")
    @ResponseBody
    public JsonResult remove(Long ids)
    {
        return new JsonResult(publicationService.deletePublicationById(ids));
    }

    // 文档2.14 功能1 用部分名字搜全称 -> 文档2.21 功能5 PART1
    @PostMapping("/publication/getInfByNameYear")
    @ResponseBody
    public RespBean getNamesByNameYear(@RequestBody Publication publication){
        Integer year = publication.getYear();
        String name = publication.getName();
        List<Publication> res = publicationMapper.getNamesByNameYear(name,year);
        return RespBean.ok("success",res);
    }

    // 文档2.21 功能5 PART2
    @GetMapping("/publication/getScore/{id}")
    @ResponseBody
    public RespBean getScore(@PathVariable Integer id){
        Integer score = publicationMapper.getScore(id);
        return score != null ? RespBean.ok("success",score) : RespBean.ok("fail!",0);
    }

//    // 文档2.14 功能2
//    @PostMapping("/publication/basic/searchByYearName")
//    @ResponseBody
//    public RespBean getPubByYearName(@RequestBody Publication publication){
//        Integer year = publication.getYear();
//        String name = publication.getName();
//        Publication p = publicationMapper.getPubByYearName(year,name); //year和name匹配的pub // 问题
//        Integer year1 = p.getYear();
//        Integer indicatorId = Math.toIntExact(p.getIndicatorID());
//        Integer year2 = publicationMapper.getMaxYearByIdYear(indicatorId,year); //当前indicator中最大的year
//        if (year1 < year2)
//            p.setScore(0);
//        return RespBean.ok("success",p);
//    }

    // 文档2.14 功能6 -> 2.21 功能1
    @PostMapping("/publication/dels")
    @ResponseBody
    public RespBean deleteByYearId(@RequestBody IndicatorPublication indicatorPublication){

        int res = publicationMapper.deleteByYearId(indicatorPublication.getYear(),indicatorPublication.getIndicatorIDs());
        return RespBean.ok("success",res);
    }

    // 文档2.14 功能7 用部分名字搜全称 -> 2.21 功能3
    @GetMapping("/publication/getNames/{name}")
    @ResponseBody
    public RespBean getNamesByName(@PathVariable("name") String name){
        List<String> res = publicationMapper.getNamesByName(name);
        return RespBean.ok("success",res);
    }

    //文档2.14 功能8 用部分名字搜全称 -> 2.21 功能4
    @GetMapping("/publication/getInf/{name}")
    @ResponseBody
    public RespBean getInfByName(@PathVariable("name") String name){

        List<Publication> res = publicationMapper.getInfByName(name);
        return RespBean.ok("success",res);
    }

//    // 文档2.14 功能8 用部分名字搜全称
//    @GetMapping("/publication/basic/searchByName/{name}")
//    @ResponseBody
//    public RespBean getPubsByName(@PathVariable String name){
//        List<Publication> res = publicationMapper.getPubsByName(name);
//        return RespBean.ok("success",res);
//    }

}
