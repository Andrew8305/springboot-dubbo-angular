package me.zhangxudong.platform.admin.web.common.base.property;

import com.google.common.collect.Maps;
import me.zhangxudong.platform.common.utils.StringHelper;

import java.util.Map;

/**
 * 全局配置类
 */
public class Global {

    /**
     * 当前对象实例
     */
    private static Global global = new Global();

    /**
     * 保存全局属性值
     */
    private static Map<String, String> map = Maps.newHashMap();

    /**
     * 属性文件加载对象
     */
    private static PropertiesLoader loader = new PropertiesLoader("config.properties");

    /**
     * 是/否
     */
    public static final String YES = "1";
    public static final String NO = "0";

    /**
     * 对/错
     */
    public static final String TRUE = "true";
    public static final String FALSE = "false";

    /**
     * 获取当前对象实例
     */
    public static Global getInstance() {
        return global;
    }

    /**
     * 获取配置
     */
    public static String getConfig(String key) {
        String value = map.get(key);
        if (value == null) {
            value = loader.getProperty(key);
            map.put(key, value != null ? value : StringHelper.EMPTY);
        }
        return value;
    }

    /**
     * 页面获取常量
     */
    public static Object getConst(String field) {
        try {
            return Global.class.getField(field).get(null);
        } catch (Exception e) {
            // 异常代表无配置，这里什么也不做
        }
        return null;
    }

}
