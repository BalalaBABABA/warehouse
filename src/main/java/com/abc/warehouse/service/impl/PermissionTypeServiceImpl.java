package com.abc.warehouse.service.impl;

import com.abc.warehouse.dto.Result;
import com.abc.warehouse.dto.TypeUri;
import com.abc.warehouse.dto.constants.RedisConstants;
import com.abc.warehouse.mapper.PermissionMapper;
import com.abc.warehouse.mapper.ResourceMapper;
import com.abc.warehouse.pojo.Permission;
import com.abc.warehouse.pojo.Resource;
import com.abc.warehouse.service.FreeUriService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.abc.warehouse.pojo.PermissionType;
import com.abc.warehouse.service.PermissionTypeService;
import com.abc.warehouse.mapper.PermissionTypeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
* @author 吧啦
* @description 针对表【permission_type_208201302】的数据库操作Service实现
* @createDate 2023-10-24 01:02:10
*/
@Service

public class PermissionTypeServiceImpl extends ServiceImpl<PermissionTypeMapper, PermissionType>
    implements PermissionTypeService{

    @Autowired
    private ResourceMapper resourceMapper;

    @Autowired
    private PermissionMapper permissionMapper;
    
    @Autowired
    private PermissionTypeMapper permissionTypeMapper;

    @Autowired
    private FreeUriService freeUriService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    public List<PermissionType> getTypesIsDisplayingByResourceId(Long resourceId) {
        LambdaQueryWrapper<PermissionType> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(PermissionType::getResourceId,resourceId);
        queryWrapper.eq(PermissionType::getIsdisplay,1);
        return list(queryWrapper);
    }

    @Override
    public List<PermissionType> getTypesByResourceId(Long resourceId) {
        LambdaQueryWrapper<PermissionType> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(PermissionType::getResourceId,resourceId);
        return list(queryWrapper);
    }

    public PermissionType getPermissionTypeByResourceIdAndType(Long resourceId, String type) {
        LambdaQueryWrapper<PermissionType> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(PermissionType::getResourceId,resourceId)
                .eq(PermissionType::getType,type);
        return getOne(queryWrapper);
    }

    @Override
    public Map<Long, String> getTypesMapByResourceId(Long resourceId) {
        List<PermissionType> types = getTypesByResourceId(resourceId);
        Map<Long,String> typesMap = new HashMap<>();
        for (PermissionType type : types) {
            typesMap.put(type.getId(),type.getType());
        }
        return typesMap;
    }

    @Override
    public Map<Long, PermissionType> getAllTypesMap() {
        List<PermissionType> list = list();
        Map<Long,PermissionType> typesMap = new HashMap<>();
        for (PermissionType type : list) {
            typesMap.put(type.getId(),type);
        }
        return typesMap;
    }

    @Override
    public Map<String, Long> getAllTypesStringMap() {
        List<PermissionType> list = list();
        Map<String,Long> typesMap = new HashMap<>();
        for (PermissionType type : list) {
            typesMap.put(type.getType(),type.getId());
        }
        return typesMap;

    }

    @Override
    public Result getPermissionTypesMap() {
        LambdaQueryWrapper<Resource> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.select(Resource::getId);
        List<Resource> resources = resourceMapper.selectList(queryWrapper);
        Map<Long,List<String>> map = new HashMap<>();
        for (Resource resource : resources) {
            Long resourceId = resource.getId();
            LambdaQueryWrapper<PermissionType> queryWrapper1=new LambdaQueryWrapper<>();
            queryWrapper1.eq(PermissionType::getResourceId,resourceId);
            queryWrapper1.eq(PermissionType::getIsdisplay,1);
            queryWrapper1.select(PermissionType::getType);
            List<PermissionType> permissionTypes = list(queryWrapper1);
            List<String> typesStr = permissionTypes.stream().map(PermissionType::getType).collect(Collectors.toList());
            map.put(resourceId,typesStr);
        }
        return Result.ok(map);
    }

    @Override
    @Transactional
    public Result delPermissionType(Long resourceId, String type) {
        //获取删除的权限类型id
        PermissionType permissionType = getPermissionTypeByResourceIdAndType(resourceId, type);
        Long permissionId = permissionType.getId();
        //TODO 删除了权限，没有必要删除所有key，及时key的value中包含这条权限也没有影响，这样会造成雪崩
//        redisTemplate.delete(RedisConstants.PERMISSIONS_USER_KEY+"*");
        //添加与权限有关的uri到free_uri中
        freeUriService.AddUriToFreeUri(permissionId);
        //更新数据库
        LambdaUpdateWrapper<PermissionType> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(PermissionType::getResourceId,resourceId)
                .eq(PermissionType::getType,type)
                .set(PermissionType::getIsdisplay,0);
        boolean update = update(updateWrapper);
        if(!update){
            return Result.fail("删除失败");
        }

        LambdaUpdateWrapper<Permission> updateWrapper1= new LambdaUpdateWrapper<>();
        updateWrapper1.eq(Permission::getPermissionId,permissionId);
        int delete = permissionMapper.delete(updateWrapper1);
        if(delete<0){
            return Result.fail("删除失败");
        }
        return Result.ok();
    }

    @Override
    public Result getSelectMap(long resourceId) {
        List<PermissionType> list = permissionTypeMapper.getSelectMap(resourceId);
        Map<String,Long> map = new LinkedHashMap<>();
        for (PermissionType permissionType : list) {
            map.put(permissionType.getType(),permissionType.getId());
        }
        return Result.ok(map);
    }

    public Map<Long, String> getRedisCache() {
//        //检查Redis是否有key
//        Boolean b = redisTemplate.hasKey(RedisConstants.PERMISSIONS_SET_KEY);
//        //不存在key
//        if(!b)
//        {
//            //查找数据库，重建缓存
//            createRedisCache();
//
//        }
//        //查找key，返回map
//        Map<Object, Object> map = redisTemplate.opsForHash().entries(RedisConstants.PERMISSIONS_SET_KEY);
        Map<Long, String> resultMap = new HashMap<>();
//        for (Map.Entry<Object, Object> entry : map.entrySet()) {
//            Long key = Long.valueOf(entry.getKey().toString());
//            String value = entry.getValue().toString();
//            resultMap.put(key, value);
//        }
        return resultMap;
    }

    @Override
    public void createRedisCache() {
//         清空key对应的数据
        redisTemplate.delete(RedisConstants.PERMISSIONS_SET_KEY);
        List<TypeUri> typeUriMap = permissionTypeMapper.getTypeUriMap();
        for(TypeUri uri:typeUriMap){
            redisTemplate.opsForSet().add(RedisConstants.PERMISSIONS_SET_KEY,uri.getUri());
        }
//        // 清空key对应的数据
//        redisTemplate.delete(RedisConstants.PERMISSIONS_SET_KEY);
//
//        LambdaQueryWrapper<PermissionType> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.select(PermissionType::getId, PermissionType::getUri);
//        List<PermissionType> list = list(queryWrapper);
//
//        for (PermissionType type : list) {
//            //向 Redis 中添加数据
//            redisTemplate.opsForHash().put(RedisConstants.PERMISSIONS_SET_KEY, type.getId().toString(), type.getUri());
//        }
    }


}




