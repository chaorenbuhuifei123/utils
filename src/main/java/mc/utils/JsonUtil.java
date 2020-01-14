package mc.utils;

import com.fasterxml.jackson.core.PrettyPrinter;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.TimeZone;

/**
 * @author: machao
 * @date: 2018/8/8 17:05
 */
public class JsonUtil {
    private static final Logger log = LoggerFactory.getLogger(JsonUtil.class);

    private static final PrettyPrinter PRETTY_PRINTER = new DefaultPrettyPrinter();

    private static ObjectMapper objectMapper = new ObjectMapper();

    static {
        // 忽略json字符串中不识别的属性
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // 忽略无法转换的对象 “No serializer found for class com.xxx.xxx”
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        // NULL不参与序列化
        //  objectMapper.setSerializationInclusion(Include.NON_NULL);

        // 指定时区，默认 UTC，而不是 jvm 默认时区
        objectMapper.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
    }


    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public static <T> T fromJson(String json, Class<T> cls) {
        if (StringUtil.isBlank(json)) {
            throw new IllegalArgumentException("json不能为空");
        }

        if (null == cls) {
            throw new IllegalArgumentException("cls不能为null");
        }

        try {
            return objectMapper.readValue(json, cls);
        } catch (IOException e) {
            log.error("json解析出错", e);
            throw new RuntimeException("json解析出错", e);
        }
    }

    public static <T> T fromJsonIngonreUnknowField(String data, Class<T> cls) {
        if (StringUtil.isBlank(data)) {
            throw new IllegalArgumentException("json不能为空");
        }
        if (null == cls) {
            throw new IllegalArgumentException("cls不能为null");
        }
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        try {
            return objectMapper.readValue(data, cls);
        } catch (IOException e) {
            throw new RuntimeException("json解析出错", e);
        }
    }

    public static <T> T fromJson(String json, TypeReference typeReference) {
        if (StringUtil.isBlank(json)) {
            throw new IllegalArgumentException("json不能为空");
        }

        if (null == typeReference) {
            throw new IllegalArgumentException("typeReference不能为null");
        }

        try {
            return (T) objectMapper.readValue(json, typeReference);
        } catch (IOException e) {
            log.error("json解析出错", e);
            throw new RuntimeException("json解析出错", e);
        }
    }

    public static <T> T fromJson(String json, JavaType javaType) {
        if (StringUtil.isBlank(json)) {
            throw new IllegalArgumentException("json不能为空");
        }

        if (null == javaType) {
            throw new IllegalArgumentException("javaType不能为null");
        }

        try {
            return objectMapper.readValue(json, javaType);
        } catch (IOException e) {
            log.error("json解析出错", e);
            throw new RuntimeException("json解析出错", e);
        }
    }

    public static String toJson(Object obj) {
        if (null == obj) {
            throw new IllegalArgumentException("obj不能为null");
        }
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (IOException e) {
            log.error("json解析出错", e);
            throw new RuntimeException("json解析出错", e);
        }
    }

    public static String toPrettyJson(Object obj) {
        if (null == obj) {
            throw new IllegalArgumentException("obj不能为null");
        }
        try {
            return objectMapper.writer(PRETTY_PRINTER).writeValueAsString(obj);
        } catch (IOException e) {
            log.error("json解析出错", e);
            throw new RuntimeException("json解析出错", e);
        }
    }
}
