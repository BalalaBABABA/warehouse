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
     * ???????
     * @return
     */
    @GetMapping("/types")
    public Result getPermissionTypes(){
        return permissionService.getPermissionTypes();
    }



    /**
     * ??resourceId??types
     * @return
     */
    @GetMapping("/types/{resourceId}")
    public Result getPermissionTypesByResourceId(@PathVariable("resourceId")Long resourceId){
        return permissionService.getPermissionTypesByResourceId(resourceId);
    }

    /**
     * ??resourceId???????????
     * @param resourceId
     * @return
     */
    @PostMapping("/{page}/{resource_id}")
    public Result getAllUsersPermissions(@PathVariable("page")Integer pageCount,@PathVariable("resource_id") Long resourceId){
        return permissionService.getAllUsersPermissionsByResourceId(pageCount,resourceId);
    }

    /**
     * ????????????????
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
     * ????id??????????
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
}
