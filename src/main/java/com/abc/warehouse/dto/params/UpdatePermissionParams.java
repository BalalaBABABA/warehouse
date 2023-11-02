package com.abc.warehouse.dto.params;


import lombok.Data;

import java.util.List;

@Data
public class UpdatePermissionParams {

    private Long  userId;

    private Long resourceId;

    private String type;

    private String uri;
}
