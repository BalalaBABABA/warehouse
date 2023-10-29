package com.abc.warehouse.utils;

import com.abc.warehouse.dto.constants.LoginConstants;
import com.abc.warehouse.dto.constants.RedisConstants;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JwtUtils {
    //密钥
    private static final String jwtToken = "123456wms!@#ss";

    public static String createToken(Long userId){
        Map<String,Object> claims = new HashMap<>();
        // 存入用户id和权限信息
        claims.put("userId",userId);
//        claims.put("permissions",permisisons);
        JwtBuilder jwtBuilder= Jwts.builder()
                .signWith(SignatureAlgorithm.HS256,jwtToken) //签发算法，密钥为jwtToken
                .setClaims(claims) //body数据，要唯一，自己设置
                .setIssuedAt(new Date()) //设置签发时间
                .setExpiration(new Date(System.currentTimeMillis()+24*60*60*60*1000)); //一天过期时间
        String token =jwtBuilder.compact();
        return token;
    }

    public static Map<String, Object> checkToken(String token){
        try {
            Jwt parse = Jwts.parser().setSigningKey(jwtToken).parse(token);
            return (Map<String, Object>) parse.getBody();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;

    }
}
