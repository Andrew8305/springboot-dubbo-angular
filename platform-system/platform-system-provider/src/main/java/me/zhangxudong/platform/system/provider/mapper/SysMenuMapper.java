package me.zhangxudong.platform.system.provider.mapper;


import me.zhangxudong.platform.common.service.dao.CrudDao;
import me.zhangxudong.platform.system.api.entity.SysMenu;
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
