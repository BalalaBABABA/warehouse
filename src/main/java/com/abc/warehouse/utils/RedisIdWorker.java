package com.abc.warehouse.utils;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@Component
public class RedisIdWorker {
    private static  final long BEGIN_TIMESTAMP = 1672531200L;  //开始时间戳
    private static  final int COUNT_BITS = 32;  //开始时间戳

    private StringRedisTemplate stringredisTemplate;  //序列号的位数

    public RedisIdWorker(StringRedisTemplate stringredisTemplate){
        this.stringredisTemplate = stringredisTemplate;
    }

    //符号位1 + 时间戳31 + 序列号32
    public long nextId(String keyPrefix){
        // 获取当前时间戳
        LocalDateTime now = LocalDateTime.now();
        long nowSecond = now.toEpochSecond(ZoneOffset.UTC);
        long timeStamp = nowSecond - BEGIN_TIMESTAMP;

        // 获取序列号
        String date = now.format(DateTimeFormatter.ofPattern("yyyy:MM:dd"));
        long count = stringredisTemplate.opsForValue().increment("icr:" + keyPrefix + ":" + date);
        System.out.println(keyPrefix);
        System.out.println(count);

        // 生成id
        return timeStamp << COUNT_BITS | count;
    }

//    public static void main(String[] args) {
//        LocalDateTime time = LocalDateTime.of(2023, 1, 1, 0, 0, 0);
//        long second = time.toEpochSecond(ZoneOffset.UTC);
//        System.out.println("second = " + second);
//    }
}
