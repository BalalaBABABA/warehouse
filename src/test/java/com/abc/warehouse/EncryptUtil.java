package com.abc.warehouse;


import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import com.abc.warehouse.utils.Base64Helper;
import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

@SuppressWarnings("restriction")
public class EncryptUtil {
    private static final Integer KEY_LENGTH = 16 * 8;


    /**
     * 后端AES的key，由静态代码块赋值
     */
    public static String key;

    static {
        key = getKey();
    }

    /**
     * 获取key
     */
    public static String getKey() {
        StringBuilder uid = new StringBuilder();
        //产生16位的强随机数
        Random rd = new SecureRandom();
        for (int i = 0; i < KEY_LENGTH / 8; i++) {
            //产生0-2的3位随机数
            int type = rd.nextInt(3);
            switch (type) {
                case 0:
                    //0-9的随机数
                    uid.append(rd.nextInt(10));
                    break;
                case 1:
                    //ASCII在65-90之间为大写,获取大写随机
                    uid.append((char) (rd.nextInt(25) + 65));
                    break;
                case 2:
                    //ASCII在97-122之间为小写，获取小写随机
                    uid.append((char) (rd.nextInt(25) + 97));
                    break;
                default:
                    break;
            }
        }
        return uid.toString();
    }


    public static String aesEncrypt(String str, String key) throws Exception {
        if (str == null || key == null) return null;
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key.getBytes("utf-8"), "AES"));
        byte[] bytes = cipher.doFinal(str.getBytes("utf-8"));
        return Base64.encodeBase64String(bytes);
    }

    public static String aesDecrypt(String str, String key) throws Exception {
        if (str == null || key == null) return null;
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key.getBytes("utf-8"), "AES"));
        byte[] bytes = Base64.decodeBase64(str);
        bytes = cipher.doFinal(bytes);
        return new String(bytes, "utf-8");
    }
}








