package com.abc.warehouse.controller;

import com.abc.warehouse.dto.Result;
import com.abc.warehouse.dto.params.AddPermissionParams;
import com.abc.warehouse.pojo.Permission;
import com.abc.warehouse.service.PermissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.abc.warehouse.dto.params.PermissionParams;

@RestController
@Slf4j
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
     * 根据resourceId获取types
     * @return
     */
    @GetMapping("/types/{resourceId}")
    public Result getPermissionTypesByResourceId(@PathVariable("resourceId")Long resourceId){
        return permissionService.getPermissionTypesByResourceId(resourceId);
    }

    /**
     * 根据resourceId获取对应的所有用户权限
     * @param resourceId
     * @return
     */
    @PostMapping("/{page}/{resource_id}")
    public Result getAllUsersPermissions(@PathVariable("page")Integer pageCount,@PathVariable("resource_id") Long resourceId){
        return permissionService.getAllUsersPermissionsByResourceId(pageCount,resourceId);
    }

    /**
     * 根据权限类型，更新用户的某个权限
     */
    @PostMapping("/update/{flag}")
    public Result updateUserPermission(@RequestBody Permission permission,@PathVariable("flag")Boolean flag){
        return permissionService.updateUserPermission(permission,flag);
    }

    /**
     * 根据资源id，取消某个用户的对某个资源的所有权限
     * @param userId
     * @param resourceId
     * @return
     */
    @PostMapping("/del/{user_id}/{resource_id}")
    public Result deleteUserResource(@PathVariable("user_id")Long userId,
                                     @PathVariable("resource_id")Long resourceId
                                     ){
        return permissionService.deleteUserResource(userId,resourceId);
    }

    /**
     * 给某些用户，添加某一种权限
     * @return
     */
    @PostMapping("/add")
    public Result addOneUserPermission(@RequestBody AddPermissionParams params){
        return permissionService.addOneUserPermission(params);
    }

    /**
     * 根据userId(user_name)和resource_id，查找该资源下，该用户的权限
     * @return
     */
    @PostMapping("/search")
    public Result searchPermissionByUserId(@RequestBody PermissionParams params){
        return permissionService.searchPermissionByUser(params);
    }

    /**
     * 根据resource_id和role，查找该资源下，该用户的权限
     * @return
     */
    @PostMapping("/search/role")
    public Result searchPermissionByRole(@RequestBody PermissionParams params)
    {
        return permissionService.searchPermissionByRole(params);
    }


}
