package mc.utils;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.log4j.Logger;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 对象工具类
 *
 * @author machao
 */
public class ObjectUtil {
    /**
     * 输出本类的相关log日志
     */
    protected static final Logger logger = Logger.getLogger(ObjectUtil.class);

    /**
     * 私有构造函数
     */
    private ObjectUtil() {
    }


    /**
     * 根据指定格式格式化流水码
     *
     * @param rule
     * @param code
     * @return
     */
    public static String formatNumber(String rule, String code) {
        // 使用“0”填充编码至指定长度
        String formatter = "%0" + rule.length() + "d";
        return String.format(formatter, Long.valueOf(code));
    }


    private static final Pattern p1 = Pattern.compile("\\r|\n");

    /**
     * JAVA 替换 字符串中:回车符、换行符 为空串
     *
     * @param str
     * @return
     * @author machao
     * @date 2013-11-1 // 简单方法：String s = "你要去除的字符串"; //
     * 这样也可以把空格和回车去掉，其他也可以照这样做。 // 1.去除空格：s = s.replace('\\s',''); //
     * 2.去除回车：s = s.replace('\n',''); 注：\n 回车(\u000a) \t 水平制表符(\u0009) \s
     * 空格(\u0008) \r 换行(\u000d)
     */
    public static String replaceBlank(String str) {
        if (str == null || "".equals(str)) {
            return str;
        }
        Matcher m = p1.matcher(str);
        String after = m.replaceAll("");
        return after;
    }

    /**
     * 作者：machao 把List列表按照固定个数分页
     *
     * @param list     待分页的列表
     * @param pageSize 每页固定条数,如10条
     * @param curPage  当前页:下标从1开始
     */
    @SuppressWarnings("rawtypes")
    public static List paging(List list, int pageSize, int curPage) {
        // 列表为空，直接返回
        if (pageSize <= 0 || curPage <= 0 || list == null || list.size() <= 0) {
            return list;
        }

        // 按照每pageSize条分页，并返回该页的subList对象
        int listSize = list.size();            //列表总条数
        int totalPage = (listSize + pageSize - 1) / pageSize;
        //当前页数，大于总页数，则直接返回空列表
        if (curPage > totalPage) {
            return Collections.EMPTY_LIST;
        }

        int pageStart = (curPage - 1) * pageSize;    //开始条数
        int pageEnd = pageStart + pageSize;    //结束条数 = 开始条数 + 每页条数
        // 当结束条数 大于 总条数，改取 列表总条数
        if (pageEnd > listSize) {
            pageEnd = listSize;
        }
        // 返回当前页对应的列表
        return list.subList(pageStart, pageEnd);
    }

    /**
     * 作者：machao 把List列表按照固定个数分页
     *
     * @param list      待分页的列表
     * @param pageSize  每页固定条数
     * @param pageStart 开始条数:下标从0开始
     */
    @SuppressWarnings("rawtypes")
    public static List listToPage(List list, int pageSize, int pageStart) {
        // 列表为空，直接返回
        if (list == null || list.size() <= 0) {
            return list;
        }
        // 按照每pageSize条分页，并返回该页的subList对象
        int listSize = list.size(); // 列表总条数
        int pageEnd = pageStart + pageSize; // 开始条数 + 每页条数 = 结束条数
        // 当结束条数 大于 总条数，改取 列表总条数
        if (pageEnd > listSize) {
            pageEnd = listSize;
        }
        // 返回当前页对应的列表
        return list.subList(pageStart, pageEnd);
    }

    /**
     * 作者：machao 用逗号分隔List列表
     */
    public static String listToStr(List<Object> list) {
        if (list != null && !list.isEmpty()) {
            String str = null;
            for (int i = 0; i < list.size(); i++) {
                Object obj = list.get(i);
                if (obj != null) {
                    if (str != null) {
                        str += "," + obj.toString();
                    } else {
                        str = obj.toString();
                    }
                }
            }
            return str;
        } else {
            return null;
        }
    }

    /**
     * 作者：machao 将Long型转为字符串
     *
     * @return
     */
    public static String longToStr(Long obj) {
        if (obj != null) {
            return obj.toString();
        } else {
            return null;
        }
    }

