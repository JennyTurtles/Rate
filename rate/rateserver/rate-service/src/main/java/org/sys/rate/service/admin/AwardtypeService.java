package org.sys.rate.service.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.sys.rate.mapper.AwardtypeMapper;
import org.sys.rate.model.Awardtype;

import java.util.List;

@Service
public class AwardtypeService {

    @Autowired
    private AwardtypeMapper awardtypeMapper;

    public Awardtype selectAwardtypeById(Long ID){
        return awardtypeMapper.selectAwardtypeById(ID);
    }

    /**
     * 查询类别列表
     *
     * @param awardtype 类别
     * @return 类别集合
     */
    public List<Awardtype> selectAwardtypeList(Awardtype awardtype){
        return awardtypeMapper.selectAwardtypeList(awardtype);
    }

    /**
     * 新增类别
     *
     * @param awardtype 类别
     * @return 结果
     */
    public int insertAwardtype(Awardtype awardtype){
        return awardtypeMapper.insertAwardtype(awardtype);
    }

    /**
     * 修改类别
     *
     * @param awardtype 类别
     * @return 结果
     */
    public int updateAwardtype(Awardtype awardtype){
        return awardtypeMapper.insertAwardtype(awardtype);
    }

    /**
     * 删除类别
     *
     * @param ID 类别ID
     * @return 结果
     */
    public int deleteAwardtypeById(Long ID){
        return awardtypeMapper.deleteAwardtypeById(ID);
    }

    public List<Awardtype> selectList(){
        return awardtypeMapper.selectList();
    }
}
