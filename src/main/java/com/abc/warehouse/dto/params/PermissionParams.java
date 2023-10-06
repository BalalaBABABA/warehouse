package com.abc.warehouse.dto.params;

import lombok.Data;

import static com.abc.warehouse.dto.constants.PageConstants.PERMISSION_SEARCH_PAGE_SIZE;

@Data
public class PermissionParams {

    private Long userId;
    private String empName;

    private String role;

    private Long resourceId;

    private int currentPage;

    private int pageSize = PERMISSION_SEARCH_PAGE_SIZE;
}
