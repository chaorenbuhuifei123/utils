package mc.utils;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import java.util.Iterator;
import java.util.Map;

/**
 * @author machao
 * @CreateDate 2019/4/8 13:36
 * @Version 1.0
 * @Describe 参数处理工具类
 */
public class ParamUtil {

    /**
     * 参数脱敏处理
     *
     * @param param       参数
     * @param beforeDigit 参数前面需要的位数
     * @param afterDigit  参数后面需要的位数
     * @return {String} 脱敏后参数
     */
    public static String desensitizationParam(String param, int beforeDigit, int afterDigit) {
        if (StringUtil.isNull(param) || param.length() < (beforeDigit + afterDigit)) {
            return param;
        }

        return param.substring(0, beforeDigit) + generateStar(param.length() - beforeDigit - afterDigit) + param.substring(param.length() - afterDigit);
    }

    /**
     * 获取请求的所有参数
     *
     * @param request
     * @return
     */
    public static String getParams(HttpServletRequest request) {
        Map<String, String[]> params = request.getParameterMap();
        Iterator<Map.Entry<String, String[]>> iterator = params.entrySet().iterator();
        String requestStr = "";
        while (iterator.hasNext()) {
            Map.Entry<String, String[]> entry = iterator.next();
            for (int i = 0; i < entry.getValue().length; i++) {
                requestStr += "&" + entry.getKey() + "=" + entry.getValue()[i];
            }

        }
        if (StringUtils.isBlank(requestStr)) {
            return "";
        }
        return requestStr.replaceFirst("&", "?");
    }

    /**
     * 给链接上拼接参数
     *
     * @param url   链接地址
     * @param param 参数
     * @param value 值
     * @return {String} 添加参数后的链接
     */
    public static String addParamToURL(String url, String param, String value) {
        Assert.hasText(url, "URL不能为空");
        Assert.hasText(param, "参数名不能为空");
        if (url.indexOf("?") > 0 && url.indexOf("=") > 0) {
            return url.concat("&" + param + "=" + value);
        }
        url = url.replace("?", "");
        return url.concat("?" + param + "=" + value);
    }

    /**
     * 校验姓名
     * @param customerName
     * @return
     */
    public static boolean checkName(String customerName){
        customerName = customerName.replaceAll("\\·", "").replaceAll("\\•", "");
        String customerNameReg = "^([a-zA-Z]{2,50}|[\\u4e00-\\u9fa5]{2,25})$";
        if(!customerName.matches(customerNameReg)){
            return false;
        }
        return true;
    }

    /**
     * 校验手机号码
     * @param phone
     * @return
     */
    public static boolean checkPhone(String phone){
        String phoneReg = "^(13[0-9]|15[0-9]|18[0-9]|14[0-9]|17[0-9]|19[0-9]|16[0-9])\\d{8}$";
        if(phone.matches(phoneReg)){
            return true;
        }
        return false;
    }

    /**
     * 校验手机号码  1开头11位
     * @param phone
     * @return
     */
    public static boolean checkPhone2(String phone){
        String phoneReg = "^1\\d{10}$";
        if(phone.matches(phoneReg)){
            return true;
        }
        return false;
    }

    /**
     * 校验身份证
     * @param pid
     * @return
     */
    public static boolean checkPid(String pid){
        String pidReg = "^[1-9][0-9]{5}(19[0-9]{2}|20[0-9]{2})(0[1-9]|1[0-2])(0[1-9]|[12][0-9]|3[01])[0-9]{3}[0-9X]$";
        if(pid.matches(pidReg)){
            return true;
        }
        return false;
    }

    private static String generateStar(int digit) {
        String star = "";
        for (int i = 0; i < digit; i++) {
            star += "*";
        }
        return star;
    }

    /**
     * 根据时间获取签名值
     * @return
     */
    public static String getKey(){
        String ts=TimeUtil.getNowString("yyyyMMddHH");
        return DigestUtil.MD5Str( "ERH" + ts + "JYU");
    }

}
