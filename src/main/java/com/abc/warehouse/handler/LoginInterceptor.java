package com.abc.warehouse.handler;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.abc.warehouse.dto.Result;
import com.abc.warehouse.dto.UserDTO;
import com.abc.warehouse.dto.constants.ErrorCode;
import com.abc.warehouse.dto.constants.RedisConstants;
import com.abc.warehouse.mapper.PermissionMapper;
import com.abc.warehouse.pojo.Permission;
import com.abc.warehouse.pojo.PermissionType;
import com.abc.warehouse.pojo.User;
import com.abc.warehouse.service.FreeUriService;
import com.abc.warehouse.service.PermissionService;
import com.abc.warehouse.service.PermissionTypeService;
import com.abc.warehouse.utils.JwtUtils;
import com.abc.warehouse.utils.UserHolder;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.util.AntPathMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.UriTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.abc.warehouse.dto.constants.RedisConstants.PERMISSIONS_USER_TTL;

@Component
@Order(0)
@Slf4j

public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private FreeUriService freeUriService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        /**
         * 1.获取请求路径
         * 2.判断 请求路径 是否为HandlerMethod（controller方法）
         * 3.判断 请求路径 是否属于放行uri
         * 4.判断token是否为空
         * 5.解析token，是否解析正确
         * 6.redis登录验证,token有效期验证
         * 7.从token解析出userId和role
         * 8.判断role是否是admin
         *     1.是admin,直接放行
         *     2.不是，继续
         * 9.查该用户权限uriList缓存
         *     1.有,读缓存
         *     2.没有,重建权限缓存
         * 10.刷新token和权限uri缓存的有效期
         * 11.判断 请求路径 是否属于该用户权限uriList
         * 12.匹配成功
         *     1.放入ThreadLocal
         *     2.放行
         * 13.匹配不成功，返回无权限错误
         */
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        String token = request.getHeader("Authorization");
        String requestURI = request.getRequestURI();
        log.info("=================request start===========================");
        log.info("request uri:{}",requestURI);
        log.info("request method:{}",request.getMethod());
        log.info("token:{}", token);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        //获取放行uri,如果有匹配的就直接放行
        List<String> freeUriList = freeUriService.getFreeUriList();
        AntPathMatcher pathMatcher = new AntPathMatcher();
        for (String freeUri : freeUriList) {
            if (pathMatcher.match(freeUri, requestURI)) {
                return true;
            }
        }

        // 判断token是否为空
        if(StringUtils.isBlank(token)){
            log.warn("token为空");
            String resultJson =JSONUtil.toJsonStr(Result.fail(ErrorCode.NO_LOGIN.getCode(), ErrorCode.NO_LOGIN.getMsg()));
            response.getWriter().write(resultJson);
            return false;
        }
        // jwt解析token
        Map<String, Object> map = JwtUtils.checkToken(token);
        if(map==null){
            log.warn("token登录验证失败");
            String resultJson =JSONUtil.toJsonStr(Result.fail(ErrorCode.TOKEN_ILLEGAL_EXIST.getCode(), ErrorCode.TOKEN_ILLEGAL_EXIST.getMsg()));
            response.getWriter().write(resultJson);
            return false;
        }
        //获取以token,如果redis查到token为空，则说明token有效期已过
        String tokenJson = redisTemplate.opsForValue().get(RedisConstants.LOGIN_USER_KEY + token);
        if(StringUtils.isBlank(tokenJson)){
            log.warn("token有效期已过");
            String resultJson =JSONUtil.toJsonStr(Result.fail(ErrorCode.TOKEN_ILLEGAL_EXIST.getCode(), ErrorCode.TOKEN_ILLEGAL_EXIST.getMsg()));
            response.getWriter().write(resultJson);
            return false;
        }

        //从token解析出userId和role
        Map<String, Object> objectMap = JSONUtil.parseObj(tokenJson);
        Object userObj = objectMap.get("user");
        UserDTO userDTO = BeanUtil.toBean(userObj, UserDTO.class);
        Long userId = userDTO.getId();
        String role = userDTO.getRole();

        //如果是amdin，直接放行
        if(role.equals("admin")){
            log.warn("result:超级管理员直接通过");
            log.info("=================request end===========================");
            return true;
        }

        //先查该用户权限缓存
        List<String> permissions = permissionService.getPermissionCacheById(userId);

        //刷新用户权限uri有效期
        redisTemplate.expire(RedisConstants.PERMISSIONS_USER_KEY + userId,PERMISSIONS_USER_TTL, TimeUnit.SECONDS);
        //刷新token有效期
        redisTemplate.expire(RedisConstants.LOGIN_USER_KEY + token,PERMISSIONS_USER_TTL,TimeUnit.SECONDS);

        //请求路径 与 权限uri 匹配

        for (String permissionUri : permissions) {
            if (pathMatcher.match(permissionUri, requestURI)) {
                log.warn("result:用户有权限，访问成功");
                log.info("=================request end===========================");
                return true;
            }
        }

        //返回错误消息，无权限消息
        log.warn("result:用户没有访问权限");
        log.info("=================request end===========================");
        String resultJson =JSONUtil.toJsonStr(Result.fail(ErrorCode.NO_PERMISSION.getCode(), ErrorCode.NO_PERMISSION.getMsg()));
        response.getWriter().write(resultJson);
        return false;
    }

//    @Override
//    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
//        UserHolder.removeUser();
//    }
}
