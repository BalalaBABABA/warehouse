package com.abc.warehouse.controller;


import com.abc.warehouse.annotation.Encrypt;
import com.abc.warehouse.dto.Result;
import com.abc.warehouse.pojo.Material;
import com.abc.warehouse.pojo.User;
import com.abc.warehouse.service.UserService;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @GetMapping
    public Result enter(){
        return Result.ok();
    }

    @PostMapping("/save")
    public Result save(@RequestBody User user){
        return userService.saveUser(user);
    }


    @GetMapping("/delete/{id}")
    public Result delete(@PathVariable("id") Long id){
        return userService.deleteUser(id);
    }

    @GetMapping("/search/{page}")
    @Encrypt
    public Result search(@PathVariable("page") Integer page){
        return userService.userPage(page);
    }


    @GetMapping("/searchByName/{page}/{name}")
    @Encrypt
    public Result searchByName(@PathVariable("page") Integer page, @PathVariable("name") String name){
        return userService.searchByName(page, name);
    }

    @GetMapping("/searchById/{page}/{id}")
    @Encrypt
    public Result searchById(@PathVariable("page") Integer page, @PathVariable("id") Long id){
        return userService.searchById(page, id);
    }

    @PostMapping("/update")
    public Result update(@RequestBody User user){
        return userService.updateUser(user);
    }

    @PostMapping("/resetPassword")
    public Result resetPassword(@RequestBody User user){
        return userService.resetPassword(user);
    }

    @GetMapping("/findAllUserName")
    public Result findAllUserName(){
        return Result.ok(userService.findAllUserName());
    }

    @GetMapping("/getAll")
    public Result getAllUser(){
        return Result.ok(userService.getAllUser());
    }

    @GetMapping("/getNowUser")
    public Result getNowUser(@RequestHeader("Authorization")String token)
    {
        return userService.getNowUser(token);
    }


}
