package com.abc.warehouse.service.impl;

import com.abc.warehouse.dto.Result;
import com.abc.warehouse.dto.constants.PageConstants;
import com.abc.warehouse.utils.GenerateID;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.abc.warehouse.pojo.Material;
import com.abc.warehouse.service.MaterialService;
import com.abc.warehouse.mapper.MaterialMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MaterialServiceImpl extends ServiceImpl<MaterialMapper, Material>
    implements MaterialService{

    @Resource
    private GenerateID generateID;

    @Override
    public Result saveMaterial(Material material) {
        long id = generateID.getId("3", "Material");
        material.setId(id);
        save(material);
        return Result.ok();
    }

    @Override
    public Result delMaterial(Long id) {
        removeById(id);
        return Result.ok();
    }

    @Override
    public Result updateMaterial(Material material) {
        updateById(material);
        return Result.ok();
    }

    @Override
    public Result materialPage(Integer curPage) {
        LambdaQueryWrapper<Material> wrapper = new LambdaQueryWrapper<>();  //查询条件构造器
        IPage<Material> pageQuery = new Page<>(curPage, PageConstants.MATERIAL_SEARCH_PAGE_SIZE);
        IPage<Material> page = baseMapper.selectPage(pageQuery, wrapper);
        return Result.ok(page.getRecords(), page.getPages());
    }

    @Override
    public Result materialId(Integer curPage, Long id){
        QueryWrapper<Material> wrapper = new QueryWrapper<>();
        wrapper.like("id", id);
        IPage<Material> pageQuery = new Page<>(curPage, PageConstants.MATERIAL_SEARCH_PAGE_SIZE);
        IPage<Material> page = baseMapper.selectPage(pageQuery, wrapper);
        return Result.ok(page.getRecords(), page.getPages());
    }

    @Override
    public Result materialName(Integer curPage, String name){
        QueryWrapper<Material> wrapper = new QueryWrapper<>();
        wrapper.like("name", name);
        IPage<Material> pageQuery = new Page<>(curPage, PageConstants.MATERIAL_SEARCH_PAGE_SIZE);
        IPage<Material> page = baseMapper.selectPage(pageQuery, wrapper);
        return Result.ok(page.getRecords(), page.getPages());
    }

    @Override
    public Result materialHouseName(Integer curPage, String name){
        QueryWrapper<Material> wrapper = new QueryWrapper<>();
        wrapper.like("house_name", name);
        IPage<Material> pageQuery = new Page<>(curPage, PageConstants.MATERIAL_SEARCH_PAGE_SIZE);
        IPage<Material> page = baseMapper.selectPage(pageQuery, wrapper);
        return Result.ok(page.getRecords(), page.getPages());
    }

    @Override
    public Result materialType(Integer curPage, String type) {
        QueryWrapper<Material> wrapper = new QueryWrapper<>();
        wrapper.like("type", "%" + type + "%");
        IPage<Material> pageQuery = new Page<>(curPage, PageConstants.MATERIAL_SEARCH_PAGE_SIZE);
        IPage<Material> page = baseMapper.selectPage(pageQuery, wrapper);
        return Result.ok(page.getRecords(), page.getPages());
    }

    @Override
    public Result materialComments(Integer curPage, String comments) {
        QueryWrapper<Material> wrapper = new QueryWrapper<>();
        if(comments.equals(".nothing.")){
            wrapper.eq("comments", "");
        }else{
            wrapper.like("comments", "%" + comments + "%");
        }
        IPage<Material> pageQuery = new Page<>(curPage, PageConstants.MATERIAL_SEARCH_PAGE_SIZE);
        IPage<Material> page = baseMapper.selectPage(pageQuery, wrapper);
        return Result.ok(page.getRecords(), page.getPages());
    }

    @Override
    public List<String> getMaterialByType(String type) {
        QueryWrapper<Material> wrapper = new QueryWrapper<>();
        wrapper.eq("type", type);
        List<Material>  materials = baseMapper.selectList(wrapper);
        return materials.stream().map(Material::getName).collect(Collectors.toList());
    }

}




