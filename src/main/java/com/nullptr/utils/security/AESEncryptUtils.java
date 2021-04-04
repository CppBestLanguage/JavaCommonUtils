package com.nullptr.utils.security;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 密码验证组件
 *
 * @author nullptr
 * @version 1.2 2020-4-18
 * @since 1.0 2019-12-27
 *
 * @see Cipher
 * @see KeyGenerator
 */
public class AESEncryptUtils {
    private static final String ALGORITHM_NAME = "AES";
    private static final Base64 BASE_64 = new Base64();
    private static final Log log = LogFactory.getLog(AESEncryptUtils.class);

    private AESEncryptUtils() {
    }

    /**
     * 获取密钥
     *
     * @return 密钥字符串
     *
     * @since 1.0
     */
    public static String getSecretKey() {
        try {
            // 获取AES算法密钥注册器
            KeyGenerator generator = KeyGenerator.getInstance(ALGORITHM_NAME);
            // 初始化密钥为128字节
            generator.init(128);
            // 获取密钥
            SecretKey key = generator.generateKey();
            // 生成十六进制密钥字符串
            String hexStringKey = byteArrayToHexString(key.getEncoded());
            // 对密钥字符串进行Base64编码
            return base64encode(hexStringKey.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 字符串解码
     *
     * @param cipherText 密文字符串
     * @param secretKey 密钥字符串
     * @return 解码后的明文字符串
     *
     * @since 1.0
     */
    public static String decrypt(String cipherText, String secretKey) {
        // 对密钥进行Base64解码，获取十六进制密钥字符串
        String hexStringKey = new String(base64Decode(secretKey), StandardCharsets.UTF_8);
        // 获取密钥字节数组
        byte[] keyBytes = hexStringKey.getBytes(StandardCharsets.UTF_8);
        // 使用密钥字节数组和AES加密算法生成密钥实例
        SecretKey key = new SecretKeySpec(keyBytes, ALGORITHM_NAME);
        try {
            // 获取编码器实例
            Cipher cipher = Cipher.getInstance(ALGORITHM_NAME);
            // 初始化编码器模式位解码模式，并设置密钥
            cipher.init(Cipher.DECRYPT_MODE, key);
            // 使用Base4算法对密文进行第一层解码
            byte[] base64Plaintext = base64Decode(cipherText);
            // 使用AES算法对密文进行二次解码，并返回解码后的明文字符串
            return new String(cipher.doFinal(base64Plaintext), StandardCharsets.UTF_8);
        } catch (NoSuchAlgorithmException | InvalidKeyException |
                NoSuchPaddingException | BadPaddingException |
                IllegalBlockSizeException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * Base64解码
     * @param cipherText 密文字符串
     * @return 解码后的明文字节数组
     *
     * @since 1.0
     */
    public static byte[] base64Decode(String cipherText) {
        return BASE_64.decode(cipherText);
    }

    /**
     * Bae64编码
     * @param cipherText 明文字符串
     * @return 编码后的密文字符串
     *
     * @since 1.0
     */
    public static String base64encode(byte[] cipherText) {
        return BASE_64.encodeToString(cipherText);
    }

    /**
     * 字节数组转换成16进制字符串
     *
     * @param bytes 字节数组
     * @return 转换后的16进制字符串
     *
     * @since 1.0
     */
    private static String byteArrayToHexString(byte[] bytes) {
        StringBuilder builder = new StringBuilder();
        // 遍历密钥字节数组
        for (byte aByte : bytes) {
            // 将字节码转成16进制字符串
            String strHex = Integer.toHexString(aByte);
            // 字节码长度大于3时
            if (strHex.length() > 3) {
                // 取前6位
                builder.append(strHex.substring(6));
                // 字节码长度小于2时
            } else if (strHex.length() < 2) {
                // 头部补充0
                builder.append("0").append(strHex);
            } else {
                builder.append(strHex);
            }
        }
        return builder.toString();
    }
}
