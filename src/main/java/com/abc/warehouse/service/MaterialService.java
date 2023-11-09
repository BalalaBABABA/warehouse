package com.abc.warehouse.service;

import com.abc.warehouse.dto.Result;
import com.abc.warehouse.pojo.Material;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 吧啦
* @description 针对表【material_208201302(物料表)】的数据库操作Service
* @createDate 2023-09-23 16:58:22
*/
public interface MaterialService extends IService<Material> {
    Result saveMaterial(Material material);
    Result delMaterial(Long id);
    Result updateMaterial(Material material);
    Result materialPage(Integer curPage);
    Result materialId(Integer curPage, Long id);
    Result materialName(Integer curPage, String name);
    Result materialHouseId(Integer curPage, Long id);
    Result materialType(Integer curPage, String type);
    Result materialComments(Integer curPage, String comments);
}
