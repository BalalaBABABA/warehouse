package com.abc.warehouse.utils;

import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class SimpleRedisLock implements ILock{
    /**
     * 业务名称
     */
    private String name;

    private StringRedisTemplate stringRedisTemplate;


    private static final String ID_PREFIX = UUID.randomUUID().toString()+"_";
    private static final String KEY_PREFIX = "lock:";
    private static final DefaultRedisScript<Long> UNLOCK_SCRIPT;
    static{
        UNLOCK_SCRIPT=new DefaultRedisScript<>();
        UNLOCK_SCRIPT.setLocation(new ClassPathResource("unlock.lua"));
    }
    public SimpleRedisLock(String name, StringRedisTemplate stringRedisTemplate) {
        this.name = name;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public boolean tryLock(long timeoutSec) {
        //获取当前线程标识
        String threadId = ID_PREFIX+Thread.currentThread().getId();
        //获取锁
        Boolean result = stringRedisTemplate.opsForValue().setIfAbsent(KEY_PREFIX+ name, threadId, timeoutSec, TimeUnit.SECONDS);
        //判断获取锁是否成功
        return Boolean.TRUE.equals(result);
    }

    @Override
    public void unlock() {
        String currentThreadFlag = ID_PREFIX+Thread.currentThread().getId();
        stringRedisTemplate.execute(
                UNLOCK_SCRIPT,
                Collections.singletonList(KEY_PREFIX+name),
                currentThreadFlag
        );
//        //获得线程标识
//        String currentThreadFlag = ID_PREFIX+Thread.currentThread().getId();
//        String redisThreadFlag = stringRedisTemplate.opsForValue().get(KEY_PREFIX + name);
//        //判断标识是否一致
//        if(currentThreadFlag .equals(redisThreadFlag )){
//            //一致，释放锁
//            stringRedisTemplate.delete(KEY_PREFIX + name);
//        }
//        //不一致，什么都不做

    }
}
