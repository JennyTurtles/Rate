package org.sys.rate.model;

import java.util.ArrayList;
import java.util.List;

//与前端对应，每个节点包含所有子节点
public class TreeNode {
    private Integer id;
    private String label;
    private String type;
    private List<TreeNode> children;
    private String order;
    private Integer score;

    public TreeNode(Integer id, String label, String type,String order, Integer score) {
        this.id = id;
        this.label = label;
        this.type = type;
        this.children = new ArrayList<TreeNode>();
        this.order = order;
        this.score = score;
    }

    public Integer getId() {
        return id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public String getLabel() {
        return label;
    }

    public List<TreeNode> getChildren() {
        return children;
    }

    public String getOrder() {
        return order;
    }

    public Integer getScore() {
        return score;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setChildren(List<TreeNode> children) {
        this.children = children;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public void setScore(Integer score) {
        this.score = score;
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
                '}';
    }
}
