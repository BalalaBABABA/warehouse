package com.abc.warehouse.controller;

import com.abc.warehouse.dto.Result;
import com.abc.warehouse.service.LogoutService;
import com.abc.warehouse.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/logout")
@Slf4j
public class LogoutController {

    @Autowired
    private LogoutService logoutService;

    /**
     * 退出登录
     * @param token
     * @return
     */
    @GetMapping
    public Result logout(@RequestHeader("Authorization")String token){
        return logoutService.logout(token);
    }
}
