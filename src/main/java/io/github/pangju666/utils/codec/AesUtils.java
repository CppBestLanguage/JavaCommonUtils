package io.github.pangju666.utils.codec;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES加密工具类
 *
 * @author 胖橘
 * @version 1.0
 * @since 1.0
 */
public class AesUtils {
    protected static final String ALGORITHM_NAME = "AES";

    protected AesUtils() {
    }

    /**
     * 生成密钥
     *
     * @return 密钥字符串
     */
    public static byte[] generateSecretKey() {
        return generateSecretKey(128);
    }

    /**
     * 生成密钥
     *
     * @param keySize 密钥长度
     * @return 密钥字符串
     */
    public static byte[] generateSecretKey(int keySize) {
        try {
            KeyGenerator generator = KeyGenerator.getInstance(ALGORITHM_NAME);
            generator.init(keySize);
            SecretKey key = generator.generateKey();
            return key.getEncoded();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("AES加密算法在当前环境下不可用", e);
        }
    }

    /**
     * 加密
     *
     * @param source 源数据
     * @param secretKey 密钥
     * @return 加密后的数据
     */
    public static byte[] encrypt(byte[] source, byte[] secretKey) {
        try {
            SecretKey key = new SecretKeySpec(secretKey, ALGORITHM_NAME);
            Cipher cipher = Cipher.getInstance(ALGORITHM_NAME);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return cipher.doFinal(source);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new RuntimeException("AES加密算法在当前环境下不可用", e);
        } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            throw new RuntimeException("AES加密失败", e);
        }
    }

    /**
     * 加密，并将结果转换为字符串
     *
     * @param source 源数据
     * @param secretKey 密钥
     * @return 加密后的数据字符串
     */
    public static String encryptToString(byte[] source, byte[] secretKey) {
        return new String(encrypt(source, secretKey), StandardCharsets.UTF_8);
    }

    /**
     * 解密
     *
     * @param source 源数据
     * @param secretKey 密钥
     * @return 解密后的数据
     */
    public static byte[] decrypt(byte[] source, byte[] secretKey) {
        try {
            SecretKey key = new SecretKeySpec(secretKey, ALGORITHM_NAME);
            Cipher cipher = Cipher.getInstance(ALGORITHM_NAME);
            cipher.init(Cipher.DECRYPT_MODE, key);
            return cipher.doFinal(source);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new RuntimeException("AES加密算法在当前环境下不可用", e);
        } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            throw new RuntimeException("AES解密失败", e);
        }
    }

    /**
     * 解密
     *
     * @param source 源数据
     * @param secretKey 密钥
     * @return 解密后的数据
     */
    public static byte[] decrypt(String source, byte[] secretKey) {
        return decrypt(source.getBytes(), secretKey);
    }
}
