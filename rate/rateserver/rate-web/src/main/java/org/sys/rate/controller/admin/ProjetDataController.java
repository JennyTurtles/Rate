package org.sys.rate.controller.admin;

import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.sys.rate.config.JsonResult;
import org.sys.rate.mapper.OperationMapper;
import org.sys.rate.model.*;
import org.sys.rate.service.admin.*;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/project/data/basic")
public class ProjetDataController {

    @Resource
    private OperationMapper operMapper;
    @Resource
    private PaperService paperService;
    @Resource
    private PatentService patentService;
    @Resource
    private AwardService awardService;
    @Resource
    private MonographService monographService;
    @Resource
    private ProjectService projectService;
    @Resource
    private CompetitionService competitionService;
    @Resource
    private DecisionService decisionService;
    @Resource
    private ProductService productService;
    @Resource
    private StandardService standardService;
    @Resource
    private XinProjectService xinProjectService;

    @GetMapping("/studentID")//无页码要求
    public JsonResult<List> getXinPorjectList(Integer studentID) {
        List<ProjectData> list = new ArrayList<>();
        List<XinProject> xinProjects = xinProjectService.selectListByIds(studentID);
        if (xinProjects != null && !xinProjects.isEmpty()) {

            xinProjects.forEach(x -> {
                ProjectData projectData = new ProjectData();
                projectData.setId(x.getMid());
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String formattedDate = sdf.format(x.getDate());
                projectData.setApplyTime(formattedDate);
                projectData.setName(x.getName());
                projectData.setCategory(x.getType());
                projectData.setParticipants(x.getSname());
                projectData.setStatus(x.getState());
                projectData.setPointtype(x.getPointtype());
                projectData.setRemark(x.getRemark());
                projectData.setPoint(Long.parseLong(x.getPoint() + ""));

                list.add(projectData);
            });
        }
        return new JsonResult<>(list);
    }

    @GetMapping("/updatePointType")//无页码要求
    public JsonResult<String> updatePointType(Integer mid,String type,Integer pointtype) {
        int result = xinProjectService.updatePointType(mid,type,pointtype);
        if (result ==1){
            throw  new RuntimeException("数据不存在");
        }
        return new JsonResult<>("修改积分成功");
    }


    @PostMapping("/teacherOrAdmin")//无页码要求
    public JsonResult<List> teacherOrAdmin(@RequestBody XinProjectVo xinProjectVo) {
        List<ProjectData> list = new ArrayList<>();
        List<XinProject> xinProjects = xinProjectService.selectList(xinProjectVo);

        String pointFrontStr = xinProjectVo.getPointFront();
        String pointBackStr = xinProjectVo.getPointBack();

        Integer pointFront =( pointFrontStr != null && !"".equals(pointFrontStr) )? Integer.parseInt(pointFrontStr) : null;
        Integer pointBack =( pointBackStr != null && !"".equals(pointBackStr) )? Integer.parseInt(pointBackStr) : null;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (xinProjects != null && !xinProjects.isEmpty()) {
            for (XinProject x : xinProjects) {
                Integer point = x.getPoint();
                String state = x.getState();

                // 情况 1: pointFront 和 pointBack 都有值
                if (pointFront != null && pointBack != null) {
//                    if (("adm_pass".equals(state)) && point != null && point >= pointFront && point <= pointBack) {
                    if ( point != null && point >= pointFront && point <= pointBack) {
                        ProjectData projectData = new ProjectData();
                        projectData.setId(x.getMid());
                        String formattedDate = sdf.format(x.getDate());
                        projectData.setApplyTime(formattedDate);
                        projectData.setName(x.getName());
                        projectData.setCategory(x.getType());
                        projectData.setParticipants(x.getSname());
                        projectData.setStatus(x.getState());
                        projectData.setRemark(x.getRemark());
                        projectData.setPointtype(x.getPointtype());
                        projectData.setPoint(Long.parseLong(x.getPoint() + ""));
                        list.add(projectData);
                    }
                }// 情况 2: pointFront 有值，pointBack 没有值
                else if (pointFront != null && pointBack == null) {
//                    if (("adm_pass".equals(state) ) && point != null && point >= pointFront) {
                    if (point != null && point >= pointFront) {
                        ProjectData projectData = new ProjectData();
                        projectData.setId(x.getMid());
                        String formattedDate = sdf.format(x.getDate());
                        projectData.setApplyTime(formattedDate);
                        projectData.setName(x.getName());
                        projectData.setCategory(x.getType());
                        projectData.setParticipants(x.getSname());
                        projectData.setStatus(x.getState());
                        projectData.setRemark(x.getRemark());
                        projectData.setPointtype(x.getPointtype());
                        projectData.setPoint(Long.parseLong(x.getPoint() + ""));
                        list.add(projectData);
                    }
                } // 情况 3: pointFront 没有值，pointBack 有值
                else if (pointFront == null && pointBack != null) {
//                    if (("adm_pass".equals(state) ) && point != null && point <= pointBack) {
                    if ( point != null && point <= pointBack) {
                        ProjectData projectData = new ProjectData();
                        projectData.setId(x.getMid());
                        String formattedDate = sdf.format(x.getDate());
                        projectData.setApplyTime(formattedDate);
                        projectData.setName(x.getName());
                        projectData.setCategory(x.getType());
                        projectData.setParticipants(x.getSname());
                        projectData.setStatus(x.getState());
                        projectData.setRemark(x.getRemark());
                        projectData.setPointtype(x.getPointtype());
                        projectData.setPoint(Long.parseLong(x.getPoint() + ""));
                        list.add(projectData);
                    }
                } // 情况 4: pointFront 和 pointBack 都没有值，但 state 是通过
                else if (pointFront == null && pointBack == null) {
                    ProjectData projectData = new ProjectData();
                    projectData.setId(x.getMid());
                    String formattedDate = sdf.format(x.getDate());
                    projectData.setApplyTime(formattedDate);
                    projectData.setName(x.getName());
                    projectData.setCategory(x.getType());
                    projectData.setParticipants(x.getSname());
                    projectData.setStatus(x.getState());
                    projectData.setRemark(x.getRemark());
                    projectData.setPointtype(x.getPointtype());
                    projectData.setPoint(Long.parseLong(x.getPoint() + ""));
                    list.add(projectData);
                }
            }

        }


//        if(xinProjects != null && !xinProjects.isEmpty()){
//
//            xinProjects.forEach(x -> {
//                ProjectData projectData = new ProjectData();
//                projectData.setId(x.getMid());
////                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                String formattedDate = sdf.format(x.getDate());
//                projectData.setApplyTime(formattedDate);
//                projectData.setName(x.getName());
//                projectData.setCategory(x.getType());
//                projectData.setParticipants(x.getSname());
//                projectData.setStatus(x.getState());
//                projectData.setRemark(x.getRemark());
//                projectData.setPoint(Long.parseLong(x.getPoint()+""));
//
//                list.add(projectData);
//            });
//        }
        return new JsonResult<>(list);
    }


