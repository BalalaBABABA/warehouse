package com.abc.warehouse.utils;

import cn.hutool.core.util.RandomUtil;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import org.apache.shiro.crypto.hash.Sha256Hash;
/**
 * @description: 密码加密、验证的工具类
 * @author : 小何
 */
public class PasswordEncoder {

    public static String encode(String password) {
        // 生成盐
        String salt = RandomUtil.randomString(20);
        // 加密
        return encode(password,salt);
    }
    public static String encode(String password, String salt) {
        // 加密
        return new Sha256Hash(password,salt).toString();
    }

    /**
     *
     * @param encodedPassword: 从数据库中查询的密码
     * @param rawPassword: 用户输入的密码
     * @return
     */
    public static Boolean matches(String encodedPassword, String rawPassword) {
        if (encodedPassword == null || rawPassword == null) {
            return false;
        }
//        if(!encodedPassword.contains("@")){
//            throw new RuntimeException("密码格式不正确！");
//        }
        String[] arr = encodedPassword.split("@");
        // 获取盐
        String salt = arr[0];
        // 比较
        return encodedPassword.equals(encode(rawPassword, salt));
    }
}