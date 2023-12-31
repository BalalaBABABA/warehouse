package com.abc.backend_server.pojo;

import lombok.Data;

import java.io.Serializable;

@Data
public class PermissionTypeUri implements Serializable {

    private Integer id;

    private Long typeId;
    private String name;
}