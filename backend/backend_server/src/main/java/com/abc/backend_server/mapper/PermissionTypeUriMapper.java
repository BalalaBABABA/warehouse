package com.abc.backend_server.mapper;

import com.abc.backend_server.pojo.PermissionTypeUri;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PermissionTypeUriMapper {
    List<PermissionTypeUri> listByTypeId(@Param("typeId") Long typeId);

    Boolean delById(@Param("id")Long id);

    Boolean delByTypeId(@Param("typeId")Long typeId);

    Boolean addUri(@Param("typeId") Long typeId,@Param("uri") String uri);

    void updateUri(@Param("id") Integer id,@Param("name") String name);
}