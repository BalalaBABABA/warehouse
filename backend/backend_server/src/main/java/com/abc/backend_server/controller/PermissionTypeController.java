package com.abc.backend_server.controller;

import com.abc.backend_server.dto.PermissionTypeDto;
import com.abc.backend_server.dto.Result;
import com.abc.backend_server.pojo.PermissionTypeUri;
import com.abc.backend_server.pojo.Resource;
import com.abc.backend_server.service.PermissionTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/permissiontype")
public class PermissionTypeController {
    @Autowired
    private PermissionTypeService permissionTypeService;
    @GetMapping("/listAll")
    public Result listAll(){
        return permissionTypeService.listAll();
    }

    @PostMapping("/addtype")
    public Result addPermissionType(@RequestBody PermissionTypeDto permissionTypeDto){
        Long resourceId = permissionTypeDto.getResourceId();
        String type = permissionTypeDto.getType();
        return permissionTypeService.AddPermissionType(resourceId,type);
    }

    @GetMapping("/deltype")
    public Result delPermissionType(@RequestParam("typeId")Long typeId){

        return permissionTypeService.delPermissionType(typeId);
    }

    @PostMapping("/addresource")
    public Result addResource(@RequestBody Resource resource){

        return permissionTypeService.AddResource(resource);
    }

    @GetMapping("/delresource")
    public Result delResource(@RequestParam("resourceId")Long resourceId){
        return permissionTypeService.delResource(resourceId);
    }

    @PostMapping("/adduri")
    public Result addUri(@RequestBody PermissionTypeUri permissionTypeUri){
        Long typeId = permissionTypeUri.getTypeId();
        String uri = permissionTypeUri.getName();
        return permissionTypeService.AddUri(typeId,uri);
    }

    @GetMapping("/deluri")
    public Result delUri(@RequestParam("id")Long id){
        return permissionTypeService.delUri(id);
    }
}
