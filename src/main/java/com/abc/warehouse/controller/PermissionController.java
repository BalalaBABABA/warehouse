package com.abc.warehouse.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.abc.warehouse.annotation.Decrypt;
import com.abc.warehouse.annotation.Encrypt;
import com.abc.warehouse.annotation.JsonParam;
import com.abc.warehouse.dto.EncryotResult;
import com.abc.warehouse.dto.Result;
import com.abc.warehouse.dto.params.AddPermissionParams;
import com.abc.warehouse.dto.params.UpdatePermissionParams;
import com.abc.warehouse.service.PermissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.abc.warehouse.dto.params.SearchPermissionParams;

@RestController
@Slf4j
@RequestMapping("/permission")
public class PermissionController {
    @Autowired
    private PermissionService permissionService;

    @GetMapping
    public Result enter(){
        return Result.ok();
    }
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
    @Encrypt
    public Result getPermissionTypesByResourceId(@PathVariable("resourceId")Long resourceId){
        return permissionService.getPermissionTypesByResourceId(resourceId);
    }

    /**
     * 根据resourceId获取对应的所有用户权限
     * @param resourceId
     * @return
     */
    @GetMapping("/get/{page}/{resource_id}")
    @Encrypt
    public Result getAllUsersPermissions(@PathVariable("page")Integer pageCount,@PathVariable("resource_id") Long resourceId){
        return permissionService.getAllUsersPermissionsByResourceId(pageCount,resourceId);
    }

    /**
     * 根据权限类型，更新用户的某个权限
     */
    @PostMapping("/update")
    @Decrypt
    @Encrypt
    public Result updateUserPermission(
            @JsonParam("userId") Long userId,
            @JsonParam("resourceId")Long resourceId,
            @JsonParam("type")String type,
            @JsonParam("flag")Boolean flag
            ){
        UpdatePermissionParams params = new UpdatePermissionParams(userId,resourceId,type,flag);
        return permissionService.updateUserPermission(params);
    }

    /**
     * 根据资源id，取消某个用户的对某个资源的所有权限
     * @param userId
     * @param resourceId
     * @return
     */
    @PostMapping("/update/all")
    @Decrypt
    @Encrypt
    public Result updateUserResource(@JsonParam("userId") Long userId,
                                     @JsonParam("resourceId")Long resourceId,
                                     @JsonParam("flag")Boolean flag
                                     ){
        return permissionService.updateUserResource(userId,resourceId,flag);
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
    @Encrypt
    public Result searchPermissionByUserId(@RequestBody SearchPermissionParams params){
        return permissionService.searchPermissionByUser(params);

    }

    /**
     * 根据resource_id和role，查找该资源下，该用户的权限
     * @return
     */
    @PostMapping("/search/role")
    @Encrypt
    public Result searchPermissionByRole(@RequestBody SearchPermissionParams params)
    {
        return permissionService.searchPermissionByRole(params);
    }


}
