package org.sys.rate.controller.admin;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.sys.rate.config.JsonResult;
import org.sys.rate.mapper.IndicatorMapper;
import org.sys.rate.mapper.ProjectTypeMapper;
import org.sys.rate.model.*;
import org.sys.rate.service.admin.ProjectService;
import org.sys.rate.service.admin.ProjectTypeService;
import org.sys.rate.service.mail.MailToTeacherService;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 专著成果Controller
 *
 * @author system
 * @date 2022-03-13
 */
@RestController
@RequestMapping("")
public class ProjectTypeController {

    @Resource
    private ProjectTypeService projectTypeService;
    @Resource
    private ProjectTypeMapper projectTypeMapper;


    @GetMapping("/projectByYear")
    public RespPageBean listByName(@RequestParam("indicatorId") Integer indicatorId,
                                   @RequestParam("year") Integer year,
                                   @RequestParam("pageNum") Integer pageNum,
                                   @RequestParam("pageSize") Integer pageSize) {
        List<ProjectType> list = projectTypeService.selectProjectTypeListByYear(indicatorId, year, pageNum, pageSize);
        int total = list.size();
        RespPageBean respPageBean = new RespPageBean();
        respPageBean.setData(list);
        respPageBean.setTotal((long) total);
        return respPageBean;
    }

    @PostMapping("/projectType")
    public RespBean addProjectType(@RequestBody ProjectType projectType){
        try {
            Integer res = projectTypeService.addProjectType(projectType);
            if(res == 0){
                return RespBean.ok("重复添加，已忽略", res);
            }
            return RespBean.ok("添加成功", res);
        } catch (Exception e) {
            return RespBean.error("Failed to add project type");
        }
    }

    @PutMapping("/projectType")
    public RespBean editProjectType(@RequestBody ProjectType projectType){
        // 当projectType下面对应的project，就不应该随意更新或者删除。这个可以在数据库上加以限制，但是不好
        try {
            Integer res = projectTypeService.editProjectType(projectType);
            return res != 0 ? RespBean.ok("修改成功！", res) : RespBean.error("科研项目重名！");
        } catch (Exception e) {
            return RespBean.error("Failed to add project type");
        }

    }

    @DeleteMapping("/projectType")
    public RespBean deleteProjectTypeById(@RequestParam("id") Integer id,@RequestParam Integer year){
        try {
            projectTypeService.deleteProjectTypeById(id);
//            projectTypeMapper.deleteByYearIndicatorID(year,id);
            return RespBean.ok("delete project successfully!");
        } catch (Exception e) {
            return RespBean.error("delete project error!");
        }
    }
    @PostMapping("/projectType/dels")
    public RespBean deleteByYearId(@RequestParam Integer year, @RequestParam Integer indicatorID){
        try {
            projectTypeMapper.deleteByYearIndicatorID(year,indicatorID);
            return RespBean.ok("删除成功！");
        } catch (Exception e){
            return RespBean.error("删除失败！");
        }
    }
    @PostMapping("/projectType/import")
    public RespBean multiImportPublication(@RequestBody List<ProjectType> projectTypes){
        try {
            for (ProjectType projectType:projectTypes){
                projectTypeMapper.addProjectType(projectType);
            }
            return RespBean.ok("添加成功！");
        } catch (Exception e){
            return RespBean.error("添加失败！");
        }
    }
}
