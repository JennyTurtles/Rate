package org.sys.rate.mapper;

import org.apache.ibatis.annotations.Select;

public interface SecretaryMapper {
    @Select("SELECT COUNT(*) from expertactivities WHERE teacherID = #{Id} AND is_secretary = 1 Limit 1")
    int isSecretary(Integer Id);
}
