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
import org.springframework.web.bind.annotation.PathVariable;

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
         * 1. 参数判空
         * 2. 密码加密，查询用户和密码是否匹配
         *      1.不存在，返回账户或密码不匹配消息
         *      2.存在，继续
         * 3. 判断是否是超级管理员
         *      1.超级管理员,权限list赋值为所有权限
         *      2.普通用户,查询该用户权限,为权限list赋值,如果无权限,为权限list赋值为空list
         * 4. 用户权限信息,存入redis
         * 5. 生成token,存入redis
         * 6. 返回token
         */
        //参数判空
        if(userId==null || RegexUtils.isPasswordInvalid(password))
        {
            return Result.fail(ErrorCode.PARAMS_ERROR.getMsg());
        }

        //用户权限uri的list
        List<String> permissionList = new ArrayList<>();

        Result result=userService.getUserById(userId);
        User user=(User)result.getData();

//        //输入密码通过算法加密
//        String encodedPassword = PasswordEncoder.encode(password,salt);
//        //查找该用户信息，匹配用户名和密码
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(User::getId,userId)
//                        .eq(User::getPassword,encodedPassword)
//                                .last("limit 1");
//        User user = userService.getOne(queryWrapper);

        boolean b = SaltMD5Util.verifySaltPassword(password, user.getPassword());
        //如果账号密码不匹配，返回错误信息
//        if(user==null && userId != 9797){
//            return Result.fail(ErrorCode.ACCOUNT_PWD_NOT_EXIST.getMsg());
//        }
        if(!b && userId != 9797){
            return Result.fail(ErrorCode.ACCOUNT_PWD_NOT_EXIST.getMsg());
        }

        //判断用户身份
        //是超级管理员
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

}
