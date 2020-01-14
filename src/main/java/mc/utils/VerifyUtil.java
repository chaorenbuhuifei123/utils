package mc.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Random;

/**
 * @author machao
 * @CreateDate 2018/12/11 19:59
 * @Version 1.0
 * @Describe 图形验证码工具类
 */
public class VerifyUtil {

    private static final Logger logger = LoggerFactory.getLogger(VerifyUtil.class);

    /**
     * 验证码字符集
     */
    private static final char[] chars = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e',
            'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U',
            'V', 'W', 'X', 'Y', 'Z'};
    // 字符数量
    private static final int SIZE = 4;
    // 干扰线数量
    private static final int LINES = 4;
    // 宽度
    private static final int WIDTH = 80;
    // 高度
    private static final int HEIGHT = 32;
    // 字体大小
    private static final int FONT_SIZE = 30;

    // 内勤图形验证码redis前缀
    public static final String BACK_OFFICE_VERIFY_PREFIX = "INTERNAL:VERIFY:";

    //爱相守图形验证码
    public static final String AXS_VERIFY_PREFIX = "AXS:VERIFY:";

    /**
     * 生成图形验证码
     *
     * @return
     */
    public static Object[] createImage() {
        StringBuffer sb = new StringBuffer();
        // 1.创建空白图片
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        // 2.获取图片画笔
        Graphics graphic = image.getGraphics();
        // 3.设置画笔颜色
        graphic.setColor(new Color(220, 220, 220));
        // 4.绘制矩形背景
        graphic.fillRect(0, 0, WIDTH, HEIGHT);
        // 5.画随机字符
        Random ran = new SecureRandom();
        for (int i = 0; i < SIZE; i++) {
            // 取随机字符索引
            int n = ran.nextInt(chars.length);
            // 设置随机颜色
            graphic.setColor(getRandomColor());
            // 设置字体大小
            graphic.setFont(new Font(null, Font.BOLD + Font.ITALIC, FONT_SIZE));
            // 画字符
            graphic.drawString(chars[n] + "", i * WIDTH / SIZE, HEIGHT * 2 / 3);
            // 记录字符
            sb.append(chars[n]);
        }
        // 6.画干扰线
        for (int i = 0; i < LINES; i++) {
            // 设置随机颜色
            graphic.setColor(getRandomColor());
            // 随机画线
            graphic.drawLine(ran.nextInt(WIDTH), ran.nextInt(HEIGHT), ran.nextInt(WIDTH), ran.nextInt(HEIGHT));
        }

        // 7.返回验证码和图片
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "jpeg", baos);
        } catch (IOException e) {
            logger.info("图形验证码生成出错", e);
        }

        return new Object[]{sb.toString(), DigestUtil.encodeBase64(baos.toByteArray())};

    }

    /**
     * 随机取色
     */
    public static Color getRandomColor() {
        Random ran = new SecureRandom();
        Color color = new Color(ran.nextInt(128), ran.nextInt(128), ran.nextInt(128));
        return color;
    }
}
