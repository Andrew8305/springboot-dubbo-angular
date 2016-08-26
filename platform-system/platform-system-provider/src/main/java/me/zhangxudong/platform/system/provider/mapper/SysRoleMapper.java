package me.zhangxudong.platform.system.provider.mapper;


import me.zhangxudong.platform.common.service.dao.CrudDao;
import me.zhangxudong.platform.system.api.entity.SysRole;
import org.apache.ibatis.annotations.Mapper;

/**
 * 角色DAO接口
 * Created by zhangxd on 15/10/20.
 */
@Mapper
public interface SysRoleMapper extends CrudDao<SysRole> {

    SysRole getByName(SysRole role);

    /**
     * 维护角色与菜单权限关系
     *
     * @param role
     * @return
     */
    int deleteRoleMenu(SysRole role);

    int insertRoleMenu(SysRole role);

}
