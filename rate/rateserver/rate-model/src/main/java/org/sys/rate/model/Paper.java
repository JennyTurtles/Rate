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
     * 用于判断2积分论文是否生效
     */
    private Integer haveScore;

    private String pubPage;

    private Integer publicationId;

}
