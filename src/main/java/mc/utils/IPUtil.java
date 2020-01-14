package mc.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Enumeration;


/**
 * @author machao
 * @version 创建时间：2017年6月20日 上午9:16:16
 * 类说明:ip获取
 */
public class IPUtil {
    /**
     * Logger for this class
     */
    private static final Logger logger = LoggerFactory.getLogger(IPUtil.class);
    private static String LOACAL_IP = null;


    /**
     * getRemoteIP:获取远程请求客户端的外网IP <br/>
     *
     * @param request 请求实体对象
     * @return ip 外网ip<br/>
     */
    public static String getRemoteIP(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }


    /**
     * 获取本地IP
     *
     * @return
     */
    public static synchronized String getLocalIP() {
        if (StringUtil.isNotBlank(LOACAL_IP)) {
            return LOACAL_IP;
        }
        InetAddress addr = null;
        try {
            addr = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            logger.info("异常信息：{}", e);
            return null;
        }

        byte[] ipAddr = addr.getAddress();
        String ipAddrStr = "";
        for (int i = 0; i < ipAddr.length; i++) {
            if (i > 0) {
                ipAddrStr += ".";
            }
            ipAddrStr += ipAddr[i] & 0xFF;
        }
        LOACAL_IP = ipAddrStr;
        return ipAddrStr;
    }

    /**
     * 获取本机ip地址，并自动区分Windows还是linux操作系统
     * linux系统获取 网卡名为 etho的 地址
     * @return String
     */
    public static String getMachineLocalIP() {
        String sIP = "";
        InetAddress ip = null;
        try {
            // 如果是Windows操作系统
            if (isWindowsOS()) {
                ip = InetAddress.getLocalHost();
            }
            // 如果是Linux操作系统
            else {
                boolean bFindIP = false;
                Enumeration<NetworkInterface> netInterfaces = (Enumeration<NetworkInterface>) NetworkInterface
                        .getNetworkInterfaces();
                while (netInterfaces.hasMoreElements()) {
                    if (bFindIP) {
                        break;
                    }
                    NetworkInterface ni = (NetworkInterface) netInterfaces
                            .nextElement();
                    logger.info("网卡名称：" + ni.getName());
                    // 遍历所有ip
                    Enumeration<InetAddress> ips = ni.getInetAddresses();
                    while (ips.hasMoreElements() ) {
                        ip = (InetAddress) ips.nextElement();
                        if ((ip.getHostAddress().endsWith(".0")) || (ip.getHostAddress().endsWith(".1"))) {
                            continue;
                        }
                        if (ip.isSiteLocalAddress() && !ip.isLoopbackAddress() // 127.开头的都是lookback地址
                                && ip.getHostAddress().indexOf(":") == -1 && "eth0".equals(ni.getName())) {
                            logger.info("所取网卡名称：" + ni.getName());
                            bFindIP = true;
                            break;
                        }
                    }

                }
            }
        } catch (Exception e) {
           logger.info("getMachineLocalIP异常",e);
        }

        if (null != ip) {
            sIP = ip.getHostAddress();
        }
        logger.info("返回本机ip：" +sIP);
        return sIP;
    }



    /**
     * 获得主机IP
     *
     * @return String
     */
    public static boolean isWindowsOS() {
        boolean isWindowsOS = false;
        String osName = System.getProperty("os.name");
        if (osName.toLowerCase().indexOf("windows") > -1) {
            isWindowsOS = true;
        }
        return isWindowsOS;
    }

}
