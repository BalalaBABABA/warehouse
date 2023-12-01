package com.abc.warehouse.utils;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class GenerateID {

    private StringRedisTemplate stringredisTemplate;

    public GenerateID(StringRedisTemplate stringredisTemplate){
        this.stringredisTemplate = stringredisTemplate;
    }

    //标识1位 + 时间8位 + 序列号6位
    public long getId(String type, String keyPrefix){
        // 获取当前时间戳
        LocalDateTime now = LocalDateTime.now();

        // 获取序列号
        String seqDate = now.format(DateTimeFormatter.ofPattern("yyyy:MM:dd"));
        long count = stringredisTemplate.opsForValue().increment("icr:" + keyPrefix + ":" + seqDate);
        System.out.println(count);

        // 生成id
        String date = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        return Long.parseLong( type + date + String.format("%06d", count));
    }
}
