package org.sys.rate.controller.admin;

import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.*;
import org.sys.rate.mapper.*;
import org.sys.rate.model.Msg;
import org.sys.rate.model.Operation;
import org.sys.rate.model.RespBean;
import org.sys.rate.service.admin.OperationService;

import javax.annotation.Resource;
import javax.xml.crypto.Data;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

@RestController
@RequestMapping("/oper/basic")
public class OperationController {
    @Resource
    private OperationMapper operMapper;
    @Resource
    private OperationService operationService;
    @Resource
    private PatentMapper patentMapper;
    @Resource
    private PaperMapper paperMapper;
    @Resource
    private DecisionMapper decisionMapper;
    @Resource
    private StandardMapper standardMapper;
    @Resource
    private AwardMapper awardMapper;
    @Resource
    private CompetitionMapper competitionMapper;
    @Resource
    private MonographMapper monographMapper;
    @Resource
    private ProductMapper productMapper;
    @Resource
    private ProjectMapper projectMapper;

    @PostMapping("/add")
    public RespBean addOper(Operation oper) {
        try {
            oper.setTime((Timestamp) oper.getTime());
            operMapper.insertOper(oper);
        }catch (Exception e) {
            return RespBean.error("error", null);
        }
        return RespBean.ok("success", null);
    }

    @GetMapping("/List")
    public RespBean selectOperList(Integer prodId,String type) {
        List<Operation> operationList;
        try {
            operationList = operMapper.selectList(prodId, type);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            for (Operation operation : operationList) {
                if (operation.getTime() != null) {
                    String formattedTime = dateFormat.format(operation.getTime());
                    operation.setFormattedTime(formattedTime);
                }
            }
        }catch (Exception e) {
            return RespBean.error("error", null);
        }
        return RespBean.ok("success", operationList);
    }
    @PostMapping("/deleteOperationList")
    public RespBean deleteOperationList(@RequestBody Operation operation) {
        Integer res = operationService.deleteOperationList(operation);
        if(res > 0) return RespBean.ok("success", res);
        return RespBean.error("error", null);
    }

    @GetMapping("/getAllTypePendingMessageNumber") //获取所有类别的导师通过状态的成果
    public Msg getAllTypePendingMessage(String role) {
        Integer res = 0;
        String state = "";
        //分角色请求合并为一个
        if(role.equals("admin")) {
            state = "tea_pass";
        } else if(role.equals("teacher")) {
            state = "commit";
        }
        Integer patentRes = patentMapper.selectPatentNumberOfPendingMessing(state);
        Integer paperRes = paperMapper.selectPaperNumberOfPendingMessing(state);
        Integer projectRes = projectMapper.selectProjectNumberOfPendingMessing(state);
        Integer horizontalProjectRes = projectMapper.selectHorizontalProjectNumberOfPendingMessing(state);
        Integer productRes = productMapper.selectProductNumberOfPendingMessing(state);
        Integer monographRes = monographMapper.selectMonographNumberOfPendingMessing(state);
        Integer decisionRes = decisionMapper.selectDecisionNumberOfPendingMessing(state);
        Integer standardRes = standardMapper.selectStandardNumberOfPendingMessing(state);
        Integer awardRes = awardMapper.selectAwardNumberOfPendingMessing(state);
        Integer competitionRes = competitionMapper.selectCompetitionNumberOfPendingMessing(state);
        res = paperRes + patentRes + productRes + projectRes + monographRes + decisionRes + standardRes + awardRes + competitionRes + horizontalProjectRes;
        return Msg.success().add("count", res).add("patent", patentRes).add("patent", patentRes)
                .add("paper", paperRes).add("project", projectRes).add("product", productRes)
                .add("monograph", monographRes).add("decision", decisionRes).add("standard", standardRes)
                .add("award", awardRes).add("competition", competitionRes).add("horizontalProject", horizontalProjectRes);
    }

}
