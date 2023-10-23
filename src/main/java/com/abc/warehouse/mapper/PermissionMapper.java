package com.abc.warehouse.mapper;

import com.abc.warehouse.pojo.Permission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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

    IPage<Permission> searchPermissionByRole(
            @Param("page") Page<Permission> page,
            @Param("resourceId") Long resourceId ,
            @Param("role") String role);

    IPage<Permission> searchPermissionByUser(@Param("page") Page<Permission> page,
                                            @Param("resourceId") Long resourceId,
                                            @Param("userId") Long userId,
                                            @Param("empName") String empName);
}




