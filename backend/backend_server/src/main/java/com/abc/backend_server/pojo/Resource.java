package com.abc.backend_server.pojo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class Resource implements Serializable {

    private Long id;

    private String name;

    private String uri;

    private String icon;

    private String page;

    private List<PermissionType> permission;
}