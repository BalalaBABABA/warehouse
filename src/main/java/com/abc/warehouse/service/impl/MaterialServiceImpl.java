package com.abc.warehouse.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.abc.warehouse.pojo.Material;
import com.abc.warehouse.service.MaterialService;
import com.abc.warehouse.mapper.MaterialMapper;
import org.springframework.stereotype.Service;

/**
* @author 吧啦
* @description 针对表【material_208201302(物料表)】的数据库操作Service实现
* @createDate 2023-09-19 15:31:44
*/
@Service
public class MaterialServiceImpl extends ServiceImpl<MaterialMapper, Material>
    implements MaterialService{

}