    /**
     * 作者：machao 将字符串转为BigDecimal型，错误返回0
     *
     * @param str
     */
    public static BigDecimal strToBigDecimal(String str) {
        BigDecimal d = new BigDecimal(0);
        if (str != null && !"".equals(str)) {
            try {
                d = new BigDecimal(str);
            } catch (Exception e) {
                logger.info("strToBigDecimal：字符串[" + str + "]不是数字!");
                logger.info("异常信息：{}", e);
            }
        }
        return d;
    }

    /**
     * 作者：machao 将字符串转为BigDecimal型，错误返回0
     *
     * @param str
     */
    public static BigDecimal strToBigNull(String str) {
        BigDecimal d = null;
        if (str != null && !"".equals(str)) {
            try {
                d = new BigDecimal(str);
            } catch (Exception e) {
                logger.info("strToBigDecimal：字符串[" + str + "]不是数字!");
                logger.info("异常信息：{}", e);
            }
        }
        return d;
    }

    /**
     * 作者：machao 将字符串转为Long型，错误返回null
     *
     * @param str
     * @return
     */
    public static Long stringToLong(String str) {
        Long rl = null;
        if (str != null && !"".equals(str)) {
            try {
                rl = Long.valueOf(str);
            } catch (Exception e) {
                logger.info("stringToLong：字符串[" + str + "]不全是数字!" + e);
                rl = null;
            }
        }
        return rl;
    }

    /**
     * 作者：machao String转Long
     *
     * @param str
     * @return
     */
    public static Long str2Long(String str) {
        if (isNumeric(str)) {
            return Long.valueOf(str);
        } else {
            return null;
        }
    }

    /**
     * 作者：machao String转Integer
     *
     * @param str
     * @return
     */
    public static Integer str2Integer(String str) {
        if (isNumeric(str)) {
            return Integer.valueOf(str);
        } else {
            return null;
        }
    }

    /**
     * 作者：machao String转Integer
     *
     * @param str
     * @return
     */
    public static int str2Int(String str) {
        if (isNumeric(str)) {
            return Integer.valueOf(str);
        } else {
            return 0;
        }
    }

