package com.abc.warehouse.controller;


import com.abc.warehouse.dto.Result;
import com.abc.warehouse.pojo.Material;
import com.abc.warehouse.service.MaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PageableDefault;
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
    public Result search(@PathVariable("page") Integer page){
        return materialService.materialPage(page);
    }

    @GetMapping("/searchById/{page}/{id}")
    public Result searchById(@PathVariable("page") Integer page, @PathVariable("id") Long id){
        return materialService.materialId(page, id);
    }

    @GetMapping("/searchByName/{page}/{name}")
    public Result searchByName(@PathVariable("page") Integer page, @PathVariable("name") String name){
        return materialService.materialName(page, name);
    }

    @GetMapping("/searchByHouseId/{page}/{house_id}")
    public Result searchByHouseId(@PathVariable("page") Integer page, @PathVariable("house_id") Long house_id){
        return materialService.materialHouseId(page, house_id);
    }

    @GetMapping("/searchByType/{page}/{type}")
    public Result searchByType(@PathVariable("page") Integer page, @PathVariable("type") String type){
        return materialService.materialType(page, type);
    }

    @GetMapping("/searchByComments/{page}/{comments}")
    public Result searchByComments(@PathVariable("page") Integer page, @PathVariable("comments") String comments){
        return materialService.materialComments(page, comments);
    }
}
