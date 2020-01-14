package mc.utils;

/**
 * ThreadLocal帮助类
 *
 * @author machao
 * @date 11/17/15.
 */
public class ThreadLocalUtil {
    private static final ThreadLocal<Object> threadLocal = new ThreadLocal<Object>();

    public static void set(Object object) {
        threadLocal.set(object);
    }

    public static Object get() {
        return threadLocal.get();
    }

    public static void remove() {
        threadLocal.remove();
    }
}
