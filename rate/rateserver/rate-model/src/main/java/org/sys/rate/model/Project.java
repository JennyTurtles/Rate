package org.sys.rate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class Project extends Production{
    @DateTimeFormat(pattern = "yyyy-MM")
    @JsonFormat(pattern = "yyyy-MM", timezone = "GMT+8")
    Date startDate;

    @DateTimeFormat(pattern = "yyyy-MM")
    @JsonFormat(pattern = "yyyy-MM", timezone = "GMT+8")
    Date endDate;

    private Integer projectTypeId;
    //在页面表格就需要显示老师给的备注 点击查看详情重新获取数据
    private String remark;
    ProjectType projectType;
    String projectTypeName;
}
