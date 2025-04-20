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
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.sys.rate.config.JsonResult;
import org.sys.rate.mapper.InfosMapper;
import org.sys.rate.mapper.PaperMapper;
import org.sys.rate.model.*;
import org.sys.rate.service.admin.*;
import org.sys.rate.service.mail.MailToTeacherService;
import org.sys.rate.utils.ProjectTypeEnums;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * 论文成果Controller
 *  /xinproject/basic/searchProjectByConditions
 * @author system
 * @date 2022-03-13
 */
@RestController
@RequestMapping("/xinproject/basic")
public class XinProjectController {
    @Resource
    private PaperService paperService;
    @Resource
    private PaperMapper paperMapper;
    @Resource
    PublicationService publicationService;
    @Resource
    IndicatorService indicatorService;
    @Resource
    MailToTeacherService mailToTeacherService;
    @Resource
    InfosMapper infosMapper;

    private static final Logger logger = LoggerFactory.getLogger(XinProjectController.class);
    @Resource
    private MonographService monographService;
    @Resource
    private PatentService patentService;
    @Resource
    private AwardService awardService;
    @Resource
    private CompetitionService competitionService;
    @Resource
    private DecisionService decisionService;
    @Resource
    private ProjectService projectService;
    @Resource
    private StandardService standardService;
    @Resource
    private ProductService productService;
    @Resource
    private  XinProjectService xinProjectService;
    @Resource
    private ProgramRecordService programRecordService;


    @PostMapping("/searchProjectByConditions")
    public Msg searchPaperByConditions(@RequestBody Map<String, String> params) {
        List<ProjectBase> dataArr = new LinkedList<>();
//        dataArr =  xinProjectService.searchPaperByConditions(params);

        dataArr.addAll(getPaperLst(params));
        dataArr.addAll(searchDecisionByConditions(params));
        dataArr.addAll(searchProjectByConditions(params));
        dataArr.addAll(searchHorizontalProjectByConditions(params));
        dataArr.addAll(searchProductByConditions(params));
        dataArr.addAll(searchStandardByConditions(params));
        dataArr.addAll(searchMonographByConditions(params));
        dataArr.addAll(searchCompetitionByConditions(params));
        dataArr.addAll(searchAwardByConditions(params));
        dataArr.addAll(searchPatentByConditions(params));

        dataArr.sort(Comparator.comparing(
                ProjectBase::getCreatetime,
                Comparator.nullsLast(Comparator.reverseOrder()) // 时间倒排，null 值排在最后
        ));

        Object[] res = {dataArr, dataArr.size()}; // res是分页后的数据，0是总条数
        return Msg.success().add("res", res);
    }
    @PostMapping("/searchProjectByConditionsTea")
    public Msg searchProjectByConditionsTea(@RequestBody Map<String, String> params) {
        List<ProjectBase> list = new LinkedList<>();
        list =  xinProjectService.searchPaperByConditions(params);
        List<ProjectBase> dataArr = new LinkedList<>();
        if(!CollectionUtils.isEmpty(list)){
            list.stream().forEach(x->{
                ProjectBase base = new ProjectBase();
                base.setId(x.getId());
                base.setStudentId(x.getStudentId());
                base.setStudentName(x.getStudentName());
                base.setName(x.getName());
                base.setState(x.getState());
                base.setPoint(x.getPoint());
                base.setHaveScore(x.getHaveScore());
                base.setRemark(x.getRemark());
                base.setCreatetime(x.getDate());
                base.setDate(x.getDate());
                base.setType(x.getType());
                dataArr.add(base);
            });
        }
        dataArr.sort(Comparator.comparing(
                ProjectBase::getDate,
                Comparator.nullsLast(Comparator.reverseOrder()) // 时间倒排，null 值排在最后
        ));

        Object[] res = {dataArr, dataArr.size()}; // res是分页后的数据，0是总条数
        return Msg.success().add("res", res);
    }

    private List<ProjectBase> getPaperLst(Map<String, String> params) {
        List<Paper> list = paperService.searchPaperByConditions(params.get("studentName"), params.get("state"),
                params.get("name"), params.get("pointFront"),
                params.get("pointBack"), params.get("pub"));
        List<ProjectBase> dataArr = new LinkedList<>();
        if(!CollectionUtils.isEmpty(list)){
            list.stream().forEach(x->{
                ProjectBase base = new ProjectBase();
                base.setId(x.getID());
                base.setStudentId(x.getStudentID());
                base.setStudentName(x.getStudent());
                base.setName(x.getName());
                base.setState(x.getState());
                base.setPoint(x.getPoint());
                base.setHaveScore(x.getHave_score());
                base.setRemark(x.getRemark());
                base.setCreatetime(x.getCreatetime());
                base.setDate(x.getTime());
                base.setType(ProjectTypeEnums.ACADEMIC_PAPER.getDisplayName());
                dataArr.add(base);
            });
        }
        return dataArr;
    }


