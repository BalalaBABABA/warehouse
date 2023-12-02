package com.abc.warehouse.service.impl;

import com.abc.warehouse.mapper.MaterialTypeMapper;
import com.abc.warehouse.pojo.MaterialType;
import com.abc.warehouse.service.MaterialTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MaterialTypeServiceImpl extends ServiceImpl<MaterialTypeMapper, MaterialType>
        implements MaterialTypeService {
    @Override
    public List<String> typeName() {
        List<String> typeName = list().stream()
                .map(materialType -> materialType.getName())
                .collect(Collectors.toList());
        return typeName;
    }
}
