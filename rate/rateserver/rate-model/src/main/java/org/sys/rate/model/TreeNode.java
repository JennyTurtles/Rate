package org.sys.rate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

//与前端对应，每个节点包含所有子节点
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TreeNode {
    private Integer id;
    private String label;
    private String type;
    private List<TreeNode> children;
    private String order;
    private Integer score;
    private Integer rankN;
    private String level;

    public TreeNode(Integer id, String label, String type,String order, Integer score, Integer rankN,String level) {
        this.id = id;
        this.label = label;
        this.type = type;
        this.children = new ArrayList<TreeNode>();
        this.order = order;
        this.score = score;
        this.rankN = rankN;
        this.level = level;
    }

    @Override
    public String toString() {
        return "TreeNode{" +
                "id=" + id +
                ", label='" + label + '\'' +
                ", type='" + type + '\'' +
                ", children=" + children +
                ", order='" + order + '\'' +
                ", score=" + score +
                ", rankN='" + rankN +
                '}';
    }
}
