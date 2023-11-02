package com.abc.warehouse.service;

import com.abc.warehouse.dto.Result;
import com.abc.warehouse.pojo.PermissionType;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
* @author 吧啦
* @description 针对表【permission_type_208201302】的数据库操作Service
* @createDate 2023-10-24 01:02:10
*/
public interface PermissionTypeService extends IService<PermissionType> {

     List<PermissionType> getTypesByResourceId(Long resourceId);

     Map<Long,String> getTypesMapByResourceId(Long resourceId);

     Map<Long,PermissionType> getAllTypesMap();
     Map<String, Long> getAllTypesStringMap();

     Result getPermissionTypesMap();

     Result delPermissionType(Long resourceId, String type);
}
