package org.sys.rate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class Competition extends Production{
    private Integer competitionTypeId;
    private String competitionLevel;
    private Integer indicatorId;
    //在页面表格就需要显示老师给的备注 点击查看详情重新获取数据
    private String remark;
    private CompetitionType competitionType;
    private String competitionTypeName;
}
