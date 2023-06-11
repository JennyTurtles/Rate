package org.sys.rate.service.admin;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import org.sys.rate.mapper.PaperMapper;
import org.sys.rate.model.Paper;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import java.util.Collections;
import java.util.List;

@Service
public class PaperService {

    @Resource
    private PaperMapper paperMapper;

    /**
     * 通过id寻找paper信息
     *
     * @param id
     * @return paper
     */
    public Paper getById(Integer id) {
        Paper paper = paperMapper.getById(id);
        return paper != null ? paper : null;
    }

    /**
     * 分页返回某学生的paper
     *
     * @param studentID:
     * @param page:
     * @param size:
     * @Return List<Paper>
     */
    public List<Paper> selectListByIdWithPaging(@Param("studentID") Integer studentID, @Param("page") Integer page, @Param("size") Integer size) {
        if (page != null && size != null) {
            page = (page - 1) * size;
        }
        List<Paper> paperList = paperMapper.selectListByIdWithPaging(studentID, page, size);
        return paperList;
    }

    /**
     * 返回某学生的paper
     * @param studentID:
     * @Return List<Paper>
     */
    public List<Paper> selectListById(@Param("studentID") Integer studentID) {
        return paperMapper.selectListById(studentID);
    }

    /**
     * 新增论文成果
     *
     * @param paper 论文成果
     * @return 结果
     */
    public Integer insertPaper(Paper paper) {
        return paperMapper.insertPaper(paper);
    }

    /**
     * 修改论文成果
     *
     * @param paper 论文成果
     * @return 结果
     */
    public Integer updatePaper(Paper paper) {
        return paperMapper.updatePaper(paper);
    }

    /**
     * 删除论文成果
     *
     * @param id 论文成果id
     * @return 结果
     */
    public Integer deletePaperById(Integer id) {
        return paperMapper.deletePaperByIds(Collections.singletonList(id));
    }


    /**
     * 修改论文状态
     * @param state:
     * @param ID:
     * @Return int
     */
    public Integer editState(String state, Integer id) throws MessagingException {
        Paper paper = paperMapper.getById(id);
        //mailToStuService.sendStuMail(state, paper, "论文");
        // 管理员通过的时候需要处理2分论文的情况，还要计算student的活动总分
        if ("adm_pass".equals(state)) {
            Integer stuID = paper.getStudentId();
            // TODO:这里获取分数应该是写错了，需要重写这个方法，因为paper表本身是不存储这个分数的
            Integer score = paper.getPoint();
            // 2分的时候检查是否已经发表过2分的论文
            // 已经发表过2分论文,将该论文的have_score设置为0
            // 若这2分论文没有发表过，将该论文的have_score设置为1，可是判断没有必要
            if (score == 2) {
                if (paperMapper.checkHaveScore(stuID) != null) {
                    return paperMapper.editState2(state, id, 0);
                } else {
                    paperMapper.editState2(state, id, 1);
                    return paperMapper.updateScore(stuID, score);
                }
            }
            paperMapper.updateScore(stuID, score);
        }
        return paperMapper.editState(state, id);
    }
}
