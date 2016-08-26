package me.zhangxudong.platform.common.utils;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;

/**
 * 对象操作工具类, 继承org.apache.commons.lang3.ObjectUtils类
 */
public class ObjectHelper extends ObjectUtils {

    private static Logger logger = LoggerFactory.getLogger(ObjectHelper.class);

	/**
	 * 注解到对象复制，只复制能匹配上的方法。
	 * @param annotation 注解
	 * @param object 对象
	 */
	public static void annotationToObject(Object annotation, Object object){
		if (annotation != null){
			Class<?> annotationClass = annotation.getClass();
			Class<?> objectClass = object.getClass();
			for (Method m : objectClass.getMethods()){
				if (StringUtils.startsWith(m.getName(), "set")){
					try {
						String s = StringUtils.uncapitalize(StringUtils.substring(m.getName(), 3));
						Object obj = annotationClass.getMethod(s).invoke(annotation);
						if (obj != null && !"".equals(obj.toString())){
                            m.invoke(object, obj);
						}
					} catch (Exception e) {
                        logger.info("忽略所有设置失败方法", e);
					}
				}
			}
		}
	}
	
	/**
	 * 序列化对象
	 * @param object Object
	 * @return byte[]
	 */
	public static byte[] serialize(Object object) {
		ObjectOutputStream oos;
		ByteArrayOutputStream baos;
		try {
			if (object != null){
				baos = new ByteArrayOutputStream();
				oos = new ObjectOutputStream(baos);
				oos.writeObject(object);
				return baos.toByteArray();
			}
		} catch (Exception e) {
            logger.error("序列化失败", e);
		}
		return null;
	}

	/**
	 * 反序列化对象
	 * @param bytes byte[]
	 * @return Object
	 */
	public static Object unserialize(byte[] bytes) {
		ByteArrayInputStream bais;
		try {
			if (bytes != null && bytes.length > 0){
				bais = new ByteArrayInputStream(bytes);
				ObjectInputStream ois = new ObjectInputStream(bais);
				return ois.readObject();
			}
		} catch (Exception e) {
			logger.error("反序列化失败", e);
		}
		return null;
	}
}
