package com.hqvoyage.platform.system.provider.serviceImpl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.SqlUtil;
import com.hqvoyage.platform.common.service.service.BaseService;
import com.hqvoyage.platform.common.utils.PasswordUtils;
import com.hqvoyage.platform.common.utils.StringHelper;
import com.hqvoyage.platform.system.api.entity.SysMenu;
import com.hqvoyage.platform.system.api.entity.SysRole;
import com.hqvoyage.platform.system.api.entity.SysUser;
import com.hqvoyage.platform.system.api.service.ISystemService;
import com.hqvoyage.platform.system.provider.mapper.SysMenuMapper;
import com.hqvoyage.platform.system.provider.mapper.SysRoleMapper;
import com.hqvoyage.platform.system.provider.mapper.SysUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

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
    @Autowired
    private SysMenuMapper sysMenuMapper;

    //-- User Service --//

    /**
     * 获取用户
     *
     * @param id
     * @return
     */
    public SysUser getUser(String id) {
        SysUser user = sysUserMapper.get(id);
        if (user == null) {
            return null;
        }
        user.setRoleList(sysRoleMapper.findList(new SysRole(user)));
        return user;
    }

    /**
     * 根据登录名获取用户
     *
     * @param loginName
     * @return
     */
    public SysUser getUserByLoginName(String loginName) {
        SysUser user = sysUserMapper.getByLoginName(new SysUser(null, loginName));
        if (user == null) {
            return null;
        }
        user.setRoleList(sysRoleMapper.findList(new SysRole(user)));
        return user;
    }

    public PageInfo<SysUser> findUser(Page page, SysUser user) {
        // 执行分页查询
        SqlUtil.setLocalPage(page);
        List<SysUser> list = sysUserMapper.findList(user);
        return new PageInfo<>(list);
    }

    /**
     * 无分页查询人员列表
     *
     * @param user
     * @return
     */
    public List<SysUser> findUser(SysUser user) {
        return sysUserMapper.findList(user);
    }

    @Transactional(readOnly = false)
    public void saveUser(SysUser user) {
        if (StringHelper.isBlank(user.getId())) {
            user.preInsert();
            sysUserMapper.insert(user);
        } else {
            // 更新用户数据
            user.preUpdate();
            sysUserMapper.update(user);
        }
        if (StringHelper.isNotBlank(user.getId())) {
            // 更新用户与角色关联
            sysUserMapper.deleteUserRole(user);
            if (user.getRoleList() != null && user.getRoleList().size() > 0) {
                sysUserMapper.insertUserRole(user);
            } else {
                throw new RuntimeException(user.getLoginName() + "没有设置角色！");
            }
        }
    }

    @Transactional(readOnly = false)
    public void updateUserInfo(SysUser user) {
        user.preUpdate();
        sysUserMapper.updateUserInfo(user);
    }

    @Transactional(readOnly = false)
    public void deleteUser(SysUser user) {
        sysUserMapper.delete(user);
    }

    @Transactional(readOnly = false)
    public void updatePasswordById(String id, String loginName, String newPassword) {
        SysUser user = new SysUser(id);
        user.setPassword(PasswordUtils.encryptPassword(newPassword));
        sysUserMapper.updatePasswordById(user);
        // 清除用户缓存
        user.setLoginName(loginName);
    }

    @Transactional(readOnly = false)
    public void updateUserLoginInfo(SysUser user) {
        // 保存上次登录信息
        user.setOldLoginIp(user.getLoginIp());
        user.setOldLoginDate(user.getLoginDate());
        // 更新本次登录信息
        user.setLoginDate(new Date());
        sysUserMapper.updateLoginInfo(user);
    }


    //-- Role Service --//

    public SysRole getRole(String id) {
        return sysRoleMapper.get(id);
    }

    public SysRole getRoleByName(String name) {
        SysRole r = new SysRole();
        r.setName(name);
        return sysRoleMapper.getByName(r);
    }

    public List<SysRole> findRole(SysRole role) {
        return sysRoleMapper.findList(role);
    }

    public List<SysRole> findAllRole(SysUser currentUser) {
        List<SysRole> roleList;
        if (currentUser.isAdmin()) {
            roleList = sysRoleMapper.findAllList(new SysRole());
        } else {
            SysRole role = new SysRole();
            roleList = sysRoleMapper.findList(role);
        }
        return roleList;
    }

    @Transactional(readOnly = false)
    public void saveRole(SysRole role) {
        if (StringHelper.isBlank(role.getId())) {
            role.preInsert();
            sysRoleMapper.insert(role);
        } else {
            role.preUpdate();
            sysRoleMapper.update(role);
        }
        // 更新角色与菜单关联
        sysRoleMapper.deleteRoleMenu(role);
        if (role.getMenuList().size() > 0) {
            sysRoleMapper.insertRoleMenu(role);
        }
    }

    @Transactional(readOnly = false)
    public void deleteRole(SysRole role) {
        sysRoleMapper.delete(role);
    }

    @Transactional(readOnly = false)
    public Boolean outUserInRole(SysRole role, SysUser user) {
        List<SysRole> roles = user.getRoleList();
        for (SysRole e : roles) {
            if (e.getId().equals(role.getId())) {
                roles.remove(e);
                saveUser(user);
                return true;
            }
        }
        return false;
    }

    @Transactional(readOnly = false)
    public SysUser assignUserToRole(SysRole role, SysUser user) {
        if (user == null) {
            return null;
        }
        List<String> roleIds = user.getRoleIdList();
        if (roleIds.contains(role.getId())) {
            return null;
        }
        user.getRoleList().add(role);
        saveUser(user);
        return user;
    }

    //-- Menu Service --//

    public SysMenu getMenu(String id) {
        return sysMenuMapper.get(id);
    }

    public List<SysMenu> findAllMenu(SysUser currentUser) {
        List<SysMenu> menuList;
        if (currentUser.isAdmin()) {
            menuList = sysMenuMapper.findAllList(new SysMenu());
        } else {
            SysMenu m = new SysMenu();
            m.setUserId(currentUser.getId());
            menuList = sysMenuMapper.findByUserId(m);
        }
        return menuList;
    }

    @Transactional(readOnly = false)
    public void saveMenu(SysMenu menu) {

        // 获取父节点实体
        menu.setParent(this.getMenu(menu.getParent().getId()));

        // 获取修改前的parentIds，用于更新子节点的parentIds
        String oldParentIds = menu.getParentIds();

        // 设置新的父节点串
        menu.setParentIds(menu.getParent().getParentIds() + menu.getParent().getId() + ",");

        // 保存或更新实体
        if (StringHelper.isBlank(menu.getId())) {
            menu.preInsert();
            sysMenuMapper.insert(menu);
        } else {
            menu.preUpdate();
            sysMenuMapper.update(menu);
        }

        // 更新子节点 parentIds
        SysMenu m = new SysMenu();
        m.setParentIds("%," + menu.getId() + ",%");
        List<SysMenu> list = sysMenuMapper.findByParentIdsLike(m);
        for (SysMenu e : list) {
            e.setParentIds(e.getParentIds().replace(oldParentIds, menu.getParentIds()));
            sysMenuMapper.updateParentIds(e);
        }
    }

    @Transactional(readOnly = false)
    public void updateMenuSort(SysMenu menu) {
        sysMenuMapper.updateSort(menu);
    }

    @Transactional(readOnly = false)
    public void deleteMenu(SysMenu menu) {
        sysMenuMapper.delete(menu);
    }

}
