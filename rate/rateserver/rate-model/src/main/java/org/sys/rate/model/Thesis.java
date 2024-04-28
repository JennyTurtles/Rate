package org.sys.rate.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 本科生毕业设计或论文，一个学生可以有多个毕业设计或者论文
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Thesis {
    private int comment_pass;
    private int comment_deny;
    private int comment_total;

    /**
     * 毕业论文或设计的ID
     */
    private Integer ID;
    /**
     * 学生学号
     */
    private Integer studentID;
    /**
     * 毕业论文或设计的标题
     */
    private String name;
    /**
     * 上传年份
     */
    private Integer year;
    /**
     * 上传月份，是否可以将上传月份和春季和秋季对应起来，规定一下month=3为春季，month=4为秋季，要不然要改好多
     */
    private Integer month;
    /**
     * 上传材料的地址
     */
    private String url;
    /**
     * 通过ID获取学生
     */
    private Student student;

    private Integer tutorID;

    private String tutorNumber;
    private String studentNumber;

    private Double grade;
    private Integer startThesisID;
    private Integer fillMiss;

}
