package org.sys.rate.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.sys.rate.model.Operation;

import java.util.List;

@Mapper
public interface OperationMapper {

    /**
     * 插入历史操作
     * @param oper:
     * @Return int
     */
    Integer insertOper(Operation oper);

    /**
     * 按照成果类型和成果id来查找对应的历史操作
     * @param prodId:
     * @param prodType:
     * @Return List<Operation>
     */
    List<Operation> selectList(Integer prodId, String prodType, Integer operatorId);

//    List<Operation> selectOperationListOf(Integer prodId, String prodType, Integer operatorId);

}
