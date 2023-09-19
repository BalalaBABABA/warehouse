package com.abc.warehouse.utils;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import static com.hmdp.utils.RedisConstants.*;

@Slf4j
@Component
public class CacheClient {
    private final StringRedisTemplate stringRedisTemplate;
    private static final ExecutorService CACHE_REBUILD_EXECUTOR= Executors.newFixedThreadPool(10);
    public CacheClient(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public void set(String key, Object value, Long time, TimeUnit unit)
    {
        stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(value),time,unit);
    }

    public void setWithLogicalExpire(String key, Object value, Long time, TimeUnit unit)
    {
        //设置逻辑过期
        RedisData redisData=new RedisData();
        redisData.setData(value);
        redisData.setExpireTime(LocalDateTime.now().plusSeconds(unit.toSeconds(time)));
        //写入Redis
        stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(redisData));
    }

    public <R,ID>R queryWithPassThrough(String keyPrefix, ID id, Class<R> type, Function<ID,R> dbFallback,Long time,TimeUnit unit){
        String key = keyPrefix+id;
        //从redis中查询缓存
        String json = stringRedisTemplate.opsForValue().get(key);

        //如果有缓存，返回缓存
        if(StrUtil.isNotBlank(json))
        {
            //返回
            return JSONUtil.toBean(json,type);
        }
        //上面isNotBlank方法已经判断了“”和null的情况，返回false，说明是“”或null
        // 因为缓存对象为“”，所以判断不是null
        if(json!=null){
            //命中空对象，返回错误信息
            return null;
        }

        //没有缓存，查询数据库
        R r = dbFallback.apply(id);
        if(r == null)
        {
            //查询数据库没查到，redis中存入null对象
            stringRedisTemplate.opsForValue().set(key,"",CACHE_NULL_TTL,TimeUnit.MINUTES);
            return null;
        }

        //店铺存在,存入缓存
        this.set(key,r,time,unit);
        return r;
    }



    public <R,ID>R queryWithLogicExpire(String keyPrefix,ID id,Class<R> type,Function<ID,R>dbFallback,Long time,TimeUnit unit,Long expireSeconds){
        String key = keyPrefix + id;
        //从redis中查询缓存
        String json = stringRedisTemplate.opsForValue().get(key);

        //如果未命中
        if (StrUtil.isBlank(json)) {
            //3.存在，缓存中存的null
            return null;
        }

        RedisData redisData = JSONUtil.toBean(json, RedisData.class);
        JSONObject data=(JSONObject)redisData.getData();
        R r= JSONUtil.toBean(data,type);

        LocalDateTime expireTime=redisData.getExpireTime();
        //命中，判断缓存是否过期
        if(expireTime.isAfter(LocalDateTime.now())){
            //未过期，返回
            return r;
        }
        //已过期，缓存重建

        //获取互斥锁
        String lockKey=LOCK_SHOP_KEY+id;
        boolean islock = trylock(lockKey);
        //判断获取互斥锁是否成功
        if(islock){
            //获取互斥锁成功，DoubleCheck，再次检测redis缓存是否过期
            json = stringRedisTemplate.opsForValue().get(key);
            redisData = JSONUtil.toBean(json, RedisData.class);
            r= JSONUtil.toBean((JSONObject)redisData.getData(),type);
            if(expireTime.isAfter(LocalDateTime.now())){
                //未过期，返回
                return r;
            }
            //确实过期，开启独立线程，实现缓存重建

            //创建了一个线程池
            CACHE_REBUILD_EXECUTOR.submit(()->{
                try {
                    //缓存重建
                    //1.查询店铺数据
                    R r1 = dbFallback.apply(id);
                    //2.写入redis
                    this.setWithLogicalExpire(key,r1,time,unit);

                    return r1;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                } finally {
                    //释放锁
                    unlock(lockKey);
                }
            });


        }
        return r;
    }


    public boolean trylock(String key){
        Boolean aBoolean = stringRedisTemplate.opsForValue().setIfAbsent(key, "1", 10, TimeUnit.MINUTES);
        return BooleanUtil.isTrue(aBoolean);
    }

    public void unlock(String key){
        stringRedisTemplate.delete(key);
    }
}