    public List<ProjectBase> searchDecisionByConditions(Map<String, String> params) {

        List<Decision> list = decisionService.searchDecisionByConditions(params.get("studentName"), params.get("state"), params.get("name"), params.get("pointFront"), params.get("pointBack"));

        List<ProjectBase> dataArr = new LinkedList<>();
        if(!CollectionUtils.isEmpty(list)){
            list.stream().forEach(x->{
                ProjectBase base = new ProjectBase();
                base.setId(x.getId());
                base.setStudentId(x.getStudentId());
                base.setStudentName(x.getStudent());
                base.setName(x.getName());
                base.setState(x.getState());
                base.setPoint(x.getPoint());
                base.setHaveScore(x.getHave_score());
                base.setRemark(x.getRemark());
                base.setCreatetime(x.getCreatetime());
                base.setDate(x.getDate());
                base.setType(ProjectTypeEnums.DECISION_CONSULTING.getDisplayName());
                dataArr.add(base);
            });
        }
        return dataArr;
    }

    public List<ProjectBase> searchProjectByConditions(Map<String, String> params) {
        List<Project> list = projectService.searchProjectByConditions(params.get("studentName"), params.get("state"), params.get("name"), params.get("pointFront"), params.get("pointBack"));

        List<ProjectBase> dataArr = new LinkedList<>();
        if(!CollectionUtils.isEmpty(list)){
            list.stream().forEach(x->{
                ProjectBase base = new ProjectBase();
                base.setId(x.getId());
                base.setStudentId(x.getStudentId());
                base.setStudentName(x.getStudent());
                base.setName(x.getName());
                base.setState(x.getState());
                base.setPoint(x.getPoint());
                base.setHaveScore(x.getHave_score());
                base.setRemark(x.getRemark());
                base.setCreatetime(x.getCreatetime());
                base.setDate(x.getDate());
                base.setType(ProjectTypeEnums.VERTICAL_RESEARCH_PROJECT.getDisplayName());
                dataArr.add(base);
            });
        }
        return dataArr;
    }

    public List<ProjectBase> searchHorizontalProjectByConditions(Map<String, String> params) {

        List<ProgramResult> list = programRecordService.searchHorizontalProjectByConditions(params.get("studentName"), params.get("state"), params.get("name"), params.get("pointFront"), params.get("pointBack"));

        List<ProjectBase> dataArr = new LinkedList<>();
        if(!CollectionUtils.isEmpty(list)){
            list.stream().forEach(x->{
                ProjectBase base = new ProjectBase();
                base.setId(x.getId());
                base.setStudentId(x.getStudentId());
                base.setStudentName(x.getStudent());
                base.setName(x.getName());
                base.setState(x.getState());
                base.setPoint(x.getPoint());
                base.setHaveScore(x.getHave_score());
                base.setRemark(x.getRemark());
                base.setCreatetime(x.getCreatetime());
                base.setDate(x.getDate());
                base.setType("项目开发");
                dataArr.add(base);
            });
        }
        return dataArr;
    }

    public List<ProjectBase> searchProductByConditions(Map<String, String> params) {

        List<Product> list = productService.searchProductByConditions(params.get("studentName"), params.get("state"), params.get("name"), params.get("pointFront"), params.get("pointBack"));

        List<ProjectBase> dataArr = new LinkedList<>();
        if(!CollectionUtils.isEmpty(list)){
            list.stream().forEach(x->{
                ProjectBase base = new ProjectBase();
                base.setId(x.getId());
                base.setStudentId(x.getStudentId());
                base.setStudentName(x.getStudent());
                base.setName(x.getName());
                base.setState(x.getState());
                base.setPoint(x.getPoint());
                base.setHaveScore(x.getHave_score());
                base.setRemark(x.getRemark());
                base.setCreatetime(x.getCreatetime());
                base.setDate(x.getDate());
                base.setType("撰写项目文档");
                dataArr.add(base);
            });
        }
        return dataArr;
    }

