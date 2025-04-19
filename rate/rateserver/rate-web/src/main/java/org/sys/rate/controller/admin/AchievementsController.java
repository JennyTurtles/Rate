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
import org.sys.rate.model.RespBean;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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
        File parentDir = newFile.getParentFile();
        if (!parentDir.exists()) {
            parentDir.mkdirs(); // 创建多级目录
        }

        // 保存文件
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
        File file  = new File(url);
        System.out.println("File Path: " + file.getAbsolutePath());
        if (!file.exists()) {
            System.out.println("File does not exist: " + file.getAbsolutePath());
            throw new FileNotFoundException("File not found: " + file.getAbsolutePath());
        }
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

        String name = file.getName();
        int lastIndex = name.lastIndexOf('%');
        String result = name.substring(lastIndex + 1);
        String encode = URLEncoder.encode(result, StandardCharsets.UTF_8.name());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentDispositionFormData("attachment",encode);

        headers.add("Access-Control-Expose-Headers", "Content-Disposition");
//        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=test.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(file.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }


//    @PostMapping("/downloadByUrl") //下载证明材料
//    @ResponseBody
//    public void downloadFile( String url, HttpServletResponse response)  throws IOException{
//        File file  = new File(url);
//        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
//        // 设置响应头
//        response.setContentType("application/octet-stream");
//        response.setCharacterEncoding("UTF-8");
//
//
//
//        String name = file.getName();
//        int lastIndex = name.lastIndexOf('%');
//        String result = name.substring(lastIndex + 1);
//        String encodedFileName = URLEncoder.encode(result, StandardCharsets.UTF_8.name());
//        response.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + encodedFileName);
//        // 使用文件输入流和响应输出流传输文件
//        try (FileInputStream fis = new FileInputStream(file);
//             OutputStream os = response.getOutputStream()) {
//
//            byte[] buffer = new byte[1024];
//            int bytesRead;
//            while ((bytesRead = fis.read(buffer)) != -1) {
//                os.write(buffer, 0, bytesRead);
//            }
//            os.flush();
//        } catch (IOException e) {
//            e.printStackTrace();
//            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // 500 Internal Server Error
//        }
//    }
    //    修改专利状态
//    @GetMapping("/edit_state")
//    public JsonResult getById(String state, Long ID) throws MessagingException {
//        return new JsonResult(patentService.editState(state, ID));
//    }

}
