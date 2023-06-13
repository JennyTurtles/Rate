package org.sys.rate.model;

import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

/**
 * 论文成果对象 paper
 *
 * @author system
 * @date 2022-03-13
 */
@Data
public class Paper {
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Long ID;

    /**
     * 学号
     */
    private Long studentID;

    /**
     * 论文题目
     */
    private String name;

    /**
     * 出版年
     */
    private String year;

    /**
     * 出版月
     */
    private Long month;
    private String author;

    /**
     * 排名
     */
    private Long rank;

    /**
     * 总人数
     */
    private Long total;

    /**
     * 积分
     */
    private Long point;

    // 用于判断2积分论文是否生效
    private Integer have_score;

    public Integer getHave_score() {
        return have_score;
    }

    public void setHave_score(Integer have_score) {
        this.have_score = have_score;
    }

    /**
     * 刊物id
     */
    private Long publicationID;

    //    private String content;
    private Timestamp time;//提交时间

    //    private List<String> remark;
    private String remark;
    private String url;
    private String state;
    private String pubPage;
    private Publication publication;
    private List<Operation> paperoperList;
    private Student student;
    //    private String stuname;
    private String pubName;
    private Long pubid;

    public Paper() {
    }

}
