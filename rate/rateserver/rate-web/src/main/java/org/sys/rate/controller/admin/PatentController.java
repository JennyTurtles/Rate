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
import org.sys.rate.mapper.PatentMapper;
import org.sys.rate.model.Msg;
import org.sys.rate.model.Paper;
import org.sys.rate.model.Patent;
import org.sys.rate.model.RespBean;
import org.sys.rate.service.admin.IndicatorService;
import org.sys.rate.service.admin.PatentService;
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
 * 专利成果Controller
 *
 * @author system
 * @date 2022-03-13
 */
@RestController
@RequestMapping("/patent/basic")
public class PatentController {

    @Resource
    private PatentService patentService;
    @Resource
    private PatentMapper patentMapper;
    @Resource
    PublicationService publicationService;
    @Resource
    IndicatorService indicatorService;
    @Resource
    MailToTeacherService mailToTeacherService;

    private static final Logger logger = LoggerFactory.getLogger(PatentController.class);
    private String uploadFileName;

    @GetMapping("/studentID")//无页码要求
    public JsonResult<List> getById(Integer studentID) {
        List<Patent> list = patentService.selectListByIds(studentID);
        return new JsonResult<>(list);
    }

    //    修改专利状态
    @GetMapping("/edit_state")
    public JsonResult getById(String state, Long ID) throws MessagingException {
        return new JsonResult(patentService.editState(state, ID));
    }

    @GetMapping("/List")
    public Msg getCollect(@RequestParam("pageNum") Integer pageNum, @RequestParam("pageSize") Integer pageSize) {
        //包括返回最早的提交时间 和多次驳回的理由列表
        Page page = PageHelper.startPage(pageNum, pageSize); // 设置当前所在页和每页显示的条数
        List<Patent> list = patentService.selectList();
        PageInfo info = new PageInfo<>(page.getResult());
        Object[] res = {list, info.getTotal()};
        return Msg.success().add("res", res);
    }

    //    添加专利 搜索期刊类别
    @GetMapping("/publicationList")
    public JsonResult<List> getPublicationList(String publicationName) {
        return new JsonResult<>(publicationService.selectPublicationListByName(publicationName));
    }

    /**
     * 查询专利成果列表
     */
    @PostMapping("/list")
    @ResponseBody
    public JsonResult list(Patent patent) {
        List<Patent> list = patentService.selectPatentList(patent);
        return new JsonResult(list);
    }


    /**
     * 新增保存专利成果
     */
    @PostMapping("/add")
    @ResponseBody
    public JsonResult addSave(Patent patent) throws FileNotFoundException {
        Integer res = patentService.insertPatent(patent);
        mailToTeacherService.sendTeaCheckMail(patent, "授权专利", "添加");
        return new JsonResult(patent.getId());
    }

    /**
     * 修改保存专利成果
     */
    @PostMapping("/edit")
    @ResponseBody
    public JsonResult editSave(Patent patent) throws FileNotFoundException {
        int res = patentService.updatePatent(patent);
        if (res > 0) {
            mailToTeacherService.sendTeaCheckMail(patent, "授权专利", "修改");
        }
        return new JsonResult(res);
    }

    /**
     * 删除专利成果
     */
    @DeleteMapping("/remove/{ID}")
    public JsonResult remove(@PathVariable Long ID) {
        Integer res = patentService.deletePatentById(ID);
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
    public RespBean download(Integer patentID, String filename) throws IOException {
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

    @PostMapping("/searchPatentByConditions")
    public Msg searchProjectByConditions(@RequestBody Map<String, String> params) {
        Page page = PageHelper.startPage(Integer.parseInt(params.get("pageNum")), Integer.parseInt(params.get("pageSize")));
        List<Patent> list = patentService.searchPatentByConditions(params.get("studentName"), params.get("state"), params.get("name"), params.get("pointFront"), params.get("pointBack"));
        PageInfo info = new PageInfo<>(page.getResult());
        Object[] res = {list, info.getTotal()}; // res是分页后的数据，info.getTotal()是总条数
        return Msg.success().add("res", res);
    }
    //管理员修改该学生论文积分
    @PostMapping("/editPoint/{ID}")
    public JsonResult editPoint(@PathVariable Integer ID, @RequestBody Patent patent) {
        patent.setId(ID);
        Integer res = patentMapper.editPoint(patent);
        return new JsonResult(res);
    }
}
