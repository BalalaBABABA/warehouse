package com.abc.warehouse.service;

import com.abc.warehouse.dto.Result;
import com.abc.warehouse.pojo.MaterialType;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface MaterialTypeService extends IService<MaterialType> {
    public List<String> typeName();
}