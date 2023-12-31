package com.abc.warehouse.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.abc.warehouse.dto.EncryotResult;
import com.abc.warehouse.dto.Result;
import com.abc.warehouse.dto.TypeUri;
import com.abc.warehouse.dto.UserPermission;
import com.abc.warehouse.dto.constants.RedisConstants;
import com.abc.warehouse.dto.params.AddPermissionParams;
import com.abc.warehouse.dto.params.SearchPermissionParams;
import com.abc.warehouse.dto.params.UpdatePermissionParams;
import com.abc.warehouse.mapper.PermissionTypeMapper;
import com.abc.warehouse.pojo.PermissionType;
import com.abc.warehouse.pojo.Resource;
import com.abc.warehouse.pojo.User;
import com.abc.warehouse.service.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.abc.warehouse.pojo.Permission;
import com.abc.warehouse.mapper.PermissionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.abc.warehouse.dto.constants.PageConstants.PERMISSION_SEARCH_PAGE_SIZE;
import static com.abc.warehouse.dto.constants.RedisConstants.PERMISSIONS_USER_TTL;


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
    private FreeUriService freeUriService;
    @Autowired
    private PermissionTypeMapper permissionTypeMapper;
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
        Page<User> page =new Page<>(pageCount, PERMISSION_SEARCH_PAGE_SIZE);
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
        return EncryotResult.ok(userPermissions);
    }

    @Override
    @Transactional
    public Result updateUserPermission(UpdatePermissionParams params) {
        /**
         * 1.删除用户权限缓存
         * 2.更新用户权限数据库
         */
        Long userId = params.getUserId();
        Long resourceId = params.getResourceId();
        String type =  params.getType();
        Boolean flag = params.getFlag();

        if(StringUtils.isBlank(userId.toString()) || StringUtils.isBlank(resourceId.toString())||StringUtils.isBlank(type)){
            return EncryotResult.fail("参数不能为空");
        }
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
            return save?EncryotResult.ok():EncryotResult.fail("增加权限失败");
        }
        else{
            LambdaUpdateWrapper<Permission> updateWrapper=new LambdaUpdateWrapper<>();
            updateWrapper.eq(Permission::getUserId,userId)
                    .eq(Permission::getPermissionId,permissionId);
            boolean remove = remove(updateWrapper);

            return remove?EncryotResult.ok():EncryotResult.fail("取消权限失败");
        }
    }

    @Override
    @Transactional
    public Result updateUserResource(Long userId, Long resourceId, Boolean flag) {
        /**
         * 1.删除用户权限缓存
         * 2.更新用户权限数据库
         */
        if(StringUtils.isBlank(userId.toString()) || StringUtils.isBlank(resourceId.toString())){
            return EncryotResult.fail("参数不能为空！");
        }
        // 删除缓存
        redisTemplate.delete(RedisConstants.PERMISSIONS_USER_KEY+userId);
        List<PermissionType> types = permissionTypeService.getTypesByResourceId(resourceId);
        List<Long> permissionIdList = types.stream().map(type -> type.getId()).collect(Collectors.toList());
        // 更新数据库

        if(!flag){
            LambdaUpdateWrapper<Permission> updateWrapper1=new LambdaUpdateWrapper<>();
            updateWrapper1.eq(Permission::getUserId,userId)
                    .in(Permission::getPermissionId,permissionIdList);
            boolean remove1 = remove(updateWrapper1);
            return remove1?EncryotResult.ok():EncryotResult.fail("取消权限失败");
        }else{
            List<Permission> permissionList = new ArrayList<>();
            for (Long permissionId :
                    permissionIdList) {
                Permission p = new Permission(null,userId,permissionId);
                permissionList.add(p);
            }
            boolean b = saveBatch(permissionList);
            return b?EncryotResult.ok():EncryotResult.fail("增加权限失败");
        }
    }

    @Override
    public Result getPermissionTypesByResourceId(Long resourceId) {
        List<PermissionType> list = permissionTypeService.getTypesIsDisplayingByResourceId(resourceId);
        List<String> permissons=new ArrayList<>();
        list.forEach(i->permissons.add(i.getType()));
        return EncryotResult.ok(permissons);
    }

    @Override
    @Transactional
    public Result addOneUserPermission(AddPermissionParams permission) {
        List<Long> userIds = permission.getUserIds();
        List<String> userIdList;
        Long permissionId = permission.getPermissionId();
        String type = permission.getType();
        if(permissionId == null || StringUtils.isBlank(type)){
            return Result.fail("参数不能为空！");
        }
        // TODO redis缓存雪崩如何解决？大量key同时失效
        if(userIds.isEmpty()) userIdList = Collections.emptyList();
        else userIdList = userIds.stream().map(userid -> RedisConstants.PERMISSIONS_USER_KEY + userid).collect(Collectors.toList());

        // 删除缓存
        redisTemplate.delete(userIdList);
        // 更新数据库
        permissionTypeService.updateById(new PermissionType(permissionId,null,null,1));
        if(!userIds.isEmpty()){
            permissionMapper.saveUserPermissions(userIds,permissionId);
        }
        //删除free_uri中该权限对应的所有uri
        freeUriService.deleteFromFreeUri(permissionId);

        return Result.ok();
    }


    @Override
    public Result searchPermissionByUser(SearchPermissionParams params) {
        Long resourceId = params.getResourceId();
        String empName = params.getEmpName();
        Long userId = params.getUserId();
        if((StringUtils.isBlank(empName)&&userId == null)||StringUtils.isBlank(resourceId.toString())){
            return EncryotResult.fail("参数不能为空！");
        }
        Map<Long, String> types = permissionTypeService.getTypesMapByResourceId(resourceId);
        Page<UserPermission> page = new Page<>(params.getCurrentPage(),params.getPageSize());
        IPage<UserPermission> permissions = permissionMapper.searchPermissionByUser(page,resourceId, userId, empName);
        List<UserPermission> records = permissions.getRecords();
        if(records.isEmpty())records = Collections.emptyList();
        for (UserPermission permission : records) {
            List<String> permissionList = new ArrayList<>();
            if(StringUtils.isBlank(permission.getPermissionListStr()))
            {
                permission.setPermissionList(permissionList);
                permission.setPermissionListStr(null);
                continue;
            }
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
        return EncryotResult.ok(map);

    }

    @Override
    public Result searchPermissionByRole(SearchPermissionParams params) {
        String role = params.getRole();
        Long resourceId = params.getResourceId();
        if(StringUtils.isBlank(role)||StringUtils.isBlank(resourceId.toString())){
            return EncryotResult.fail("参数不能为空！");
        }
        Map<Long, String> types = permissionTypeService.getTypesMapByResourceId(resourceId);

        Page<UserPermission> page = new Page<>(params.getCurrentPage(),params.getPageSize());
        IPage<UserPermission> permissions = permissionMapper.searchPermissionByRole(page,resourceId, role);
        List<UserPermission> records = permissions.getRecords();
        if(records.isEmpty())records = Collections.emptyList();
        for (UserPermission permission : records) {
            List<String> permissionList = new ArrayList<>();
            if(StringUtils.isBlank(permission.getPermissionListStr()))
            {
                permission.setPermissionList(permissionList);
                permission.setPermissionListStr(null);
                continue;
            }
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
        return EncryotResult.ok(map);
    }

    @Override
    public List<Permission> getByUserId(Long userId) {
        LambdaQueryWrapper<Permission> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Permission::getUserId,userId);
        List<Permission> list = list(queryWrapper);
        return list;
    }

    @Override
    public List<String> getPermissionCacheById(long userId) {
        //先查该用户权限缓存
        String permissionsJson = redisTemplate.opsForValue().get(RedisConstants.PERMISSIONS_USER_KEY + userId);
        List<String> permissions = JSONUtil.toList(permissionsJson, String.class);
        Map<Long, PermissionType> typesMap = permissionTypeService.getAllTypesMap();
        //缓存没有，查询数据库,重建缓存
        if(permissions.isEmpty()){
            //TODO 修改
            //1. 查询数据库
            List<String> permissionList =permissionMapper.getUserTypeUriList(userId);
            //2. 设置缓存
            redisTemplate.opsForValue().set(RedisConstants.PERMISSIONS_USER_KEY+userId,JSONUtil.toJsonStr(permissionList), PERMISSIONS_USER_TTL);
        }
        return permissions;
    }

    @Override
    public List<String> getAllPermissionUri(){
        List<TypeUri> typeUriMap = permissionTypeMapper.getTypeUriMap();
        List<String> permissionUris = typeUriMap.stream().map(i -> i.getUri()).collect(Collectors.toList());
        return permissionUris;
    }



}