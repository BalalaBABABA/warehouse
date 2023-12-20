package com.abc.warehouse.utils;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * RSA加解密工具<br>
 */
public class RsaUtil {
    public static String RSA_ALGORITHM = "RSA";
    public static String UTF8 = "UTF-8";

    public static KeyStore keyStore;

    static {
        try {
            createKeys();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 创建公钥私钥
     */
    public static void createKeys() throws Exception {
        KeyPairGenerator keyPairGeno = KeyPairGenerator.getInstance(RSA_ALGORITHM);
        keyPairGeno.initialize(1024);
        KeyPair keyPair = keyPairGeno.generateKeyPair();

        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

        keyStore = new KeyStore();
        keyStore.setPublicKey(Base64.encodeBase64String(publicKey.getEncoded()));
        keyStore.setPrivateKey(Base64.encodeBase64String(privateKey.getEncoded()));
//        return keyStore;
    }

    /**
     * 获取公钥对象
     */
    public static RSAPublicKey getPublicKey(byte[] pubKeyData) throws Exception {
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(pubKeyData);
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
        return (RSAPublicKey) keyFactory.generatePublic(keySpec);
    }

    /**
     * 获取公钥对象
     */
    public static RSAPublicKey getPublicKey(String pubKey) throws Exception {
        return getPublicKey(Base64.decodeBase64(pubKey));

    }

    /**
     * 获取私钥对象
     */
    public static RSAPrivateKey getPrivateKey(String priKey) throws Exception {
        return getPrivateKey(Base64.decodeBase64(priKey));
    }

    /**
     * 通过私钥byte[]将公钥还原，适用于RSA算法
     */
    public static RSAPrivateKey getPrivateKey(byte[] keyBytes) throws Exception {
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
        return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);

    }

    public static String encryptByPublicKey(String data, String publicKey) throws Exception {
        return encryptByPublicKey(data, getPublicKey(publicKey));
    }

    /**
     * 公钥加密
     */
    public static String encryptByPublicKey(String data, RSAPublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] bytes = cipher.doFinal(data.getBytes(UTF8));
        return Base64.encodeBase64String(bytes);
    }

    public static String decryptByPublicKey(String data, String rsaPublicKey) throws Exception {
        return decryptByPublicKey(data, getPublicKey(rsaPublicKey));
    }

    /**
     * 公钥解密
     */
    public static String decryptByPublicKey(String data, RSAPublicKey rsaPublicKey) throws Exception {
        Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, rsaPublicKey);
        byte[] inputData = Base64.decodeBase64(data);
        byte[] bytes = cipher.doFinal(inputData);
        return new String(bytes, UTF8);
    }

    public static String encryptByPrivateKey(String data, String privateKey) throws Exception {
        return encryptByPrivateKey(data, getPrivateKey(privateKey));
    }

    /**
     * 私钥加密
     */
    public static String encryptByPrivateKey(String data, RSAPrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        byte[] bytes = cipher.doFinal(data.getBytes(UTF8));
        return Base64.encodeBase64String(bytes);
    }

    public static String decryptByPrivateKey(String data, String privateKey) throws Exception {
        return decryptByPrivateKey(data, getPrivateKey(privateKey));
    }

    /**
     * 私钥解密
     */
    public static String decryptByPrivateKey(String data, RSAPrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] inputData = Base64.decodeBase64(data);
        byte[] bytes = cipher.doFinal(inputData);
        return new String(bytes, UTF8);
    }

    public static class KeyStore {
        private String publicKey;
        private String privateKey;

        public String getPublicKey() {
            return publicKey;
        }

        public void setPublicKey(String publicKey) {
            this.publicKey = publicKey;
        }

        public String getPrivateKey() {
            return privateKey;
        }

        public void setPrivateKey(String privateKey) {
            this.privateKey = privateKey;
        }
    }

}