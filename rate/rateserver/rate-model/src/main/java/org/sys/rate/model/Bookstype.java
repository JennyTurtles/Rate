package org.sys.rate.model;
/**
 * 类别对象 awardtype
 *
 * @author system
 * @date 2022-03-13
 */
public class Bookstype
{

    /** ID */
    private Long ID;

    /** 奖项名称 */
    private String name;

    /** 指标点id */
    private Long indicaterID;

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getIndicaterID() {
        return indicaterID;
    }

    public void setIndicaterID(Long indicaterID) {
        this.indicaterID = indicaterID;
    }
}
