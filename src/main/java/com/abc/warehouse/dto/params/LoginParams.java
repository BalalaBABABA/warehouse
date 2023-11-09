package com.abc.warehouse.dto.params;


import lombok.Data;

@Data
public class LoginParams {
    private Long userId;
    private String password;

    private String nickName;
}
