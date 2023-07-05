package org.sys.rate.model;

import lombok.Data;

@Data
public class Award extends Production{
    private String grantedStatus;
    private String awardLevel;
    private String awardClass;
    private Integer awardRank;
    private Long indicatorId;
    //在页面表格就需要显示老师给的备注 点击查看详情重新获取数据
    private String remark;
}