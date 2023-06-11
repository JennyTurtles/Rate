package org.sys.rate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Paper extends Production {
    /**
     *
     * 用于判断2积分论文是否生效，其实已经没有用了，因为只需要判断系统中该生是否有已通过的2分论文，若有，则不更新学生的分数，若无，则更新学生的分数。
     */
    private Integer haveScore;

    private String pubPage;

    private Integer publicationId;

}
