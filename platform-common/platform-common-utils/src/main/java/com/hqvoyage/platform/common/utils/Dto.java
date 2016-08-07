package com.hqvoyage.platform.common.utils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Map接口的扩展.
 * Created by zhangxd on 16/3/10.
 */
public interface Dto extends Map<String, Object> {

    /**
     * 通过键，返回一个字符串的值.
     */
    String getString(String key);

    /**
     * 通过键，返回一个数组类型的对象.
     */
    <E> List<E> getList(String key);

    /**
     * 通过键，返回一个整数值.
     */
    int getInt(String key);

    /**
     * 通过键，返回一个long值.
     */
    long getLong(String key);

    /**
     * 通过键，返回一个浮点型数值.
     */
    double getDouble(String key);

    /**
     * 通过键，返回一个BigDecimal的值.
     */
    BigDecimal getDecimal(String key);

    /**
     * 通过键，返回一个日期型的值.
     */
    Date getDate(String key);

}