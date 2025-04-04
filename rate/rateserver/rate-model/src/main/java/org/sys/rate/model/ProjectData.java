package org.sys.rate.model;

import lombok.Data;

@Data
public class ProjectData {
    private Integer id;
    // 申报时间
    private String applyTime;
    // 名称
    private String name;
    // 类别
    private String category;
    // 参与人
    private String participants;
    // 状态
    private String status;
    // 备注
    private String remark;
    // 积分
    private Long point;
    //图片url
    private String url ;

    private Integer pointtype ;



}
