package com.abc.warehouse.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.abc.warehouse.dto.Result;
import com.abc.warehouse.dto.UserPermission;
import com.abc.warehouse.dto.constants.RedisConstants;
import com.abc.warehouse.dto.params.AddPermissionParams;
import com.abc.warehouse.dto.params.SearchPermissionParams;
import com.abc.warehouse.dto.params.UpdatePermissionParams;
import com.abc.warehouse.pojo.PermissionType;
import com.abc.warehouse.pojo.Resource;
import com.abc.warehouse.pojo.User;
import com.abc.warehouse.service.PermissionTypeService;
import com.abc.warehouse.service.ResourceService;
import com.abc.warehouse.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.abc.warehouse.pojo.Permission;
import com.abc.warehouse.service.PermissionService;
import com.abc.warehouse.mapper.PermissionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
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
    private StringRedisTemplate redisTemplate;
    @Autowired
    private ResourceService resourceService;

    @Autowired
    private PermissionMapper permissionMapper;
    @Autowired
    private PermissionTypeService permissionTypeService;
    @Autowired
    private UserService userService;
    @Override
    public Result getPermissionTypes() {
        //1.查询所有资源id
        List<Resource> list = resourceService.list();
        Map<Long,List> permissions =new HashMap<>();

        //2.根据资源id查询所有权限类型

        list.forEach(resource -> {
            Long resourceId = resource.getId();
            List<PermissionType> permissionTypes = permissionTypeService.getTypesByResourceId(resourceId);
            List<String> types = permissionTypes.stream().map(permissionType -> permissionType.getType()).collect(Collectors.toList());
            permissions.put(resourceId,types);
        });
//        list.forEach(resource -> {
//            //2.1 获取id
//            Long resourceId = resource.getId();
//            QueryWrapper<Permission> queryWrapper=new QueryWrapper<>();
//            queryWrapper.eq("resource_id",resourceId).groupBy("type");
//            //2.2 查询所有id对应的权限
//            List<Permission> permissionsByresourceId = list(queryWrapper);
//            //2.3 存储权限的类型字段
//            List<String> types=new ArrayList();
//            permissionsByresourceId.forEach(permission -> types.add(permission.getType()));
//            //2.4 存入Map
//            permissions.put(resourceId,types);
//        });
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
        List<PermissionType> permissionTypes = permissionTypeService.getTypesByResourceId(resourceId);
        List<Long> permissionTypeIdList = permissionTypes.stream().map(permissionType -> permissionType.getId()).collect(Collectors.toList());
        Map<Long, String> typesMap = permissionTypeService.getTypesMapByResourceId(resourceId);
        //根据userId和resourceid查询权限表
        userPermissions.forEach(user -> {
            //根据userId和resourceid查询权限表
            LambdaQueryWrapper<Permission> queryWrapper=new LambdaQueryWrapper<>();
            queryWrapper.eq(Permission::getUserId,user.getId());
            if(!permissionTypeIdList.isEmpty())
            {
                queryWrapper.in(Permission::getPermissionId,permissionTypeIdList);
            }
            List<Permission> list = list(queryWrapper);
            List<String> permissons=new ArrayList<>();
            list.forEach(i->
                    permissons.add(typesMap.get(i.getPermissionId()))
            );
            user.setPermissionList(permissons);
        });
        return Result.ok(userPermissions);
    }

    @Override
    @Transactional
    public Result updateUserPermission(UpdatePermissionParams params, Boolean flag) {
        /**
         * 1.删除用户权限缓存
         * 2.更新用户权限数据库
         */
        Long userId = params.getUserId();
        Long resourceId = params.getResourceId();
        String type =  params.getType();
        LambdaUpdateWrapper<PermissionType> qw=new LambdaUpdateWrapper<>();
        qw.eq(PermissionType::getResourceId,resourceId)
                .eq(PermissionType::getType,type);
        PermissionType permission = permissionTypeService.getOne(qw);
        Long permissionId = permission.getId();


        // 删除缓存
        redisTemplate.delete(RedisConstants.PERMISSIONS_USER_KEY+userId);

        // 更新数据库
        if(flag)
        {
            boolean save = save(new Permission(null, userId, permissionId));
            return save?Result.ok():Result.fail("增加权限失败");
        }
        else{
            LambdaUpdateWrapper<Permission> updateWrapper=new LambdaUpdateWrapper<>();
            updateWrapper.eq(Permission::getUserId,userId)
                    .eq(Permission::getPermissionId,permissionId);
            boolean remove = remove(updateWrapper);

            return remove?Result.ok():Result.fail("取消权限失败");
        }
    }

    @Override
    @Transactional
    public Result deleteUserResource(Long userId, Long resourceId) {
        /**
         * 1.删除用户权限缓存
         * 2.更新用户权限数据库
         */
        // 删除缓存
        redisTemplate.delete(RedisConstants.PERMISSIONS_USER_KEY+userId);
        List<PermissionType> types = permissionTypeService.getTypesByResourceId(resourceId);
        List<Long> permissionIdList = types.stream().map(type -> type.getId()).collect(Collectors.toList());
        // 更新数据库

        LambdaUpdateWrapper<Permission> updateWrapper1=new LambdaUpdateWrapper<>();
        updateWrapper1.eq(Permission::getUserId,userId)
                .in(Permission::getPermissionId,permissionIdList);
        boolean remove1 = remove(updateWrapper1);
        return remove1?Result.ok():Result.fail("取消权限失败");
    }

    @Override
    public Result getPermissionTypesByResourceId(Long resourceId) {

        List<PermissionType> list = permissionTypeService.getTypesByResourceId(resourceId);
        List<String> permissons=new ArrayList<>();
        list.forEach(i->permissons.add(i.getType()));
        return Result.ok(permissons);
    }

    @Override
    @Transactional
    public Result addOneUserPermission(AddPermissionParams permission) {
        List<Long> userIds = permission.getUserIds();
        List<String> userIdList = userIds.stream().map(userid -> RedisConstants.PERMISSIONS_USER_KEY + userid).collect(Collectors.toList());
        Long resourceId = permission.getResourceId();
        String type = permission.getType();
        String uri = permission.getUri();
        if(userIds.isEmpty() || resourceId == null || StringUtils.isBlank(type)||StringUtils.isBlank(type)){
            return Result.fail("参数不能为空！");
        }
        // 删除缓存
        redisTemplate.delete(userIdList);
        LambdaUpdateWrapper<PermissionType> queryWrapper=new LambdaUpdateWrapper<>();
        PermissionType newType=new PermissionType(null,resourceId,type,uri);
        permissionTypeService.save(newType);
        Long permissionId = newType.getId();
        // 更新数据库
        permissionMapper.saveUserPermissions(userIds,permissionId);
        return Result.ok();
    }


    @Override
    public Result searchPermissionByUser(SearchPermissionParams params) {
        Long resourceId = params.getResourceId();
        String empName = params.getEmpName();
        Long userId = params.getUserId();
        Map<Long, String> types = permissionTypeService.getTypesMapByResourceId(resourceId);
        Page<UserPermission> page = new Page<>(params.getCurrentPage(),params.getPageSize());
        IPage<UserPermission> permissions = permissionMapper.searchPermissionByUser(page,resourceId, userId, empName);
        List<UserPermission> records = permissions.getRecords();
        if(records.isEmpty())records = Collections.emptyList();
        for (UserPermission permission : records) {
            List<String> permissionList = new ArrayList<>();
            String[] strings = permission.getPermissionListStr().split(",");
            for (String string : strings) {
                long permissionId =Long.parseLong(string);
                permissionList.add(types.get(permissionId));
            }
            permission.setPermissionList(permissionList);
            permission.setPermissionListStr(null);

        }
        Map<String,Object> map = new HashMap<>();
        map.put("records",permissions.getRecords());
        map.put("totalPage",permissions.getPages());
        return Result.ok(map);

    }

    @Override
    public Result searchPermissionByRole(SearchPermissionParams params) {
        String role = params.getRole();
        Long resourceId = params.getResourceId();
        Map<Long, String> types = permissionTypeService.getTypesMapByResourceId(resourceId);

        Page<UserPermission> page = new Page<>(params.getCurrentPage(),params.getPageSize());
        IPage<UserPermission> permissions = permissionMapper.searchPermissionByRole(page,resourceId, role);
        List<UserPermission> records = permissions.getRecords();
        if(records.isEmpty())records = Collections.emptyList();
        for (UserPermission permission : records) {
            List<String> permissionList = new ArrayList<>();
            String[] strings = permission.getPermissionListStr().split(",");
            for (String string : strings) {
                long permissionId =Long.parseLong(string);
                permissionList.add(types.get(permissionId));
            }
            permission.setPermissionList(permissionList);
            permission.setPermissionListStr(null);

        }
        Map<String,Object> map = new HashMap<>();
        map.put("records",permissions.getRecords());
        map.put("totalPage",permissions.getPages());
        return Result.ok(map);
    }

    @Override
    public List<Permission> getByUserId(Long userId) {
        LambdaQueryWrapper<Permission> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Permission::getUserId,userId);
        List<Permission> list = list(queryWrapper);
        return list;
    }


}