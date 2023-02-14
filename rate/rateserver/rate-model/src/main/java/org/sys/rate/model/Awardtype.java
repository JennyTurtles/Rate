package org.sys.rate.model;
/**
 * 类别对象 awardtype
 * 
 * @author system
 * @date 2022-03-13
 */
public class Awardtype
{

    /** ID */
    private Long ID;

    /** 奖项名称 */
    private String name;

    /** 指标点id */
    private Long indicaterID;

    /** 类别 */
    private String type;

    public void setID(Long ID)
    {
        this.ID = ID;
    }

    public Long getID()
    {
        return ID;
    }
    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }
    public void setIndicaterID(Long indicaterID)
    {
        this.indicaterID = indicaterID;
    }

    public Long getIndicaterID()
    {
        return indicaterID;
    }
    public void setType(String type)
    {
        this.type = type;
    }

    public String getType()
    {
        return type;
    }

}
