package mc.utils;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author machao
 * @CreateDate 2019/3/14 13:08
 * @Version 1.0
 * @Describe 文件工具类
 */
public class FileUtil {

    private static final Logger logger = LoggerFactory.getLogger(FileUtil.class);

    /**
     * 通过文件路径获取文件流
     *
     * @param filePath 文件路径
     * @return 字节流
     */
    public static byte[] getLocalFile(String filePath) {
        try {
            InputStream in = new FileInputStream(filePath);
            byte[] bytes = IOUtils.toByteArray(in);
            for (int i = 0; i < bytes.length; ++i) {
                if (bytes[i] < 0) {
                    // 调整异常数据
                    bytes[i] += 256;
                }
            }
            return bytes;
        } catch (IOException e) {
            throw new RuntimeException("获取本地文件流出错");
        }
    }
}
