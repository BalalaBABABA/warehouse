package com.abc.warehouse.dto;

import com.abc.warehouse.pojo.Permission;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPermission {

    private Long id;

    private String name;

    private String role;

    private List<String> permissionList;


    private String permissionListStr;

    public UserPermission(Long id, String name, String role, List<String> permissionList) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.permissionList = permissionList;
    }
}
