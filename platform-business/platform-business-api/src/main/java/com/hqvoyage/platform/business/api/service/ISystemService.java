package com.hqvoyage.platform.business.api.service;

import com.hqvoyage.platform.business.api.entity.SysUser;

/**
 * 系统管理，安全相关实体的管理类,包括用户、角色、菜单.
 * Created by zhangxd on 15/10/20.
 */
public interface ISystemService {


    /**
     * 根据登录名获取用户
     *
     * @param loginName 登录名
     * @return SysUser
     */
    SysUser getUserByLoginName(String loginName);

}