    /**
     * 作者：machao Object转Long
     *
     * @return
     */
    public static Long obj2Long(Object obj) {
        if (obj != null) {
            try {
                return Long.valueOf(obj.toString());
            } catch (Exception e) {
                logger.info("obj2Long：字符串[" + obj.toString() + "]不全是数字!" + e);
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * 作者：machao Object转String
     *
     * @return
     */
    public static String obj2Str(Object obj) {
        if (obj != null) {
            return obj.toString();
        } else {
            return null;
        }
    }

    /**
     * 作者：machao String转boolean
     *
     * @param str
     * @return
     */
    public static boolean stringToBoolean(String str) {
        return "true".equals(str) ? true : false;
    }

    /**
     * 作者：machao Object转String 或者空串
     *
     * @return
     */
    public static String obj2Empty(Object obj) {
        if (obj != null) {
            return obj.toString();
        } else {
            return "";
        }
    }

    private static final Pattern p2 = Pattern.compile("[0-9]*");

    /**
     * 作者：machao 判断字符串是数字
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        if (str == null || "".equals(str)) {
            return false;
        }
        Matcher isNum = p2.matcher(str);
        if (!isNum.matches()) {
            logger.warn("字符串[" + str + "]不全是数字!");
            return false;
        }
        return true;
    }

    /**
     * 根据报案经办人的姓名和code组合 返回报案经办人名称
     *
     * @param transactNameCode
     * @return String
     */
    public static String getTransactName(String transactNameCode) {
        if (transactNameCode == null || "".equals(transactNameCode)) {
            return "";
        } else if (transactNameCode.indexOf("(") > -1) {
            return transactNameCode.substring(0, transactNameCode.indexOf("("));
        } else {
            return transactNameCode;
        }
    }

    /**
     * 报文的时间格式转化成字符串格式的时间
     *
     * @param date
     * @return
     */
    public static String xmlStringToString(String date) {
        if (date != null && (date.trim().indexOf("+") > -1)) {
            return date.substring(0, date.indexOf("+"));
        }
        return date;
    }

    /**
     * 人意险币种转换
     *
     * @return
     */
    public static String getCurrencyName(String currency) {
        String currencyName = null;
        if (currency == null) {
            return "";
        }
        if ("01".equals(currency)) {
            currencyName = "人民币";
        } else if ("04".equals(currency)) {
            currencyName = "日元";
        } else if ("03".equals(currency)) {
            currencyName = "港元";
        } else if ("02".equals(currency)) {
            currencyName = "美元";
        } else if ("05".equals(currency)) {
            currencyName = "德国马克";
        } else if ("10".equals(currency)) {
            currencyName = "法国法郎";
        } else if ("06".equals(currency)) {
            currencyName = "英镑";
        } else if ("07".equals(currency)) {
            currencyName = "欧元";
        } else if ("08".equals(currency)) {
            currencyName = "新加波元";
        } else if ("09".equals(currency)) {
            currencyName = "加拿大元";
        } else if ("11".equals(currency)) {
            currencyName = "意大利里拉";
        } else if ("12".equals(currency)) {
            currencyName = "澳大利亚元";
        } else if ("13".equals(currency)) {
            currencyName = "荷兰盾";
        } else if ("14".equals(currency)) {
            currencyName = "瑞士法郎";
        } else if ("15".equals(currency)) {
            currencyName = "比利时法朗";
        } else if ("16".equals(currency)) {
            currencyName = "瑞典克朗";
        } else if ("17".equals(currency)) {
            currencyName = "挪威克朗";
        } else if ("18".equals(currency)) {
            currencyName = "丹麦克朗";
        } else if ("19".equals(currency)) {
            currencyName = "奥地利先令";
        } else if ("20".equals(currency)) {
            currencyName = "澳门元";
        } else if ("21".equals(currency)) {
            currencyName = "芬兰马克";
        } else if ("25".equals(currency)) {
            currencyName = "新西兰元";
        } else if ("23".equals(currency)) {
            currencyName = "记账瑞士法郎";
        } else if ("22".equals(currency)) {
            currencyName = "马来西亚林吉特";
        } else if ("24".equals(currency)) {
            currencyName = "外汇人民币";
        } else if ("41".equals(currency)) {
            currencyName = "台湾元";
        } else if ("26".equals(currency)) {
            currencyName = "巴西里亚尔";
        } else if ("27".equals(currency)) {
            currencyName = "西班牙比赛塔";
        } else if ("28".equals(currency)) {
            currencyName = "印尼盾";
        } else if ("29".equals(currency)) {
            currencyName = "印度卢比";
        } else if ("33".equals(currency)) {
            currencyName = "韩国元";
        } else if ("37".equals(currency)) {
            currencyName = "菲律宾比索";
        } else {
            currencyName = "其他";
        }
        return currencyName;
    }

    /**
     * 人意险缴费方式
     *
     * @param methodPayment
     * @return
     */
    public static String getMethodPaymentName(String methodPayment) {
        if ("1".equals(methodPayment)) {
            return "现金";
        } else if ("2".equals(methodPayment)) {
            return "转账";
        } else if ("3".equals(methodPayment)) {
            return "其他";
        }
        return "";
    }

    private static final Pattern p3 = Pattern.compile("[0-9]*");

    /**
     * 根据15位或者18位身份证号码判断性别,按照系统参数表attr_id=161获取
     *
     * @param idCard
     * @return
     */
    public static String judgeGenderByIdCard(String idCard) {
        if (StringUtils.isNotBlank(idCard)) {
            if (idCard.length() == 15 && p3.matcher(idCard).matches()) {
                // 身份证号码15位，都是数字，最后一位为奇数则为男否则为女
                if (Integer.valueOf(idCard.substring(idCard.length() - 1, idCard.length())) % 2 == 0) {
                    return "1010050002"; // 女
                } else {
                    return "1010050001"; // 男
                }
            } else if (idCard.length() == 18 && p3.matcher(idCard.substring(0, idCard.length() - 1)).matches()) {
                // 身份证号码18位，除最后一位其他都是数字,倒数第二位为奇数则为男否则为女
                if (Integer.valueOf(idCard.substring(idCard.length() - 2, idCard.length() - 1)) % 2 == 0) {
                    return "1010050002"; // 女
                } else {
                    return "1010050001"; // 男
                }
            }
        }
        return "2"; // 未知
    }


    /**
     * 根据15位或者18位身份证号码构造出生日期
     */
    public static String constructBirthdayByIdCard(String idCard) {
        String birthday = "";
        if (StringUtils.isNotBlank(idCard)) {
            if (idCard.length() == 15 && p3.matcher(idCard).matches()) {
                String year = "19" + idCard.substring(6, 8);
                String month = idCard.substring(8, 10);
                String day = idCard.substring(10, 12);
                birthday = year + " " + month + "-" + day;
            } else if (idCard.length() == 18) {
                String year = idCard.substring(6, 10);
                String month = idCard.substring(10, 12);
                String day = idCard.substring(12, 14);
                birthday = year + "-" + month + "-" + day;
            }
        }
        return birthday;
    }

    /**
     * 把15位身份证号转换成18位身份证号码 出生月份前加"19"(20世纪才使用的15位身份证号码),最后一位加校验码
     */
    public static String transformationIdFrom15To18(String custNo) {
        String idCardNo = null;
        if (custNo != null && custNo.trim().length() == 15) {
            custNo = custNo.trim();
            StringBuffer newIdCard = new StringBuffer(custNo);
            newIdCard.insert(6, "19");
            newIdCard.append(trasformationLastNo(newIdCard.toString()));
            idCardNo = newIdCard.toString();
        }
        return idCardNo;
    }

    /**
     * 生成身份证最后一位效验码
     */
    private static String trasformationLastNo(String id) {
        char pszSrc[] = id.toCharArray();
        int iS = 0;
        int iW[] = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
        char szVerCode[] = new char[]{'1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'};
        int i;
        for (i = 0; i < id.length(); i++) {
            iS += (pszSrc[i] - '0') * iW[i];
        }
        int iY = iS % 11;
        return String.valueOf(szVerCode[iY]);
    }

    /**
     * 把18位身份证号转换成15位身份证号码 但存在身份证号码重复
     */
    public static String transformationIdFrom18To15(String sCustno) {
        String first7No = sCustno.substring(0, 6);
        String lastNo = sCustno.substring(8, sCustno.length() - 1);
        return first7No + lastNo;
    }

    /**
     * 转换 续保优质客户等级
     */
    public static String transHqcustomerFlag(String hqcustomerFlag) {
        if (StringUtils.isNotBlank(hqcustomerFlag) && hqcustomerFlag.length() > 2) {
            // 客户等级标签
            String customerLevel = ObjectUtil.getCustomerLevel(hqcustomerFlag.substring(0, 1));
            // 出险次数
            String claimCaseCount = ObjectUtil.getClaimCaseCount(hqcustomerFlag.substring(1, 2));
            // 客户需求分群标签
            String customerReqFlag = ObjectUtil.getCustomerReqFlag(hqcustomerFlag.substring(2));
            return customerLevel + claimCaseCount + customerReqFlag;
        }
        return hqcustomerFlag;
    }

    /**
     * 客户等级标签
     *
     * @param isVIP
     * @return
     * @author machao
     * @date 2015-8-14
     */
    private static String getCustomerLevel(String isVIP) {
        if ("1".equals(isVIP)) {
            return "钻石";// 钻石级客户
        } else if ("2".equals(isVIP)) {
            return "白金";// 白金级客户
        } else if ("3".equals(isVIP)) {
            return "黄金";// 黄金级客户
        } else if ("4".equals(isVIP)) {
            return "白银";// 白银级客户
        } else if ("5".equals(isVIP)) {
            return "普通";// 普通客户
        } else {
            return "";
        }
    }

    /**
     * 出险次数
     *
     * @param countFlag
     * @return
     * @author machao
     * @date 2015-8-14
     */
    private static String getClaimCaseCount(String countFlag) {
        if ("A".equals(countFlag)) {
            return "0次";
        } else if ("B".equals(countFlag)) {
            return "1次";
        } else if ("C".equals(countFlag)) {
            return "2次";
        } else if ("D".equals(countFlag)) {
            return "3次";
        } else if ("E".equals(countFlag)) {
            return "4次";
        } else if ("F".equals(countFlag)) {
            return "5次";// 5次以上
        } else if ("G".equals(countFlag)) {
            return "-2";// 连续2年未出险 BY ZIMING.LI 2015-01-14
        } else if ("H".equals(countFlag)) {
            return "-3";// 连续3年未出险 BY ZIMING.LI 2015-01-14
        } else {
            return "";
        }
    }

    /**
     * 客户需求分群标签
     *
     * @param isVIP
     * @return
     * @author machao
     * @date 2015-8-14
     */
    private static String getCustomerReqFlag(String isVIP) {
        if ("O".equals(isVIP)) {
            return "新车";// 新车一族
        } else if ("P".equals(isVIP)) {
            return "女性";// 女性客户
        } else if ("Q".equals(isVIP)) {
            return "成功";// 成功人士
        } else if ("R".equals(isVIP)) {
            return "男性";// 男性驾车族
        } else {
            return "";
        }
    }

    /**
     * 比较身份证号码是否相等 身份证号码长度要么15，要么18，其他直接进行比较 15位与18位身份证进行比较的时候，先将15位的转换为18位身份证号码
     *
     * @param id1
     * @param id2
     * @return
     */
    public static boolean compareIdCards(String id1, String id2) {
        if (StringUtils.isNotBlank(id1) && StringUtils.isNotBlank(id2) && (id1.length() == 15 || id1.length() == 18)
                && (id2.length() == 15 || id2.length() == 18)) {
            if (id1.length() == id2.length()) {
                return StringUtils.equals(id1, id2);
            } else {
                if (id1.length() == 15) {
                    return StringUtils.equals(ObjectUtil.transformationIdFrom15To18(id1), id2);
                } else {
                    return StringUtils.equals(id1, ObjectUtil.transformationIdFrom15To18(id2));
                }
            }
        } else {
            return StringUtils.equals(id1, id2);
        }
    }

    /**
     * getPaintInsurances:获取 车身油漆单独损伤险列表 Date:2014-9-19
     *
     * @return List<String> {"11022336","11022528"...}
     * @author houyongfeng
     * @since JDK 1.6
     */
    public static List<String> getPaintInsurances() {
        List<String> paintInsurances = new ArrayList<String>();
        paintInsurances.add("11022336");// 车身油漆单独损伤险 AUTOCOMPRENHENSIVEINSURANCE2006PRODUCT
        paintInsurances.add("11022528");// 车身油漆单独损伤险 AUTOCOMPRENHENSIVEINSURANCE2007PRODUCT
        paintInsurances.add("11023528");// 车身油漆单独损伤险 AUTOCOMPRENHENSIVEINSURANCE2008PRODUCT
        paintInsurances.add("11023810");// 车身油漆单独损伤险 AUTOCOMPRENHENSIVEINSURANCE2009PRODUCT
        paintInsurances.add("11021734");// 车身油漆单独损伤险 TOYOTAHGPRODUCT
        paintInsurances.add("11021934");// 车身油漆单独损伤险 TOYOTARZPRODUCT
        paintInsurances.add("11022628");// 车身油漆单独损伤险 TSAUTOCOMPRENHENSIVEINSURANCE2008PRODUCT
        paintInsurances.add("11024110");// 车身油漆单独损伤 TSAUTOCOMPRENHENSIVEINSURANCE200910PRODUCT
        paintInsurances.add("11022728");// 车身油漆单独损伤险 TSAUTOCOMPRENHENSIVEINSURANCE2009PRODUCT
        return paintInsurances;
    }

    /**
     * 浮点型转换成如下格式字符串
     *
     * @param douNum    浮点型数值
     * @param formatNum 格式数字
     * @return
     * @author machao
     * @date 2014-11-12 格式形式如下： 1 ### 2 ##############.##
     */
    public static String doubleToStr(BigDecimal douNum, int formatNum) {
        if (douNum == null) {
            return null;
        }
        // 指定转换的格式
        String doubleStr = null;
        if (formatNum == 1) {
            DecimalFormat df = new DecimalFormat("###");
            doubleStr = df.format(douNum);
        } else if (formatNum == 2) {
            DecimalFormat df = new DecimalFormat("##############.##");
            doubleStr = df.format(douNum);
        }
        return doubleStr;
    }

    /**
     * JAVA 将字符串中":"、"'"替换成"："、"""
     *
     * @param str
     * @return
     * @date 2015-02-11
     */
    public static String replaceEnglishChar(String str) {
        if (str == null || "".equals(str)) {
            return str;
        }
        return str.replaceAll(":", "：").replaceAll("'", "\"");
    }

    /**
     * 判断案件类型
     *
     * @param smallAmount
     * @param object
     * @return
     */
    public static String getCaseType(String smallAmount, Object object) {
        if ("1".equals(smallAmount)) {
            return "A"; // A 表示快捷理赔案件
        } else if (object != null && !"".equals(object.toString())) {
            return "C"; // C 表示所有含人伤的案件
        } else {
            return "B"; // B 表示其他类型的案件
        }
    }

    /**
     * 接口单一对象，转化为字符串，用于打印日志
     *
     * @param obj
     */
    public static String log(Object obj) {
        StringBuffer strBuf = new StringBuffer();
        if (obj == null) {
            return strBuf.toString();
        }
        try {
            Class<?> cls = obj.getClass();
            Method methlist[] = cls.getMethods();//.getDeclaredMethods();
            for (int i = 0; i < methlist.length; i++) {
                Method m = methlist[i];
                String methodName = m.getName();
                String sh = methodName.substring(0, 3);
                if ("get".equals(sh)) {
                    Class<?>[] cc = null;
                    Object[] oo = null;
                    Method meth = cls.getMethod(methodName, cc);
                    Object retobj = meth.invoke(obj, oo);
                    strBuf.append("[m]" + methodName + "[v]" + retobj + "\n");
                }
            }
        } catch (Exception e) {
            logger.error(e);
        }
        return strBuf.toString();
    }

    /**
     * json格式的字符串含有反斜杠和单引号时传值会破坏json的数据格式，故进行转义处理
     * 字符串替换：反斜杠替换成\\，单引号替换成\'，其他特殊字符不会破坏json数据格式，故不替换
     *
     * @param str
     * @return 替换后的字符串
     * @author machao
     * @date 2015-5-13
     */
    public static String escapeStr(String str) {
        if (StringUtils.isBlank(str)) {
            return "";
        } else {
            /*
             * replaceAll的参数使用的是正则表达式，特殊字符需使用\来转义
             * java中\\输出\，\\\\代表\\，正则匹配时，第一个\是转义字符，第二\才是需要替换的字符
             */
            String escapeStr = str.replaceAll("\\\\", "\\\\\\\\").replaceAll("\'", "\\\\'");
            return escapeStr;
        }
    }

    /**
     * 手机号码的校验,规则:以1开头11位纯数字
     *
     * @param phoneNum 手机号码
     * @return
     * @author c_wangyu-082
     * @date 2015-6-19 下午2:00:26
     */
    public static boolean phoneNumVerify(String phoneNum) {
        if (StringUtils.isBlank(phoneNum)) {
            return false;
        }
        // 由于话务有时会带入加0开头的12位手机号码,删除开头的0 判断后面的11位是否满足手机号码校验--2015-7-29
        if (phoneNum.length() > 11 && phoneNum.startsWith("0")) {
            phoneNum = phoneNum.substring(1);
        }
        Pattern p = Pattern.compile("^1\\d{10}$");
        Matcher m = p.matcher(phoneNum);
        boolean flag = m.matches();
        if (!flag) {
            logger.warn("手机号码[" + phoneNum + "]不符合手机号码校验规范!");
        }
        return flag;
    }

    /**
     * 手机号码的校验,规则:首位为01，且长度12位，去0.
     *
     * @param phoneNum 手机号码
     * @return
     */
    public static String getPhone01(String phoneNum) {
        if (StringUtils.isBlank(phoneNum)) {
            return phoneNum;
        }
        // 首位为01，且长度12位，去0.
        if (phoneNum.length() == 12 && phoneNum.startsWith("01")) {
            logger.warn("手机号码的校验,规则:首位为01，且长度12位，去0；phoneNum=" + phoneNum);
            phoneNum = phoneNum.substring(1);
        }
        return phoneNum;
    }


    /**
     * NULL转成字符串并去空格.
     *
     * @param aStr 字符串.
     * @return String
     */
    public static String nullToStr(Object aStr) {
        return aStr == null ? "" : aStr.toString().trim();
    }

    /**
     * 作者：machao 自动将对象中所有的属性全部打印出来
     *
     * @param object
     * @return
     */
    public static String toString(Object object) {
        return new ReflectionToStringBuilder(object, ToStringStyle.DEFAULT_STYLE).toString();
    }
}