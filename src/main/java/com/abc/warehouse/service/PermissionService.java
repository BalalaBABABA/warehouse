package com.abc.warehouse.service;

import com.abc.warehouse.dto.Result;
import com.abc.warehouse.dto.params.AddPermissionParams;
import com.abc.warehouse.dto.params.PermissionParams;
import com.abc.warehouse.pojo.Permission;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 吧啦
* @description 针对表【permission_208201302(权限表)】的数据库操作Service
* @createDate 2023-09-23 17:39:26
*/
public interface PermissionService extends IService<Permission> {
    Result getPermissionTypes();
    Result getAllUsersPermissionsByResourceId(Integer pageCount, Long resourceId) ;
    Result updateUserPermission(Permission permission,Boolean flag);
    Result deleteUserResource(Long userId, Long resourceId);
    Result getPermissionTypesByResourceId(Long resourceId);

    Result addOneUserPermission(AddPermissionParams permission);

    Result searchPermissionByUser(PermissionParams permission);


    Result searchPermissionByRole(PermissionParams params);
}
