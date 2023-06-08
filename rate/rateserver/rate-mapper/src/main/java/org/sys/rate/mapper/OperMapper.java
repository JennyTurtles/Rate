package org.sys.rate.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.sys.rate.model.BooksOper;
import org.sys.rate.model.Oper;

import java.util.List;

@Mapper
public interface OperMapper {
    public int insertOper(Oper oper);
    public List<Oper> selectList(Integer ID);
}
