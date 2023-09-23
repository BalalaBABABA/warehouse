package com.abc.warehouse.controller;

import com.abc.warehouse.dto.Result;
import com.abc.warehouse.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/permission")
public class PermissionController {
    @Autowired
    private PermissionService permissionService;

    /**
     * 获取所有权限类型
     * @return
     */
    @GetMapping("/types")
    public Result getPermissionTypes(){
        return permissionService.getPermissionTypes();
    }

    /**
     * 根据resourceId获取对应的所有用户权限
     * @param resourceId
     * @return
     */
    @PostMapping("/{resource_id}")
    public Result getAllUsersPermissions(@PathVariable("resource_id") Long resourceId){
        return permissionService.getAllUsersPermissionsByResourceId(resourceId);
    }

    /**
     * 根据权限类型，更新用户的某个权限
     * @param userId
     * @param resourceId
     * @param type
     * @return
     */
    @PostMapping("/update/{user_id}/{resource_id}/{type}/{flag}")
    public Result updateUserPermission(@PathVariable("user_id")Long userId,@PathVariable("resource_id")Long resourceId
                                       ,@PathVariable("type")String type,@PathVariable("flag")Boolean flag){
        return permissionService.updateUserPermission(userId,resourceId,type,flag);
    }

    /**
     * 根据资源id，取消某个用户的权限
     * @param userId
     * @param resourceId
     * @return
     */
    @PostMapping("/del/{user_id}/{resource_id}/{flag}")
    public Result deleteUserResource(@PathVariable("user_id")Long userId,
                                     @PathVariable("resource_id")Long resourceId
                                     ){
        return permissionService.deleteUserResource(userId,resourceId);
    }
}
