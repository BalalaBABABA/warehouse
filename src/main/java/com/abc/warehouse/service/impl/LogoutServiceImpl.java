package com.abc.warehouse.service.impl;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.abc.warehouse.dto.Result;
import com.abc.warehouse.dto.UserDTO;
import com.abc.warehouse.dto.constants.RedisConstants;
import com.abc.warehouse.service.LogoutService;
import com.abc.warehouse.utils.JwtUtils;
import com.abc.warehouse.utils.UserHolder;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LogoutServiceImpl implements LogoutService {

    @Autowired
    private StringRedisTemplate redisTemplate;
    @Override
    @Transactional
    public Result logout(String token) {
        /**
         * 1.清除token
         * 2.清除permission
         */
        if(token.equals("null"))return Result.ok();
        String tokenJson = redisTemplate.opsForValue().getAndDelete(RedisConstants.LOGIN_USER_KEY + token);
        JSONObject jsonObject = JSONUtil.parseObj(tokenJson);
        JSONObject userJson = jsonObject.getJSONObject("user");
        Long userId = userJson.getLong("id");
        redisTemplate.delete(RedisConstants.PERMISSIONS_USER_KEY+userId);
        return Result.ok();
    }
}
