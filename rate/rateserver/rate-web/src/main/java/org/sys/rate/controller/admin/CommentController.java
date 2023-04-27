package org.sys.rate.controller.admin;

import org.sys.rate.mapper.CommentMapper;
import org.sys.rate.model.Comment;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.sys.rate.config.RsaUtil;
import org.sys.rate.mapper.StudentMapper;
import org.sys.rate.model.GraduateStudent;
import org.sys.rate.model.Msg;
import org.sys.rate.model.RespBean;
import org.sys.rate.model.Student;
import org.sys.rate.service.admin.GraduateStudentService;
import org.sys.rate.service.admin.LogService;
import org.sys.rate.service.expert.ExpertService;
import org.sys.rate.utils.POIUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/comment/basic")
public class CommentController {
    @Resource
    GraduateStudentService graduateStudentService;
    @Resource
    StudentMapper studentMapper;
    @Resource
    CommentMapper commentMapper;
    @PostMapping("/addCommentByExpert")
    public RespBean addCommentByExpert(@RequestBody Comment comment){
        int res = 0;
        try {
            Comment selectComment = commentMapper.selectHaveComment(comment);
            if(selectComment == null){
                //没找到这个评语记录就添加
                res = commentMapper.insertCommentByExpert(comment);
            }else{
                //有这条记录就更新评语内容和时间
                selectComment.setContent(comment.getContent());
                selectComment.setDate(comment.getDate());
                commentMapper.updateCommentByExpert(selectComment);
            }
        }catch (Exception e){
            return RespBean.error("添加失败",null);
        }
        if(res == 0)RespBean.error("添加失败",null);
        return RespBean.ok("添加成功",null);
    }
}