    @GetMapping("/studentID_old")//无页码要求
    public JsonResult<List> getById(Integer studentID) {
        List<ProjectData> list = new ArrayList<>();

        // 学术论文
        List<Paper> paperList = paperService.selectListByIds(studentID);
        list.addAll(paperToProjectDataList(paperList));

        // 获奖专利
        List<Patent> patentList = patentService.selectListByIds(studentID);
        list.addAll(patentToProjectDataList(patentList));

        // 科研获奖
        List<Award> awardList = awardService.selectAwardListById(studentID);
        list.addAll(awardToProjectDataList(awardList));

        // 学术专著
        List<Monograph> monographList = monographService.selectMonographListById(studentID);
        list.addAll(monographToProjectDataList(monographList));

        // 纵向科研
        List<Project> projectList = projectService.selectProjectListById(studentID);
        list.addAll(projectToProjectDataList(projectList));

        // 横向科研
        List<Project> projectList2 = projectService.selectHorizontalProjectListById(studentID);
        list.addAll(projectToProjectDataList2(projectList2));

        // 学科竞赛
        List<Competition> competitionList = competitionService.selectCompetitionListById(studentID);
        list.addAll(competitionToProjectDataList(competitionList));

        // 决策咨询
        List<Decision> decisionList = decisionService.selectDecisionListById(studentID);
        list.addAll(decisionToProjectDataList(decisionList));

        // 产品应用
        List<Product> productList = productService.selectListByIds(studentID);
        list.addAll(productToProjectDataList(productList));


        // 制定标准
        List<Standard> standardList = standardService.selectListByIds(studentID);
        list.addAll(standardToProjectDataList(standardList));

        return new JsonResult<>(list);
    }


