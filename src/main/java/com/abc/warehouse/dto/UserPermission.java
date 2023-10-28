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
    /**
     * 人员代码
     */

    private Long id;

    /**
     * 姓名
     */
    private String name;

    /**
     * 角色
     */
    private String role;
    /**
     * 权限列表
     */
    private List<String> permissionList;


    private String permissionListStr;

    public UserPermission(Long id, String name, String role, List<String> permissionList) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.permissionList = permissionList;
    }
}
