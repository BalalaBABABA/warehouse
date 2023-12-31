package com.abc.backend_server.service;

import com.abc.backend_server.dto.Result;
import com.abc.backend_server.pojo.PermissionType;
import com.abc.backend_server.pojo.PermissionTypeUri;
import com.abc.backend_server.pojo.Resource;

public interface PermissionTypeService {
    Result listAll();

    Result AddPermissionType(Long resourceId,String name);

    Result delPermissionType(Long typeId);

    Result AddResource(Resource resource);

    Result delResource(Long resourceId);

    Result delUri(Long uriId);

    Result AddUri(Long typeId,String uri);

    Result updateResource(Resource resource);

    Result updateUri(PermissionTypeUri permissionTypeUri);

    Result updateType(PermissionType permissionType);
}
