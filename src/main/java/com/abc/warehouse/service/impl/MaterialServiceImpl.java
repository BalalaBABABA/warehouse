package com.abc.warehouse.service.impl;

import com.abc.warehouse.dto.EncryotResult;
import com.abc.warehouse.dto.Result;
import com.abc.warehouse.dto.constants.PageConstants;
import com.abc.warehouse.pojo.Store;
import com.abc.warehouse.utils.GenerateID;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.abc.warehouse.pojo.Material;
import com.abc.warehouse.service.MaterialService;
import com.abc.warehouse.mapper.MaterialMapper;
import org.jfree.chart.needle.LongNeedle;
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
        Long id = generateID.getId("3", "Material");
        material.setId(id);
        save(material);
        return EncryotResult.ok();
    }

    @Override
    public Result saveMaterialEqual(Material material) {
        Long id = generateID.getId("3", "Material");
        material.setId(id);
        save(material);
        updateEqual(material);
        return EncryotResult.ok();
    }

    @Override
    public Result delMaterial(Long id) {
        removeById(id);
        return Result.ok();
    }

    @Override
    public Result updateMaterial(Material material) {
        updateById(material);
        return EncryotResult.ok();
    }

    @Override
    public Result updateEqualType(Material material) {
        updateById(material);
        updateEqual(material);
        return EncryotResult.ok();
    }

    private void updateEqual(Material material){
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("name", material.getName());
        List<Material> materials = list(wrapper);
        for(Material m : materials){
            if(!m.getHouseName().equals(material.getHouseName())){
                m.setType(material.getType());
                updateById(m);
            }
        }

    }

    @Override
    public Result searchAll() {
        QueryWrapper<Material> wrapper = new QueryWrapper<>();
        List<Material> list = list(wrapper);
        return EncryotResult.ok(list);
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
    public List<String> getMaterialNameByType(String type) {
        QueryWrapper<Material> wrapper = new QueryWrapper<>();
        wrapper.eq("type", type);
        List<Material> materials = baseMapper.selectList(wrapper);
        List<String> names = materials.stream().map(Material::getName).distinct().collect(Collectors.toList());
        return names;
    }

    @Override
    public List<String> getHouseByMaterialName(String name) {
        QueryWrapper<Material> wrapper = new QueryWrapper<>();
        wrapper.eq("name", name);
        List<Material> materials = baseMapper.selectList(wrapper);
        List<String> houseName = materials.stream().map(Material::getHouseName).distinct().collect(Collectors.toList());
        return houseName;
    }

    @Override
    public Material getMaterialByNameAndHouse(String name, String house) {
        QueryWrapper<Material> wrapper = new QueryWrapper<>();
        wrapper.eq("name", name)
                .eq("house_name", house);
        Material material = baseMapper.selectOne(wrapper);
        return material;
    }

    @Override
    public Material houseByYearAndName(String year, String name) {
        QueryWrapper wrapper = new QueryWrapper();
//        List<String> stores = wrapper.between("store_time", year + "-01-01 00:00:00", year + "-12-31 00:00:00")
//                .select("house_name");
        return null;
    }
}




