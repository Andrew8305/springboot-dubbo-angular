package com.hqvoyage.platform.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 复杂对象与简单对象转换工具类(通常用于 Po 转 Vo)
 */
public class BeanHelper {

    private static Logger logger = LoggerFactory.getLogger(BeanHelper.class);

    /**
     * 将源对象的值，放到与目标对象属性相同的值里面
     *
     * @param sourceClass 原始类
     * @param targetClass 目标类
     * @param sourceObj 要处理的对象
     * @return 目标对象
     */
    @SuppressWarnings("rawtypes")
    public static Object changeProValue(Class sourceClass, Class targetClass, Object sourceObj) {
        if (sourceObj  == null) {
            return null;
        }
        try {
            Object ret = targetClass.newInstance();
            Field[] target = targetClass.getDeclaredFields();
            for (Field aTarget : target) {
                String targetProperName = aTarget.getName();
                Field[] source = sourceClass.getDeclaredFields();
                for (Field f : source) {
                    String sourceProperName = f.getName();
                    if (sourceProperName.equals(targetProperName)) {
                        f.setAccessible(true);
                        aTarget.setAccessible(true);
                        aTarget.set(ret, f.get(sourceObj));
                    }
                }
            }
            return ret;
        } catch (Exception e) {
            logger.error("对象转换异常", e);
        }

        return sourceObj;
    }

    /**
     * 将源对象的值，放到与目标对象属性相同的值里面
     *
     * @param sourceClass 原始类
     * @param targetClass 目标类
     * @param objList 源对象列表
     * @return 目标对象列表
     */
    @SuppressWarnings("rawtypes")
    public static List<Object> changeProValueList(Class sourceClass, Class targetClass, List<?> objList) {
        List<Object> retList = new ArrayList<>();
        for (Object o : objList) {
            Object ret = changeProValue(sourceClass, targetClass, o);
            retList.add(ret);
        }
        return retList;
    }

}
