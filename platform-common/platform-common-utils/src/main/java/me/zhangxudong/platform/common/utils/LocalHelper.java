package me.zhangxudong.platform.common.utils;

/**
 * ThreadLocal对象,"注册"到本助手类下,在任务结束时候"统一"释放,避免因为线程池原因而产生的对象无法释放问题.
 * 如果是类下面的static的变量,请调用registStatic,否则请调用regist
 * 
 */
public class LocalHelper {

	private static LocalListener listener = new LocalListener();

	/**
	 * 清除全部的LocalContext信息.这个函数只能由Dispatcher控制器来使用.
	 *
	 */
	public static void clearContexts() {
		listener.clearContexts();

	}

    /**
     * 绑定一个request级别的ThreadLocal对象.
     */
	public static <E> ThreadLocal<E> regist(ThreadLocal<E> tl) {
		return listener.regist(tl);
	}

	/**
	 * 绑定一个global级别的ThreadLocal对象.
	 */
	public static <E> ThreadLocal<E> registStatic(ThreadLocal<E> tl) {
		return listener.registStatic(tl);
	}

	/**
	 * 注销一个request级别的ThreadLocal对象.
	 */
	public static <E> ThreadLocal<E> unregist(ThreadLocal<E> tl) {
		return listener.unregist(tl);
	}

	/**
	 * 注销一个global级别的ThreadLocal对象.
	 */
	public static <E> ThreadLocal<E> unregistStatic(ThreadLocal<E> tl) {
		return listener.unregistStatic(tl);
	}
	
}
