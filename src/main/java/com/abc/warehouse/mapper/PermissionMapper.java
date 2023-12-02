package com.abc.warehouse.mapper;

import com.abc.warehouse.dto.UserPermission;
import com.abc.warehouse.pojo.Permission;
import com.abc.warehouse.pojo.PermissionType;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
* @author 吧啦
* @description 针对表【permission_208201302(权限表)】的数据库操作Mapper
* @createDate 2023-09-23 17:39:26
* @Entity com.abc.warehouse.pojo.Permission
*/
public interface PermissionMapper extends BaseMapper<Permission> {

     void saveUserPermissions(@Param("userIds") List<Long> userIds,
                              @Param("permissionId") Long permissionId
                              ) ;

    IPage<UserPermission> searchPermissionByRole(
            @Param("page") Page<UserPermission> page,
            @Param("resourceId") Long resourceId ,
            @Param("role") String role);

    IPage<UserPermission> searchPermissionByUser(@Param("page") Page<UserPermission> page,
                                            @Param("resourceId") Long resourceId,
                                            @Param("userId") Long userId,
                                            @Param("empName") String empName);


    List<PermissionType> getSelectMap(@Param("resourceId")long resourceId);

    List<String> getUserTypeUriList(@Param("userId")Long userId);
}




