package com.hqvoyage.platform.system.api.entity;

import com.google.common.collect.Lists;
import com.hqvoyage.platform.common.utils.StringHelper;
import org.hibernate.validator.constraints.Length;

import java.util.List;

/**
 * 角色Entity
 * Created by zhangxd on 15/10/20.
 */
public class SysRole extends SysEntity<SysRole> {

    private static final long serialVersionUID = 1L;
    private String name;    // 角色名称

    private String oldName;    // 原角色名称
    private String sysData;        //是否是系统数据
    private String useable;        //是否是可用

    private SysUser user;        // 根据用户ID查询角色列表

    private List<SysMenu> menuList = Lists.newArrayList(); // 拥有菜单列表

    public SysRole() {
        super();
        this.useable = YES;
    }

    public SysRole(String id) {
        super(id);
    }

    public SysRole(SysUser user) {
        this();
        this.user = user;
    }

    public String getUseable() {
        return useable;
    }

    public void setUseable(String useable) {
        this.useable = useable;
    }

    public String getSysData() {
        return sysData;
    }

    public void setSysData(String sysData) {
        this.sysData = sysData;
    }

    @Length(min = 1, max = 100)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOldName() {
        return oldName;
    }

    public void setOldName(String oldName) {
        this.oldName = oldName;
    }

    public List<SysMenu> getMenuList() {
        return menuList;
    }

    public void setMenuList(List<SysMenu> menuList) {
        this.menuList = menuList;
    }

    public List<String> getMenuIdList() {
        List<String> menuIdList = Lists.newArrayList();
        for (SysMenu menu : menuList) {
            menuIdList.add(menu.getId());
        }
        return menuIdList;
    }

    public void setMenuIdList(List<String> menuIdList) {
        menuList = Lists.newArrayList();
        for (String menuId : menuIdList) {
            SysMenu menu = new SysMenu();
            menu.setId(menuId);
            menuList.add(menu);
        }
    }

    public String getMenuIds() {
        return StringHelper.join(getMenuIdList(), ",");
    }

    public void setMenuIds(String menuIds) {
        menuList = Lists.newArrayList();
        if (menuIds != null) {
            String[] ids = StringHelper.split(menuIds, ",");
            setMenuIdList(Lists.newArrayList(ids));
        }
    }

    public SysUser getUser() {
        return user;
    }

    public void setUser(SysUser user) {
        this.user = user;
    }

}
