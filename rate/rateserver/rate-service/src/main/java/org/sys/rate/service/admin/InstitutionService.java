package org.sys.rate.service.admin;

import org.sys.rate.mapper.InstitutionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.sys.rate.model.Institution;
import org.sys.rate.model.RespPageBean;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @作者 江南一点雨
 * @公众号 江南一点雨
 * @微信号 a_java_boy
 * @GitHub https://github.com/lenve
 * @博客 http://wangsong.blog.csdn.net
 * @网站 http://www.javaboy.org
 * @时间 2019-10-29 7:44
 */
@Service
public class InstitutionService {
    @Autowired
    InstitutionMapper institutionMapper;
    @Autowired
    RabbitTemplate rabbitTemplate;

    public final static Logger logger = LoggerFactory.getLogger(InstitutionService.class);
    SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
    SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
    DecimalFormat decimalFormat = new DecimalFormat("##.00");

    public RespPageBean getInstitutionPage(Integer page, Integer size, Institution employee) {
        if (page != null && size != null) {
            page = (page - 1) * size;
        }
        List<Institution> data = institutionMapper.getInstitutionByPage(page, size, employee);
        Long total = institutionMapper.getTotal(employee);
        RespPageBean bean = new RespPageBean();
        bean.setData(data);
        bean.setTotal(total);
        return bean;
    }

    public Integer addInstitution(Institution employee) {
        int result = institutionMapper.insert(employee);
        return result;
    }

    public Integer deleteInstitution(Institution company) {
        int result = institutionMapper.delete(company);
        return result;
    }

    public Integer updateInstitution(Institution company) {
        int result = institutionMapper.update(company);
        return result;
    }

    public RespPageBean searchInstitution(Integer page, Integer size,String company) {
        if (page != null && size != null) {
            page = (page - 1) * size;
        }
        List<Institution> data = institutionMapper.getInstitutionByPageAndCompany(page, size, company);
        Long total = institutionMapper.getTotalByCompany(company);
        RespPageBean bean = new RespPageBean();
        bean.setData(data);
        bean.setTotal(total);
        return bean;
    }
}
