package com.abc.warehouse.controller;

import com.abc.warehouse.dto.Result;
import com.abc.warehouse.dto.UserDTO;
import com.abc.warehouse.utils.UserHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/userInfo")
public class UserInfoController {
    @GetMapping
    public Result enter(){
        return Result.ok();
    }

    @GetMapping("/show")
    public Result showById(){
        UserDTO user = UserHolder.getUser();
        return Result.ok(UserHolder.getUser());
    }

}
