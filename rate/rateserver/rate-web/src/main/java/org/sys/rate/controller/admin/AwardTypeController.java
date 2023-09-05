package org.sys.rate.controller.admin;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.sys.rate.mapper.AwardTypeMapper;
import org.sys.rate.model.RespBean;

import javax.annotation.Resource;

@RestController
@RequestMapping("/awardType")
public class AwardTypeController {

    @Resource
    AwardTypeMapper awardTypeMapper;

    @DeleteMapping("")
    public RespBean deleteByYearId(@RequestParam Integer year, @RequestParam Integer id){
        try {
            awardTypeMapper.deleteById(id);
            return RespBean.ok("删除成功！");
        } catch (Exception e){
            return RespBean.error("删除失败！");
        }
    }
}
