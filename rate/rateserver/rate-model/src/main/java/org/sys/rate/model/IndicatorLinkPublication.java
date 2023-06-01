package org.sys.rate.model;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @projectName: Rate
 * @package: org.sys.rate.model
 * @className: IndicatorLinkPublication
 * @author: ZYK
 * @description: TODO
 * @date: 2023/6/1 13:38
 * @version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IndicatorLinkPublication {

    Integer id;

    @TableField("publication_id")
    Integer publicationId;

    @TableField("indicator_id")
    Integer indicatorId;

    Integer year;

    Integer flag;
}
