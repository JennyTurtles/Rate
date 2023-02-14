package org.sys.rate.model;

import lombok.Data;

//评分项表
@Data
public class InfoItemValue {
    private Integer ID;

    private String name;

    private String content;

    public Integer getId() {
        return ID;
    }

    public void setId(Integer ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
