package org.sys.rate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParticipantsDisplay {
    private Integer ID;
    private String name;
    private String code;
    private Double score;
    private String groupName;
    private List<String> displayItemName; // 顺序放置展示项名称
    private Map<String,DisplayItem> map; // 展示项名称到展示项的映射
}
