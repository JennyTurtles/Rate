package org.sys.rate.model;



/**
 * 著作对象 book
 * 
 * @author system
 * @date 2022-03-13
 */
public class Books
{
    private static final long serialVersionUID = 1L;

    /**  */
    private Long ID;

    /** 著作名称 */
    private String name;

    /**  */
    private String unit;

    /** 作者 */
    private String member;

    /** 排名 */
    private String rank;

    /** 版次 */
    private String version;

    /** 出版年 */
    private String year;

    /** 出版月 */
    private String month;

    /**  */
    private String time;

    /** 著作还是教材 */
    private String type;

    /** 出版社 */
    private String place;

    /** 指标点id */
    private Long indicaterID;

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
    public void setUnit(String unit)
    {
        this.unit = unit;
    }

    public String getUnit()
    {
        return unit;
    }
    public void setMember(String member)
    {
        this.member = member;
    }

    public String getMember()
    {
        return member;
    }
    public void setRank(String rank)
    {
        this.rank = rank;
    }

    public String getRank()
    {
        return rank;
    }
    public void setVersion(String version)
    {
        this.version = version;
    }

    public String getVersion()
    {
        return version;
    }
    public void setYear(String year)
    {
        this.year = year;
    }

    public String getYear()
    {
        return year;
    }
    public void setMonth(String month)
    {
        this.month = month;
    }

    public String getMonth()
    {
        return month;
    }
    public void setTime(String time)
    {
        this.time = time;
    }

    public String getTime()
    {
        return time;
    }
    public void setType(String type)
    {
        this.type = type;
    }

    public String getType()
    {
        return type;
    }
    public void setPlace(String place)
    {
        this.place = place;
    }

    public String getPlace()
    {
        return place;
    }
    public void setIndicaterID(Long indicaterID)
    {
        this.indicaterID = indicaterID;
    }

    public Long getIndicaterID()
    {
        return indicaterID;
    }


}
