package com.abc.warehouse.service;

import com.abc.warehouse.dto.Result;

public interface UserInfoService {
    Result updatePhone(String newPhone,String token);
}
