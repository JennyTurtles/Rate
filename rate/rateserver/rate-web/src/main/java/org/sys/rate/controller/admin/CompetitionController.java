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
import org.sys.rate.model.*;
import org.sys.rate.service.admin.CompetitionService;
import org.sys.rate.service.mail.MailToTeacherService;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 专著成果Controller
 *
 * @author system
 * @date 2022-03-13
 */
@RestController
@RequestMapping("/competition/basic")
public class CompetitionController {

    @Resource
    private CompetitionService competitionService;
    @Resource
    private IndicatorMapper indicatorMapper;
    @Resource
    MailToTeacherService mailToTeacherService;

    private static final Logger logger = LoggerFactory.getLogger(CompetitionController.class);
    private String uploadFileName;


    @GetMapping("/studentID")//无页码要求
    public JsonResult<List> getById(Integer studentID) {
        List<Competition> list = competitionService.selectCompetitionListById(studentID);
        return new JsonResult<>(list);
    }

    //    修改专著状态
    @GetMapping("/edit_state")
    public JsonResult getById(String state, Long ID) throws MessagingException {
        return new JsonResult(competitionService.editState(state, ID));
    }

    @GetMapping("/List")
    public Msg getCollect() {
        //包括返回最早的提交时间 和多次驳回的理由列表
//        Page page = PageHelper.startPage(pageNum, pageSize);
        List<Competition> list = competitionService.selectAllCompetitionList();
//        PageInfo info = new PageInfo<>(page.getResult());
//        Object[] res = {list, info.getTotal()};
        return Msg.success().add("res", list);
    }
    /**
     * 新增保存专著成果
     */
    @PostMapping("/add")
    @ResponseBody
    public JsonResult addSave(Competition project) throws FileNotFoundException {
        Integer res = competitionService.insertCompetition(project);
        mailToTeacherService.sendTeaCheckMail(project, "学科竞赛");
        return new JsonResult(project.getId());
    }

    /**
     * 修改保存专著成果
     */
    @PostMapping("/edit")
    @ResponseBody
    public JsonResult editSave(Competition project) throws FileNotFoundException {
        int res = competitionService.updateCompetition(project);
        mailToTeacherService.sendTeaCheckMail(project, "学科竞赛");
        return new JsonResult(res);
    }

    /**
     * 删除专著成果
     */
    @DeleteMapping("/remove/{ID}")
    public JsonResult remove(@PathVariable Long ID) {
        Integer res = competitionService.deleteCompetitionById(ID);
        return new JsonResult(res);
    }

    @PostMapping("/upload")
    public JsonResult upload(@RequestParam MultipartFile file) throws IOException {
        String filename = file.getOriginalFilename();
        String fPath = new File("upload").getAbsolutePath() + "/" + filename;
        File newFile = new File(fPath);
        file.transferTo(newFile);

        uploadFileName = filename;
        //返回文件存储路径
        return new JsonResult(fPath);
    }

    @GetMapping("/download")
    public RespBean download(Integer projectID, String filename) throws IOException {
        File newFile = new File(new File("upload").getAbsolutePath() + "/" + filename);
        return RespBean.ok("success", newFile);
    }

    @PostMapping("/deleteFile")//删除某个文件
    @ResponseBody
    public JsonResult delete(String filepath) {
        boolean flag = false;
        File file = new File(filepath);
        if (file.isFile() && file.exists()) {
            flag = file.delete();
        }
        return new JsonResult(flag);
    }
    @GetMapping("/downloadByUrl")
    @ResponseBody
    public ResponseEntity<InputStreamResource> downloadFile(String url) throws IOException {
        File file = new File(url);
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=test.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(file.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    @GetMapping("/getIndicatorByYearAndType")
    public JsonResult getIndicatorByYearAndType(String year) {
        List<CompetitionType> list = competitionService.getIndicatorByYearAndType(year);
        return new JsonResult(list);
    }
    @GetMapping("/getIndicatorScore")
    public JsonResult getScore(Integer id) {
        return new JsonResult(indicatorMapper.getIndicatorById(id));
    }
    @PostMapping("/searchCompetitionByConditions")
    public Msg searchCompetitionByConditions(@RequestBody Map<String, String> params) {
        Page page = PageHelper.startPage(Integer.parseInt(params.get("pageNum")), Integer.parseInt(params.get("pageSize")));
        List<Competition> list = competitionService.searchCompetitionByConditions(params.get("studentName"), params.get("state"), params.get("name"), params.get("pointFront"), params.get("pointBack"));
        PageInfo info = new PageInfo<>(page.getResult());
        Object[] res = {list, info.getTotal()}; // res是分页后的数据，info.getTotal()是总条数
        return Msg.success().add("res", res);
    }
}
