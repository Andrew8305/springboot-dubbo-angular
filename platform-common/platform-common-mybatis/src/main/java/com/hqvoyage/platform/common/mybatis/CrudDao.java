package com.hqvoyage.platform.common.mybatis;

import java.util.List;

/**
 * DAO支持类实现
 * Created by zhangxd on 15/10/20.
 */
public interface CrudDao<T> extends BaseDao {

	/**
	 * 获取单条数据
	 * @param id 主键
	 * @return T
	 */
	T get(String id);
	
	/**
	 * 获取单条数据
	 * @param entity T
	 * @return T
	 */
	T get(T entity);
	
	/**
	 * 查询数据列表
	 * @param entity T
	 * @return List<T>
	 */
	List<T> findList(T entity);

    /**
     * 查询所有数据列表
     * @param entity T
     * @return List<T>
     */
    List<T> findAllList(T entity);

	/**
	 * 插入数据
	 * @param entity T
	 * @return int
	 */
	int insert(T entity);
	
	/**
	 * 更新数据
	 * @param entity T
	 * @return int
	 */
	int update(T entity);
	
	/**
	 * 删除数据
	 * @param entity T
	 * @return int
	 */
	int delete(T entity);
	
}