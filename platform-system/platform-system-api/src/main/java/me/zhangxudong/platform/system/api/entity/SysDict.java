package me.zhangxudong.platform.system.api.entity;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * 字典Entity
 * Created by zhangxd on 15/10/20.
 */
public class SysDict extends SysEntity<SysDict> {

    private static final long serialVersionUID = 1L;
    private String value;    // 数据值
    private String label;    // 标签名
    private String type;    // 类型
    private String description;// 描述
    private Integer sort;    // 排序
    private String parentId;//父Id

    public SysDict() {
        super();
    }

    public SysDict(String id) {
        super(id);
    }

    public SysDict(String value, String label) {
        this.value = value;
        this.label = label;
    }

    @Length(min = 1, max = 100)
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Length(min = 1, max = 100)
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Length(min = 1, max = 100)
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Length(min = 0, max = 100)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @NotNull
    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    @Length(min = 1, max = 100)
    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    @Override
    public String toString() {
        return label;
    }
}