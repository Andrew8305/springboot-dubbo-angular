package com.hqvoyage.platform.business.provider.mapper;


import com.hqvoyage.platform.business.api.entity.SysRole;
import com.hqvoyage.platform.common.service.dao.CrudDao;
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
