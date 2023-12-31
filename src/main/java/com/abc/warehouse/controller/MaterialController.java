package com.abc.warehouse.controller;


import cn.hutool.core.date.DateTime;
import cn.hutool.json.JSONObject;
import com.abc.warehouse.annotation.Decrypt;
import com.abc.warehouse.annotation.Encrypt;
import com.abc.warehouse.annotation.JsonParam;
import com.abc.warehouse.dto.EncryotResult;
import com.abc.warehouse.dto.Result;
import com.abc.warehouse.mapper.DeliverMapper;
import com.abc.warehouse.mapper.MaterialMapper;
import com.abc.warehouse.mapper.StoreMapper;
import com.abc.warehouse.pojo.Deliver;
import com.abc.warehouse.pojo.Material;
import com.abc.warehouse.pojo.Store;
import com.abc.warehouse.service.HouseService;
import com.abc.warehouse.service.MaterialService;
import com.abc.warehouse.service.MaterialTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    @Autowired
    private StoreMapper storeMapper;
    @Autowired
    private DeliverMapper deliverMapper;

    @GetMapping
    public Result enter(){
        return Result.ok();
    }

    @PostMapping("/save")
    @Encrypt
    @Decrypt
    public Result save(@JsonParam("id") Long id, @JsonParam("name") String name,
                       @JsonParam("houseName") String houseName, @JsonParam("type") String type,
                       @JsonParam("unit") String unit, @JsonParam("stock") Integer stock,
                       @JsonParam("comments") String comments){
        Material material = new Material(id, name, houseName, type, unit, stock, comments);
        material.setCreateTime(null);
        return materialService.saveMaterial(material);
    }

    @PostMapping("/saveEqual")
    @Encrypt
    @Decrypt
    public Result saveEqual(@JsonParam("id") Long id, @JsonParam("name") String name,
                            @JsonParam("houseName") String houseName, @JsonParam("type") String type,
                            @JsonParam("unit") String unit, @JsonParam("stock") Integer stock,
                            @JsonParam("comments") String comments){
        Material material = new Material(id, name, houseName, type, unit, stock, comments);
        return materialService.saveMaterialEqual(material);
    }

    @GetMapping("/del/{id}")
    public Result del(@PathVariable("id") Long id){
        return materialService.delMaterial(id);
    }

    @PostMapping("/update")
    @Encrypt
    @Decrypt
    public Result update(@JsonParam("id") Long id, @JsonParam("name") String name,
                         @JsonParam("houseName") String houseName, @JsonParam("type") String type,
                         @JsonParam("unit") String unit, @JsonParam("stock") Integer stock,
                         @JsonParam("comments") String comments){
        Material material = new Material(id, name, houseName, type, unit, stock, comments);
        material.setCreateTime(null);
        return materialService.updateMaterial(material);
    }

    @PostMapping("/updateEqualType")
    @Encrypt
    @Decrypt
    public Result updateEqualType(@JsonParam("id") Long id, @JsonParam("name") String name,
                                  @JsonParam("houseName") String houseName, @JsonParam("type") String type,
                                  @JsonParam("unit") String unit, @JsonParam("stock") Integer stock,
                                  @JsonParam("comments") String comments){
        Material material = new Material(id, name, houseName, type, unit, stock, comments);
        return materialService.updateEqualType(material);
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
        Material material = materialService.getMaterialByNameAndHouse(name, house);
        if(material == null){
            return new Result(false, null, null, 0L);
        }
        return EncryotResult.ok(material);
    }

    @GetMapping("/ifStoreOrDeliver/{materialId}")
    @Encrypt
    public Result ifStoreOrDeliver(@PathVariable("materialId") Long materialId){
        if(materialMapper.ifStore(materialId) > 0 || materialMapper.ifDeliver(materialId) > 0){
            return EncryotResult.ok(true);
        }
        return EncryotResult.ok(false);
    }

    @GetMapping("/houseByYearAndName")
    @Encrypt
    @Decrypt
    public Result houseByYearAndName(@JsonParam("year") String year, @JsonParam("name") String name){
        return null;
    }

    @PostMapping("/houseByYearAndMaterialName")
    @Encrypt
    @Decrypt
    public Result houseByYearAndMaterialName(@JsonParam("Year") String year,@JsonParam("MaterialName") String materialName){
        String start = "1970";
        String end = String.valueOf(DateTime.now().year());
        if(year != null && !year.equals("")){
            start = year;
            end = year;
        }
        Timestamp startYear = Timestamp.valueOf(start + "-01-01 00:00:00");
        Timestamp endYear = Timestamp.valueOf(end + "-12-31 23:59:59");
        List<Store> stores = storeMapper.selectStoreByYearAndMaterialName(startYear, endYear, materialName);
        List<Deliver> delivers = deliverMapper.selectDeliverByYearAndMaterialName(startYear, endYear, materialName);
        Set<String> houses = new HashSet<>();
        if(stores.size() > 0){
            for(Store store : stores){
                houses.add(store.getHouseName());
            }
        }
        if(delivers.size() > 0){
            for(Deliver deliver : delivers){
                houses.add(deliver.getHouseName());
            }
        }
        if(houses.size() > 0){
            return new Result(true, "0", houses, Long.valueOf(houses.size()));
        }
        return new Result(false, null, null, 0L);
    }
}