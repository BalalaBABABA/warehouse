package com.abc.warehouse.mapper;

import com.abc.warehouse.annotation.JsonParam;
import com.abc.warehouse.pojo.Material;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author 吧啦
* @description 针对表【material_208201302(物料表)】的数据库操作Mapper
* @createDate 2023-09-23 16:58:22
* @Entity com.abc.warehouse.pojo.Material
*/
public interface MaterialMapper extends BaseMapper<Material> {
    Integer ifStore(@JsonParam("materialId") Long materialId);
    Integer ifDeliver(@JsonParam("materialId") Long materialId);
}