    private List<ProjectData> paperToProjectDataList(List<Paper> paramsList) {
        if (CollectionUtils.isEmpty(paramsList)) {
            return Collections.EMPTY_LIST;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<ProjectData> list = new ArrayList<>();
        for (Paper paper : paramsList) {
            Operation operation = operMapper.selectMinDataList(paper.getID().intValue(), "学术论文");
            List<Operation> operationList = operMapper.selectList(paper.getID().intValue(), "学术论文");

            ProjectData projectData = new ProjectData();
            projectData.setId(paper.getID().intValue());
            projectData.setApplyTime(dateFormat.format(operation.getTime()));
            projectData.setName(paper.getName());
            projectData.setCategory("学术论文");
            projectData.setParticipants(paper.getAuthor());
            projectData.setStatus(paper.getState());
            projectData.setRemark(CollectionUtils.isEmpty(operationList) ? "" : operationList.get(0).getRemark());
            projectData.setPoint(paper.getPoint());
            list.add(projectData);
        }

        return list;
    }

    private List<ProjectData> patentToProjectDataList(List<Patent> paramsList) {
        if (CollectionUtils.isEmpty(paramsList)) {
            return Collections.EMPTY_LIST;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<ProjectData> list = new ArrayList<>();
        for (Patent patent : paramsList) {
            Operation operation = operMapper.selectMinDataList(patent.getId().intValue(), "授权专利");
            List<Operation> operationList = operMapper.selectList(patent.getId().intValue(), "授权专利");

            ProjectData projectData = new ProjectData();
            projectData.setId(patent.getId().intValue());
            projectData.setApplyTime(dateFormat.format(operation.getTime()));
            projectData.setName(patent.getName());
            projectData.setCategory("授权专利");
            projectData.setParticipants(patent.getAuthor());
            projectData.setStatus(patent.getState());
            projectData.setRemark(CollectionUtils.isEmpty(operationList) ? "" : operationList.get(0).getRemark());
            projectData.setPoint(Long.valueOf(patent.getPoint()));
            list.add(projectData);
        }

        return list;
    }

    private List<ProjectData> awardToProjectDataList(List<Award> paramsList) {
        if (CollectionUtils.isEmpty(paramsList)) {
            return Collections.EMPTY_LIST;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<ProjectData> list = new ArrayList<>();
        for (Award award : paramsList) {
            Operation operation = operMapper.selectMinDataList(award.getId(), "科研获奖");
            List<Operation> operationList = operMapper.selectList(award.getId().intValue(), "科研获奖");

            ProjectData projectData = new ProjectData();
            projectData.setId(award.getId());
            projectData.setApplyTime(dateFormat.format(operation.getTime()));
            projectData.setName(award.getName());
            projectData.setCategory("科研获奖");
            projectData.setParticipants(award.getAuthor());
            projectData.setStatus(award.getState());
            projectData.setRemark(CollectionUtils.isEmpty(operationList) ? "" : operationList.get(0).getRemark());
            projectData.setPoint(Long.valueOf(award.getPoint()));
            list.add(projectData);
        }

        return list;
    }


    private List<ProjectData> monographToProjectDataList(List<Monograph> paramsList) {
        if (CollectionUtils.isEmpty(paramsList)) {
            return Collections.EMPTY_LIST;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<ProjectData> list = new ArrayList<>();
        for (Monograph monograph : paramsList) {
            Operation operation = operMapper.selectMinDataList(monograph.getId(), "学术专著和教材");
            List<Operation> operationList = operMapper.selectList(monograph.getId(), "学术专著和教材");

            ProjectData projectData = new ProjectData();
            projectData.setId(monograph.getId());
            projectData.setApplyTime(dateFormat.format(operation.getTime()));
            projectData.setName(monograph.getName());
            projectData.setCategory("学术专著和教材");
            projectData.setParticipants(monograph.getAuthor());
            projectData.setStatus(monograph.getState());
            projectData.setRemark(CollectionUtils.isEmpty(operationList) ? "" : operationList.get(0).getRemark());
            projectData.setPoint(Long.valueOf(monograph.getPoint()));
            list.add(projectData);
        }

        return list;
    }


    private List<ProjectData> projectToProjectDataList(List<Project> paramsList) {
        if (CollectionUtils.isEmpty(paramsList)) {
            return Collections.EMPTY_LIST;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<ProjectData> list = new ArrayList<>();
        for (Project project : paramsList) {
            Operation operation = operMapper.selectMinDataList(project.getId(), "纵向科研项目");
            List<Operation> operationList = operMapper.selectList(project.getId(), "纵向科研项目");

            ProjectData projectData = new ProjectData();
            projectData.setId(project.getId());
            projectData.setApplyTime(dateFormat.format(operation.getTime()));
            projectData.setName(project.getName());
            projectData.setCategory("纵向科研项目");
            projectData.setParticipants(project.getAuthor());
            projectData.setStatus(project.getState());
            projectData.setRemark(CollectionUtils.isEmpty(operationList) ? "" : operationList.get(0).getRemark());
            projectData.setPoint(Long.valueOf(project.getPoint()));
            list.add(projectData);
        }

        return list;
    }

    private List<ProjectData> projectToProjectDataList2(List<Project> paramsList) {
        if (CollectionUtils.isEmpty(paramsList)) {
            return Collections.EMPTY_LIST;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<ProjectData> list = new ArrayList<>();
        for (Project project : paramsList) {
            Operation operation = operMapper.selectMinDataList(project.getId(), "横向科研项目");
            List<Operation> operationList = operMapper.selectList(project.getId(), "横向科研项目");

            ProjectData projectData = new ProjectData();
            projectData.setId(project.getId());
            projectData.setApplyTime(dateFormat.format(operation.getTime()));
            projectData.setName(project.getName());
            projectData.setCategory("横向科研项目");
            projectData.setParticipants(project.getAuthor());
            projectData.setStatus(project.getState());
            projectData.setRemark(CollectionUtils.isEmpty(operationList) ? "" : operationList.get(0).getRemark());
            projectData.setPoint(Long.valueOf(project.getPoint()));
            list.add(projectData);
        }

        return list;
    }

    private List<ProjectData> competitionToProjectDataList(List<Competition> paramsList) {
        if (CollectionUtils.isEmpty(paramsList)) {
            return Collections.EMPTY_LIST;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<ProjectData> list = new ArrayList<>();
        for (Competition competition : paramsList) {
            Operation operation = operMapper.selectMinDataList(competition.getId(), "学科竞赛");
            List<Operation> operationList = operMapper.selectList(competition.getId(), "学科竞赛");

            ProjectData projectData = new ProjectData();
            projectData.setId(competition.getId());
            projectData.setApplyTime(dateFormat.format(operation.getTime()));
            projectData.setName(competition.getName());
            projectData.setCategory("学科竞赛");
            projectData.setParticipants(competition.getAuthor());
            projectData.setStatus(competition.getState());
            projectData.setRemark(CollectionUtils.isEmpty(operationList) ? "" : operationList.get(0).getRemark());
            projectData.setPoint(Long.valueOf(competition.getPoint()));
            list.add(projectData);
        }

        return list;
    }


    private List<ProjectData> decisionToProjectDataList(List<Decision> paramsList) {
        if (CollectionUtils.isEmpty(paramsList)) {
            return Collections.EMPTY_LIST;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<ProjectData> list = new ArrayList<>();
        for (Decision decision : paramsList) {
            Operation operation = operMapper.selectMinDataList(decision.getId(), "决策咨询");
            List<Operation> operationList = operMapper.selectList(decision.getId(), "决策咨询");

            ProjectData projectData = new ProjectData();
            projectData.setId(decision.getId());
            if (operation != null) {
                projectData.setApplyTime(dateFormat.format(operation.getTime()));
            }
            projectData.setName(decision.getName());
            projectData.setCategory("决策咨询");
            projectData.setParticipants(decision.getAuthor());
            projectData.setStatus(decision.getState());
            projectData.setRemark(CollectionUtils.isEmpty(operationList) ? "" : operationList.get(0).getRemark());
            projectData.setPoint(Long.valueOf(decision.getPoint()));
            list.add(projectData);
        }

        return list;
    }

    private List<ProjectData> productToProjectDataList(List<Product> paramsList) {
        if (CollectionUtils.isEmpty(paramsList)) {
            return Collections.EMPTY_LIST;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<ProjectData> list = new ArrayList<>();
        for (Product product : paramsList) {
            Operation operation = operMapper.selectMinDataList(product.getId(), "产品应用");
            List<Operation> operationList = operMapper.selectList(product.getId(), "产品应用");

            ProjectData projectData = new ProjectData();
            projectData.setId(product.getId());
            projectData.setApplyTime(dateFormat.format(operation.getTime()));
            projectData.setName(product.getName());
            projectData.setCategory("产品应用");
            projectData.setParticipants(product.getAuthor());
            projectData.setStatus(product.getState());
            projectData.setRemark(CollectionUtils.isEmpty(operationList) ? "" : operationList.get(0).getRemark());
            projectData.setPoint(Long.valueOf(product.getPoint()));
//            projectData.setUrl(product.getUrl());
            list.add(projectData);
        }

        return list;
    }

    private List<ProjectData> standardToProjectDataList(List<Standard> paramsList) {
        if (CollectionUtils.isEmpty(paramsList)) {
            return Collections.EMPTY_LIST;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<ProjectData> list = new ArrayList<>();
        for (Standard standard : paramsList) {
            Operation operation = operMapper.selectMinDataList(standard.getId(), "制定标准");
            List<Operation> operationList = operMapper.selectList(standard.getId(), "制定标准");

            ProjectData projectData = new ProjectData();
            projectData.setId(standard.getId());
            projectData.setApplyTime(dateFormat.format(operation.getTime()));
            projectData.setName(standard.getName());
            projectData.setCategory("制定标准");
            projectData.setParticipants(standard.getAuthor());
            projectData.setStatus(standard.getState());
            projectData.setRemark(CollectionUtils.isEmpty(operationList) ? "" : operationList.get(0).getRemark());
            projectData.setPoint(Long.valueOf(standard.getPoint()));
            list.add(projectData);
        }

        return list;
    }

}
