package com.abc.warehouse.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.abc.warehouse.dto.Result;
import com.abc.warehouse.dto.UserPermission;
import com.abc.warehouse.pojo.Resource;
import com.abc.warehouse.pojo.User;
import com.abc.warehouse.service.ResourceService;
import com.abc.warehouse.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.abc.warehouse.pojo.Permission;
import com.abc.warehouse.service.PermissionService;
import com.abc.warehouse.mapper.PermissionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.abc.warehouse.utils.SystemConstants.DEFAULT_PAGE_SIZE;

/**
 * @author 吧啦
 * @description 针对表【permission_208201302(权限表)】的数据库操作Service实现
 * @createDate 2023-09-23 16:58:22
 */
@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission>
        implements PermissionService{

    @Autowired
    private ResourceService resourceService;

    @Autowired
    private UserService userService;
    @Override
    public Result getPermissionTypes() {
        //1.查询所有资源id
        List<Resource> list = resourceService.list();
        Map<Long,List> permissions =new HashMap<>();

        //2.根据资源id查询所有权限类型
        list.forEach(resource -> {
            //2.1 获取id
            Long resourceId = resource.getId();
            QueryWrapper<Permission> queryWrapper=new QueryWrapper<>();
            queryWrapper.eq("resource_id",resourceId).groupBy("type");
            //2.2 查询所有id对应的权限
            List<Permission> permissionsByresourceId = list(queryWrapper);
            //2.3 存储权限的类型字段
            List<String> types=new ArrayList();
            permissionsByresourceId.forEach(permission -> types.add(permission.getType()));
            //2.4 存入Map
            permissions.put(resourceId,types);
        });
        return Result.ok(permissions);
    }

    @Override
    public Result getAllUsersPermissionsByResourceId(Integer pageCount, Long resourceId) {
        //设置分页参数
        Page<User> page =new Page<>(pageCount, DEFAULT_PAGE_SIZE);
        List<User> userList = userService.page(page, null).getRecords();

        //获取所有用户
        List<UserPermission> userPermissions = userList
                .stream().map(user -> BeanUtil.copyProperties(user, UserPermission.class))
                .collect(Collectors.toList());
        userPermissions.forEach(user -> {
            //根据userId和resourceid查询权限表
            QueryWrapper<Permission> queryWrapper=new QueryWrapper<>();
            queryWrapper.eq("resource_id",resourceId)
                    .eq("user_id",user.getId());
            List<Permission> list = list(queryWrapper);
            List<String> permissons=new ArrayList<>();
            list.forEach(i->permissons.add(i.getType()));
            user.setPermissionList(permissons);
        });
        return Result.ok(userPermissions);
    }

    @Override
    public Result updateUserPermission(Long userId, Long resourceId, String type, Boolean flag) {
        if(flag)
        {
            boolean save = save(new Permission(null, userId, resourceId, type));
            return save?Result.ok():Result.fail("增加权限失败");
        }
        else{
            UpdateWrapper<Permission> updateWrapper=new UpdateWrapper<>();
            updateWrapper.eq("user_id",userId)
                    .eq("resource_id",resourceId)
                    .eq("type",type);
            boolean remove = remove(updateWrapper);
            return remove?Result.ok():Result.fail("取消权限失败");
        }
    }//哈哈哈哈哈音乐会很过分发货后

    @Override
    public Result deleteUserResource(Long userId, Long resourceId) {
        UpdateWrapper<Permission> updateWrapper=new UpdateWrapper<>();
        updateWrapper.eq("user_id",userId)
                .eq("resource_id",resourceId);
        boolean remove = remove(updateWrapper);
        return remove?Result.ok():Result.fail("取消权限失败");
    }

    @Override
    public Result getPermissionTypesByResourceId(Long resourceId) {
        QueryWrapper<Permission> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("resource_id",resourceId).groupBy("type");

        List<Permission> list = list(queryWrapper);
        List<String> permissons=new ArrayList<>();
        list.forEach(i->permissons.add(i.getType()));
        return Result.ok(permissons);
    }



}