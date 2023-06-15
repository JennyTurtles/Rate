package org.sys.rate.controller.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.sys.rate.config.JsonResult;
import org.sys.rate.model.Operation;
import org.sys.rate.model.Paper;
import org.sys.rate.model.Publication;
import org.sys.rate.model.RespBean;
import org.sys.rate.service.admin.*;
import org.sys.rate.service.mail.MailToTeacherService;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;


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
    PublicationService publicationService;
    @Resource
    IndicatorService indicatorService;
    @Resource
    MailToTeacherService mailToTeacherService;

    private static final Logger logger = LoggerFactory.getLogger(PaperController.class);


    private String uploadFileName;


    @GetMapping("/studentID")//无页码要求
    public JsonResult<List> getById(Integer studentID) {
        List<Paper> list = paperService.selectListByIds(studentID);
        //好像是要返回时间最晚的被驳回记录的那条理由？
        List<Paper> pp;
        for (int i = 0; i < list.size(); i++) {
            Paper paper = list.get(i);
            List<Operation> paperOperation = paper.getPaperoperList();
            int j;
            Timestamp time = paperOperation.get(0).getTime();
            //说明只有学生提交的那一条操作记录，不需要继续
            if (paperOperation.size() == 1) {
                continue;
            }
            int flag = 0, maxTimeIndex = 0;
            for (j = 0; j < paperOperation.size(); j++) {
                if (paperOperation.get(j).getOperationName().equals("审核驳回") && paperOperation.get(j).getOperatorRole().equals("teacher")) {
                    flag = 1;
                    if (time.getTime() < paperOperation.get(j).getTime().getTime()) {
                        maxTimeIndex = j;
                        time = paperOperation.get(j).getTime();
                    }
                }
            }
            if (paperOperation.get(maxTimeIndex).getRemark() != "" && flag == 1) {
                if (paper.getState().equals("commit") || paper.getState().equals("tea_pass")) {
                    paper.setRemark(" ");
                } else {
                    paper.setRemark(paperOperation.get(maxTimeIndex).getRemark());
                }
            }
        }
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
        List<Paper> pp;
        for (int i = 0; i < list.size(); i++) {
            Paper x = list.get(i);
            Timestamp maxtime;
            List<Operation> x_p = x.getPaperoperList();
            if (x_p == null || x_p.size() == 0){
                continue;
            }
            int j;
            Timestamp timeCommit = x_p.get(0).getTime();
            Timestamp timeReject = x_p.get(0).getTime();
            if (x_p.size() == 1) {//只有一个提交
                x.setTime(x_p.get(0).getTime());
                continue;
            }
            int flagCommit = 0, flagReject = 0, indexCommit = 0, indexReject = 0;
//            List<String> remarks=new ArrayList<String>();
            for (j = 0; j < x_p.size(); j++) {//这篇论文的操作列表
                if (x_p.get(j).getOperationName().equals("提交论文") && x_p.get(j).getOperatorRole().equals("student")) {
                    flagCommit = 1;
                    //返回提交时间最早的一条
                    if (timeCommit.getTime() > x_p.get(j).getTime().getTime()) {
                        indexCommit = j;
                        timeCommit = x_p.get(j).getTime();
                    }
                }
                if (x_p.get(j).getOperationName().equals("审核驳回") && x_p.get(j).getOperatorRole().equals("teacher")) {
                    flagReject = 1;
//                    remarks.add(x_p.get(j).getRemark());
                    //返回驳回时间最晚的一条
                    if (timeReject.getTime() < x_p.get(j).getTime().getTime()) {
                        indexReject = j;
                        timeReject = x_p.get(j).getTime();
                    }
                }
            }
            if (x_p.get(indexReject).getRemark() != "" && flagReject == 1) {
                if (x.getState().equals("commit") || x.getState().equals("tea_pass")) {
                    x.setRemark(" ");
                } else {
                    x.setRemark(x_p.get(indexReject).getRemark());
                }
            }
            x.setTime(x_p.get(indexCommit).getTime());
        }
        return new JsonResult<>(list);
    }

    //    添加论文 搜索期刊类别
    @GetMapping("/publicationList")
    public JsonResult<List> getPublicationList(String publicationName) {
        return new JsonResult<>(publicationService.selectPublicationListByName(publicationName));
//        return new JsonResult<>(paperService.selectPublicationList(publicationName));
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


    /**
     * 新增保存论文成果
     */
    @PostMapping("/add")
    @ResponseBody
    public JsonResult addSave(Paper paper) throws FileNotFoundException {
        Integer res = paperService.insertPaper(paper);
//        mailToTeacherService.sendTeaCheckMail(paper, "学术论文", uploadFileName);
        return new JsonResult(paper.getID());
    }

    /**
     * 修改保存论文成果
     */
    @PostMapping("/edit")
    @ResponseBody
    public JsonResult editSave(Paper paper) throws FileNotFoundException {
        mailToTeacherService.sendTeaCheckMail(paper, "学术论文", uploadFileName);
        return new JsonResult(paperService.updatePaper(paper));
    }

    /**
     * 删除论文成果
     */
    @DeleteMapping("/remove/{ID}")
    public JsonResult remove(@PathVariable Long ID) {
        Integer res=paperService.deletePaperById(ID);
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

    //@PostMapping("/score")
    //@ResponseBody
    //public JsonResult getScore(@RequestBody Publication publication) { // name+year 得到对应的publication
    //    String name = publication.getName();
    //    int year = publication.getYear();
    //    Publication pub = publicationService.selectPublicationByNameYear(name, year);
    //    if (pub != null) {
    //        pub.setScore(indicatorService.selectScoreById((int) (long) pub.getIndicatorID()));
    //    }
    //    return new JsonResult(pub);
    //}

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
}
