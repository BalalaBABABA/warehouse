package com.abc.warehouse.controller;

import com.abc.warehouse.dto.Result;
import com.abc.warehouse.pojo.Resource;
import com.abc.warehouse.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/resource")
public class ResourceController {
    @Autowired
    private ResourceService resourceService;

    @GetMapping("/")
    public Result getAllResources()
    {
        return resourceService.getAllResources();
    }

    @PostMapping("/{name}")
    public Result saveNewResource(@PathVariable("name")String name){
        boolean save = resourceService.save(new Resource(null, name));
        return save?Result.ok():Result.fail("增加权限功能失败！");
    }
}
