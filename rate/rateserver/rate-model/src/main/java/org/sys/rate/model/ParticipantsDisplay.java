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
    private List<String> displayItemName; // 顺序放置展示项的别名
//    private Map<String,String> nameMap; // 别名到真名的映射
    private Map<String,DisplayItem> map; // 真名到展示项的映射
}
