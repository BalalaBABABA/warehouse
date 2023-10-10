package com.abc.warehouse.controller;


import com.abc.warehouse.dto.Result;
import com.abc.warehouse.pojo.User;
import com.abc.warehouse.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 用户登录
     * @param request
     * @param user
     * @return
     */
    @PostMapping("/login")
    public Result login(HttpServletRequest request, @RequestBody User user)
    {


        return null;
    }

    @PostMapping("/save")
    public Result save(@RequestBody User user){
        //设置初始密码123456，需要进行md5加密处理
        user.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        userService.save(user);
        return Result.ok("新增用户成功");
    }

    @GetMapping("/{id}")
    public Result getUserById(@PathVariable("id")Long id){
        // 调用userService的方法实现
        return Result.ok("你好");
    }

    @GetMapping("/totalpage")
    public long getTotalPage(){
        return userService.getTotalPage();
    }

    @GetMapping("/list")
    public Result getAllUser(){
        return userService.getAllUser();
    }

    @PostMapping("/{id}")
    public Result getAllUser(@PathVariable("id") Long id){
        return userService.deleteUserById(id);
    }
}
