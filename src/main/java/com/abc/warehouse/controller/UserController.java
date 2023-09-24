package com.abc.warehouse.controller;

import com.abc.warehouse.dto.Result;
import com.abc.warehouse.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public Result getUserById(@PathVariable("id")Long id){
        // 调用userService的方法实现
        return Result.ok("你好");//444444
    }

    @GetMapping("/totalpage")
    public long getTotalPage(){
        return userService.getTotalPage();
    }//1112223333

    @GetMapping("/list")
    public Result getAllUser(){
        return userService.getAllUser();
    }

    @PostMapping("/{id}")
    public Result getAllUser(@PathVariable("id") Long id){
        return userService.deleteUserById(id);
    }
}
