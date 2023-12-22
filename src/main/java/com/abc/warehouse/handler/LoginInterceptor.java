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
@Slf4j

public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private FreeUriService freeUriService;
    @Autowired
    private PermissionTypeService permissionTypeService;
    private static final String PERMISSION_URI_REGEX = "(/\\w+)+";
    @Override
    @Transactional
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        /**
         * 1.判断 请求路径 是否为HandlerMethod（controller方法）
         * 2.判断token是否为空
         * 3.token不为空，登录验证,token有效期验证
         * 4.判断是否是admin
         * 5.查权限缓存，判断是否有权限,有，读缓存，刷新缓存有效期，没有，重建缓存
         * 6.认证成功
         *     1.放入ThreadLocal
         *     2.放行
         * 7.认证不成功
         *     1.放入ThreadLocal
         *     2.返回错误
         */
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        String token = request.getHeader("Authorization");
        Result result = new Result();
        log.info("=================request start===========================");
        String requestURI = request.getRequestURI();
        log.info("request uri:{}",requestURI);
        log.info("request method:{}",request.getMethod());
        log.info("token:{}", token);
        log.info("=================request end===========================");
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
            result.setSuccess(false);
            result.setCode(ErrorCode.NO_LOGIN.getCode());
            result.setErrorMsg(ErrorCode.NO_LOGIN.getMsg());
            String resultJson =JSONUtil.toJsonStr(result);
            //response.setStatus(ErrorCode.NO_LOGIN.getCode());
            response.getWriter().write(resultJson);
            return false;
        }

        Map<String, Object> map = JwtUtils.checkToken(token);
        if(map==null){
            log.warn("token登录验证失败");
            result.setSuccess(false);
            result.setCode(ErrorCode.TOKEN_ILLEGAL_EXIST.getCode());
            result.setErrorMsg(ErrorCode.TOKEN_ILLEGAL_EXIST.getMsg());
            String resultJson =JSONUtil.toJsonStr(result);
            response.getWriter().write(resultJson);
            return false;
        }
        //如果redis查到token为空，则说明token有效期已过
        String tokenJson = redisTemplate.opsForValue().get(RedisConstants.LOGIN_USER_KEY + token);
        if(StringUtils.isBlank(tokenJson)){
            log.warn("token有效期已过");
            result.setSuccess(false);
            result.setCode(ErrorCode.TOKEN_ILLEGAL_EXIST.getCode());
            result.setErrorMsg(ErrorCode.TOKEN_ILLEGAL_EXIST.getMsg());
            String resultJson =JSONUtil.toJsonStr(result);
            response.getWriter().write(resultJson);
            return false;
        }
        Map<String, Object> objectMap = JSONUtil.parseObj(tokenJson);
        Object userObj = objectMap.get("user");
        UserDTO userDTO = BeanUtil.toBean(userObj, UserDTO.class);
        Long userId = userDTO.getId();
        String role = userDTO.getRole();

        //如果是amdin，直接放行
        if(role.equals("admin")){
            //放入ThreadLocal
            UserHolder.saveUser(userDTO);
            return true;
        }

        //先查该用户权限缓存
        List<String> permissions = permissionService.getPermissionCacheById(userId);
        //刷新用户权限uri有效期
        redisTemplate.expire(RedisConstants.PERMISSIONS_USER_KEY + userId,PERMISSIONS_USER_TTL,TimeUnit.SECONDS);
        //刷新token有效期
        redisTemplate.expire(RedisConstants.LOGIN_USER_KEY + token,PERMISSIONS_USER_TTL,TimeUnit.SECONDS);

//        AntPathMatcher pathMatcher = new AntPathMatcher();
        for (String permissionUri : permissions) {
            if (pathMatcher.match(permissionUri, requestURI)) {
                //放入ThreadLocal
                UserHolder.saveUser(userDTO);
                return true;
            }
        }

        log.warn("用户没有访问权限  uri:"+requestURI);

        result.setSuccess(false);
        result.setErrorMsg(ErrorCode.NO_PERMISSION.getMsg());
        String resultJson =JSONUtil.toJsonStr(result);
        response.getWriter().write(resultJson);

        //放入ThreadLocal
        UserHolder.saveUser(userDTO);
        return false;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserHolder.removeUser();
    }
}
