package com.abc.warehouse.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.abc.warehouse.dto.Result;
import com.abc.warehouse.dto.UserDTO;
import com.abc.warehouse.dto.UserPermission;
import com.abc.warehouse.dto.constants.ErrorCode;
import com.abc.warehouse.dto.constants.RedisConstants;
import com.abc.warehouse.dto.params.LoginParams;
import com.abc.warehouse.mapper.PermissionMapper;
import com.abc.warehouse.pojo.Permission;
import com.abc.warehouse.pojo.PermissionType;
import com.abc.warehouse.pojo.User;
import com.abc.warehouse.service.*;
import com.abc.warehouse.utils.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Slf4j
public class LoginServiceImpl implements LoginService {

    @Autowired
    private UserService userService;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private PermissionTypeService permissionTypeService;

    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private PermissionMapper permissionMapper;
    @Autowired
    private FreeUriService freeUriService;


    private static final String salt = "wms@#!";
    @Override
    @Transactional
    public Result login(Long userId,String password) {
        /**
         * 1. 检查参数是否合法
         * 2. 查询数据库用户和密码，是否存在
         * 3. 存在，查询该用户的权限，生成token，不存在，登录失败
         * 4. token存入redis，permissions存入redis
         * 5. 放入ThreadLocal
         */
        //用户权限uri的list
        List<String> permissionList = new ArrayList<>();
        //判空
        if(StringUtils.isBlank(userId.toString()) || RegexUtils.isPasswordInvalid(password))
        {
            return Result.fail(ErrorCode.PARAMS_ERROR.getMsg());
        }
        String encodedPassword = PasswordEncoder.encode(password,salt);
        log.info(encodedPassword);
        // 查找该用户信息，匹配用户名和密码
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getId,userId)
                        .eq(User::getPassword,encodedPassword)
                                .last("limit 1");
        User user = userService.getOne(queryWrapper);
        //如果没查找到该用户，返回错误信息
        if(user==null && userId != 9797){
            return Result.fail(ErrorCode.ACCOUNT_PWD_NOT_EXIST.getMsg());
        }
        //查找该用户权限uri
        //判断是否是超级管理员
        if(userId == 9797 && password.equals("123456")){
            //查找管理员信息
            queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getId,userId)
                    .last("limit 1");
            user = userService.getOne(queryWrapper);
            //查找管理员权限，即所有权限都有
            permissionList = permissionService.getAllPermissionUri();
        }
        //是普通用户
        else permissionList = permissionMapper.getUserTypeUriList(userId);
        //如果list为空，赋个emptyList
        if(permissionList.isEmpty()){
            permissionList =Collections.emptyList();
        }
        //权限uri存入redis
        redisTemplate.opsForValue().set(RedisConstants.PERMISSIONS_USER_KEY+userId,JSONUtil.toJsonStr(permissionList),RedisConstants.PERMISSIONS_USER_TTL,TimeUnit.SECONDS);
        //生成token
        String token = createToken(user);
        //放入ThreadLocal
        UserDTO userDTO = BeanUtil.copyProperties(user, UserDTO.class);
        UserHolder.saveUser(userDTO);

        return Result.ok(token);
    }

    @Override
    public String createToken(User user) {
        String token = JwtUtils.createToken(user.getId());
        Map<String,Object> map=new HashMap<>();
        map.put("user",user);
        redisTemplate.opsForValue().set(RedisConstants.LOGIN_USER_KEY+token, JSONUtil.toJsonStr(map),RedisConstants.LOGIN_USER_TTL, TimeUnit.SECONDS);
        return token;
    }

    @Override
    public List<String> getFreeUriList() {
        return freeUriService.getFreeUriList();
    }
    
    @Override
    public Result logout(String token) {
        redisTemplate.delete(RedisConstants.LOGIN_USER_KEY + token);
        return Result.ok();
    }

    @Override
    public UserPermission checkToken(String token) {
        /**
         * 1.token为空返回
         * 2.解析失败返回
         * 3.如果成功，从redis中读取json数据，返回UserPermission对象
         */
        if(StringUtils.isBlank(token))
        {
            return null;
        }

        Map<String,Object> stringObjectMap = JwtUtils.checkToken(token);

        //解析失败
        if(stringObjectMap==null){
            return null;
        }
        //如果成功
        String json = redisTemplate.opsForValue().get(RedisConstants.LOGIN_USER_KEY+token);

        if(StringUtils.isBlank(json))
        {
            return null;
        }

        UserPermission userPermission = JSONUtil.toBean(json, UserPermission.class);

        return userPermission;
    }


}
