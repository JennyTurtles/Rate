package org.sys.rate.controller.admin;/**
 * ClassName: PublicationSubmissionController
 * Package: org.sys.rate.controller.admin
 * Description:
 *
 * @Author ZYK
 * @Create 2023/7/7 20:23
 * @Version 1.0
 */

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.sys.rate.config.JsonResult;
import org.sys.rate.mapper.PublicationSubmissionMapper;
import org.sys.rate.model.*;
import org.sys.rate.service.admin.PublicationService;
import org.sys.rate.service.admin.PublicationSubmissionService;

import javax.annotation.Resource;
import java.beans.Transient;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * @Description:
 * @param: $
 * @return: $
 */
@RestController
@RequestMapping("/publicationSubmission")
public class PublicationSubmissionController {

    @Resource
    private PublicationService publicationService;

    @Resource
    private PublicationSubmissionMapper submissionMapper;

    @Resource
    private PublicationSubmissionService submissionService;


    @GetMapping("/get")
    public Msg getAllSubmission(@RequestParam("state") String state, @RequestParam("page") Integer pageNum, @RequestParam("size") Integer size) {
        Page page = PageHelper.startPage(pageNum,size);
        List<PublicationSubmission> submissions = "all".equals(state) ? submissionMapper.getAllSubmission() : submissionMapper.getSubmissionByState(state);
        if (submissions.isEmpty()) {
            return Msg.success().add("没有找到任何提交记录", null);
        }
        PageInfo info = new PageInfo<>(page.getResult());
        Object[] res = {submissions, info.getTotal()}; // res是分页后的数据，info.getTotal()是总条数
        return Msg.success().add("res", res);
    }

    @GetMapping("/getStuSubmission")
    public Msg getAllSubmission(@RequestParam("stuID") Integer stuID, @RequestParam("page") Integer pageNum, @RequestParam("size") Integer size) {
        Page page = PageHelper.startPage(pageNum,size);
        List<PublicationSubmission> list = submissionService.getAllStuSubmission(stuID);
        PageInfo info = new PageInfo<>(page.getResult());
        Object[] res = {list, info.getTotal()}; // res是分页后的数据，info.getTotal()是总条数
        return Msg.success().add("res", res);
    }

    @GetMapping("/check")
    public RespBean checkSubmission(PublicationSubmission submission) {
        try {
            // 检查是否重复
            boolean isDup = submissionMapper.check(submission.getIndicatorId(), submission.getPublicationId(), submission.getYear()) != null;
            return RespBean.ok("200", isDup);
        } catch (Exception e) {
            // 处理异常情况
            return RespBean.error("Error occurred during submission check.");
        }
    }


    @PostMapping("editState")
    @Transactional
    public RespBean editState(PublicationSubmission submission) {
        // 1. edit state
        submissionMapper.editState(submission.getId(), submission.getState(), submission.getComment());
        if (submission.getState().equals("pass")) {
            // 2. add publication  这里需要进行查重！！
            Publication publication = new Publication();
            if (submission.getPublicationId() == null) {
                publication.setName(submission.getPublicationName());
                publication.setAbbr(submission.getPublicationAbbr());
                publication.setPublisher(submission.getPublisherName());
                publication.setUrl(submission.getPublicationUrl());
            }
            publication.setId(submission.getPublicationId());

            Indicator indicator = new Indicator();
            indicator.setId(submission.getIndicatorId());

            List<Indicator> indicatorList = Collections.singletonList(indicator);
            List<Integer> dateList = Collections.singletonList(submission.getYear());

            publication.setIndicatorList(indicatorList);
            publication.setDateList(dateList);
            publicationService.insertPublication(publication);
        }
        return RespBean.ok("200");
    }

    @PostMapping("insert")
    public RespBean insert(PublicationSubmission submission) {
        // postRequest1不支持传对象的json
        try {
            int insert = submissionMapper.insert(submission);
            if (insert > 0) {
                return RespBean.ok("200");
            } else {
                return RespBean.error("未能成功插入 PublicationSubmission");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return RespBean.error("插入数据时发生异常");
        }
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

    @PostMapping("/upload")
    public JsonResult upload(@RequestParam MultipartFile file) throws IOException {
        String filename = file.getOriginalFilename();
        String fPath = new File("upload").getAbsolutePath() + "/" + filename;
        try {
            File newFile = new File(fPath);
            file.transferTo(newFile);

            //返回文件存储路径
            return new JsonResult(fPath);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonResult("文件上传失败");
        }
    }

    @GetMapping("/download")
    public RespBean download(String filename) throws IOException {
        File newFile = new File(new File("upload").getAbsolutePath() + "/" + filename);
        return RespBean.ok("success", newFile);
    }

}
