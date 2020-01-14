package mc.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;

/**
 * 字符串帮助类
 *
 * @author machao
 * @date 2017/2/4
 */
public class StringUtil {

    /**
     * 判断是否是空字符串
     *
     * @param str
     * @return
     * @throws Exception
     */
    public static boolean isNull(String str) {
        if (str == null || "".equals(str) || "undefined".equals(str) || "null".equals(str)) {
            return true;
        }
        return false;
    }

    public static boolean isNotBlank(String str){
        return !isBlank(str);
    }

    /**
     * 如果字符串为null或者""或者"  "都返回true
     *
     * @param str
     * @return
     */
    public static boolean isBlank(String str) {
        return (str == null || str.trim().length() == 0);
    }

    /**
     * 如果字符串为null或者""返回true
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        return (str == null || str.length() == 0);
    }

    /**
     * 判断两个字符串时候相等
     *
     * @param actual
     * @param expected
     * @return
     */
    public static boolean isEquals(String actual, String expected) {
        return actual == expected || (actual == null ? expected == null : actual.equals(expected));
    }

    /**
     * 把null转换成空字符串
     *
     * @param str
     * @return 如果参数str不是null，则直接返回str
     */
    public static String nullStrToEmpty(String str) {
        return (str == null ? "" : str);
    }

    /**
     * 使得字符串的首字母大写
     *
     * <pre>
     * capitalizeFirstLetter(null)     =   null;
     * capitalizeFirstLetter("")       =   "";
     * capitalizeFirstLetter("2ab")    =   "2ab"
     * capitalizeFirstLetter("a")      =   "A"
     * capitalizeFirstLetter("ab")     =   "Ab"
     * capitalizeFirstLetter("Abc")    =   "Abc"
     * </pre>
     *
     * @param str
     * @return
     */
    public static String capitalizeFirstLetter(String str) {
        if (isEmpty(str)) {
            return str;
        }

        char c = str.charAt(0);
        return (!Character.isLetter(c) || Character.isUpperCase(c)) ? str : new StringBuilder(str.length())
                .append(Character.toUpperCase(c)).append(str.substring(1)).toString();
    }

    /**
     * URLEncode中文字符串，编码是utf-8
     *
     * <pre>
     * utf8Encode(null)        =   null
     * utf8Encode("")          =   "";
     * utf8Encode("aa")        =   "aa";
     * utf8Encode("啊啊啊啊")   = "%E5%95%8A%E5%95%8A%E5%95%8A%E5%95%8A";
     * </pre>
     *
     * @param str
     * @return
     * @throws UnsupportedEncodingException if an error occurs
     */
    public static String utf8Encode(String str) {
        if (!isEmpty(str) && str.getBytes().length != str.length()) {
            try {
                return URLEncoder.encode(str, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("UnsupportedEncodingException occurred. ", e);
            }
        }
        return str;
    }

    /**
     * URLEncode中文字符串,编码是utf-8，如果出错了，返回defaultReturn
     *
     * @param str
     * @param defaultReturn
     * @return
     */
    public static String utf8Encode(String str, String defaultReturn) {
        if (!isEmpty(str) && str.getBytes().length != str.length()) {
            try {
                return URLEncoder.encode(str, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                return defaultReturn;
            }
        }
        return str;
    }


    /**
     * 计算occur在str中出现的次数
     *
     * @param str
     * @param occur
     * @return
     */
    public static Long calculateOccur(String str, String occur) {
        if (isBlank(str)) {
            return 0L;
        }
        Long count = 0L;
        String tmp = str;
        while (tmp.indexOf(occur) >= 0) {
            tmp = tmp.substring(tmp.indexOf(occur)+1);
            ++count;
        }
        return count;
    }

    /**
     * 判断字符串中是否存在子串
     * @param str  以分隔符组成的字符串  默认逗号分隔
     * @param subStr 子串
     * @return
     */
    public static boolean contains(String str, String subStr){
        return contains(",",str,subStr);
    }

    /**
     * 判断字符串中是否存在子串
     * @param splitKey 分隔符
     * @param str  以分隔符组成的字符串
     * @param subStr 子串
     * @return
     */
    public static boolean contains(String splitKey, String str, String subStr){
        if(isBlank(str)||isBlank(subStr)||isBlank(splitKey)){
            return false;
        }
        return Arrays.stream(str.split(splitKey)).anyMatch(p->p.equals(subStr));
    }
}
