package mc.utils;

import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * 加密的一些帮助工具
 *
 * @author machao
 * @date 2017-02-04
 */
public class DigestUtil {

    /**
     * Used to build output as Hex
     */
    private static final char[] DIGITS_LOWER = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
            'e', 'f'};

    private static final byte[] BJCA_KEY = {-44, 99, 78, -12, -84, -124, -117, 79, -3, -40, -31, 116, 48, -125, -88, 72};

    private static org.slf4j.Logger logger = LoggerFactory.getLogger(DigestUtil.class);

    /**
     * AES CBC 加密
     *
     * @param src
     * @param sKey 密钥
     * @param iv   偏移量
     */
    public static String aesCbcPKCS5PaddingEncrypt(String src, String sKey, String iv)
            throws Exception {
        Cipher cipher = getAesCbcPKCS5PaddingCipher(sKey, iv, Cipher.ENCRYPT_MODE);
        byte[] encrypted = cipher.doFinal(src.getBytes());
        return byte2String(encrypted);
    }

    /**
     * AES CBC 解密
     *
     * @param src
     * @param sKey 密钥
     * @param iv   偏移量
     */
    public static String aesCbcPKCS5PaddingDecrypt(String src, String sKey, String iv)
            throws Exception {
        Cipher cipher = getAesCbcPKCS5PaddingCipher(sKey, iv, Cipher.DECRYPT_MODE);
        byte[] encrypted = string2Byte(src);
        byte[] original = cipher.doFinal(encrypted);
        return new String(original);
    }

    private static String byte2String(byte[] src) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < src.length; i++) {
            String hex = Integer.toHexString(src[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    private static byte[] string2Byte(String src) {
        byte[] ret = new byte[src.length() / 2];
        for (int i = 0; i < src.length() / 2; i++) {
            int high = Integer.parseInt(src.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(src.substring(i * 2 + 1, i * 2 + 2), 16);
            ret[i] = (byte) (high * 16 + low);
        }
        return ret;
    }

    private static Cipher getAesCbcPKCS5PaddingCipher(String sKey, String iv, int mode)
            throws Exception {
        byte[] raw = sKey.getBytes("ASCII");
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        IvParameterSpec ivps = new IvParameterSpec(iv.getBytes());
        cipher.init(mode, skeySpec, ivps);
        return cipher;
    }

    /**
     * MD5加密
     *
     * @param str
     * @return String
     * @author machao
     * @date 2017-02-04
     */
    private static byte[] MD5(String str) {
        Assert.hasText(str, "str不能为空");
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            return messageDigest.digest(str.getBytes("utf8"));
        } catch (Exception e) {
            throw new RuntimeException("出错啦!", e);
        }
    }

    /**
     * MD5 加密
     *
     * @param str 需要加密的字符串
     * @return string 直接返回32位的md5加密字符串
     */
    public static String MD5Str(String str) {
        return new String(DigestUtil.bytes2hex(DigestUtil.MD5(str)));
    }

    /**
     * SHA1 加密
     *
     * @param str
     * @return 直接返回32位的SHA1 加密字符串
     */
    public static String SHAStr(String str) {
        return new String(DigestUtil.bytes2hex(DigestUtil.SHA(str, "sha-1")));
    }

    /**
     * SHA256 加密
     *
     * @param str
     * @return 直接返回32位的SHA256 加密字符串
     */
    public static String SHA256Str(String str) {
        return new String(DigestUtil.bytes2hex(DigestUtil.SHA(str, "sha-256")));
    }

    /**
     * sha1加密
     *
     * @param str
     * @return
     * @author machao
     * @date 2017-02-04
     */
    private static byte[] SHA(String str, String shaAlgorithm) {
        Assert.hasText(str, "str不能为空");
        Assert.hasText(shaAlgorithm, "shaAlgorithm不能为空");
        try {
            MessageDigest md = MessageDigest.getInstance(shaAlgorithm);
            return md.digest(str.getBytes("utf8"));
        } catch (Exception e) {
            throw new RuntimeException("出错啦!", e);
        }

    }


    /**
     * DES算法加密
     *
     * @param source
     * @param key
     * @param hexOrBase64 true:hex方式,false:base64方式
     * @return
     * @author machao
     * @date 2017-02-04
     */
//    public static String encryptDES(String source, String key, boolean hexOrBase64) {
//        Assert.hasText(source, "source不能为空");
//        Assert.isTrue(!StringUtil.isBlank(key) && key.trim().length() >= 8, "key的长度至少是8");
//        try {
//            DESKeySpec desKeySpec = new DESKeySpec(key.getBytes());
//            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
//            SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
//            Cipher cipher = Cipher.getInstance("DES");
//            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
//            if (hexOrBase64) {
//                return new String(DigestUtil.bytes2hex(cipher.doFinal(source.getBytes())));
//            } else {
//                return DigestUtil.encodeBase64(cipher.doFinal(source.getBytes()));
//            }
//
//        } catch (Exception e) {
//            throw new RuntimeException("出错啦!", e);
//        }
//    }

    /**
     * 奇葩的DES算法加密(来自老架构)   A.给微信支付订单金额，订单号使用
     * @param strMing  代价密字符串
     * @param key     加密key
     * @return
     */
//    public static String encryptDES(String strMing,String key){
//        Assert.hasText(strMing,"strMing不能为空");
//        Assert.hasText(key,"key不能为空");
//        byte [] byteMi = null ;
//        byte [] byteMing = null ;
//        String strMi = "" ;
//        BASE64Encoder base64en = new BASE64Encoder();
//        try {
//            byteMing = strMing.getBytes( "UTF-8" );
//            byteMi = desEcbPKCS5PaddingEncrypt(byteMing,key);
//            strMi = base64en.encode(byteMi);
//            strMi=DigestUtil.encodeBase64(strMi.getBytes("utf-8"));
//            strMi=URLEncoder.encode(strMi,"UTF-8");
//        } catch (Exception e) {
//            throw new RuntimeException("出错了:", e);
//        } finally {
//            base64en = null;
//            byteMing = null;
//            byteMi = null;
//        }
//        return strMi;
//    }

    /**
     * 加密 String 明文输入 ,String 密文输出
     */
//    public static String desEcbPKCS5PaddingEncrypt(String strMing, String key) {
//        try {
//            byte[] byteMing = strMing.getBytes("UTF-8");
//            byte[] byteMi = desEcbPKCS5PaddingEncrypt(byteMing, key);
//            return DigestUtil.encodeBase64(byteMi);
//        } catch (Exception e) {
//            throw new RuntimeException("出错啦!",e);
//        }
//    }

    /**
     * DES/ECB/PKCS5Padding加密算法
     */
//    public static byte[] desEcbPKCS5PaddingEncrypt(byte[] source, String key) {
//        try {
//            // 处理密钥
//            SecretKeySpec skey = new SecretKeySpec(key.getBytes("UTF-8"), "DES");
//            // 加密
//            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
//            cipher.init(Cipher.ENCRYPT_MODE, skey);
//            return cipher.doFinal(source);
//        } catch (Exception e) {
//            throw new RuntimeException("出错啦!",e);
//        }
//    }

    /**
     * DES/ECB/PKCS5Padding解密算法
     * @param source
     * @param key  密钥
     * @return
     */
    public static byte[] desDecrypt(byte[] source, String key) {
        try {
            // 处理密钥
            SecretKeySpec skey = new SecretKeySpec(key.getBytes("utf-8"), "DES");
            // 加密
            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, skey);
            return cipher.doFinal(source);
        } catch (Exception e) {
            throw new RuntimeException("出错啦!",e);
        }
    }

    /**
     * DES算法解密
     *
     * @param source
     * @param key
     * @param hexOrBase64 true:hex方式,false:base64方式
     * @return
     * @author machao
     * @date 2017-02-04
     */
//    public static String decryptDES(String source, String key, boolean hexOrBase64) {
//        Assert.hasText(source, "source不能为空");
//        Assert.isTrue(!StringUtil.isBlank(key) && key.trim().length() >= 8, "key的长度至少是8");
//        try {
//            DESKeySpec desKeySpec = new DESKeySpec(key.getBytes());
//            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
//            SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
//            Cipher cipher = Cipher.getInstance("DES");
//            cipher.init(Cipher.DECRYPT_MODE, secretKey);
//            byte[] bytes;
//            if (hexOrBase64) {
//                bytes = cipher.doFinal(DigestUtil.hex2bytes(source));
//            } else {
//                bytes = cipher.doFinal(DigestUtil.decodeBase64(source));
//            }
//            return new String(bytes);
//        } catch (Exception e) {
//            throw new RuntimeException("出错啦!", e);
//        }
//    }


    /**
     * AES加密 128 AES/ECB/PKCS5Padding
     *
     * @param source      需要加密的字符串
     * @param key         需要加密的秘钥
     * @param hexOrBase64 true:hex,false:base64
     * @return 返回加密结果
     */
    public static String encryptAES(String source, String key, boolean hexOrBase64) {
        Assert.hasText(source, "source不能为空");
        Assert.isTrue(!StringUtil.isBlank(key) && key.length() == 16, "key的长度需要16");
        try {
            SecretKey secretKey = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            if (hexOrBase64) {
                return new String(DigestUtil.bytes2hex(cipher.doFinal(source.getBytes())));
            } else {
                return DigestUtil.encodeBase64(cipher.doFinal(source.getBytes()));
            }
        } catch (Exception e) {
            throw new RuntimeException("出错啦!", e);
        }

    }

    /**
     * AES加密 128 AES/ECB/PKCS5Padding
     *
     * @param source      需要加密的字符串
     * @param key         需要加密的秘钥
     * @param hexOrBase64 true:hex,false:base64
     * @return 返回加密结果
     */
    public static String encryptAES(String source, byte[] key, boolean hexOrBase64) {
        Assert.hasText(source, "source不能为空");
        Assert.isTrue(null != key && key.length == 16, "key的长度需要16");
        try {
            SecretKey secretKey = new SecretKeySpec(key, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            if (hexOrBase64) {
                return new String(DigestUtil.bytes2hex(cipher.doFinal(source.getBytes())));
            } else {
                return DigestUtil.encodeBase64(cipher.doFinal(source.getBytes()));
            }
        } catch (Exception e) {
            throw new RuntimeException("出错啦!", e);
        }
    }


    /**
     * AES加密 128 AES/ECB/PKCS5Padding
     *
     * @param source      需要加密的字符串
     * @param key         需要加密的秘钥
     * @param hexOrBase64 true:hex,false:base64
     * @param code 编码格式
     * @return 返回加密结果
     */
    public static String encryptAES(String source, byte[] key, boolean hexOrBase64, String code) {
        Assert.hasText(source, "source不能为空");
        Assert.isTrue(null != key && key.length == 16, "key的长度需要16");
        try {
            SecretKey secretKey = new SecretKeySpec(key, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            if (hexOrBase64) {
                return new String(DigestUtil.bytes2hex(cipher.doFinal(source.getBytes(code))));
            } else {
                return DigestUtil.encodeBase64(cipher.doFinal(source.getBytes(code)));
            }
        } catch (Exception e) {
            throw new RuntimeException("出错啦!", e);
        }
    }

    /**
     * 生成加密秘钥
     *
     * @return
     */
    public static SecretKeySpec getAesKey(final String password) {
        //返回生成指定算法密钥生成器的 KeyGenerator 对象
        KeyGenerator kg = null;
        try {
            kg = KeyGenerator.getInstance("AES");
            //AES 要求密钥长度为 128
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.setSeed(password.getBytes());
            kg.init(128, secureRandom);
            //生成一个密钥
            SecretKey secretKey = kg.generateKey();
            return new SecretKeySpec(secretKey.getEncoded(), "AES");// 转换为AES专用密钥
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("出错啦!", e);
        }
    }


    /**
     * AES解密 128 AES/ECB/PKCS5Padding
     *
     * @param source      需要解密的字符串
     * @param key         需要解密的秘钥
     * @param hexOrBase64 true:hex,false:base64
     * @return 返回解密结果
     */
    public static String decryptAES(String source, String key, boolean hexOrBase64) {
        Assert.hasText(source, "source不能为空");
        Assert.isTrue(!StringUtil.isBlank(key) && key.length() == 16, "key的长度需要16");
        try {
            SecretKey secretKey = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            if (hexOrBase64) {
                return new String(cipher.doFinal(DigestUtil.hex2bytes(source)));
            } else {
                return new String(cipher.doFinal(DigestUtil.decodeBase64(source)));
            }
        } catch (Exception e) {
            throw new RuntimeException("出错啦!", e);
        }
    }

    /**
     * AES解密 128 AES/ECB/PKCS5Padding
     *
     * @param source      需要解密的字符串
     * @param key         需要解密的秘钥
     * @param hexOrBase64 true:hex,false:base64
     * @return 返回解密结果
     */
    public static String decryptAES(String source, byte[] key, boolean hexOrBase64) {
        Assert.hasText(source, "source不能为空");
        Assert.isTrue(null != key && key.length == 16, "key的长度需要16");
        try {
            SecretKey secretKey = new SecretKeySpec(key, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            if (hexOrBase64) {
                return new String(cipher.doFinal(DigestUtil.hex2bytes(source)));
            } else {
                return new String(cipher.doFinal(DigestUtil.decodeBase64(source)));
            }
        } catch (Exception e) {
            throw new RuntimeException("出错啦!", e);
        }
    }


    /**
     * AES解密 128 AES/ECB/PKCS5Padding
     *
     * @param source      需要解密的字符串
     * @param key         需要解密的秘钥
     * @param hexOrBase64 true:hex,false:base64
     * @param code 编码格式
     * @return 返回解密结果
     */
    public static String decryptAES(String source, byte[] key, boolean hexOrBase64, String code) {
        Assert.hasText(source, "source不能为空");
        Assert.isTrue(null != key && key.length == 16, "key的长度需要16");
        try {
            SecretKey secretKey = new SecretKeySpec(key, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            if (hexOrBase64) {
                return new String(cipher.doFinal(DigestUtil.hex2bytes(source)),code);
            } else {
                return new String(cipher.doFinal(DigestUtil.decodeBase64(source)),code);
            }
        } catch (Exception e) {
            throw new RuntimeException("出错啦!", e);
        }
    }

    /**
     * 生成RSA算法密钥队
     *
     * @return
     * @throws Exception
     * @author machao
     * @date 2017-02-04
     */
    public static KeyPair getKeyPair() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            return keyPairGenerator.generateKeyPair();
        } catch (Exception e) {
            throw new RuntimeException("出错啦!", e);
        }

    }

    /**
     * 获得RSA算法公钥
     *
     * @param keyPair
     * @return
     * @author machao
     * @date 2017-02-04
     */
    public static String getPublicKey(KeyPair keyPair) {
        Assert.notNull(keyPair, "keyPair不能为null");
        try {
            PublicKey publicKey = keyPair.getPublic();
            byte[] bytes = publicKey.getEncoded();
            return Base64.getEncoder().encodeToString(bytes);
        } catch (Exception e) {
            throw new RuntimeException("出错啦!", e);
        }
    }

    /**
     * 获得RSA算法秘钥
     *
     * @param keyPair
     * @return
     * @author machao
     * @date 2017-02-04
     */
    public static String getPrivateKey(KeyPair keyPair) {
        Assert.notNull(keyPair, "keyPair不能为null");
        try {
            PrivateKey privateKey = keyPair.getPrivate();
            byte[] bytes = privateKey.getEncoded();
            return Base64.getEncoder().encodeToString(bytes);
        } catch (Exception e) {
            throw new RuntimeException("出错啦!", e);
        }
    }

    /**
     * 加载公钥
     *
     * @param pubStr
     * @return
     * @author machao
     * @date 2017-02-04
     */
    private static PublicKey loadPublicKey(String pubStr) {
        Assert.hasText(pubStr, "pubStr不能为空");
        try {
            byte[] keyBytes = Base64.getDecoder().decode(pubStr);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey publicKey = keyFactory.generatePublic(keySpec);
            return publicKey;
        } catch (Exception e) {
            throw new RuntimeException("出错啦!", e);
        }
    }

    /**
     * 加载秘钥
     *
     * @param priStr
     * @return
     * @author machao
     * @date 2017-02-04
     */
    private static PrivateKey loadPrivateKey(String priStr) {
        Assert.hasText(priStr, "priStr不能为空");
        try {
            byte[] keyBytes = Base64.getDecoder().decode(priStr);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
            return privateKey;
        } catch (Exception e) {
            throw new RuntimeException("出错啦!", e);
        }
    }

    /**
     * RSA算法加密
     *
     * @param content
     * @param publicKey
     * @return
     * @author machao
     * @date 2017-02-04
     */
    private static byte[] publicEncrypt(byte[] content, PublicKey publicKey) {
        Assert.notNull(content, "content不能为null");
        Assert.notNull(publicKey, "publicKey不能为null");
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            return cipher.doFinal(content);
        } catch (Exception e) {
            throw new RuntimeException("出错啦!", e);
        }
    }

    /**
     * RSA算法加密
     *
     * @param content     需要加密的内容
     * @param publicKey   公钥
     * @param hexOrBase64 true:hex,false:base64
     * @return
     * @author machao
     * @date 2017-02-04
     */
    public static String publicEncrypt(String content, String publicKey, boolean hexOrBase64) {
        Assert.notNull(content, "content不能为null");
        Assert.notNull(publicKey, "publicKey不能为null");
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, DigestUtil.loadPublicKey(publicKey));
            if (hexOrBase64) {
                return new String(DigestUtil.bytes2hex(cipher.doFinal(content.getBytes())));
            } else {
                return DigestUtil.encodeBase64(cipher.doFinal(content.getBytes()));
            }
        } catch (Exception e) {
            throw new RuntimeException("出错啦!", e);
        }
    }

    /**
     * RSA算法解密
     *
     * @param content    需要解密的内容
     * @param privateKey 私钥
     * @return
     * @author machao
     * @date 2017-02-04
     */
    private static byte[] privateDecrypt(byte[] content, PrivateKey privateKey) {
        Assert.notNull(content, "content不能为null");
        Assert.notNull(privateKey, "privateKey不能为null");
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            return cipher.doFinal(content);
        } catch (Exception e) {
            throw new RuntimeException("出错啦!", e);
        }
    }

    /**
     * RSA算法解密
     *
     * @param content     需要解密的内容
     * @param privateKey  私钥
     * @param hexOrBase64 true:hex,false:base64
     * @return
     * @author machao
     * @date 2017-02-04
     */
    public static String privateDecrypt(String content, String privateKey, boolean hexOrBase64) {
        Assert.notNull(content, "content不能为null");
        Assert.notNull(privateKey, "privateKey不能为null");
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, DigestUtil.loadPrivateKey(privateKey));
            if (hexOrBase64) {
                return new String(cipher.doFinal(DigestUtil.hex2bytes(content)));
            } else {
                return new String(cipher.doFinal(DigestUtil.decodeBase64(content)));
            }
        } catch (Exception e) {
            throw new RuntimeException("出错啦!", e);
        }
    }

    /**
     * 进行数字签名
     *
     * @param content
     * @param privateKey
     * @return
     * @author machao
     * @date 2017-02-04
     */
    private static byte[] sign(byte[] content, PrivateKey privateKey) {
        Assert.notNull(content, "content不能为null");
        Assert.notNull(privateKey, "privateKey不能为null");
        try {
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initSign(privateKey);
            signature.update(content);
            return signature.sign();
        } catch (Exception e) {
            throw new RuntimeException("出错啦!", e);
        }
    }

    /**
     * 进行数字签名
     *
     * @param content
     * @param privateKey
     * @return
     * @author machao
     * @date 2017-02-04
     */
    public static String sign(String content, String privateKey, boolean hexOrBase64) {
        Assert.notNull(content, "content不能为null");
        Assert.notNull(privateKey, "privateKey不能为null");
        try {
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initSign(DigestUtil.loadPrivateKey(privateKey));
            signature.update(content.getBytes());
            if (hexOrBase64) {
                return new String(DigestUtil.bytes2hex(signature.sign()));
            } else {
                return DigestUtil.encodeBase64(signature.sign());
            }
        } catch (Exception e) {
            throw new RuntimeException("出错啦!", e);
        }
    }

    /**
     * 校验签名
     *
     * @param content
     * @param sign
     * @param publicKey
     * @return
     * @author machao
     * @date 2017-02-04
     */
    private static boolean verify(byte[] content, byte[] sign, PublicKey publicKey) {
        Assert.notNull(content, "content不能为null");
        Assert.notNull(sign, "sign不能为null");
        Assert.notNull(publicKey, "publicKey不能为null");
        try {
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initVerify(publicKey);
            signature.update(content);
            return signature.verify(sign);
        } catch (Exception e) {
            throw new RuntimeException("出错啦!", e);
        }
    }

    /**
     * 校验签名
     *
     * @param content
     * @param sign
     * @param publicKey
     * @return
     * @author machao
     * @date 2017-02-04
     */
    public static boolean verify(String content, String sign, String publicKey, boolean hexOrBase64) {
        Assert.notNull(content, "content不能为null");
        Assert.notNull(sign, "sign不能为null");
        Assert.notNull(publicKey, "publicKey不能为null");
        try {
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initVerify(DigestUtil.loadPublicKey(publicKey));
            signature.update(content.getBytes());
            if (hexOrBase64) {
                return signature.verify(DigestUtil.hex2bytes(sign));
            } else {
                return signature.verify(DigestUtil.decodeBase64(sign));
            }
        } catch (Exception e) {
            throw new RuntimeException("出错啦!", e);
        }
    }

    /**
     * 2进制转16进制
     *
     * @param data a byte[] to convert to Hex characters
     * @return A char[] containing hexadecimal characters
     * @author machao
     * @date 2017-02-04
     */
    public static char[] bytes2hex(final byte[] data) {
        Assert.notNull(data, "data不能为null");
        final int l = data.length;
        final char[] out = new char[l << 1];
        // two characters form the hex value.
        for (int i = 0, j = 0; i < l; i++) {
            out[j++] = DIGITS_LOWER[(0xF0 & data[i]) >>> 4];
            out[j++] = DIGITS_LOWER[0x0F & data[i]];
        }
        return out;
    }

    /**
     * 16进制转2进制
     *
     * @param hexStr
     * @return
     * @author machao
     * @date 2017-02-04
     */
    public static byte[] hex2bytes(String hexStr) {
        Assert.hasText(hexStr, "hexStr不能为空");
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }

    /**
     * base64 加密
     *
     * @param data
     * @return
     * @author machao
     * @date 2017-02-04
     */
    public static String encodeBase64(byte[] data) {
        Assert.notNull(data, "data不能为null");
        return Base64.getEncoder().encodeToString(data);
    }

    /**
     * base64 解密
     *
     * @param text
     * @return
     * @author machao
     * @date 2017-02-04
     */
    public static byte[] decodeBase64(String text) {
        Assert.hasText(text, "text不能为空");
        return Base64.getDecoder().decode(text);
    }

    private static String encodePwdBySha(String pwd) {
        String result = "";
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] b = md.digest(pwd.getBytes());
            for (int i = 0; i < b.length; i++) {
                String tmp = Integer.toHexString(b[i] & 0xFF);
                if (tmp.length() == 1) {
                    result += "0" + tmp;
                } else {
                    result += tmp;
                }
            }
        } catch (Exception e) {
            logger.error("BJCA加密出错", e);
        }
        return result;
    }
}