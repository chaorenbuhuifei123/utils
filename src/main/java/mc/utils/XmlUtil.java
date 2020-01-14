package mc.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.thoughtworks.xstream.XStream;

import java.lang.reflect.ParameterizedType;
import java.util.concurrent.ConcurrentHashMap;

public class XmlUtil {

    private static final ConcurrentHashMap<String, XStream> cache = new ConcurrentHashMap<>();

    private static XStream getXStreamInstance(Class[] classes) {
        StringBuilder key = new StringBuilder();
        for (Class cls : classes) {
            key.append(cls.getName());
        }
        //先从缓存取
        XStream xStream = cache.get(key.toString());
        if (null == xStream) {
            //如果缓存中不存在,则新增并加入缓存
            xStream = new XStream();
            xStream.ignoreUnknownElements();
            xStream.processAnnotations(classes);
            xStream.autodetectAnnotations(true);
            cache.put(key.toString(), xStream);
        }
        return xStream;
    }


    /**
     * JavaBean转换成xml
     *
     * @param classes
     * @param obj
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static String beanToXml(Class[] classes, Object obj) {
        XStream xstream = getXStreamInstance(classes);
        String xml = xstream.toXML(obj);
        return xml;
    }


    /**
     * xml转换成javabean
     *
     * @param tTypeReference
     * @param str
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static <T> T xmlToBean(TypeReference<T> tTypeReference, String str) {
        try {
            ParameterizedType parameterizedType = ((ParameterizedType) tTypeReference.getType());
            Class cls1 = Class.forName(parameterizedType.getRawType().getTypeName());
            Class cls2 = Class.forName(parameterizedType.getActualTypeArguments()[0].getTypeName());
            XStream xstream = getXStreamInstance(new Class[]{cls1, cls2});
            return (T) xstream.fromXML(str);
        } catch (Exception e) {
            throw new RuntimeException("无法解析此xml", e);
        }
    }


    /**
     * 把xml字符串转换成实体类
     *
     * @param cls 需要转换成的类型
     * @param str xml字符串
     * @return
     */
    public static Object xmlToBean(Class cls, String str) {
        XStream xstream = getXStreamInstance(new Class[]{cls});
        xstream.allowTypeHierarchy(cls);
        return xstream.fromXML(str);
    }


}
