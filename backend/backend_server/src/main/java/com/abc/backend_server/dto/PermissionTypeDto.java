package com.abc.backend_server.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class PermissionTypeDto implements Serializable {
    private Long resourceId;
    private String type;
}
