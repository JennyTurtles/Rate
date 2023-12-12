package org.sys.rate.controller.admin;

import com.baomidou.mybatisplus.extension.api.R;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.sys.rate.config.JsonResult;
import org.sys.rate.mapper.IndicatorMapper;
import org.sys.rate.mapper.PaperMapper;
import org.sys.rate.mapper.PublicationMapper;

import org.sys.rate.model.*;
import org.sys.rate.service.admin.PublicationService;
import org.sys.rate.service.admin.IndicatorService;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Collections;
import java.util.List;


/**
 * 刊物Controller
 *
 * @author system
 * @date 2022-03-13
 */
@RestController
@Validated
public class PublicationController {
    @Resource
    private PublicationService publicationService;

    @Resource
    private PublicationMapper publicationMapper;

    @Resource
    private PaperMapper paperMapper;

    @Resource
    private IndicatorMapper indicatorMapper;

    @Resource
    private IndicatorService indicatorService;

    /**
     * 模糊查询相关期刊，返回期刊的全称
     *
     * @param publicationName:
     * @Return RespBean
     */
    @GetMapping("/publication/basic/listByNameYear/{publicationName}/{year}")
    public RespBean listByName(@PathVariable String publicationName, @PathVariable Integer year) {
        List<String> list = publicationService.selectPublicationListByName(publicationName, year);
        return RespBean.ok("模糊查询相关期刊", list);
    }

    /**
     * 新增保存刊物
     */
    @PostMapping("/publication/basic/add")
    public RespBean addSave(@Valid Publication publication) {
        Integer res = publicationService.insertPublication(publication);
        if (res == null || res != 0)
            return RespBean.ok("添加期刊成功！");
        else
            return RespBean.ok("重复添加，已忽略");
    }

    /**
     * 修改保存刊物
     */
    @PutMapping("/publication/basic/edit")
    public RespBean editSave(@Valid @RequestBody Publication publication) {
        try {
            // 参数校验通过，进行相关处理
            publicationService.updatePublication(publication);
        } catch (DuplicateKeyException e) {
            // 异常处理，返回错误信息
            return RespBean.error("期刊重名！");
        }
        try {
            publicationMapper.updateIndicatorPublicationYear(publication);
            return RespBean.ok("修改期刊成功");
        }catch (DuplicateKeyException e){
            return RespBean.ok("修改期刊成功"); // 不处理
        }
    }


    /**
     * 通过全称和year进行最佳搜索
     *
     * @param year:
     * @param name:
     * @Return RespBean
     */
    @GetMapping("/publication/getInfByNameYear")
    public RespBean getPublicationNamesByNameYear(Integer year, String name) {
        Publication publication = publicationMapper.getPublicationByNameYear(name, year);
        Indicator bestIndicator = publicationService.chooseBestIndicator(publication, year);
        if (bestIndicator != null) {
            publication.setIndicatorList(Collections.singletonList(bestIndicator));
            return RespBean.ok("success", publication);
        } else {
            return RespBean.ok("success");
        }
    }

    /**
     * 搜索2分论文，返回2分论文的主键
     *
     * @param stuId:
     */
    @GetMapping("/publication/checkScore/{stuId}")
    public RespBean checkScore(@PathVariable Integer stuId) {
        Integer id = paperMapper.checkScore(Long.valueOf(stuId));
        if (id == null) {
            return RespBean.ok("success", -1);
        } else {
            return RespBean.ok("success", id);
        }
    }

    @GetMapping("/publication/getScore/{id}")
    public RespBean getScore(@PathVariable Integer id) {
        Integer score = indicatorMapper.getScore(id);
        return score != null ? RespBean.ok("success", score) : RespBean.ok("fail!", 0);
    }


    // 文档2.14 功能6 -> 2.21 功能1
    // 待修改
    @PostMapping("/publication/dels")
    public RespBean deleteByYearId(@RequestParam Integer year, @RequestParam String indicatorName){
        if (publicationMapper.deleteByYearIndicatorNames(year,indicatorName) < 0)
            return RespBean.error("删除失败！");
        return RespBean.ok("删除成功！");
    }

    // 文档2.14 功能7 用部分名字搜全称 -> 2.21 功能3
    // 待修改
    //@PostMapping("/publication/getNames")
    //@ResponseBody
    //public RespBean getNamesByName(@RequestBody Publication publication){
    //    String name = publication.getName();
    //    String type = publication.getType();
    //    List<String> res = new ArrayList<>();
    //    if (type.equals("论文")){
    //        res = publicationMapper.getNamesByName(name);
    //    }
    //    return RespBean.ok("success",res);
    //}

    /**
     * 根据期刊全称获取期刊在数据库中的详细信息。
     *
     * @param name:
     * @Return RespBean
     */
    @GetMapping("/publication/getInf/{name}")
    public RespBean getInfByName(@PathVariable("name") String name) {
        List<Publication> res = publicationMapper.getPublicationInfByName(name);
        return RespBean.ok("success", res);
    }
    @GetMapping("/publication/basic/listByName")
    public RespBean getlistByName(String publicationName) {
        List<Publication> res = publicationMapper.getlistByName(publicationName);
        return RespBean.ok("success", res);
    }


    @DeleteMapping("publication")
    public RespBean deletePublicationById(@RequestParam("id") Integer id, @RequestParam("year") Integer year){
        try {
            publicationService.deletePublicationById(id, year);
            return RespBean.ok("delete publication successfully!");
        } catch (Exception e) {
            return RespBean.error("delete publication error!");
        }
    }

    @PostMapping("/publications")
    public RespBean multiImportPublication(@RequestBody List<Publication> publications){
        for (Publication publication:publications){
            if (publicationMapper.checkByNames(publication.getName())!=1){
                publicationMapper.insertPublication(publication);
            }
            publicationMapper.insertIndicatorPublication(publication.getIndicatorId(), publicationMapper.getIdByName(publication.getName()), publication.getYear());
        }
        return RespBean.ok("添加成功！");
    }
}
