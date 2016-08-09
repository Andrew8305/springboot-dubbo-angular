package com.hqvoyage.platform.admin.web.utils;

import com.hqvoyage.platform.admin.web.common.base.SpringContextHolder;
import com.hqvoyage.platform.admin.web.security.SystemAuthorizingRealm.Principal;
import com.hqvoyage.platform.system.api.entity.SysMenu;
import com.hqvoyage.platform.system.api.entity.SysUser;
import com.hqvoyage.platform.system.api.service.ISystemService;

import java.util.List;

/**
 * 用户工具类
 * Created by zhangxd on 15/10/20.
 */
public class UserUtils {

    private static ISystemService systemService = SpringContextHolder.getBean(ISystemService.class);

    /**
     * 根据ID获取用户
     *
     * @param id
     * @return 取不到返回null
     */
    public static SysUser get(String id) {
        return systemService.getUser(id);
    }

    /**
     * 获取当前用户
     *
     * @return 取不到返回 new User()
     */
    public static SysUser getUser() {
        Principal principal = WebUtils.getPrincipal();
        if (principal != null) {
            SysUser user = get(principal.getId());
            if (user != null) {
                return user;
            }
            return new SysUser();
        }
        // 如果没有登录，则返回实例化空的User对象。
        return new SysUser();
    }

    /**
     * 获取当前用户授权菜单
     *
     * @return
     */
    public static List<SysMenu> getMenuList() {
        return systemService.findAllMenu(getUser());
    }

}
