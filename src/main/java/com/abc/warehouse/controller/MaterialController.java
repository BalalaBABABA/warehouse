package com.abc.warehouse.controller;


import cn.hutool.json.JSONObject;
import com.abc.warehouse.annotation.Decrypt;
import com.abc.warehouse.annotation.Encrypt;
import com.abc.warehouse.annotation.JsonParam;
import com.abc.warehouse.dto.EncryotResult;
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

    @GetMapping("/searchAll")
    @Encrypt
    public Result searchAll(){
        return materialService.searchAll();
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

    @PostMapping("/getMaterialNameByType")
    @Encrypt
    public Result getMaterialNameByType(@RequestBody String typeName){
        JSONObject json = new JSONObject(typeName);
        String name = json.getStr("typeName");
        return EncryotResult.ok(materialService.getMaterialNameByType(name));
    }

    @PostMapping("/getHouseByMaterialName")
    @Encrypt
    @Decrypt
    public Result getHouseByMaterialName(@JsonParam("name") String name){
        return EncryotResult.ok(materialService.getHouseByMaterialName(name));
    }

    @PostMapping("/getMaterialByNameAndHouse")
    @Encrypt
    @Decrypt
    public Result getMaterialByNameAndHouse(@JsonParam("name") String name, @JsonParam("house") String house){
        return EncryotResult.ok(materialService.getMaterialByNameAndHouse(name, house));
    }
}