package org.sys.rate.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.sys.rate.config.JsonResult;
import org.sys.rate.model.Patent;
import org.sys.rate.model.PatentOper;
import org.sys.rate.model.Student;
import org.sys.rate.service.admin.PatentService;
import org.sys.rate.service.admin.PatentTypeService;
import org.sys.rate.service.admin.StudentService;
import org.sys.rate.service.mail.MailToTeacherService;

import javax.mail.MessagingException;
import java.util.List;

/**
 * 著作Controller
 *
 * @author system
 * @date 2022-03-13
 */
@RestController
@RequestMapping("/patent/basic")
public class PatentController
{

    @Autowired
    private PatentService patentService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private PatentTypeService patentTypeService;

    @GetMapping("/List")
    public JsonResult<List> getCollect(@RequestParam(value = "ID", required = false) Integer id){
        List<Patent> patents = patentService.selectList(id);
        return new JsonResult<>(patents);
    }
    @PostMapping("/List")
    public JsonResult<List> postList(@RequestBody Patent patent){
        List<Patent> patents = patentService.selectPatentList(patent);
        return new JsonResult<>(patents);
    }

    @PostMapping("/list")
    @ResponseBody
    public JsonResult list(Patent patent)
    {
        List<Patent> list = patentService.selectPatentList(patent);
        for (Patent patent1 : list) {
            Student byId = studentService.getById(Math.toIntExact(patent1.getStudentID()));
            patent1.setStudent(byId);

        }
        return new JsonResult(list);
    }







    /**
     * 修改保存著作
     */
    @PostMapping("/edit")
    @ResponseBody
    public JsonResult editSave(Patent patent)
    {
        return new JsonResult(patentService.updatePatent(patent));
    }

    /**
     * 删除著作
     */
    @PostMapping( "/remove")
    @ResponseBody
    public JsonResult remove(Long ids)
    {
        return new JsonResult(patentService.deletePatentById(ids));
    }


    //    修改论文状态
    @GetMapping("/edit_state")
    public JsonResult getById(String state, Long ID) throws MessagingException {
        return new JsonResult(patentService.editState(state, ID));
    }

    @PostMapping("/add")
    @ResponseBody
    public JsonResult addSave(PatentOper paperoper)
    {
        return new JsonResult(patentService.insertPaperOper(paperoper));
//        return new JsonResult();
    }


    private String uploadFileName;

    @Autowired
    private MailToTeacherService mailToTeacherService;



}
