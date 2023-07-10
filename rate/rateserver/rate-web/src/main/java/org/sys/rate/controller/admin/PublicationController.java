package org.sys.rate.controller.admin;

import com.baomidou.mybatisplus.extension.api.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.sys.rate.config.JsonResult;
import org.sys.rate.mapper.IndicatorMapper;
import org.sys.rate.mapper.PaperMapper;
import org.sys.rate.mapper.PublicationMapper;

import org.sys.rate.model.Indicator;
import org.sys.rate.model.Publication;
import org.sys.rate.model.RespBean;
import org.sys.rate.service.admin.PublicationService;
import org.sys.rate.service.admin.IndicatorService;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * 刊物Controller
 *
 * @author system
 * @date 2022-03-13
 */
@RestController
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
    public RespBean addSave(Publication publication) {
        publicationService.insertPublication(publication);
        return RespBean.ok("添加期刊成功！");
    }

    /**
     * 修改保存刊物
     */
    @PostMapping("/publication/basic/edit")
    public RespBean editSave(Publication publication) {
        return RespBean.ok("插入期刊成功", publicationService.updatePublication(publication));
    }

    /**
     * 删除刊物
     */
    @PostMapping("/publication/basic/remove")
    public RespBean deletePublicationById(Integer ids) {
        return RespBean.ok("删除期刊成功！",publicationService.deletePublicationById(Collections.singletonList(ids)));
    }

    /**
     * 通过全称和year进行最佳搜索
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
    //@PostMapping("/publication/dels")
    //@ResponseBody
    //public RespBean deleteByYearId(@RequestBody IndicatorPublication indicatorPublication){
    //
    //    int res = publicationMapper.deleteByYearIndicatorNames(indicatorPublication.getYear(),indicatorPublication.getIndicatorNames());
    //    return RespBean.ok("success",res);
    //}

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
     * @param name:
     * @Return RespBean
     */
    @GetMapping("/publication/getInf/{name}")
    public RespBean getInfByName(@PathVariable("name") String name) {
        Publication res = publicationMapper.getPublicationInfByName(name);
        return RespBean.ok("success", res);
    }



}
