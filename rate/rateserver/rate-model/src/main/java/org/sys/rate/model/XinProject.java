package org.sys.rate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class XinProject {

    private Long id; // 主键ID

    private String name; // 论文题目

    private Integer point; // 积分

    private Timestamp date; // 操作时间

    private String sname; // 参与人

    private String state = "commit"; // 状态 默认值为 'commit'

    private String remark; // 备注

    private Integer mid; // 中间id

    private String type; // 类别
    private Integer pointtype; // 类别

    private Integer sid;//学生ID

    public XinProject(String name, Integer point,
                      String sname,
                      String state, String remark,
                      Integer mid, String type, Integer sid) {
        this.name = name;
        this.point = point;
        this.sname = sname;
        this.state = state != null ? state : "commit";
        this.remark = remark;
        this.mid = mid;
        this.type = type;
        this.sid = sid;
    }
}
