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
         * 1. 判断token是否为”null“
         *      1. 是,说明还未登录,也不用管退出,直接返回ok
         *      2. 否,继续
         * 2. 从缓存中获得token
         * 3. 清除token的缓存
         * 4. 解析token中的userId
         * 5. 根据userId清除该用户权限信息缓存
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
