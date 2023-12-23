package com.abc.warehouse.controller;


import com.abc.warehouse.annotation.Decrypt;
import com.abc.warehouse.annotation.Encrypt;
import com.abc.warehouse.annotation.JsonParam;
import com.abc.warehouse.dto.Result;
import com.abc.warehouse.mapper.MaterialMapper;
import com.abc.warehouse.pojo.Material;
import com.abc.warehouse.service.HouseService;
import com.abc.warehouse.service.MaterialService;
import com.abc.warehouse.service.MaterialTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/material")
public class MaterialController {
    @Autowired
    private MaterialService materialService;
    @Autowired
    private HouseService houseService;
    @Autowired
    private MaterialTypeService materialTypeService;
    @Autowired
    private MaterialMapper materialMapper;

    @GetMapping
    public Result enter(){
        return Result.ok();
    }

    @PostMapping("/save")
    public Result save(@RequestBody Material material){
        material.setCreateTime(null);
        return materialService.saveMaterial(material);
    }

    @GetMapping("/del/{id}")
    public Result del(@PathVariable("id") Long id){
        return materialService.delMaterial(id);
    }

    @PostMapping("/update")
    public Result update(@RequestBody Material material){
        material.setCreateTime(null);
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

    @GetMapping("/searchByHouseName/{page}/{house_name}")
    public Result searchByHouseName(@PathVariable("page") Integer page, @PathVariable("house_name") String house_name){
        return materialService.materialHouseName(page, house_name);
    }

    @GetMapping("/searchByType/{page}/{type}")
    public Result searchByType(@PathVariable("page") Integer page, @PathVariable("type") String type){
        return materialService.materialType(page, type);
    }

    @GetMapping("/searchByComments/{page}/{comments}")
    public Result searchByComments(@PathVariable("page") Integer page, @PathVariable("comments") String comments){
        return materialService.materialComments(page, comments);
    }

    @GetMapping("/searchHouseName")
    public Result searchHouseName(){
        return Result.ok(houseService.searchHouseName());
    }

    @GetMapping("/typeName")
    public Result typeName(){
        return Result.ok(materialTypeService.typeName());
    }

    @PostMapping("/typeMaterial")
    @Encrypt
    public Result getMaterialByType(@JsonParam("typeName") String typeName){
        System.out.println(typeName);
        return Result.ok(materialService.getMaterialByType(typeName));
    }

    @PostMapping("/searchByTypeAndName")
    @Encrypt
    @Decrypt
    public Result getMaterialByTypeAndName(@JsonParam("type") String type, @JsonParam("name") String name){
        List<Material> materiaList = materialMapper.selectByTypeAndName(type, name);
        if(materiaList != null){
            return new Result(true, "0", materiaList, Long.valueOf(materiaList.size()));
        }else{
            return new Result(false, "Failed", null, 0L);
        }
    }

//通过类名查询相应所有物料，选中物料查询所在仓库和库存
}
