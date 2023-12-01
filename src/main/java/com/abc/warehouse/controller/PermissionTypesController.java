package com.abc.warehouse.controller;

import com.abc.warehouse.dto.Result;
import com.abc.warehouse.service.PermissionTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/permissiontype")
public class PermissionTypesController {

    @Autowired
    private PermissionTypeService permissionTypeService;

    @GetMapping("/map")
    public Result getTypesMap(){
        return Result.ok(permissionTypeService.getAllTypesMap());
    }

    @GetMapping("/types/map")
    public Result getPermissionTypesMap(){
        return permissionTypeService.getPermissionTypesMap();
    }

    @GetMapping("/del/{resourceId}/{type}")
    public Result delPermissionType(@PathVariable("resourceId")Long resourceId,@PathVariable("type")String type){
        return permissionTypeService.delPermissionType(resourceId,type);
    }

    @GetMapping("/select/map/{resourceId}")
    public Result getSelectMap(@PathVariable("resourceId")long resourceId){
        return permissionTypeService.getSelectMap(resourceId);
    }
}
