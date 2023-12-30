package com.abc.backend_server.pojo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class PermissionType implements Serializable {
    private Long id;

    private Long resourceId;

    private String name;


    private List<PermissionTypeUri> uri;

}