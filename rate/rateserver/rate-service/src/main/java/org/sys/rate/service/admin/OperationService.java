package org.sys.rate.service.admin;

import org.springframework.stereotype.Service;
import org.sys.rate.mapper.OperationMapper;
import org.sys.rate.model.Operation;

import java.util.List;

@Service
public class OperationService {

    private OperationMapper operationMapper;

    public OperationService(OperationMapper operationMapper) {
        this.operationMapper = operationMapper;
    }

    public Integer insertOper(Operation oper){
        return operationMapper.insertOper(oper);
    }

    public List<Operation> selectOper(Integer prodId, String operType){
        return operationMapper.selectList(prodId, operType);
    }

}
