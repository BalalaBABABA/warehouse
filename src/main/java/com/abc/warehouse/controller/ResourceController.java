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

    @GetMapping
    public Result getAllResources()
    {
        return resourceService.getAllResources();
    }

    @PostMapping("/{name}")
    public Result saveNewResource(@PathVariable("name")String name){
        resourceService.save(new Resource(null, name));
        return Result.ok(null);
    }
}
