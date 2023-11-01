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
import java.util.UUID;


/**
 * 论文成果Controller
 *
 * @author system
 * @date 2022-03-13
 */
@RestController
@RequestMapping("/achievements/basic")
public class AchievementsController {

    private static final Logger logger = LoggerFactory.getLogger(AchievementsController.class);

    private String uploadFileName;

    @PostMapping("/upload") //学生上传
    public JsonResult upload(@RequestParam MultipartFile file) throws IOException {
        String filename = file.getOriginalFilename();
        String fPath = new File("files").getAbsolutePath() + "/upload/#$%" + UUID.randomUUID() + "#$%" + filename;
        File newFile = new File(fPath);
        file.transferTo(newFile);

        //返回文件存储路径
        return new JsonResult(fPath);
    }

    @GetMapping("/download")
    public RespBean download(Integer paperID, String filename) throws IOException {
        File newFile = new File(new File("files").getAbsolutePath() + "\\upload\\" + filename);
        return RespBean.ok("success", newFile);
    }

    @PostMapping("/downloadByUrl") //下载证明材料
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
    //    修改专利状态
//    @GetMapping("/edit_state")
//    public JsonResult getById(String state, Long ID) throws MessagingException {
//        return new JsonResult(patentService.editState(state, ID));
//    }

}
