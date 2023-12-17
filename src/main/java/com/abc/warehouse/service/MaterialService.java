package com.abc.warehouse.service;

import com.abc.warehouse.dto.Result;
import com.abc.warehouse.pojo.Material;
import com.baomidou.mybatisplus.extension.service.IService;

public interface MaterialService extends IService<Material> {
    Result saveMaterial(Material material);
    Result delMaterial(Long id);
    Result updateMaterial(Material material);
    Result materialPage(Integer curPage);
    Result materialId(Integer curPage, Long id);
    Result materialName(Integer curPage, String name);
    Result materialHouseName(Integer curPage, String name);
    Result materialType(Integer curPage, String type);
    Result materialComments(Integer curPage, String comments);
}
