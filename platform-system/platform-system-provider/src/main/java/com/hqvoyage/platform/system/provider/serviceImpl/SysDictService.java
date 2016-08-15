package com.hqvoyage.platform.system.provider.serviceImpl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hqvoyage.platform.common.service.service.CrudService;
import com.hqvoyage.platform.system.api.entity.SysDict;
import com.hqvoyage.platform.system.api.service.ISysDictService;
import com.hqvoyage.platform.system.provider.mapper.SysDictMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 字典Service
 * Created by zhangxd on 15/10/20.
 */
@Service
@Transactional(readOnly = true)
public class SysDictService extends CrudService<SysDictMapper, SysDict> implements ISysDictService {

    /**
     * 查询字段类型列表
     *
     * @return
     */
    public List<String> findTypeList() {
        return dao.findTypeList(new SysDict());
    }

    @Transactional(readOnly = false)
    @CacheEvict(value = "sysDictList", key = "'sysDictList' + #dict.getType()")
    public void save(SysDict dict) {
        super.save(dict);
    }

    @Transactional(readOnly = false)
    @CacheEvict(value = "sysDictList", key = "'sysDictList' + #dict.getType()")
    public void delete(SysDict dict) {
        super.delete(dict);
    }

    @Cacheable(value = "sysDictList", key = "'sysDictList' + #type")
    public List<SysDict> getDictList(String type) {
        Map<String, List<SysDict>> dictMap = Maps.newHashMap();
        for (SysDict dict : dao.findAllList(new SysDict())) {
            List<SysDict> dictList = dictMap.get(dict.getType());
            if (dictList != null) {
                dictList.add(dict);
            } else {
                dictMap.put(dict.getType(), Lists.newArrayList(dict));
            }
        }
        List<SysDict> dictList = dictMap.get(type);
        if (dictList == null) {
            dictList = Lists.newArrayList();
        }
        return dictList;
    }

}
