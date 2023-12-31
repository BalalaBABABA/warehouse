package com.abc.warehouse.service;

import com.abc.warehouse.dto.Result;
import com.abc.warehouse.dto.UserPermission;
import com.abc.warehouse.dto.params.LoginParams;
import com.abc.warehouse.pojo.User;

import java.util.List;

public interface LoginService {
    Result login(Long userId,String password);
    String createToken(User user);

}
