package com.abc.warehouse.service.impl;

import com.abc.warehouse.dto.Result;
import com.abc.warehouse.dto.UserDTO;
import com.abc.warehouse.utils.JwtUtils;
import com.abc.warehouse.utils.UserHolder;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.abc.warehouse.pojo.Resource;
import com.abc.warehouse.service.ResourceService;
import com.abc.warehouse.mapper.ResourceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
* @author 吧啦
* @description 针对表【resource_208201302(资源表)】的数据库操作Service实现
* @createDate 2023-09-23 16:58:22
*/
@Service
public class ResourceServiceImpl extends ServiceImpl<ResourceMapper, Resource>
    implements ResourceService{

    @Autowired
    private ResourceMapper resourceMapper;

    @Override
    public Result getAllResources() {
        List<Resource> list = list();
        Map<String,String> map=new HashMap<>();
        list.forEach(resource -> {
            map.put(resource.getName(),resource.getId().toString());
        });
        return Result.ok(map);
    }

    @Override
    public Result getAllResourcesInfo() {
        List<Resource> list = list();
        LinkedHashMap<String,Object> map=new LinkedHashMap<>();
        list.forEach(resource -> {
            map.put(resource.getName(),resource);
        });
        return Result.ok(map);
    }

    @Override
    public Result getUserResourcesInfo(String token) {
        Long userId = JwtUtils.getUserIdFromToken(token);
        List<Resource> list ;
        if(userId == 9797){
            list = list();
        }
        else list = resourceMapper.getUserResources(userId);
        Map<String,Object> map=new HashMap<>();
        list.forEach(resource -> {
            map.put(resource.getName(),resource);
        });
        return Result.ok(map);
    }
}




