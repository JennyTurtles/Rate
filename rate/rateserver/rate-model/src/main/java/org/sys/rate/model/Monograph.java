package org.sys.rate.model;

import lombok.Data;

@Data
public class Monograph extends Production{
    private Long indicatorId;
    private String publisher;
    private String isbn;
    //在页面表格就需要显示老师给的备注 点击查看详情重新获取数据
    private String remark;
}
