package mc.utils;

import com.google.common.collect.Maps;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author: machao
 * @date: 2018/6/6 09:58
 */
public class HttpClientUtil {

    private static final Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);

    private static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    private static final MediaType MULTI_PART
            = MediaType.parse("multipart/form-data; charset=utf-8");

    private static final MediaType TEXT_XML
            = MediaType.parse("text/xml; charset=utf-8");


    /**
     * 获取一个代理的httpClient
     *
     * @param proxyHostOrIp 代理服务器ip
     * @param port          代理服务器端口
     * @param username      代理服务器用户名
     * @param password      代理服务器密码
     * @return
     */
    public static OkHttpClient getProxyClient(String proxyHostOrIp, int port, String username, String password) {
        logger.info(String.format("代理服务器ip:%s,端口%d", proxyHostOrIp, port));
        logger.info(String.format("代理服务器用户名:%s,密码%s", username, password));
        SocketAddress sa = new InetSocketAddress(proxyHostOrIp, port);
        Proxy proxy = new Proxy(Proxy.Type.HTTP, sa);
        OkHttpClient client = Spring1Util.getBean(OkHttpClient.class);
        return client.newBuilder().proxy(proxy).proxyAuthenticator((route, response) -> {
            if (response.request().header("Proxy-Authorization") != null) {
                // Give up, we've already failed to authenticate.
                return null;
            }
            String credential = Credentials.basic(username, password);
            return response.request().newBuilder()
                    .header("Proxy-Authorization", credential)
                    .build();
        }).build();
    }

    /**
     * 获取一个代理的httpClient
     *
     * @param proxyHostOrIp 代理服务器ip
     * @param port          代理服务器端口
     * @return
     */
    public static OkHttpClient getProxyClient(String proxyHostOrIp, int port) {
        logger.info(String.format("代理服务器ip:%s,端口%d", proxyHostOrIp, port));
        SocketAddress sa = new InetSocketAddress(proxyHostOrIp, port);
        Proxy proxy = new Proxy(Proxy.Type.HTTP, sa);
        OkHttpClient client = Spring1Util.getBean(OkHttpClient.class);
        return client.newBuilder().proxy(proxy).build();
    }

    /**
     * 获取一个代理的httpClient
     *
     * @param proxyHostOrIp 代理服务器ip
     * @param port          代理服务器端口
     * @param username      代理服务器用户名
     * @param password      代理服务器密码
     * @return
     */
    public static OkHttpClient getProxyClient(String proxyHostOrIp, int port, String username, String password, long writeTimeout, long readTimeout) {
        logger.info(String.format("代理服务器ip:%s,端口%d", proxyHostOrIp, port));
        logger.info(String.format("代理服务器用户名:%s,密码%s", username, password));
        SocketAddress sa = new InetSocketAddress(proxyHostOrIp, port);
        Proxy proxy = new Proxy(Proxy.Type.HTTP, sa);
        OkHttpClient client = Spring1Util.getBean(OkHttpClient.class);
        return client.newBuilder().proxy(proxy).proxyAuthenticator((route, response) -> {
            if (response.request().header("Proxy-Authorization") != null) {
                // Give up, we've already failed to authenticate.
                return null;
            }
            String credential = Credentials.basic(username, password);
            return response.request().newBuilder()
                    .header("Proxy-Authorization", credential)
                    .build();
        }).writeTimeout(writeTimeout, TimeUnit.SECONDS).readTimeout(readTimeout, TimeUnit.SECONDS).build();
    }

    /**
     * 获取一个代理的httpClient
     *
     * @param proxyHostOrIp 代理服务器ip
     * @param port          代理服务器端口
     * @return
     */
    public static OkHttpClient getProxyClient(String proxyHostOrIp, int port, long writeTimeout, long readTimeout) {
        logger.info(String.format("代理服务器ip:%s,端口%d", proxyHostOrIp, port));
        SocketAddress sa = new InetSocketAddress(proxyHostOrIp, port);
        Proxy proxy = new Proxy(Proxy.Type.HTTP, sa);
        OkHttpClient client = Spring1Util.getBean(OkHttpClient.class);
        return client.newBuilder().proxy(proxy).writeTimeout(writeTimeout, TimeUnit.SECONDS).readTimeout(readTimeout, TimeUnit.SECONDS).build();
    }


    /**
     * 获取一个httpClient
     *
     * @return
     */
    public static OkHttpClient getClient() {
        OkHttpClient client = Spring1Util.getBean(OkHttpClient.class);
        return client;
    }


    /**
     * 获取一个httpClient
     *
     * @return
     */
    public static OkHttpClient getClient(long writeTimeout, long readTimeout) {
        OkHttpClient client = Spring1Util.getBean(OkHttpClient.class);
        return client.newBuilder().readTimeout(readTimeout, TimeUnit.SECONDS).writeTimeout(writeTimeout, TimeUnit.SECONDS).build();
    }

    private static String appendParams(final String url, Map<String, String> params) {
        StringBuilder sb = new StringBuilder();
        sb.append(url);
        if (url.contains("?")) {
            for (String key : params.keySet()) {
                sb.append(String.format("&%s=%s", key, params.get(key)));
            }
        } else {
            int i = 0;
            for (String key : params.keySet()) {
                if (i == 0) {
                    sb.append(String.format("?%s=%s", key, params.get(key)));
                } else {
                    sb.append(String.format("&%s=%s", key, params.get(key)));
                }
                i++;
            }
        }
        return sb.toString();
    }

    /**
     * 客户端调用 http协议 GET方法
     *
     * @param client 通过本类中的getProxyClient()方法或者getClient()方法获取
     * @param url    访问地址
     * @param params 请求参数
     * @return String文本类响应体
     */
    public static String httpGet(OkHttpClient client, final String url, Map<String, String> params) {
        return httpGet(client, url, params, null);
    }

    /**
     * 客户端调用 http协议 GET方法
     *
     * @param client  通过本类中的getProxyClient()方法或者getClient()方法获取
     * @param url     访问地址
     * @param params  请求参数
     * @param headers http请求头
     * @return String文本类响应体
     */
    public static String httpGet(OkHttpClient client, final String url, Map<String, String> params, Map<String, String> headers) {
        return httpGet(client, url, params, headers, "utf-8");
    }

    /**
     * 客户端调用 http协议 GET方法
     *
     * @param client  通过本类中的getProxyClient()方法或者getClient()方法获取
     * @param url     访问地址
     * @param params  请求参数
     * @param headers http请求头
     * @param charset 指定文本响应体解析编码格式如:utf-8
     * @return String文本类响应体
     */
    public static String httpGet(OkHttpClient client, final String url, Map<String, String> params, Map<String, String> headers, String charset) {
        Assert.hasText(url, "url不能为空");
        if (params == null) {
            params = Maps.newHashMap();
        }

        if (headers == null) {
            headers = Maps.newHashMap();
        }

        String urlNew = appendParams(url, params);

        //拼接headers
        Headers.Builder headersBuilder = new Headers.Builder();
        for (String key : headers.keySet()) {
            headersBuilder.set(key, headers.get(key));
        }
        Request request = new Request.Builder().url(urlNew).headers(headersBuilder.build()).get().build();

        Response response = null;
        try {
            response = client.newCall(request).execute();
            return new String(response.body().bytes(), charset);
        } catch (IOException e) {
            throw new RuntimeException("HttpClientUtil类中的httpGet方法调用地址url:" + url + "失败", e);
        }
    }

    /**
     * 客户端调用 http协议 GET方法
     *
     * @param client  通过本类中的getProxyClient()方法或者getClient()方法获取
     * @param url     访问地址
     * @param params  请求参数
     * @param headers http请求头
     * @return 字节流响应体
     */
    public static byte[] httpGet2(OkHttpClient client, final String url, Map<String, String> params, Map<String, String> headers) {
        Assert.hasText(url, "url不能为空");
        if (params == null) {
            params = Maps.newHashMap();
        }

        if (headers == null) {
            headers = Maps.newHashMap();
        }

        String urlNew = appendParams(url, params);

        //拼接headers
        Headers.Builder headersBuilder = new Headers.Builder();
        for (String key : headers.keySet()) {
            headersBuilder.set(key, headers.get(key));
        }
        Request request = new Request.Builder().url(urlNew).headers(headersBuilder.build()).get().build();

        Response response = null;
        try {
            response = client.newCall(request).execute();
            return response.body().bytes();
        } catch (IOException e) {
            throw new RuntimeException("HttpClientUtil类中的httpGet方法调用地址url:" + url + "失败", e);
        }
    }


    /**
     * 客户端 http协议 POST请求
     *
     * @param client 通过本类中的getProxyClient()方法或者getClient()方法获取
     * @param url    访问地址
     * @param params 请求参数
     * @return String文本类响应体
     */
    public static String httpPost(OkHttpClient client, String url, Map<String, String> params) {
        return httpPost(client, url, params, null);
    }

    /**
     * 客户端 http协议 POST请求
     *
     * @param client  通过本类中的getProxyClient()方法或者getClient()方法获取
     * @param url     访问地址
     * @param params  请求参数
     * @param headers http请求参数头
     * @return String文本类型响应体
     */
    public static String httpPost(OkHttpClient client, String url, Map<String, String> params, Map<String, String> headers) {
        return httpPost(client, url, params, headers, "utf-8");
    }

    /**
     * 客户端 http协议 POST请求
     *
     * @param client  通过本类中的getProxyClient()方法或者getClient()方法获取
     * @param url     访问地址
     * @param params  请求参数
     * @param headers http请求头
     * @param charset 指定文本响应体解析编码格式如:utf-8
     * @return String文本类型响应体
     */
    public static String httpPost(OkHttpClient client, String url, Map<String, String> params, Map<String, String> headers, String charset) {
        Assert.hasText(url, "url不能为空");
        if (null == params) {
            params = Maps.newHashMap();
        }

        if (null == headers) {
            headers = Maps.newHashMap();
        }

        FormBody.Builder builder = new FormBody.Builder();
        //拼接参数
        for (String key : params.keySet()) {
            builder.add(key, params.get(key));
        }

        //拼接headers
        Headers.Builder headersBuilder = new Headers.Builder();
        for (String key : headers.keySet()) {
            headersBuilder.set(key, headers.get(key));
        }

        RequestBody body = builder.build();
        Request request = new Request.Builder().url(url).headers(headersBuilder.build()).post(body).build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
            return new String(response.body().bytes(), charset);
        } catch (IOException e) {
            throw new RuntimeException("HttpClientUtil类中的httpPost方法调用地址url:" + url + "失败", e);
        }
    }

    /**
     * 客户端 http协议 POST请求
     *
     * @param client 通过本类中的getProxyClient()方法或者getClient()方法获取
     * @param url    访问地址
     * @param json   请求体为json字符串形式
     * @return String文本类型响应体
     */
    public static String httpPost(OkHttpClient client, String url, String json) {
        return httpPost(client, url, json, null);
    }


    /**
     * 客户端 http协议 POST请求
     *
     * @param client  通过本类中的getProxyClient()方法或者getClient()方法获取
     * @param url     访问地址
     * @param json    请求体为json字符串形式
     * @param headers http请求头
     * @return String文本类型响应体
     */
    public static String httpPost(OkHttpClient client, String url, String json, Map<String, String> headers) {
        return httpPost(client, url, json, headers, "utf-8");
    }

    /**
     * 客户端 http协议 POST请求
     *
     * @param client  通过本类中的getProxyClient()方法或者getClient()方法获取
     * @param url     访问地址
     * @param json    请求体为json字符串形式
     * @param headers http请求头
     * @param charset 指定文本响应体解析编码格式如:utf-8
     * @return String文本类型响应体
     */
    public static String httpPost(OkHttpClient client, String url, String json, Map<String, String> headers, String charset) {
        Assert.notNull(client, "okHttpClient不能为null");
        Assert.hasText(url, "url不能为空");
        Assert.hasText(json, "json不能为空");

        if (headers == null) {
            headers = Maps.newHashMap();
        }

        //拼接headers
        Headers.Builder headersBuilder = new Headers.Builder();
        for (String key : headers.keySet()) {
            headersBuilder.set(key, headers.get(key));
        }

        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder().url(url).headers(headersBuilder.build()).post(body).build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
            return new String(response.body().bytes(), charset);
        } catch (IOException e) {
            throw new RuntimeException("HttpClientUtil类中的httpPost方法调用地址url:" + url + "失败", e);
        }
    }


    /**
     * 客户端 http协议 POST请求
     *
     * @param client  通过本类中的getProxyClient()方法或者getClient()方法获取
     * @param url     访问地址
     * @param json    请求体为json字符串形式
     * @param headers http请求头
     * @return InputStream
     */
    public static byte[] httpPost2(OkHttpClient client, String url, String json, Map<String, String> headers) {
        Assert.notNull(client, "okHttpClient不能为null");
        Assert.hasText(url, "url不能为空");
        Assert.hasText(json, "json不能为空");
        InputStream ins = null;
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        if (headers == null) {
            headers = Maps.newHashMap();
        }
        //拼接headers
        Headers.Builder headersBuilder = new Headers.Builder();
        for (String key : headers.keySet()) {
            headersBuilder.set(key, headers.get(key));
        }

        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder().url(url).headers(headersBuilder.build()).post(body).build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
            ins = response.body().byteStream();
            //创建一个Buffer字符串
            byte[] buffer = new byte[1024];
            //每次读取的字符串长度，如果为-1，代表全部读取完毕
            int len = 0;
            //使用一个输入流从buffer里把数据读取出来
            while ((len = ins.read(buffer)) != -1) {
                //用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
                outStream.write(buffer, 0, len);
            }
            return outStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("HttpClientUtil类中的httpPost2方法调用地址url:" + url + "失败", e);
        } finally {
            if (response != null) {
                response.close();
            }
            if (ins != null) {
                try {
                    ins.close();
                } catch (IOException e) {
                    throw new RuntimeException("HttpClientUtil类中的httpPost2方法调用地址url:" + url + "失败", e);
                }
            }
            if (outStream != null) {
                try {
                    outStream.close();
                } catch (IOException e) {
                    throw new RuntimeException("HttpClientUtil类中的httpPost2方法调用地址url:" + url + "失败", e);
                }
            }
        }
    }


    /**
     * 客户端 http协议 POST请求
     *
     * @param client  通过本类中的getProxyClient()方法或者getClient()方法获取
     * @param url     访问地址
     * @param data    请求体为字节流的形式
     * @param headers http请求头
     * @param charset 指定文本响应体解析编码格式如:utf-8
     * @return String文本类型响应体
     */
    public static String httpPost(OkHttpClient client, String url, byte[] data, Map<String, String> headers, String charset) {
        Assert.notNull(client, "okHttpClient不能为null");
        Assert.hasText(url, "url不能为空");
        Assert.notNull(data, "data不能为空");

        if (headers == null) {
            headers = Maps.newHashMap();
        }

        //拼接headers
        Headers.Builder headersBuilder = new Headers.Builder();
        for (String key : headers.keySet()) {
            headersBuilder.set(key, headers.get(key));
        }

        RequestBody body = RequestBody.create(null, data);
        Request request = new Request.Builder().url(url).headers(headersBuilder.build()).post(body).build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
            return new String(response.body().bytes(), charset);
        } catch (IOException e) {
            throw new RuntimeException("HttpClientUtil类中的httpPost方法调用地址url:" + url + "失败", e);
        }
    }

    /**
     * 客户端 http协议 POST请求
     *
     * @param client  通过本类中的getProxyClient()方法或者getClient()方法获取
     * @param url     访问地址
     * @param dataXMl xml形式
     * @param headers http请求头
     * @param charset 指定文本响应体解析编码格式如:utf-8
     * @return String文本类型响应体
     */
    public static String httpPostXML(OkHttpClient client, String url, String dataXMl, Map<String, String> headers, String charset) {
        Assert.notNull(client, "okHttpClient不能为null");
        Assert.hasText(url, "url不能为空");
        Assert.hasText(dataXMl, "dataXMl不能为空");

        if (headers == null) {
            headers = Maps.newHashMap();
        }

        //拼接headers
        Headers.Builder headersBuilder = new Headers.Builder();
        for (String key : headers.keySet()) {
            headersBuilder.set(key, headers.get(key));
        }

        RequestBody body = RequestBody.create(TEXT_XML, dataXMl);
        Request request = new Request.Builder().url(url).headers(headersBuilder.build()).post(body).build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
            return new String(response.body().bytes(), charset);
        } catch (IOException e) {
            throw new RuntimeException("HttpClientUtil类中的httpPost方法调用地址url:" + url + "失败", e);
        }
    }

    /**
     * @author: machao
     * @date: 2019/2/18 16:57
     * @description:
     */
    @Service
    @Lazy(false)
    private static class Spring1Util implements ApplicationContextAware {
        private static ApplicationContext applicationContext;

        /**
         * 实现ApplicationContextAware接口的context注入函数, 将其存入静态变量.
         */
        @Override
        public void setApplicationContext(ApplicationContext applicationContext) {
            Spring1Util.applicationContext = applicationContext; // NOSONAR
        }

        /**
         * 取得存储在静态变量中的ApplicationContext.
         */
        public static ApplicationContext getApplicationContext() {
            checkApplicationContext();
            return applicationContext;
        }

        /**
         * 从静态变量ApplicationContext中取得Bean, 自动转型为所赋值对象的类型.
         */
        @SuppressWarnings("unchecked")
        public static <T> T getBean(String name) {
            checkApplicationContext();
            return (T) applicationContext.getBean(name);
        }

        /**
         * 从静态变量ApplicationContext中取得Bean, 自动转型为所赋值对象的类型.
         */
        @SuppressWarnings("unchecked")
        public static <T> T getBean(Class<T> clazz) {
            checkApplicationContext();
            return (T) applicationContext.getBean(clazz);
        }

        /**
         * 清除applicationContext静态变量.
         */
        public static void cleanApplicationContext() {
            applicationContext = null;
        }

        private static void checkApplicationContext() {
            if (applicationContext == null) {
                throw new IllegalStateException(
                        "applicaitonContext未注入,请在applicationContext.xml中定义SpringContextHolder");
            }
        }

    }
}