    public List<ProjectBase> searchStandardByConditions(Map<String, String> params) {

        List<Standard> list = standardService.searchStandardByConditions(params.get("studentName"), params.get("state"), params.get("name"), params.get("pointFront"), params.get("pointBack"));

        List<ProjectBase> dataArr = new LinkedList<>();
        if(!CollectionUtils.isEmpty(list)){
            list.stream().forEach(x->{
                ProjectBase base = new ProjectBase();
                base.setId(x.getId());
                base.setStudentId(x.getStudentId());
                base.setStudentName(x.getStudent());
                base.setName(x.getName());
                base.setState(x.getState());
                base.setPoint(x.getPoint());
                base.setHaveScore(x.getHave_score());
                base.setRemark(x.getRemark());
                base.setCreatetime(x.getCreatetime());
                base.setDate(x.getDate());
                base.setType(ProjectTypeEnums.STANDARD_DEVELOPMENT.getDisplayName());
                dataArr.add(base);
            });
        }
        return dataArr;
    }

    public List<ProjectBase> searchMonographByConditions(Map<String, String> params) {

        List<Monograph> list = monographService.searchMonographByConditions(params.get("studentName"), params.get("state"), params.get("name"), params.get("pointFront"), params.get("pointBack"));

        List<ProjectBase> dataArr = new LinkedList<>();
        if(!CollectionUtils.isEmpty(list)){
            list.stream().forEach(x->{
                ProjectBase base = new ProjectBase();
                base.setId(x.getId());
                base.setStudentId(x.getStudentId());
                base.setStudentName(x.getStudent());
                base.setName(x.getName());
                base.setState(x.getState());
                base.setPoint(x.getPoint());
                base.setHaveScore(x.getHave_score());
                base.setRemark(x.getRemark());
                base.setCreatetime(x.getCreatetime());
                base.setDate(x.getDate());
                base.setType(ProjectTypeEnums.ACADEMIC_MONOGRAPH_AND_TEXTBOOK.getDisplayName());
                dataArr.add(base);
            });
        }
        return dataArr;
    }

    public List<ProjectBase> searchCompetitionByConditions(Map<String, String> params) {

        List<Competition> list = competitionService.searchCompetitionByConditions(params.get("studentName"), params.get("state"), params.get("name"), params.get("pointFront"), params.get("pointBack"));

        List<ProjectBase> dataArr = new LinkedList<>();
        if(!CollectionUtils.isEmpty(list)){
            list.stream().forEach(x->{
                ProjectBase base = new ProjectBase();
                base.setId(x.getId());
                base.setStudentId(x.getStudentId());
                base.setStudentName(x.getStudent());
                base.setName(x.getName());
                base.setState(x.getState());
                base.setPoint(x.getPoint());
                base.setHaveScore(x.getHave_score());
                base.setRemark(x.getRemark());
                base.setDate(x.getDate());
                base.setCreatetime(x.getCreatetime());
                base.setType(ProjectTypeEnums.ACADEMIC_COMPETITION.getDisplayName());
                dataArr.add(base);
            });
        }
        return dataArr;
    }

    public List<ProjectBase> searchAwardByConditions(Map<String, String> params) {
        List<Award> list = awardService.searchAwardByConditions(params.get("studentName"), params.get("state"), params.get("name"), params.get("pointFront"), params.get("pointBack"));

        List<ProjectBase> dataArr = new LinkedList<>();
        if(!CollectionUtils.isEmpty(list)){
            list.stream().forEach(x->{
                ProjectBase base = new ProjectBase();
                base.setId(x.getId());
                base.setStudentId(x.getStudentId());
                base.setStudentName(x.getStudent());
                base.setName(x.getName());
                base.setState(x.getState());
                base.setPoint(x.getPoint());
                base.setHaveScore(x.getHave_score());
                base.setRemark(x.getRemark());
                base.setCreatetime(x.getCreatetime());
                base.setDate(x.getDate());
                base.setType(ProjectTypeEnums.RESEARCH_AWARD.getDisplayName());
                dataArr.add(base);
            });
        }
        return dataArr;
    }

    public List<ProjectBase> searchPatentByConditions(Map<String, String> params) {
        List<Patent> list = patentService.searchPatentByConditions(params.get("studentName"), params.get("state"), params.get("name"), params.get("pointFront"), params.get("pointBack"));

        List<ProjectBase> dataArr = new LinkedList<>();
        if(!CollectionUtils.isEmpty(list)){
            list.stream().forEach(x->{
                ProjectBase base = new ProjectBase();
                base.setId(x.getId());
                base.setStudentId(x.getStudentId());
                base.setStudentName(x.getStudent());
                base.setName(x.getName());
                base.setState(x.getState());
                base.setPoint(x.getPoint());
                base.setHaveScore(x.getHave_score());
                base.setRemark(x.getRemark());
                base.setCreatetime(x.getCreatetime());
                base.setDate(x.getDate());
                base.setType(ProjectTypeEnums.AUTHORIZED_PATENT.getDisplayName());
                dataArr.add(base);
            });
        }
        return dataArr;
    }

}
