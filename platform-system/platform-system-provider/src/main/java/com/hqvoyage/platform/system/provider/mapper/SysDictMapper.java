package com.hqvoyage.platform.system.provider.mapper;


import com.hqvoyage.platform.common.mybatis.CrudDao;
import com.hqvoyage.platform.system.api.entity.SysDict;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 字典DAO接口
 * Created by zhangxd on 15/10/20.
 */
@Mapper
public interface SysDictMapper extends CrudDao<SysDict> {

    List<String> findTypeList(SysDict dict);

}
