package com.abc.warehouse.dto.params;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class UpdatePermissionParams {

    private Long  userId;

    private Long resourceId;

    private String type;

//    private String uri;
    private Boolean flag;
}
