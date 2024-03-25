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
import org.sys.rate.mapper.InfosMapper;
import org.sys.rate.mapper.PaperMapper;
import org.sys.rate.model.Msg;
import org.sys.rate.model.Paper;
import org.sys.rate.model.RespBean;
import org.sys.rate.service.admin.IndicatorService;
import org.sys.rate.service.admin.PaperService;
import org.sys.rate.service.admin.PublicationService;
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
 * 论文成果Controller
 *
 * @author system
 * @date 2022-03-13
 */
@RestController
@RequestMapping("/paper/basic")
public class PaperController {
    @Resource
    private PaperService paperService;
    @Resource
    private PaperMapper paperMapper;
    @Resource
    PublicationService publicationService;
    @Resource
    IndicatorService indicatorService;
    @Resource
    MailToTeacherService mailToTeacherService;
    @Resource
    InfosMapper infosMapper;

    private static final Logger logger = LoggerFactory.getLogger(PaperController.class);


    private String uploadFileName;


    @GetMapping("/studentID")//无页码要求
    public JsonResult<List> getById(Integer studentID) {
        List<Paper> list = paperService.selectListByIds(studentID);
        return new JsonResult<>(list);
    }

    //    修改论文状态
    @GetMapping("/edit_state")
    public JsonResult getById(String state, Long ID) throws MessagingException {
        return new JsonResult(paperService.editState(state, ID));
    }

    @GetMapping("/List")
    public JsonResult<List> getCollect() {//老师查询所有学生提交的论文
        //包括返回最早的提交时间 和多次驳回的理由列表
        List<Paper> list = paperService.selectList();
        return new JsonResult<>(list);
    }

    /**
     * 查询论文成果列表
     */
    @PostMapping("/list")
    @ResponseBody
    public JsonResult list(Paper paper) {
        List<Paper> list = paperService.selectPaperList(paper);
        return new JsonResult(list);
    }

    //管理员修改该学生论文积分
    @PostMapping("/editPoint/{ID}")
    public JsonResult editPoint(@PathVariable Long ID, @RequestBody Paper paper) {
        paper.setID(ID);
        Integer res = paperMapper.editPoint(paper);
        return new JsonResult(res);
    }


    /**
     * 新增保存论文成果
     */
    @PostMapping("/add")
    @ResponseBody
    public JsonResult addSave(Paper paper) {
        paperService.insertPaper(paper);
        mailToTeacherService.sendTeaCheckMail(paper, "学术论文","添加");
        return new JsonResult(paper.getID());
    }

    /**
     * 修改保存论文成果
     */
    @PostMapping("/edit")
    @ResponseBody
    public JsonResult editSave(Paper paper) throws FileNotFoundException {
        int res = paperService.updatePaper(paper);
        mailToTeacherService.sendTeaCheckMail(paper, "学术论文", "修改");
        return new JsonResult(res);
    }

    /**
     * 删除论文成果
     */
    @DeleteMapping("/remove/{ID}")
    public JsonResult remove(@PathVariable Long ID) {
        Integer res = paperService.deletePaperById(ID);
        return new JsonResult(res);
    }

    @PostMapping("/upload")
    public JsonResult upload(@RequestParam MultipartFile file) throws IOException {
        String filename = file.getOriginalFilename();
        String fPath = new File("upload").getAbsolutePath() + "/" + filename;
        File newFile = new File(fPath);
        file.transferTo(newFile);

        //返回文件存储路径
        return new JsonResult(fPath);
    }

    @GetMapping("/download")
    public RespBean download(Integer paperID, String filename) throws IOException {
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
    public ResponseEntity<InputStreamResource> downloadFile(Integer infoItemID, Integer participantID, Integer activityID) throws IOException {
        String url = infosMapper.selectInfosContent(activityID,participantID,infoItemID);
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

    @PostMapping("/searchPaperByConditions")
    public Msg searchPaperByConditions(@RequestBody Map<String, String> params) {
        Page page = PageHelper.startPage(Integer.parseInt(params.get("pageNum")), Integer.parseInt(params.get("pageSize")));
        List<Paper> list = paperService.searchPaperByConditions(params.get("studentName"), params.get("state"),
                params.get("name"), params.get("pointFront"),
                params.get("pointBack"), params.get("pub"));
        PageInfo info = new PageInfo<>(page.getResult());
        Object[] res = {list, info.getTotal()}; // res是分页后的数据，info.getTotal()是总条数
        return Msg.success().add("res", res);
    }

}
