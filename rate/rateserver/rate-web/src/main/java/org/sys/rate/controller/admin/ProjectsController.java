package org.sys.rate.controller.admin;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.sys.rate.config.JsonResult;
import org.sys.rate.model.*;
import org.sys.rate.service.admin.ProjectsOperService;
import org.sys.rate.service.admin.ProjectsService;
import org.sys.rate.service.admin.ProjectstypeService;
import org.sys.rate.service.admin.StudentService;
import org.sys.rate.service.mail.MailService;

import javax.annotation.Resource;
import javax.mail.MessagingException;

/**
 * 项目Controller
 *
 * @author system
 * @date 2022-03-13
 */
@RestController
@RequestMapping("/projects/basic")
public class ProjectsController
{

    @Resource
    private ProjectsService projectsService;
    @Resource
    ProjectstypeService projectstypeService;
    @Resource
    ProjectsOperService projectsOperService;
    @Resource
    MailService mailService;
    @Resource
    StudentService studentService;

    private String uploadFileName;

    @GetMapping("/List")
    public JsonResult<List> getCollect(@RequestParam(value = "ID", required = false) Integer id){
        return new JsonResult<>(projectsService.selectList(id));
    }

    @GetMapping("/studentID")
    public JsonResult<List> getById(Integer studentID){
        List<Projects> list=projectsService.selectListByIds(studentID);
        List<Projects> pp;
        for (int i=0;i<list.size();i++) {
            Projects x = list.get(i);
            Timestamp maxtime;
            List<ProjectsOper> x_p = x.getProjectsOperList();
            int j;
            Timestamp time = x_p.get(0).getTime();
            if (x_p.size() == 1) {
                continue;
            }
            int flag=0,index=0;
//            List<String> remarks=new ArrayList<String>();
            for(j=0;j<x_p.size();j++){
                if(x_p.get(j).getOperation().equals("审核驳回")&&x_p.get(j).getOperatorRole().equals("teacher")){
                    flag=1;
//                    remarks.add(x_p.get(j).getRemark());
                    if(time.getTime()<x_p.get(j).getTime().getTime()){
                        index=j;
                       time=x_p.get(j).getTime();
                    }
                }
            }
            if(x_p.get(index).getRemark()!=""&&flag==1){
                if (x.getState().equals("commit") || x.getState().equals("tea_pass")) {
                    x.setDivision(" ");
                }else {
                    x.setDivision(x_p.get(index).getRemark());
                }
//                x.setDivision(remarks);
            }
        }
        return new JsonResult<>(list);
    }

    //    添加项目 搜索项目类别
    @GetMapping("/publicationList")
    public JsonResult<List> getTypeList(String TypeName) {
        System.out.println("调用了typeList");
        return new JsonResult<>(projectstypeService.selectTypeListByName(TypeName));
    }

    /**
     * 查询项目列表
     */
    @GetMapping("/list")
    @ResponseBody
    public JsonResult list(Projects projects)
    {
        List<Projects> list = projectsService.selectProjectsList(projects);
        return new JsonResult(list);
    }

    /**
     * 查询项目列表
     */
    @PostMapping("/list")
    @ResponseBody
    public JsonResult psotList(@RequestBody Projects projects)
    {
        List<Projects> list = projectsService.selectProjectsList(projects);
        return new JsonResult(list);
    }

    /**
     * 新增保存项目
     */

//    @PostMapping("/add")
//    @ResponseBody
//    public JsonResult addSave(Projects projects) throws MessagingException {
//        Integer res=projectsService.insertProjects(projects);
//        System.out.println("新增的projects id:");
//        System.out.println(projects.getID());
//        System.out.println(projects.getStarttime());
//
//        //mailService.sendMail(projects, uploadFileName);
//        return new JsonResult(projects.getID());
////        return new JsonResult();
//    }


    /**
     * 修改保存论文成果
     */
    @PostMapping("/edit")
    @ResponseBody
    public JsonResult editSave(Projects projects) throws MessagingException {
        System.out.println(projects.getName());
        System.out.println(projects.getID());
        //mailService.sendMail(projects, uploadFileName);
        return new JsonResult(projectsService.updateProjects(projects));
    }

    /**
     * 删除项目
     */
    @DeleteMapping( "/remove/{ID}")
//    @ResponseBody
    public JsonResult remove(@PathVariable Long ID)
    {
        System.out.println(ID);
        Integer res=projectsService.deleteProjectsById(ID);
//        if(res< 300 && res >= 200){
//            return RespBean.ok("success");
//        }else {
//            return RespBean.error("something wrong!");
//        }
        return new JsonResult(res);
    }

    @PostMapping("/upload")
    public JsonResult upload(@RequestParam MultipartFile file) throws IOException {
        String filename = file.getOriginalFilename();
        String fPath=new File("upload").getAbsolutePath() + "/" + filename;
        File newFile = new File(fPath);
        file.transferTo(newFile);

        uploadFileName = filename;
        //返回文件存储路径
        return new JsonResult(fPath);
    }

    @GetMapping("/download")
    public RespBean download(Integer projectID,String filename) throws IOException {
        File newFile = new File(new File("upload").getAbsolutePath() + "/" + filename);
//        file.transferTo(newFile);
        return RespBean.ok("success");
    }
    @PostMapping("/deleteFile")//删除某个文件
    @ResponseBody
    public JsonResult delete(String filepath){
        boolean flag = false;
        File file = new File(filepath);
        if( file.isFile() && file.exists()){
            flag = file.delete();
        }
        return new JsonResult(flag);
    }


    //    修改论文状态
    @GetMapping("/edit_state")
    public JsonResult getById(String state, Long ID) throws MessagingException {
        return new JsonResult(projectsService.editState(state, ID));
    }

    @PostMapping("/add")
    @ResponseBody
    public JsonResult addSave(ProjectsOper paperoper)
    {
        return new JsonResult(projectsService.insertPaperOper(paperoper));
//        return new JsonResult();
    }
}
