package com.abc.warehouse.service;

import java.util.Map;

public interface MsmService {
    //发送验证码
    boolean send(Map<String, Object> param, String phone);
}
