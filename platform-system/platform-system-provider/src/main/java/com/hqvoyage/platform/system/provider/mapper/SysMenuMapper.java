package com.hqvoyage.platform.system.provider.mapper;


import com.hqvoyage.platform.common.service.dao.CrudDao;
import com.hqvoyage.platform.system.api.entity.SysMenu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 菜单DAO接口
 * Created by zhangxd on 15/10/20.
 */
@Mapper
public interface SysMenuMapper extends CrudDao<SysMenu> {

    List<SysMenu> findByParentIdsLike(SysMenu menu);

    List<SysMenu> findByUserId(SysMenu menu);

    int updateParentIds(SysMenu menu);

    int updateSort(SysMenu menu);

}
