package org.sys.rate.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.sys.rate.model.XinProject;

import java.util.List;

/**
 * xin_project Mapper接口
 *
 * @author system
 * @date 2022-03-13
 */
@Mapper
public interface XinProjectMapper
{

    public XinProject selectXinProject(XinProject xinProject);

    public int insertXinProject(XinProject xinProject);

    public int updateXinProject(XinProject xinProject);

    public int deleteXinProject(Integer mid, String type);


}
