package com.abc.warehouse.service;

import com.abc.warehouse.dto.Result;
import com.abc.warehouse.pojo.Material;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface MaterialService extends IService<Material> {
    Result saveMaterial(Material material);
    Result delMaterial(Long id);
    Result updateMaterial(Material material);
    Result searchAll();
    Result materialPage(Integer curPage);
    Result materialId(Integer curPage, Long id);
    Result materialName(Integer curPage, String name);
    Result materialHouseName(Integer curPage, String name);
    Result materialType(Integer curPage, String type);
    Result materialComments(Integer curPage, String comments);
    List<String> getMaterialNameByType(String type);
    List<String> getHouseByMaterialName(String name);
    Material getMaterialByNameAndHouse(String name, String house);
}
