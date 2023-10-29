package com.abc.warehouse.service;

import com.abc.warehouse.dto.Result;
import com.abc.warehouse.dto.UserPermission;
import com.abc.warehouse.dto.params.LoginParams;
import com.abc.warehouse.pojo.User;

public interface LoginService {
    Result login(LoginParams loginParams);
    Result logout(String token);

    UserPermission checkToken(String token);
}
