package com.abc.warehouse.service.impl;

import com.abc.warehouse.dto.Result;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.Mapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.abc.warehouse.pojo.Material;
import com.abc.warehouse.service.MaterialService;
import com.abc.warehouse.mapper.MaterialMapper;
import org.springframework.stereotype.Service;

/**
* @author 吧啦
* @description 针对表【material_208201302(物料表)】的数据库操作Service实现
* @createDate 2023-09-23 16:58:22
*/
@Service
public class MaterialServiceImpl extends ServiceImpl<MaterialMapper, Material>
    implements MaterialService{

    @Override
    public Result saveMaterial(Material material) {
        save(material);
        return Result.ok();
    }

    @Override
    public Result delMaterial(Long id) {
        removeById(id);
        return Result.ok("ok");
    }

    @Override
    public Result updateMaterial(Material material) {
        updateById(material);
        return Result.ok();
    }

    @Override
    public void materialPage(Integer curPage) {
        LambdaQueryWrapper<Material> wrapper = new LambdaQueryWrapper<>();  //查询条件构造器
        IPage<Material> pageQuery = new Page<>(curPage, 5);
        IPage<Material> page = baseMapper.selectPage(pageQuery, wrapper);

        System.out.println("当前页：" + page.getCurrent());
        System.out.println("每页显示条数：" + page.getSize());
        System.out.println("总记录数：" + page.getTotal());
        System.out.println("总页数：" + page.getPages());
    }


}




