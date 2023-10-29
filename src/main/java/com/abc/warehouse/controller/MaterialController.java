package com.abc.warehouse.controller;


import com.abc.warehouse.dto.Result;
import com.abc.warehouse.pojo.Material;
import com.abc.warehouse.service.MaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/material")
public class MaterialController {
    @Autowired
    private MaterialService materialService;

    @PostMapping("/save")
    public Result save(@RequestBody Material material){
        return materialService.saveMaterial(material);
    }

    @GetMapping("/del/{id}")
    public Result del(@PathVariable("id") Long id){
        return materialService.delMaterial(id);
    }

    @PostMapping("/update")
    public Result update(@RequestBody Material material){
        return materialService.updateMaterial(material);
    }

    @GetMapping("/search/{page}")
    public void search(@PathVariable("page") Integer page){
        materialService.materialPage(page);
    }
}
