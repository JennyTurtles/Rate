package org.sys.rate.service.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.sys.rate.mapper.ActivitiesMapper;
import org.sys.rate.mapper.LogMapper;
import org.sys.rate.model.Activities;
import org.sys.rate.model.Log;
import org.sys.rate.model.RespPageBean;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;


@Service
public class LogService {
    @Autowired
    LogMapper logMapper;

    public final static Logger logger = LoggerFactory.getLogger(LogService.class);

    public RespPageBean getLogsPage(Integer page, Integer size, Log employee, Integer institutionID) {
        if (page != null && size != null) {
            page = (page - 1) * size;
        }
        List<Log> data = logMapper.getLogByPage(page, size, institutionID);
        Long total = logMapper.getTotal(employee, institutionID);
        RespPageBean bean = new RespPageBean();
        bean.setData(data);
        bean.setTotal(total);
        return bean;
    }

    public Integer addLogs(Log employee) {
        int result = logMapper.insert(employee);
        return result;
    }
}
