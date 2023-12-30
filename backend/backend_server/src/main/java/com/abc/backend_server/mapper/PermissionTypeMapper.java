package com.abc.backend_server.mapper;

import com.abc.backend_server.pojo.PermissionType;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PermissionTypeMapper {

    Boolean DelPermissionType(@Param("typeId") Long typeId);
    Boolean AddPermissionType(@Param("resourceId") Long resourceId,@Param("type")String type);

    Boolean DelUserPermission(@Param("typeId")Long typeId);
    List<PermissionType> listByResourceId(@Param("resourceId")Long resourceId);


    void updateType(@Param("id") Long id, @Param("name") String name);

}