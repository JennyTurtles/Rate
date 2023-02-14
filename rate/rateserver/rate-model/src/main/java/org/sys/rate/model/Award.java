package org.sys.rate.model;


/**
 * 回答对象 award
 * 
 * @author system
 * @date 2022-03-13
 */
public class Award
{
    private static final long serialVersionUID = 1L;

    /**  */
    private Long ID;

    /** 成果名称 */
    private String name;

    /** 奖项名称 */
    private String awardname;

    /** 获奖年度 */
    private Long year;

    /** 所有完成人 */
    private String member;

    /** 所属单位 */
    private String unit;

    /** 人数 */
    private Long total;

    /** 排名 */
    private Long rank;

    /** 奖项id */
    private Long awardID;

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
    public void setAwardname(String awardname)
    {
        this.awardname = awardname;
    }

    public String getAwardname()
    {
        return awardname;
    }
    public void setYear(Long year)
    {
        this.year = year;
    }

    public Long getYear()
    {
        return year;
    }
    public void setMember(String member)
    {
        this.member = member;
    }

    public String getMember()
    {
        return member;
    }
    public void setUnit(String unit)
    {
        this.unit = unit;
    }

    public String getUnit()
    {
        return unit;
    }
    public void setTotal(Long total)
    {
        this.total = total;
    }

    public Long getTotal()
    {
        return total;
    }
    public void setRank(Long rank)
    {
        this.rank = rank;
    }

    public Long getRank()
    {
        return rank;
    }
    public void setAwardID(Long awardID)
    {
        this.awardID = awardID;
    }

    public Long getAwardID()
    {
        return awardID;
    }


}
