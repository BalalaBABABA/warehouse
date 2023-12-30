package com.abc.warehouse.controller;

import com.abc.warehouse.dto.Result;
import com.abc.warehouse.dto.UserDTO;
import com.abc.warehouse.service.UserService;
import com.abc.warehouse.utils.UserHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/userInfo")
public class UserInfoController {
    @Autowired
    private UserService userService;

    @GetMapping
    public Result enter(){
        return Result.ok();
    }

    @GetMapping("/show")
    public Result showById(){
        UserDTO user = UserHolder.getUser();
        return Result.ok(UserHolder.getUser());
    }
    @GetMapping("/updatePassword/{newPassword}")
    public Result updatePassword(@PathVariable("newPassword") String newPassword){
        return userService.updatePassword(newPassword);
    }
    @GetMapping("/updatePhone/{newPhone}")
    public Result updatePhone(@PathVariable("newPhone") String newPhone,@RequestHeader("Authorization")String token){
        System.out.println("updatePhone");
        return userService.updatePhone(newPhone,token);
    }
}
