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
import org.sys.rate.mapper.DecisionTypeMapper;
import org.sys.rate.mapper.IndicatorMapper;
import org.sys.rate.model.Decision;
import org.sys.rate.model.DecisionType;
import org.sys.rate.model.Msg;
import org.sys.rate.model.RespBean;
import org.sys.rate.service.admin.DecisionService;
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
@RequestMapping("/decision/basic")
public class DecisionController {

    @Resource
    private DecisionService decisionService;
    @Resource
    IndicatorMapper indicatorMapper;
    @Resource
    MailToTeacherService mailToTeacherService;
    @Resource
    private DecisionTypeMapper decisionTypeMapper;

    private static final Logger logger = LoggerFactory.getLogger(DecisionController.class);
    private String uploadFileName;

    @GetMapping("/studentID")//无页码要求
    public JsonResult<List> getById(Integer studentID) {
        List<Decision> list = decisionService.selectDecisionListById(studentID);
        return new JsonResult<>(list);
    }

    //    修改专利状态
    @GetMapping("/edit_state")
    public JsonResult getById(String state, Long ID) throws MessagingException {
        return new JsonResult(decisionService.editState(state, ID));
    }

    @GetMapping("/List")
    public Msg getCollect(@RequestParam("pageNum") Integer pageNum, @RequestParam("pageSize") Integer pageSize) {
        //包括返回最早的提交时间 和多次驳回的理由列表
        Page page = PageHelper.startPage(pageNum, pageSize);
        List<Decision> list = decisionService.selectAllDecisionList();
        PageInfo info = new PageInfo<>(page.getResult());
        Object[] res = {list, info.getTotal()};
        return Msg.success().add("res", res);
    }

    /**
     * 新增保存专利成果
     */
    @PostMapping("/add")
    @ResponseBody
    public JsonResult addSave(Decision decision) throws FileNotFoundException {
        Integer res = decisionService.insertDecision(decision);
        mailToTeacherService.sendTeaCheckMail(decision, "决策咨询","添加");
        return new JsonResult(decision.getId());
    }

    /**
     * 修改保存专利成果
     */
    @PostMapping("/edit")
    @ResponseBody
    public JsonResult editSave(Decision decision) throws FileNotFoundException {
        int res = decisionService.updateDecision(decision);
        if (res > 0) {
            mailToTeacherService.sendTeaCheckMail(decision, "决策咨询","修改");
        }
        return new JsonResult(res);
    }

    /**
     * 删除专利成果
     */
    @DeleteMapping("/remove/{ID}")
    public JsonResult remove(@PathVariable Long ID) {
        Integer res = decisionService.deleteDecisionById(ID);
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
    public RespBean download(Integer decisionID, String filename) throws IOException {
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
    public JsonResult getIndicatorByYearAndType(String year, Integer indicatorId) {
        List<DecisionType> list = decisionService.getIndicatorByYearAndType(year, indicatorId);
        return new JsonResult(list);
    }

    @GetMapping("/getIndicatorScore")
    public JsonResult getScore(Integer id) {
        return new JsonResult(indicatorMapper.getIndicatorById(id));
    }

    @PostMapping("/searchDecisionByConditions")
    public Msg searchProjectByConditions(@RequestBody Map<String, String> params) {
        Page page = PageHelper.startPage(Integer.parseInt(params.get("pageNum")), Integer.parseInt(params.get("pageSize")));
        List<Decision> list = decisionService.searchDecisionByConditions(params.get("studentName"), params.get("state"), params.get("name"), params.get("pointFront"), params.get("pointBack"));
        PageInfo info = new PageInfo<>(page.getResult());
        Object[] res = {list, info.getTotal()}; // res是分页后的数据，info.getTotal()是总条数
        return Msg.success().add("res", res);
    }

    @PostMapping("/decisionType")
    public RespBean addDecisionType(@RequestBody DecisionType decisionType) {
        // 1.向decisionType插入
        // 2.向indicator中插入，no，这里其实就只需要设置indicator中的rankN就可以了！
        try {
            decisionService.addDecisionType(decisionType);
            return RespBean.ok("插入decisionType成功！");
        } catch (Exception e) {
            return RespBean.error("插入decisionType失败！");
        }

    }

    @PutMapping("/decisionType")
    public RespBean editDecisionType(@RequestBody DecisionType decisionType) {
        try {
            decisionService.editDecisionType(decisionType);
            return RespBean.ok("修改decisionType成功！");
        } catch (Exception e) {
            return RespBean.error("修改decisionType失败！");
        }
    }

    @PostMapping("/decisionType/dels")
    public RespBean deleteByYearId(@RequestParam Integer year, @RequestParam Integer indicatorID) {
        try {
            decisionTypeMapper.deleteByYearIndicatorID(year, indicatorID);
            return RespBean.ok("删除成功！");
        } catch (Exception e) {
            return RespBean.error("删除失败！");
        }
    }

    @PostMapping("/decisionType/import")
    public RespBean multiImportPublication(@RequestBody List<DecisionType> decisionTypes) {
        try {
            for (DecisionType decisionType : decisionTypes) {
                decisionTypeMapper.addDecisionType(decisionType);
            }
            return RespBean.ok("添加成功！");
        } catch (Exception e) {
            return RespBean.error("添加失败！");
        }
    }

}
