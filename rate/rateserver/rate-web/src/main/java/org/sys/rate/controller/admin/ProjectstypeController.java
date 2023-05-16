package org.sys.rate.controller.admin;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.sys.rate.config.JsonResult;
import org.sys.rate.mapper.ProjectstypeMapper;
import org.sys.rate.model.Projects;
import org.sys.rate.model.Projectstype;
import org.sys.rate.model.Publication;
import org.sys.rate.model.RespBean;
import org.sys.rate.service.admin.ProjectstypeService;

import javax.annotation.Resource;


/**
 * 著作类型Controller
 * 
 * @author system
 * @date 2022-03-13
 */
@RestController
@RequestMapping("/projectType/basic")
public class ProjectstypeController
{

    @Autowired
    private ProjectstypeService projectstypeService;

    @Resource
    private ProjectstypeMapper projectstypeMapper;

    @GetMapping("/List")
    public JsonResult<List> getCollect(){
//        不传递参数 查找所有类别
        List<Projectstype> projectstypes = projectstypeService.selectList();
//        List<Long> scores = new ArrayList<>();
//        for (Publication publication : publications)
//            scores.add(projectstypeService.selectScoreById(publication.getIndicatorID()));
        List<Object> res = new ArrayList<>();
        res.add(projectstypes);
//        res.add(scores);
        return new JsonResult<>(res);
    }

    @GetMapping("/listByName")
    public JsonResult listByName(String TypeName){
        List<Projectstype> list = projectstypeService.selectTypeListByName(TypeName);
//        System.out.println(list.get(0).getIndicator().getScore());
        return new JsonResult(list);
    }

    /**
     * 查询著作类型列表
     */
    @PostMapping("/list")
    @ResponseBody
    public JsonResult list(Projectstype projectstype)
    {
        List<Projectstype> list = projectstypeService.selectProjectstypeList(projectstype);
        return new JsonResult(list);
    }

    // 按照indicatorId进行分类，每类中对年份进行筛选，取小于目标年份且最大的年份的所有期刊
//    @GetMapping("/list/{year}")
//    public JsonResult listByYear(@PathVariable Integer year){
//        return new JsonResult(projectstypeService.selectPublicationListByYear(year));
//    }

    /**
     * 新增保存著作类型
     */
    @PostMapping("/add")
    @ResponseBody
    public JsonResult addSave(Projectstype projectstype)
    {
        return new JsonResult(projectstypeService.insertProjectstype(projectstype));
    }


    /**
     * 修改保存著作类型
     */
    @PostMapping("/edit")
    @ResponseBody
    public JsonResult editSave(Projectstype projectstype)
    {
        return new JsonResult(projectstypeService.updateProjectstype(projectstype));
    }

    /**
     * 删除著作类型
     */
    @PostMapping( "/remove")
    @ResponseBody
    public JsonResult remove(Long ids)
    {
        return new JsonResult(projectstypeService.deleteProjectstypeById(ids));
    }

    // 用部分名字搜全称
    @GetMapping("/searchNamesByStr/{name}")
    @ResponseBody
    public RespBean getNamesByStr(@PathVariable String name){
        List<String> res = projectstypeMapper.getNamesByStr(name);
        return RespBean.ok("success",res);
    }

    @PostMapping("/searchByName")
    @ResponseBody
    public RespBean getTypeByName(@RequestBody String name){
        System.out.println(name);
        Projectstype p = projectstypeMapper.getTypeByName(name);
        System.out.println(p);
        return RespBean.ok("success",p);
    }

    @PostMapping("/delTypes")
    @ResponseBody
    public RespBean deleteById(@RequestBody Projectstype projectstype){
        Integer indicatorID = Math.toIntExact(projectstype.getIndicatorId());
        int res = projectstypeMapper.deleteById(indicatorID);
        return RespBean.ok("success", res);
    }

    // 用部分名字搜全称
    @PostMapping("searchNamesByIdName")
    @ResponseBody
    public RespBean getNamesByIdName(@RequestBody Projectstype projectstype){
        Integer indicatorID = Math.toIntExact(projectstype.getIndicatorId());
        String name = projectstype.getName();
        List<String> res = projectstypeMapper.getNamesByIdName(indicatorID,name);
        return RespBean.ok("success",res);
    }

    // 用部分名字搜全称
    @GetMapping("searchByName/{name}")
    @ResponseBody
    public RespBean getPubsByName(@PathVariable String name){
        List<Projectstype> res = projectstypeMapper.getTypesByName(name);
        return RespBean.ok("success",res);
    }
}
