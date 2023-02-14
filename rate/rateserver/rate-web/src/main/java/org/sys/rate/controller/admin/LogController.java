package org.sys.rate.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.sys.rate.model.Activities;
import org.sys.rate.model.Log;
import org.sys.rate.model.RespBean;
import org.sys.rate.model.RespPageBean;
import org.sys.rate.service.admin.ActivitiesService;
import org.sys.rate.service.admin.LogService;
import org.sys.rate.service.admin.ScoreItemService;

import java.util.List;


@RestController
@RequestMapping("/log/basic")
public class LogController {
    @Autowired
    LogService logService;


    @GetMapping("/")
    public RespPageBean getLogByPage(@RequestParam(defaultValue = "1") Integer page,
                                            @RequestParam(defaultValue = "10") Integer size,
                                            @RequestParam(defaultValue = "1") Integer institutionID,
                                     Log employee) {
        return logService.getLogsPage(page, size, employee, institutionID);
    }
}

