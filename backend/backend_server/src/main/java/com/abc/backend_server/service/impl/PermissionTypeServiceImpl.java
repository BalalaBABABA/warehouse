package com.abc.backend_server.service.impl;

import com.abc.backend_server.dto.Result;
import com.abc.backend_server.mapper.PermissionTypeMapper;
import com.abc.backend_server.mapper.PermissionTypeUriMapper;
import com.abc.backend_server.mapper.ResourceMapper;
import com.abc.backend_server.pojo.PermissionType;
import com.abc.backend_server.pojo.PermissionTypeUri;
import com.abc.backend_server.pojo.Resource;
import com.abc.backend_server.service.PermissionTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service

public class PermissionTypeServiceImpl implements PermissionTypeService {

    @Autowired
    private PermissionTypeMapper typeMapper;
    @Autowired
    private PermissionTypeUriMapper uriMapper;

    @Autowired
    private ResourceMapper resourceMapper;


    @Override
    @Transactional
    public Result listAll() {
        List<Resource> resources = resourceMapper.listAll();
        //遍历resource
        for (Resource resource : resources) {
            //获取resource的id
            Long resourceId = resource.getId();
            //根据resourceId遍历permissiontype
            List<PermissionType> permissionTypes = typeMapper.listByResourceId(resourceId);
            for (PermissionType permissionType : permissionTypes) {
                //获得permissiontype的id
                Long typeId = permissionType.getId();
                //根据permissiontypeId获取uri列表
                List<PermissionTypeUri> permissionTypeUris = uriMapper.listByTypeId(typeId);
                permissionType.setUri(permissionTypeUris);
            }
            resource.setPermission(permissionTypes);
        }
        return Result.ok(resources);
    }

    @Override
    public Result AddPermissionType(Long resourceId,String name) {
        Boolean aBoolean = typeMapper.AddPermissionType(resourceId, name);
        if(aBoolean){
            return Result.ok();
        }
        else return Result.fail("添加失败");
    }

    @Override
    @Transactional
    public Result delPermissionType(Long typeId) {
        typeMapper.DelPermissionType(typeId);
        uriMapper.delByTypeId(typeId);
        typeMapper.DelUserPermission(typeId);
        return Result.ok();
    }

    @Override
    public Result AddResource(Resource resource) {
        String icon = resource.getIcon();
        String name = resource.getName();
        String uri = resource.getUri();
        String page = resource.getPage();
        Boolean aBoolean = resourceMapper.addResource(name, uri, icon, page);
        if(aBoolean){
            return Result.ok();
        }
        else return Result.fail("添加失败");
    }

    @Override
    @Transactional
    public Result delResource(Long resourceId) {
        List<PermissionType> permissionTypes = typeMapper.listByResourceId(resourceId);
        resourceMapper.delResource(resourceId);

        for (PermissionType permissionType : permissionTypes) {
            Long typeId = permissionType.getId();
            typeMapper.DelPermissionType(typeId);
            uriMapper.delByTypeId(typeId);
            typeMapper.DelUserPermission(typeId);
        }
        return Result.ok();
    }

    @Override
    public Result delUri(Long uriId) {
        Boolean aBoolean = uriMapper.delById(uriId);
        if(aBoolean){
            return Result.ok();
        }
        else return Result.fail("删除失败");
    }

    @Override
    public Result AddUri(Long typeId, String uri) {
        Boolean aBoolean = uriMapper.addUri(typeId, uri);
        if(aBoolean){
            return Result.ok();
        }
        else return Result.fail("添加失败");
    }

    @Override
    public Result updateResource(Resource resource) {
        Long id = resource.getId();
        String name = resource.getName();
        String icon = resource.getIcon();
        String page = resource.getPage();
        String uri = resource.getUri();
        resourceMapper.updateResource(id,name,uri,icon,page);
        return Result.ok();
    }

    @Override
    public Result updateUri(PermissionTypeUri permissionTypeUri) {
        Integer id = permissionTypeUri.getId();
        String name = permissionTypeUri.getName();
        uriMapper.updateUri(id,name);
        return Result.ok();
    }

    @Override
    public Result updateType(PermissionType permissionType) {
        Long id = permissionType.getId();
        String name = permissionType.getName();
        typeMapper.updateType(id,name);
        return Result.ok();
    }


}
