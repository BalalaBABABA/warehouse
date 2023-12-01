package com.abc.warehouse.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.abc.warehouse.dto.Result;
import com.abc.warehouse.dto.UserDTO;
import com.abc.warehouse.dto.UserPermission;
import com.abc.warehouse.dto.constants.ErrorCode;
import com.abc.warehouse.dto.constants.RedisConstants;
import com.abc.warehouse.dto.params.LoginParams;
import com.abc.warehouse.pojo.Permission;
import com.abc.warehouse.pojo.PermissionType;
import com.abc.warehouse.pojo.User;
import com.abc.warehouse.service.LoginService;
import com.abc.warehouse.service.PermissionService;
import com.abc.warehouse.service.PermissionTypeService;
import com.abc.warehouse.service.UserService;
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


    private static final String salt = "wms@#!";
    @Override
    @Transactional
    public Result login(LoginParams loginParams) {
        /**
         * 1. 检查参数是否合法
         * 2. 查询数据库用户和密码，是否存在
         * 3. 存在，查询该用户的权限，生成token，不存在，登录失败
         * 4. token存入redis，permissions存入redis
         * 5. 放入ThreadLocal
         */
        String password = loginParams.getPassword();
        Long userId = loginParams.getUserId();
        if(StringUtils.isBlank(userId.toString()) || RegexUtils.isPasswordInvalid(password))
        {
            return Result.fail(ErrorCode.PARAMS_ERROR.getMsg());
        }
        password = PasswordEncoder.encode(password,salt);
        log.info(password);
        // TODO 修改为调用service方法
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getId,userId)
                        .eq(User::getPassword,password)
                                .last("limit 1");
        User user = userService.getOne(queryWrapper);
        if(user==null){
            return Result.fail(ErrorCode.ACCOUNT_PWD_NOT_EXIST.getMsg());
        }

        List<Permission> permissions = permissionService.getByUserId(userId);
//        Set<String> set =new HashSet<>();
//        for (Permission permission : permissions) {
//            set.add(permission.getUri());
//        }
        Map<Long, PermissionType> typesMap = permissionTypeService.getAllTypesMap();
        List<String> permissionList ;
        if(permissions.isEmpty()){
            permissionList =Collections.emptyList();
        }else{
            permissionList = permissions.stream().map(permission -> typesMap.get(permission.getPermissionId()).getUri()).collect(Collectors.toList());
        }

        redisTemplate.opsForValue().set(RedisConstants.PERMISSIONS_USER_KEY+userId,JSONUtil.toJsonStr(permissionList),RedisConstants.PERMISSIONS_USER_TTL,TimeUnit.SECONDS);

        String token = JwtUtils.createToken(userId);
        Map<String,Object> map=new HashMap<>();
        map.put("user",user);
        redisTemplate.opsForValue().set(RedisConstants.LOGIN_USER_KEY+token, JSONUtil.toJsonStr(map),RedisConstants.LOGIN_USER_TTL, TimeUnit.SECONDS);

        //放入ThreadLocal
        UserHolder.saveUser(BeanUtil.copyProperties(user, UserDTO.class));
        return Result.ok(token);
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
