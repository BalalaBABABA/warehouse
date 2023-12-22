package com.abc.warehouse.controller;

import cn.hutool.core.bean.BeanUtil;
import com.abc.warehouse.dto.Result;
import com.abc.warehouse.dto.UserDTO;
import com.abc.warehouse.mapper.UserMapper;
import com.abc.warehouse.pojo.User;
import com.abc.warehouse.utils.JwtUtils;
import com.abc.warehouse.utils.UserHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/userInfo")
public class UserInfoController {
    @Autowired
    private UserMapper userMapper;
    @GetMapping
    public Result enter(){
        return Result.ok();
    }

    @GetMapping("/show")
    public Result showById(@RequestHeader("Authorization")String token){
        Long userId = JwtUtils.getUserIdFromToken(token);
        User user = userMapper.selectById(userId);
        UserDTO userDTO = BeanUtil.copyProperties(user, UserDTO.class);
        return Result.ok(userDTO);
    }

}
