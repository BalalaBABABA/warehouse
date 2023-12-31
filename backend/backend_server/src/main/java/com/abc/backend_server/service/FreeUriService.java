package com.abc.backend_server.service;

import com.abc.backend_server.dto.Result;
import com.abc.backend_server.pojo.FreeUri;
import com.abc.backend_server.pojo.PermissionTypeUri;

import java.util.List;

public interface FreeUriService {
    Result getFreeUriList();

    Result delByPermissionId(Long permissionId);

    Result delByUriId(Long uriId);

    Result delByResourceId(Long resourceId);

    Result delIdList(List<FreeUri> list);

    Result AddFreeUri(String uri);

    Result updateFreeUri(FreeUri freeUri);

}
