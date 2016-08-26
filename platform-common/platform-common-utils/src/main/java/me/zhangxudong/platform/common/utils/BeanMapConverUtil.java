package me.zhangxudong.platform.common.utils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * JavaBean和Map之间相互转化的工具类
 */
public final class BeanMapConverUtil {

    /**
     * 将一个 JavaBean 对象转化为一个 Map。如果对象属性为null，则转换为""。
     *
     * @param <T>  数据类型
     * @param bean Bean
     */
    public static <T> Map<String, String> beanToMap(T bean) {

        Class<?> type = bean.getClass();
        Map<String, String> returnMap = new HashMap<>();

        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(type);
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor descriptor : propertyDescriptors) {
                String propertyName = descriptor.getName();
                if (!propertyName.equals("class")) {
                    Method readMethod = descriptor.getReadMethod();
                    Object result = readMethod.invoke(bean);
                    returnMap.put(propertyName, result != null ? result.toString() : "");
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return returnMap;
    }

    /**
     * 将JavaBean转换为HashMap，Map中的值为Object。如果Bean属性为null，则Map中的值也为null
     *
     * @param <T>  数据类型
     * @param bean Bean
     */
    public static <T> Map<String, Object> beanToMapObj(T bean) {

        Class<?> type = bean.getClass();
        Map<String, Object> returnMap = new HashMap<>();

        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(type);
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor descriptor : propertyDescriptors) {
                String propertyName = descriptor.getName();
                if (!propertyName.equals("class")) {
                    Method readMethod = descriptor.getReadMethod();
                    Object result = readMethod.invoke(bean);
                    returnMap.put(propertyName, result);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return returnMap;
    }

    /**
     * 将JavaBean转换为HashMap，Map中的值为Object。如果Bean属性为null，则Map中不存该Key。
     *
     * @param <T>  数据类型
     * @param bean Bean
     */
    public static <T> Map<String, Object> beanToMapObjExceptNull(T bean) {

        Class<?> type = bean.getClass();
        Map<String, Object> returnMap = new HashMap<>();

        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(type);
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor descriptor : propertyDescriptors) {
                String propertyName = descriptor.getName();
                if (!propertyName.equals("class")) {
                    Method readMethod = descriptor.getReadMethod();
                    Object result = readMethod.invoke(bean);
                    if (result != null) {
                        returnMap.put(propertyName, result);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return returnMap;
    }

    /**
     * 将一个Map对象转化为一个JavaBean
     *
     * @param <T>      数据类型
     * @param paramMap Map
     * @param clazz    Class
     */
    public static <T> T mapToBean(Map<String, Object> paramMap, Class<T> clazz) {
        T beanObj;
        try {
            beanObj = clazz.newInstance();
            String propertyName;
            Object propertyValue;
            for (Map.Entry<String, Object> entity : paramMap.entrySet()) {
                propertyName = entity.getKey();
                propertyValue = entity.getValue();
                setProperties(beanObj, propertyName, propertyValue);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return beanObj;
    }

    private static <T> void setProperties(T entity, String propertyName, Object value) throws Exception {
        PropertyDescriptor pd = new PropertyDescriptor(propertyName, entity.getClass());
        Method methodSet = pd.getWriteMethod();
        methodSet.invoke(entity, value);
    }

    static class User {
        private String a;
        private String b;

        public String getA() {
            return a;
        }

        public void setA(String a) {
            this.a = a;
        }

        public String getB() {
            return b;
        }

        public void setB(String b) {
            this.b = b;
        }
    }

}
