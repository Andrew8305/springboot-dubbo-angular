package com.hqvoyage.platform.system.provider.mapper;


import com.hqvoyage.platform.common.mybatis.CrudDao;
import com.hqvoyage.platform.system.api.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户DAO接口
 * Created by zhangxd on 15/10/20.
 */
@Mapper
public interface SysUserMapper extends CrudDao<SysUser> {

    /**
     * 根据登录名称查询用户
     *
     * @param user
     * @return
     */
    SysUser getByLoginName(SysUser user);

    /**
     * 查询全部用户数目
     *
     * @return
     */
    long findAllCount(SysUser user);

    /**
     * 更新用户密码
     *
     * @param user
     * @return
     */
    int updatePasswordById(SysUser user);

    /**
     * 更新登录信息，如：登录IP、登录时间
     *
     * @param user
     * @return
     */
    int updateLoginInfo(SysUser user);

    /**
     * 删除用户角色关联数据
     *
     * @param user
     * @return
     */
    int deleteUserRole(SysUser user);

    /**
     * 插入用户角色关联数据
     *
     * @param user
     * @return
     */
    int insertUserRole(SysUser user);

    /**
     * 更新用户信息
     *
     * @param user
     * @return
     */
    int updateUserInfo(SysUser user);

}
