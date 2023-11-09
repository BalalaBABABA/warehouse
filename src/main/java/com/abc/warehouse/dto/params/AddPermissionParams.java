package com.abc.warehouse.dto.params;

import lombok.Data;

import java.util.List;

@Data
public class AddPermissionParams {

    private List<Long>  userIds;

    private Long resourceId;

    private String type;

    private String uri;
}
