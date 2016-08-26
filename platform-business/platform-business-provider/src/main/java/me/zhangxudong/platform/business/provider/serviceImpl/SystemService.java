package me.zhangxudong.platform.business.provider.serviceImpl;

import me.zhangxudong.platform.business.api.entity.SysUser;
import me.zhangxudong.platform.business.api.service.ISystemService;
import me.zhangxudong.platform.business.provider.mapper.SysRoleMapper;
import me.zhangxudong.platform.business.provider.mapper.SysUserMapper;
import me.zhangxudong.platform.common.service.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 系统管理，安全相关实体的管理类,包括用户、角色、菜单.
 * Created by zhangxd on 15/10/20.
 */
@Service
@Transactional(readOnly = true)
public class SystemService extends BaseService implements ISystemService {

    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private SysRoleMapper sysRoleMapper;

    public SysUser getUserByLoginName(String loginName) {
        SysUser user = sysUserMapper.getByLoginName(loginName);
        if (user == null) {
            return null;
        }
        user.setRoles(sysRoleMapper.findListByUserId(user.getId()));
        return user;
    }

}
