package  org.sys.rate.model;


/**
 * 著作类型对象 projectstype
 * 
 * @author system
 * @date 2022-03-13
 */
public class Projectstype
{

    /**  */
    private Long ID;

    /** 项目名称 */
    private String name;

    /** 指标点id */
    private Long indicatorId;


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

    public Long getIndicatorId() {
        return indicatorId;
    }

    public void setIndicatorId(Long indicatorId) {
        this.indicatorId = indicatorId;
    }
}
