package me.zhangxudong.platform.business.provider.mapper;


import me.zhangxudong.platform.business.api.entity.SysRole;
import me.zhangxudong.platform.common.service.dao.CrudDao;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 角色DAO接口
 * Created by zhangxd on 15/10/20.
 */
@Mapper
public interface SysRoleMapper extends CrudDao<SysRole> {

    List<SysRole> findListByUserId(String userId);

}
