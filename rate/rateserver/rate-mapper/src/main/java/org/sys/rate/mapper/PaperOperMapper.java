package org.sys.rate.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.sys.rate.model.PaperOper;

import java.util.List;

/**
 * 论文成果Mapper接口
 * 
 * @author system
 * @date 2022-03-13
 */
@Mapper
public interface PaperOperMapper
{

    public int insertPaperoper(PaperOper paperoper);
    public List<PaperOper> selectList(Long ID);
}
