package org.sys.rate.service.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.sys.rate.mapper.PatentMapper;
import org.sys.rate.model.Patent;

import java.util.List;

@Service
public class PatentService {
    @Autowired
    private PatentMapper patentMapper;

    /**
     * 查询著作
     *
     * @param ID 著作ID
     * @return 著作
     */
    public Patent selectPatentById(Long ID){
       return patentMapper.selectPatentById(ID);
    }

    /**
     * 查询著作列表
     *
     * @param patent 著作
     * @return 著作集合
     */
    public List<Patent> selectPatentList(Patent patent){
        return patentMapper.selectPatentList(patent);
    }

    /**
     * 新增著作
     *
     * @param patent 著作
     * @return 结果
     */
    public int insertPatent(Patent patent){
        return patentMapper.insertPatent(patent);
    }

    /**
     * 修改著作
     *
     * @param patent 著作
     * @return 结果
     */
    public int updatePatent(Patent patent){
        return patentMapper.updatePatent(patent);
    }

    /**
     * 删除著作
     *
     * @param ID 著作ID
     * @return 结果
     */
    public int deletePatentById(Long ID){
        return patentMapper.deletePatentById(ID);
    }

    public List<Patent> selectList(){
        return patentMapper.selectList();
    }
}
