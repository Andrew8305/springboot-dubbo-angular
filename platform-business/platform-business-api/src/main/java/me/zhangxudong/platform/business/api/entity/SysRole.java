package me.zhangxudong.platform.business.api.entity;

import me.zhangxudong.platform.common.api.DataEntity;

/**
 * 角色Entity
 * Created by zhangxd on 15/10/20.
 */
public class SysRole extends DataEntity<SysRole> {

    private String name;

    private String description;

    private Boolean enabled;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}
