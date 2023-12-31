package com.abc.backend_server.mapper;

import com.abc.backend_server.pojo.FreeUri;

import java.util.List;

import com.abc.backend_server.pojo.PermissionTypeUri;
import org.apache.ibatis.annotations.Param;

public interface FreeUriMapper {
    List<FreeUri> getFreeUriList();

    Integer delByPermissionId( @Param("permissionId")Long permissionId);

    Integer delByUriId(@Param("uriId")Long uriId);

    Integer delByResourceId(Long resourceId);

    Boolean delFreeUriList(@Param("list") List<Integer> list);

    Boolean addFreeUri(@Param("uri") String uri);

    void updateFreeUri(@Param("id") Integer id, @Param("uri") String uri);
}