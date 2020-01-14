package mc.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @classDesc: 功能描述(生成解析国密加密byte数组)
 * @author:
 * @createTime: 2019-08-23 18:06
 * @version: 1.0
 */
public class BytesUtil {

    private static final Logger log = LoggerFactory.getLogger(BytesUtil.class);

    /**
     * 把String转换成16字节的byte数组
     *
     * @param key
     * @return
     */
    public static byte[] strToByteArr(String key) {
        if (key.getBytes().length != 16) {
            throw new IllegalArgumentException("key的长度需要16");
        }
        return key.getBytes();
    }

    public static String byteArrToStr(byte[] byteArr) {
        if (byteArr.length != 16) {
            throw new IllegalArgumentException("byteArr的长度需要16");
        }
        return new String(byteArr);
    }

    /**
     * 将byte数组转换为表示16进制值的字符串， 如：byte[]{8,18}转换为：0813
     *
     * @param byteArr
     * @return
     * @throws Exception
     */
    public static String byteArrToHexStr(byte[] byteArr)  {
        int iLen = byteArr.length;
        // 每个byte用两个字符才能表示，所以字符串的长度是数组长度的两倍
        StringBuffer sb = new StringBuffer(iLen * 2);
        for (int i = 0; i < iLen; i++) {
            int intTmp = byteArr[i];
            // 把负数转换为正数
            while (intTmp < 0) {
                intTmp = intTmp + 256;
            }
            // 小于0F的数需要在前面补0
            if (intTmp < 16) {
                sb.append("0");
            }
            sb.append(Integer.toString(intTmp, 16));
        }
        return sb.toString();
    }

    /**
     * 将表示16进制值的字符串转换为byte数组
     *
     * @param hexStr
     * @return
     * @throws Exception
     */
    public static byte[] hexStrToByteArr(String hexStr)  {
        byte[] arrOut = null;
        try {
            byte[] arrB = hexStr.getBytes();
            int iLen = arrB.length;

            // 两个字符表示一个字节，所以字节数组长度是字符串长度除以2
            arrOut = new byte[iLen / 2];
            for (int i = 0; i < iLen; i = i + 2) {
                String strTmp = new String(arrB, i, 2);
                arrOut[i / 2] = (byte) Integer.parseInt(strTmp, 16);
            }
        }catch(Exception e){
            log.error("16进制值的字符串转换为byte数组出错", e);
        }
        return arrOut;
    }
}