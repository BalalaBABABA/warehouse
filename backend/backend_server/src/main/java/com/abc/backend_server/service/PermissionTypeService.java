package com.abc.backend_server.service;

import com.abc.backend_server.dto.Result;
import com.abc.backend_server.pojo.Resource;

public interface PermissionTypeService {
    Result listAll();

    Result AddPermissionType(Long resourceId,String name);

    Result delPermissionType(Long typeId);

    Result AddResource(Resource resource);

    Result delResource(Long resourceId);

    Result delUri(Long uriId);

    Result AddUri(Long typeId,String uri);
}
