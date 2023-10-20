package com.abc.warehouse.handler;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.abc.warehouse.dto.UserDTO;
import com.abc.warehouse.dto.constants.RedisConstants;
import com.abc.warehouse.pojo.Permission;
import com.abc.warehouse.pojo.User;
import com.abc.warehouse.service.PermissionService;
import com.abc.warehouse.utils.JwtUtils;
import com.abc.warehouse.utils.UserHolder;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.abc.warehouse.dto.constants.RedisConstants.PERMISSIONS_USER_TTL;

@Component
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private PermissionService  permissionService;

    @Override
    @Transactional
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        /**
         * 1.判断 请求路径 是否为HandlerMethod（controller方法）
         * 2.判断token是否为空
         * 3.token不为空，登录验证
         * 4.查权限缓存，判断是否有权限,有，读缓存，刷新有效期，没有，重建缓存
         * 5.认证成功，放行
         */
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        String token = request.getHeader("Authorization");

        log.info("=================request start===========================");
        String requestURI = request.getRequestURI();
        log.info("request uri:{}",requestURI);
        log.info("request method:{}",request.getMethod());
        log.info("token:{}", token);
        log.info("=================request end===========================");

        if(StringUtils.isBlank(token)){
            log.warn("token为空");
            return false;
        }

        Map<String, Object> map = JwtUtils.checkToken(token);
        if(map==null){
            log.warn("token登录验证失败");
            return false;
        }

        String tokenJson = redisTemplate.opsForValue().get(RedisConstants.LOGIN_USER_KEY + token);
        Map<String, Object> objectMap = JSONUtil.parseObj(tokenJson);
        Object userObj = objectMap.get("user");
        UserDTO userDTO = BeanUtil.toBean(userObj, UserDTO.class);
        Long userId = userDTO.getId();

        //先查缓存
        String permissionsJson = redisTemplate.opsForValue().get(RedisConstants.PERMISSIONS_USER_KEY + userId);
        List<String> permissions = JSONUtil.toList(permissionsJson, String.class);
        //刷新有效期
        redisTemplate.expire(RedisConstants.PERMISSIONS_USER_KEY + userId,PERMISSIONS_USER_TTL,TimeUnit.SECONDS);
        //缓存没有，查询数据库,并加入缓存
        if(permissions.isEmpty()){
            //1. 查询数据库
            List<Permission> permissionList = permissionService.getByUserId(userId);
            permissions = permissionList.stream().map(permission -> permission.getUri()).collect(Collectors.toList());
            //2. 设置缓存
            redisTemplate.opsForValue().set(RedisConstants.PERMISSIONS_USER_KEY+userId,JSONUtil.toJsonStr(permissions), PERMISSIONS_USER_TTL);
        }

        if(permissions.isEmpty())return true;
        for (String permissionUri : permissions) {
            if(permissionUri.equals(requestURI)){
                return true;
            }
        }
        log.warn("用户没有访问权限  uri:"+requestURI);
        return false;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserHolder.removeUser();
    }
}
