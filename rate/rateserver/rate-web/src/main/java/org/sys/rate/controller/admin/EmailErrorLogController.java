package org.sys.rate.controller.admin;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.sys.rate.mapper.EmailErrorLogMapper;
import org.sys.rate.model.EmailErrorLog;
import org.sys.rate.model.RespBean;
import org.sys.rate.service.mail.EmailErrorLogService;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/exception")
public class EmailErrorLogController {
    @Resource
    EmailErrorLogService emailErrorLogService;
    @Resource
    EmailErrorLogMapper emailErrorLogMapper;

    @GetMapping("/getAll")
    public RespBean getAll(@RequestParam("page") Integer page, @RequestParam("size") Integer size){
        Page pageNum = PageHelper.startPage(page,size);
        List<EmailErrorLog> all = emailErrorLogService.getAllEmailErrorLogs();
        PageInfo info = new PageInfo<>(pageNum.getResult());
        Object[] res = {all, info.getTotal()};
        return RespBean.ok("success",res);
    }

    @PostMapping("/delete")
    public RespBean deleteByID(@RequestBody List<EmailErrorLog> data){
        emailErrorLogService.deleteMultiEmailErrorLog(data);
        return RespBean.ok("success");
    }

    @PostMapping("/deleteAll")
    public RespBean deleteAll(String start, String end) throws ParseException {
        SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date1 = start.equals("") ? null : sdf.parse(start);
        Date date2 = end.equals("") ? null : sdf.parse(start);
        Timestamp startTime = start.equals("") ? null : new Timestamp(date1.getTime());
        Timestamp endTime = end.equals("") ? null : new Timestamp(date2.getTime());
        emailErrorLogMapper.deleteAll(startTime,endTime);
        return RespBean.ok("success");
    }

    @GetMapping("/filterByDate")
    public RespBean filterByDate(Integer page, Integer size, String start, String end) throws ParseException {
        Page pageNum = PageHelper.startPage(page,size);
        SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date1 = sdf.parse(start);
        Date date2 = sdf.parse(end);
        Timestamp startTime = new Timestamp(date1.getTime());
        Timestamp endTime = new Timestamp(date2.getTime());
        List<EmailErrorLog> all = emailErrorLogMapper.filterByDate(startTime,endTime);
        PageInfo info = new PageInfo<>(pageNum.getResult());
        Object[] res = {all, info.getTotal()};
        return RespBean.ok("success",res);
    }

    @GetMapping("/getById")
    public RespBean getById(Long id){
        EmailErrorLog emailErrorLog = emailErrorLogMapper.getById(id);
        return RespBean.ok("success", emailErrorLog);
    }
}
