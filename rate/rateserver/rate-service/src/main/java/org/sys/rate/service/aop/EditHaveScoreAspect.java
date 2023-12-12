package org.sys.rate.service.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.sys.rate.mapper.GraduateStudentMapper;
import org.sys.rate.mapper.PaperMapper;
import org.sys.rate.mapper.productionMapper;
import org.sys.rate.model.Production;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Component
@Aspect
public class EditHaveScoreAspect {

    @Resource
    private productionMapper productionMapper;

    @Resource
    GraduateStudentMapper graduateStudentMapper;

    @Pointcut("execution(* org.sys.rate.service.admin.*Service.editState(..))")
    public void editHaveScore() {
    }

    @Pointcut("execution(* org.sys.rate.service.admin.PaperService.editState(..))")
    public void editHaveScoreForPaper() {
    }

    // 对paper以外的8种成果AOP
    @Before("editHaveScore() && ! editHaveScoreForPaper()")
    public void aroundEditHaveScore(JoinPoint joinPoint) {
        String state = joinPoint.getArgs()[0].toString();
        Long ID = (Long) joinPoint.getArgs()[1];
        if(!state.equals("adm_pass")) // 仅在管理员通过时，修改have_score
            return;

        // 获取production
        String serviceName = joinPoint.getTarget().getClass().getName();
        String productionName = serviceName.substring(serviceName.lastIndexOf(".") + 1, serviceName.indexOf("Service"));
        String table = "i_" + productionName.toLowerCase();
        if (table.equals("i_product")) table = "i_application";
        Production production = productionMapper.checkProductionById(table, ID.intValue());

        // 2分的成果，且已发表过同类的成果，have_score设置为0
        Integer studentID = production.getStudentId();
        Integer point = production.getPoint();
        if(point.equals(2) && productionMapper.checkScore(table,studentID) != null){
            productionMapper.editHaveScore(table, state,ID, 0);
            return;
        }

        // 其他的成果，have_score设置为1，更新研究生表的积分
        productionMapper.editHaveScore(table, state,ID, 1);
        graduateStudentMapper.updateScore(Long.valueOf(studentID), point.longValue());
    }

}
