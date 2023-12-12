package org.sys.rate.service.admin;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import org.sys.rate.mapper.MonographMapper;
import org.sys.rate.mapper.OperationMapper;
import org.sys.rate.model.Monograph;
import org.sys.rate.model.Operation;
import org.sys.rate.service.mail.MailToStuService;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class MonographService {
    @Resource
    private MonographMapper monographMapper;
    @Resource
    private OperationMapper operationMapper;
    @Resource
    private MailToStuService mailToStuService;

    public List<Monograph> selectMonographListById(@Param("studentID") Integer studentID) {
        List<Monograph> list = monographMapper.selectMonographListById(studentID);
        return list;
    }

    /**
     * 新增科研专著教材成果
     *
     * @param monograph 科研专著教材成果
     * @return 结果
     */
    public int insertMonograph(Monograph monograph) {
        return monographMapper.insertMonograph(monograph);
    }

    public int updateMonograph(Monograph monograph) {
        return monographMapper.updateMonograph(monograph);
    }

    /**
     * 删除科研专著教材成果
     *
     * @param ID 科研专著教材成果ID
     * @return 结果
     */
    public int deleteMonographById(Long ID) {
        return monographMapper.deleteMonographById(ID);
    }

    public List<Monograph> selectAllMonographList() {
        List<Monograph> list = monographMapper.selectAllMonographList();
        return list;
    }

    //    修改科研专著教材状态
    public int editState(String state, Long ID) throws MessagingException {
        Monograph monograph = monographMapper.getById(Math.toIntExact(ID));
        mailToStuService.sendStuMail(state, monograph, null, "学术专著和教材");
        return monographMapper.editState(state, ID);
    }

    public List<Monograph> searchMonographByConditions(String studentName, String state, String monoName, String pointFront, String pointBack) {
        List<Monograph> list = monographMapper.searchMonographByConditions(studentName, state, monoName, pointFront, pointBack);
        return list;
    }
}