//package com.abc.warehouse.utils;
//
//
//        import org.apache.tomcat.util.codec.binary.Base64;
//        import org.bouncycastle.jce.provider.BouncyCastleProvider;
//
//        import javax.crypto.Cipher;
//        import javax.crypto.spec.IvParameterSpec;
//        import javax.crypto.spec.SecretKeySpec;
//        import java.nio.charset.StandardCharsets;
//        import java.security.SecureRandom;
//        import java.util.Random;
//
///**
// * AES加、解密算法工具类
// */
//public class AesUtil {
//    /**
//     * 加密算法AES
//     */
//    private static final String KEY_ALGORITHM = "AES";
//
//    /**
//     * key的长度，Wrong key size: must be equal to 128, 192 or 256
//     * 传入时需要16、24、36
//     */
//    private static final Integer KEY_LENGTH = 16 * 8;
//    /**
//     * 算法名称/加密模式/数据填充方式
//     * 默认：AES/ECB/PKCS7Padding
//     */
//    private static final String ALGORITHMS = "AES/ECB/PKCS7Padding";
//
//    /**
//     * 后端AES的key，由静态代码块赋值
//     */
//    public static String key;
//
//    static {
//        key = getKey();
//    }
//
//    /**
//     * 获取key
//     */
//    public static String getKey() {
//        StringBuilder uid = new StringBuilder();
//        //产生16位的强随机数
//        Random rd = new SecureRandom();
//        for (int i = 0; i < KEY_LENGTH / 8; i++) {
//            //产生0-2的3位随机数
//            int type = rd.nextInt(3);
//            switch (type) {
//                case 0:
//                    //0-9的随机数
//                    uid.append(rd.nextInt(10));
//                    break;
//                case 1:
//                    //ASCII在65-90之间为大写,获取大写随机
//                    uid.append((char) (rd.nextInt(25) + 65));
//                    break;
//                case 2:
//                    //ASCII在97-122之间为小写，获取小写随机
//                    uid.append((char) (rd.nextInt(25) + 97));
//                    break;
//                default:
//                    break;
//            }
//        }
//        return uid.toString();
//    }
//
//    /**
//     * 加密
//     *
//     * @param content    加密的字符串
//     * @param encryptKey key值
//     */
//    public static String encrypt1(String content, String encryptKey) throws Exception {
//        //设置Cipher对象
//        Cipher cipher = Cipher.getInstance(ALGORITHMS,new BouncyCastleProvider());
//        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(encryptKey.getBytes(), KEY_ALGORITHM));
//
//        //调用doFinal
//        byte[] b = cipher.doFinal(content.getBytes(StandardCharsets.UTF_8));
//
//        // 转base64
//        return org.apache.tomcat.util.codec.binary.Base64.encodeBase64String(b);
//
//    }
//
//    /**
//     * 解密
//     *
//     * @param encryptStr 解密的字符串
//     * @param decryptKey 解密的key值
//     */
//    public static String decrypt1(String encryptStr, String decryptKey) throws Exception {
//        //base64格式的key字符串转byte
//        byte[] decodeBase64 = org.apache.tomcat.util.codec.binary.Base64.decodeBase64(encryptStr);
//
//        //设置Cipher对象
//        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding",new BouncyCastleProvider());
////        Cipher cipher = Cipher.getInstance(ALGORITHMS,new BouncyCastleProvider());
//        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(decryptKey.getBytes(), KEY_ALGORITHM));
//
//        //调用doFinal解密
//        byte[] decryptBytes = cipher.doFinal(decodeBase64);
//        return new String(decryptBytes);
//    }
//
//    /**
//     * AES算法加密明文
//     * @param data 明文
//     * @param key 密钥，长度16
//     * @param iv 偏移量，长度16
//     * @return 密文
//     */
//    public static String encrypt(String data,String key,String iv) throws Exception {
//        try {
//            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding"); //or NoPadding
//            int blockSize = cipher.getBlockSize();
//            byte[] dataBytes = data.getBytes();
//            int plaintextLength = dataBytes.length;
//
//            if (plaintextLength % blockSize != 0) {
//                plaintextLength = plaintextLength + (blockSize - (plaintextLength % blockSize));
//            }
//
//            byte[] plaintext = new byte[plaintextLength];
//            System.arraycopy(dataBytes, 0, plaintext, 0, dataBytes.length);
//
//            SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
//            IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());
//
//            cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);
//            byte[] encrypted = cipher.doFinal(plaintext);
//
//            return Base64Helper.encode(encrypted).trim();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    /**
//     * AES算法解密密文
//     * @param data 密文
//     * @param key 密钥，长度16
//     * @param iv 偏移量，长度16
//     * @return 明文
//     */
//    public static String decrypt(String data,String key,String iv) throws Exception {
//        try
//        {
//            byte[] encrypted1 = Base64Helper.decode(data);
//
//            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding"); //or NoPadding
//            SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
//            IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());
//
//            cipher.init(Cipher.DECRYPT_MODE, keyspec,ivspec);
//
//            byte[] original = cipher.doFinal(encrypted1);
//            String originalString = new String(original);
//            return originalString.trim();
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    public static String encrypt(String str, String key) throws Exception {
//        if (str == null || key == null) return null;
//        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding",new BouncyCastleProvider());
//        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key.getBytes("utf-8"), "AES"));
//        byte[] bytes = cipher.doFinal(str.getBytes("utf-8"));
//        return org.apache.tomcat.util.codec.binary.Base64.encodeBase64String(bytes);
//    }
//
//    public static String decrypt(String str, String key) throws Exception {
//        if (str == null || key == null) return null;
//        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding",new BouncyCastleProvider());
//        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key.getBytes("utf-8"), "AES"));
//        byte[] bytes = org.apache.tomcat.util.codec.binary.Base64.decodeBase64(str);
//        bytes = cipher.doFinal(bytes);
//        return new String(bytes, "utf-8");
//    }
//
//}