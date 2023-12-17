package com.abc.warehouse.utils;

import io.jsonwebtoken.*;

import java.security.SecureRandom;
import java.util.*;

public class JwtUtils {
    private static final  int KEY_LENGTH = 32;
    //随机密钥
    private static String secretKey = generateRandomKey();

//    public static String getSecretKey(){
//        return secretKey;
//    }

    public static void setSecretKey(String newSecretKey){
        secretKey = newSecretKey;
    }

    public static void updateSecretKey(){
        String newSecretKey  = generateRandomKey();
        setSecretKey(newSecretKey);
    }

    public static String generateRandomKey(){
        byte[] key = new byte[KEY_LENGTH];
        new SecureRandom().nextBytes(key);
        return Base64.getEncoder().encodeToString(key);
    }
//    //密钥
//    private static final String jwtToken = "123456wms!@#ss";

    public static String createToken(Long userId){
        Map<String,Object> claims = new HashMap<>();
        // 存入用户id和权限信息
        claims.put("userId",userId);
        JwtBuilder jwtBuilder= Jwts.builder()
                .signWith(SignatureAlgorithm.HS256,secretKey) //签发算法，密钥为jwtToken
                .setClaims(claims) //body数据，要唯一，自己设置
                .setIssuedAt(new Date()) //设置签发时间
                .setExpiration(new Date(System.currentTimeMillis()+24*60*60*60*1000)); //一天过期时间
        String token =jwtBuilder.compact();
        return token;
    }


    public static Long getUserIdFromToken(String token){
        try {
            Jwt parse = Jwts.parser().setSigningKey(secretKey).parse(token);
            Map<String, Object> map = (Map<String, Object>) parse.getBody();
            Integer userId = (Integer) map.get("userId");
            return userId.longValue();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static Map<String, Object> checkToken(String token){
        try {
            Jwt parse = Jwts.parser().setSigningKey(secretKey).parse(token);
            return (Map<String, Object>) parse.getBody();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;

    }
}
