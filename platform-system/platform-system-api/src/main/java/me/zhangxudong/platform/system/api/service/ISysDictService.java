package me.zhangxudong.platform.system.api.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import me.zhangxudong.platform.system.api.entity.SysDict;

import java.util.List;

/**
 * 字典Service
 * Created by zhangxd on 15/10/20.
 */
public interface ISysDictService {

    SysDict get(String id);

    List<String> findTypeList();

    void save(SysDict dict);

    void delete(SysDict dict);

    List<SysDict> getDictList(String type);

    PageInfo<SysDict> findPage(Page page, SysDict entity);

    List<SysDict> findList(SysDict entity);

}
