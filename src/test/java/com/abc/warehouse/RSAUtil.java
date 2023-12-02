package com.abc.warehouse;
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
public class RSAUtil {
    public static String RSA_ALGORITHM = "RSA";
    public static String UTF8 = "UTF-8";

    /**
     * 创建公钥私钥
     */
    public static KeyStore createKeys() throws Exception {
        KeyPairGenerator keyPairGeno = KeyPairGenerator.getInstance(RSA_ALGORITHM);
        keyPairGeno.initialize(1024);
        KeyPair keyPair = keyPairGeno.generateKeyPair();

        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

        KeyStore keyStore = new KeyStore();
        keyStore.setPublicKey(Base64.encodeBase64String(publicKey.getEncoded()));
        keyStore.setPrivateKey(Base64.encodeBase64String(privateKey.getEncoded()));
        return keyStore;
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
//
//package com.abc.warehouse.utils;
//
//
//
//
//        import org.apache.commons.codec.binary.Base64;
//
//
//        import javax.crypto.Cipher;
//        import java.io.ByteArrayOutputStream;
//        import java.security.*;
//        import java.security.interfaces.RSAPrivateKey;
//        import java.security.interfaces.RSAPublicKey;
//        import java.security.spec.PKCS8EncodedKeySpec;
//        import java.security.spec.X509EncodedKeySpec;
//        import java.util.HashMap;
//        import java.util.Map;
//
//        import static com.lowagie.text.xml.xmp.XmpWriter.UTF8;
//
///**
// * RSA加、解密算法工具类
// */
//public class RsaUtil {
//
//    /**
//     * 加密算法AES
//     */
//    private static final String KEY_ALGORITHM = "RSA";
//
//    /**
//     * 算法名称/加密模式/数据填充方式
//     * 默认：RSA/ECB/PKCS1Padding
//     */
//    private static final String ALGORITHMS = "RSA/ECB/PKCS1Padding";
//
//    /**
//     * Map获取公钥的key
//     */
//    private static final String PUBLIC_KEY = "publicKey";
//
//    /**
//     * Map获取私钥的key
//     */
//    private static final String PRIVATE_KEY = "privateKey";
//
//    /**
//     * RSA最大加密明文大小
//     */
//    private static final int MAX_ENCRYPT_BLOCK = 117;
//
//    /**
//     * RSA最大解密密文大小
//     */
//    private static final int MAX_DECRYPT_BLOCK = 128;
//
//    /**
//     * RSA 位数 如果采用2048 上面最大加密和最大解密则须填写:  245 256
//     */
//    private static final int INITIALIZE_LENGTH = 1024;
//
//    /**
//     * 后端RSA的密钥对(公钥和私钥)Map，由静态代码块赋值
//     */
//    private static Map<String, Object> genKeyPair = new HashMap<>();
//
//    static {
//        try {
//            genKeyPair.putAll(genKeyPair());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 生成密钥对(公钥和私钥)
//     */
//    private static Map<String, Object> genKeyPair() throws Exception {
//        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
//        keyPairGen.initialize(INITIALIZE_LENGTH);
//        KeyPair keyPair = keyPairGen.generateKeyPair();
//        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
//        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
//        Map<String, Object> keyMap = new HashMap<String, Object>(2);
//        //公钥
//        keyMap.put(PUBLIC_KEY, publicKey);
//        //私钥
//        keyMap.put(PRIVATE_KEY, privateKey);
//        return keyMap;
//    }
//
//    /**
//     * 私钥解密
//     *
//     * @param encryptedData 已加密数据
//     * @param privateKey    私钥(BASE64编码)
//     */
//    public static byte[] decryptByPrivateKey(byte[] encryptedData, String privateKey) throws Exception {
//
//        //base64格式的key字符串转Key对象
//        byte[] keyBytes = Base64.decodeBase64(privateKey);
//        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
//        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
//        Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
//
//        //设置加密、填充方式
//        /*
//            如需使用更多加密、填充方式，引入
//            <dependency>
//                <groupId>org.bouncycastle</groupId>
//                <artifactId>bcprov-jdk16</artifactId>
//                <version>1.46</version>
//            </dependency>
//            并改成
//            Cipher cipher = Cipher.getInstance(ALGORITHMS ,new BouncyCastleProvider());
//         */
//        Cipher cipher = Cipher.getInstance(ALGORITHMS);
//        cipher.init(Cipher.DECRYPT_MODE, privateK);
//
//        //分段进行解密操作
//        return encryptAndDecryptOfSubsection(encryptedData, cipher, MAX_DECRYPT_BLOCK);
//    }
//
//    /**
//     * <P>
//     * 私钥解密
//     * </p>
//     *
//     * @param encryptedDataString 已加密数据
//     * @param privateKey          私钥(BASE64编码)
//     * @return
//     * @throws Exception
//     */
//    public static String decryptByPrivateKey(String encryptedDataString, String privateKey) throws Exception {
//        //byte[] encryptedData = encryptedDataString.getBytes();
//        //应该先使用Base64方式进行解码
//        byte[] encryptedData = Base64.decodeBase64(encryptedDataString);
//        byte[] keyBytes = Base64.decodeBase64(privateKey);
//        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
//        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
//        Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
//        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
//        cipher.init(Cipher.DECRYPT_MODE, privateK);
//        int inputLen = encryptedData.length;
//        ByteArrayOutputStream out = new ByteArrayOutputStream();
//        int offSet = 0;
//        byte[] cache;
//        int i = 0;
//        // 对数据分段解密
//        while (inputLen - offSet > 0) {
//            if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
//                cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
//            } else {
//                cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
//            }
//            out.write(cache, 0, cache.length);
//            i++;
//            offSet = i * MAX_DECRYPT_BLOCK;
//        }
//        byte[] decryptedData = out.toByteArray();
//        out.close();
//        return new String(decryptedData);
//    }
//
//
//
//    public static String encryptByPublicKey(String data, String publicKeyString) throws Exception {
//        // 将字符串形式的公钥转换为字节数组
//        byte[] publicKeyBytes = Base64.decodeBase64(publicKeyString);
//
//        // 创建密钥工厂和密钥规范
//        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
//        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(publicKeyBytes);
//
//        // 生成公钥
//        PublicKey publicKey = keyFactory.generatePublic(x509KeySpec);
//
//        // 使用公钥加密数据
//        Cipher cipher = Cipher.getInstance("RSA");
//        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
//        byte[] bytes = cipher.doFinal(data.getBytes("UTF-8"));
//        return Base64.encodeBase64String(bytes);
//
//    }
//
//    public static String publicKeyToString(RSAPublicKey publicKey) {
//        byte[] publicKeyBytes = publicKey.getEncoded();
//        return Base64.encodeBase64String(publicKeyBytes);
//    }
//    /**
//     * 公钥加密
//     *
//     * @param data      源数据
//     * @param publicKey 公钥(BASE64编码)
//     */
//    public static byte[] encryptByPublicKey(byte[] data, String publicKey) throws Exception {
//        byte[] decodedBytes = java.util.Base64.getMimeDecoder().decode(publicKey.getBytes());
//        X509EncodedKeySpec keySpecX509B = new X509EncodedKeySpec(decodedBytes);
//        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
//        RSAPublicKey publicK = (RSAPublicKey) keyFactory.generatePublic(keySpecX509B);
////        byte[] keyBytes = Base64.decodeBase64(publicKey);
////        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
////        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
////        // TODO 报错java.security.spec.InvalidKeySpecException: java.security.InvalidKeyException: invalid key format
////        Key publicK = keyFactory.generatePublic(x509KeySpec);
//
//        //设置加密、填充方式
//        /*
//            如需使用更多加密、填充方式，引入
//            <dependency>
//                <groupId>org.bouncycastle</groupId>
//                <artifactId>bcprov-jdk16</artifactId>
//                <version>1.46</version>
//            </dependency>
//            并改成
//            Cipher cipher = Cipher.getInstance(ALGORITHMS ,new BouncyCastleProvider());
//         */
//        Cipher cipher = Cipher.getInstance(ALGORITHMS);
//        cipher.init(Cipher.ENCRYPT_MODE, publicK);
//
//        //分段进行加密操作
//        return encryptAndDecryptOfSubsection(data, cipher, MAX_ENCRYPT_BLOCK);
//    }
//
//    /**
//     * 获取私钥
//     */
//    public static String getPrivateKey() {
//        Key key = (Key) genKeyPair.get(PRIVATE_KEY);
//        return Base64.encodeBase64String(key.getEncoded());
//    }
//
//    /**
//     * 获取公钥
//     */
//    public static String getPublicKey() {
//        Key key = (Key) genKeyPair.get(PUBLIC_KEY);
//        byte[] encoded = key.getEncoded();
//        String publickey =Base64.encodeBase64String(key.getEncoded());
//        return publickey;
//    }
//
//    /**
//     * 分段进行加密、解密操作
//     */
//    private static byte[] encryptAndDecryptOfSubsection(byte[] data, Cipher cipher, int encryptBlock) throws Exception {
//        int inputLen = data.length;
//        ByteArrayOutputStream out = new ByteArrayOutputStream();
//        int offSet = 0;
//        byte[] cache;
//        int i = 0;
//        // 对数据分段加密
//        while (inputLen - offSet > 0) {
//            if (inputLen - offSet > encryptBlock) {
//                cache = cipher.doFinal(data, offSet, encryptBlock);
//            } else {
//                cache = cipher.doFinal(data, offSet, inputLen - offSet);
//            }
//            out.write(cache, 0, cache.length);
//            i++;
//            offSet = i * encryptBlock;
//        }
//        byte[] toByteArray = out.toByteArray();
//        out.close();
//        return toByteArray;
//    }
//}