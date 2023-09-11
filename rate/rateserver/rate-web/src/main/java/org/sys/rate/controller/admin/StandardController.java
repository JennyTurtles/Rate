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
import org.sys.rate.model.Msg;
import org.sys.rate.model.Standard;
import org.sys.rate.model.RespBean;
import org.sys.rate.service.admin.IndicatorService;
import org.sys.rate.service.admin.StandardService;
import org.sys.rate.service.admin.PublicationService;
import org.sys.rate.service.admin.StandardService;
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
@RequestMapping("/standard/basic")
public class StandardController {

    @Resource
    private StandardService standardService;
    @Resource
    IndicatorService indicatorService;
    @Resource
    MailToTeacherService mailToTeacherService;

    private static final Logger logger = LoggerFactory.getLogger(StandardController.class);
    private String uploadFileName;

    @GetMapping("/studentID")//无页码要求
    public RespBean getById(Integer studentID) {
        List<Standard> list = standardService.selectListByIds(studentID);
        return RespBean.ok("success", list);
    }

    //    修改专利状态
    @GetMapping("/edit_state")
    public JsonResult getById(String state, Long ID) throws MessagingException {
        return new JsonResult(standardService.editState(state, ID));
    }

    @GetMapping("/List")
    public Msg getCollect(@RequestParam("pageNum") Integer pageNum, @RequestParam("pageSize") Integer pageSize) {
        //包括返回最早的提交时间 和多次驳回的理由列表
        Page page = PageHelper.startPage(pageNum, pageSize); // 设置当前所在页和每页显示的条数
        List<Standard> list = standardService.selectList();
        PageInfo info = new PageInfo<>(page.getResult());
        Object[] res = {list, info.getTotal()};
        return Msg.success().add("res", res);
    }

    /**
     * 查询专利成果列表
     */
    @PostMapping("/list")
    @ResponseBody
    public JsonResult list(Standard standard) {
        List<Standard> list = standardService.selectStandardList(standard);
        return new JsonResult(list);
    }


    /**
     * 新增保存专利成果
     */
    @PostMapping("/add")
    @ResponseBody
    public JsonResult addSave(Standard standard) throws FileNotFoundException {
        Integer res = standardService.insertStandard(standard);
        mailToTeacherService.sendTeaCheckMail(standard, "制定标准");
        return new JsonResult(standard.getId());
    }

    /**
     * 修改保存专利成果
     */
    @PostMapping("/edit")
    @ResponseBody
    public JsonResult editSave(Standard standard) throws FileNotFoundException {
        int res = standardService.updateStandard(standard);
        if (res > 0) {
            mailToTeacherService.sendTeaCheckMail(standard, "制定标准");
        }
        return new JsonResult(res);
    }

    /**
     * 删除专利成果
     */
    @DeleteMapping("/remove/{ID}")
    public JsonResult remove(@PathVariable Long ID) {
        Integer res = standardService.deleteStandardById(ID);
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
    public RespBean download(Integer standardID, String filename) throws IOException {
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

    @PostMapping("/searchStandardByConditions")
    public Msg searchProjectByConditions(@RequestBody Map<String, String> params) {
        Page page = PageHelper.startPage(Integer.parseInt(params.get("pageNum")), Integer.parseInt(params.get("pageSize")));
        List<Standard> list = standardService.searchStandardByConditions(params.get("studentName"), params.get("state"), params.get("name"), params.get("pointFront"), params.get("pointBack"));
        PageInfo info = new PageInfo<>(page.getResult());
        Object[] res = {list, info.getTotal()}; // res是分页后的数据，info.getTotal()是总条数
        return Msg.success().add("res", res);
    }
}
