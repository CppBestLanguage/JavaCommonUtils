package io.github.pangju666.utils.codec;

import javax.crypto.*;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * RSA加密工具类
 *
 * @author 胖橘
 * @version 1.0
 * @since 1.0
 */
public class RsaUtil {
    protected static final String ALGORITHM_NAME = "RSA";

    protected RsaUtil() {
    }

    /**
     * 生成密钥
     *
     * @return 公私密钥数据对像
     */
    public static RsaKeyPair generateSecretKey() throws NoSuchAlgorithmException {
        return generateSecretKey(128);
    }

    /**
     * 生成密钥
     *
     * @param keySize 密钥长度
     * @return 公私密钥数据对像
     */
    public static RsaKeyPair generateSecretKey(int keySize) {
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance(ALGORITHM_NAME);
            generator.initialize(keySize);
            KeyPair keyPair = generator.generateKeyPair();
            byte[] publicKey = keyPair.getPublic().getEncoded();
            byte[] privateKey = keyPair.getPrivate().getEncoded();
            return new RsaKeyPair(publicKey, privateKey);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("RSA加密算法在当前环境下不可用", e);
        }
    }

    /**
     * 加密
     *
     * @param source 源数据
     * @param publicKey 公钥
     * @return 加密后的数据
     */
    public static byte[] encode(byte[] source, byte[] publicKey) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM_NAME);
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM_NAME);
            X509EncodedKeySpec encodedKeySpec = new X509EncodedKeySpec(publicKey);
            cipher.init(Cipher.ENCRYPT_MODE, keyFactory.generatePublic(encodedKeySpec));
            return cipher.doFinal(source);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new RuntimeException("RSA加密算法在当前环境下不可用", e);
        } catch (InvalidKeySpecException | InvalidKeyException |
                IllegalBlockSizeException | BadPaddingException e) {
            throw new RuntimeException("RSA加密失败", e);
        }
    }

    /**
     * 加密，并将结果转换为字符串
     *
     * @param source 源数据
     * @param publicKey 公钥
     * @return 加密后的数据字符串
     */
    public static String encodeToString(byte[] source, byte[] publicKey) {
        return new String(encode(source, publicKey), StandardCharsets.UTF_8);
    }

    /**
     * 解密
     *
     * @param source 源数据
     * @param privateKey 私钥
     * @return 解密后的数据
     */
    public static byte[] decode(byte[] source, byte[] privateKey) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM_NAME);
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM_NAME);
            PKCS8EncodedKeySpec encodedKeySpec = new PKCS8EncodedKeySpec(privateKey);
            cipher.init(Cipher.DECRYPT_MODE, keyFactory.generatePrivate(encodedKeySpec));
            return cipher.doFinal(source);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new RuntimeException("RSA加密算法在当前环境下不可用", e);
        } catch (InvalidKeySpecException | InvalidKeyException |
                IllegalBlockSizeException | BadPaddingException e) {
            throw new RuntimeException("RSA加密失败", e);
        }
    }

    /**
     * 解密
     *
     * @param source 源数据
     * @param privateKey 密钥
     * @return 解密后的数据
     */
    public static byte[] decode(String source, byte[] privateKey) {
        return decode(source.getBytes(), privateKey);
    }

    public static class RsaKeyPair {
        private byte[] publicKey;
        private byte[] privateKey;

        public RsaKeyPair() {
        }

        public RsaKeyPair(byte[] publicKey, byte[] privateKey) {
            this.publicKey = publicKey;
            this.privateKey = privateKey;
        }

        public byte[] getPublicKey() {
            return publicKey;
        }

        public void setPublicKey(byte[] publicKey) {
            this.publicKey = publicKey;
        }

        public byte[] getPrivateKey() {
            return privateKey;
        }

        public void setPrivateKey(byte[] privateKey) {
            this.privateKey = privateKey;
        }
    }
}
