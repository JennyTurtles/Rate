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
        // 需要查重，这个放在前端进行判断
        try {
            Integer res = projectTypeService.addProjectType(projectType);
            return RespBean.ok("Project type added successfully", res);
        } catch (Exception e) {
            return RespBean.error("Failed to add project type");
        }
    }

    @PutMapping("/projectType")
    public RespBean editProjectType(@RequestBody ProjectType projectType){
        // 当projectType下面对应的project，就不应该随意更新或者删除。这个可以在数据库上加以限制，但是不好
        try {
            projectTypeService.editProjectType(projectType);
            return RespBean.ok("Project type edited successfully");
        } catch (Exception e) {
            return RespBean.error("Failed to add project type");
        }

    }

    @DeleteMapping("/projectType")
    public RespBean deleteProjectTypeById(@RequestParam("id") Integer id){
        try {
            projectTypeService.deleteProjectTypeById(id);
            return RespBean.ok("delete project successfully!");
        } catch (Exception e) {
            return RespBean.error("delete project error!");
        }
    }



}
