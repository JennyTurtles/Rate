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
    private int ID;
    /**
     * 学生学号
     */
    private int studentID;
    /**
     * 毕业论文或设计的标题
     */
    private String name;
    /**
     * 上传年份
     */
    private int year;
    /**
     * 上传月份
     */
    private int month;
    /**
     * 上传材料的地址
     */
    private String url;
    /**
     * 通过ID获取学生
     */
    private Student student;


}
