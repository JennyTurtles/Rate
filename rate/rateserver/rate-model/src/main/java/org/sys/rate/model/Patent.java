package org.sys.rate.model;



/**
 * 著作对象 patent
 * 
 * @author system
 * @date 2022-03-13
 */
public class Patent
{
    private static final long serialVersionUID = 1L;

    /**  */
    private Long ID;

    /** 专利名称 */
    private String name;

    /** 类别 */
    private String type;

    /** 年 */
    private String year;

    /** 本人排名 */
    private Long rank;

    /** 总人数 */
    private Long total;

    /** 权力类别 */
    private String rights;

    /** 指标点id */
    private Long indicaterID;

    /** 月 */
    private String month;

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
    public void setType(String type)
    {
        this.type = type;
    }

    public String getType()
    {
        return type;
    }
    public void setYear(String year)
    {
        this.year = year;
    }

    public String getYear()
    {
        return year;
    }
    public void setRank(Long rank)
    {
        this.rank = rank;
    }

    public Long getRank()
    {
        return rank;
    }
    public void setTotal(Long total)
    {
        this.total = total;
    }

    public Long getTotal()
    {
        return total;
    }
    public void setRights(String rights)
    {
        this.rights = rights;
    }

    public String getRights()
    {
        return rights;
    }
    public void setIndicaterID(Long indicaterID)
    {
        this.indicaterID = indicaterID;
    }

    public Long getIndicaterID()
    {
        return indicaterID;
    }
    public void setMonth(String month)
    {
        this.month = month;
    }

    public String getMonth()
    {
        return month;
    }


}
