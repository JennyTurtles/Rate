package org.sys.rate.model;


/**
 * 刊物对象 publication
 * 
 * @author system
 * @date 2022-03-13
 */
public class Publication
{
    private static final long serialVersionUID = 1L;

    /**  */
    private Long ID;

    /** 刊物全称 */
    private String name;

    /** 简称 */
    private String abbr;

    /** 出版社 */
    private String publisher;

    /** 网址 */
    private String url;

    /** 类型 */
    private String type;

    /** 级别 */
    private String level;

    private Integer score;
    private Integer year;

    /** 指标点id */
    private Long indicatorID;

    private Indicator indicator;
    private String indicatorName;
    public Integer getYear() {
        return year;
    }

    public String getIndicatorName() {
        return indicatorName;
    }

    public void setIndicatorName(String indicatorName) {
        this.indicatorName = indicatorName;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

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
    public void setAbbr(String abbr)
    {
        this.abbr = abbr;
    }

    public Indicator getIndicator()
    {
        return indicator;
    }
    public void setIndicator(Indicator indicator)
    {
        this.indicator = indicator;
    }

    public String getAbbr()
    {
        return abbr;
    }
    public void setPublisher(String publisher)
    {
        this.publisher = publisher;
    }

    public String getPublisher()
    {
        return publisher;
    }
    public void setUrl(String url)
    {
        this.url = url;
    }

    public String getUrl()
    {
        return url;
    }
    public void setType(String type)
    {
        this.type = type;
    }

    public String getType()
    {
        return type;
    }
    public void setLevel(String level)
    {
        this.level = level;
    }

    public String getLevel()
    {
        return level;
    }
    public void setIndicatorID(Long indicaterID)
    {
        this.indicatorID = indicaterID;
    }

    public Long getIndicatorID()
    {
        return indicatorID;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }
}
