package com.abc.warehouse.service.impl;

import com.abc.warehouse.dto.Result;
import com.abc.warehouse.dto.constants.RedisConstants;
import com.abc.warehouse.service.LogoutService;
import com.abc.warehouse.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class LogoutServiceImpl implements LogoutService {

    @Autowired
    private StringRedisTemplate redisTemplate;
    @Override
    public Result logout(String token) {
        /**
         * 清楚token
         */
        redisTemplate.delete(RedisConstants.LOGIN_USER_KEY+token);
        return Result.ok();
    }
}
