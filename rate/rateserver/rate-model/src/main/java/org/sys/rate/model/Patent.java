package org.sys.rate.model;

import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
public class Patent extends Production{
    private String grantedStatus;
    //在页面表格就需要显示老师给的备注 点击查看详情重新获取数据
    private String remark;
}
