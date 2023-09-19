package com.abc.warehouse.utils;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@Component
public class RedisIdWorker {
    /**
     * 开始的时间戳
     */
    private static  final long BEGIN_TIMESTAMP=1692921600L;
    /**
     * 序列号的位数
     */
    private static  final long COUNT_BITS=32;

    private StringRedisTemplate stringRedisTemplate;

    public RedisIdWorker(StringRedisTemplate stringRedisTemplate)
    {
        this.stringRedisTemplate=stringRedisTemplate;
    }

    public long nextId(String keyPrefix){
        //1.生成时间戳
        LocalDateTime now= LocalDateTime.now();
        long nowSeconds = now.toEpochSecond(ZoneOffset.UTC);
        long timestamp=nowSeconds-BEGIN_TIMESTAMP;

        //2.生成序列号
        //获得当前日期
        String date = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        //自增长
        Long count = stringRedisTemplate.opsForValue().increment("icr:" + keyPrefix + ":" + date);
        //3.拼接并返回
        return timestamp << 32 | count;
    }

}
