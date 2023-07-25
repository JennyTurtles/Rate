package org.sys.rate.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.sys.rate.model.AwardType;
import org.sys.rate.model.ProjectType;

import java.util.List;

/**
 * 专利成果Mapper接口
 *
 * @author system
 * @date 2022-03-13
 */
@Mapper
public interface AwardTypeMapper {
    public List<AwardType> getIndicatorByYearAndType(String year, String type);

}

