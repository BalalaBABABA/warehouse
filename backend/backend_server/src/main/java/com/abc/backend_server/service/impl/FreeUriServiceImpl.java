package com.abc.backend_server.service.impl;

import cn.hutool.json.JSONUtil;
import com.abc.backend_server.dto.RedisConstants;
import com.abc.backend_server.dto.Result;
import com.abc.backend_server.mapper.FreeUriMapper;
import com.abc.backend_server.pojo.FreeUri;
import com.abc.backend_server.pojo.PermissionTypeUri;
import com.abc.backend_server.service.FreeUriService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FreeUriServiceImpl implements FreeUriService {

    @Autowired
    private FreeUriMapper freeUriMapper;
    @Autowired
    private StringRedisTemplate redisTemplate;


    @Override
    public Result getFreeUriList() {
        List<FreeUri> freeUriList =  freeUriMapper.getFreeUriList();
        return Result.ok(freeUriList);
    }

    @Override
    @Transactional
    public Result delByPermissionId(Long permissionId) {
        //删除缓存
        redisTemplate.delete(RedisConstants.FREE_URI_KEY);
        //更新数据库
        Integer  a= freeUriMapper.delByPermissionId(permissionId);

        return Result.ok("更新了"+a+"条记录");
    }

    @Override
    @Transactional
    public Result delByUriId(Long uriId) {
        //删除缓存
        redisTemplate.delete(RedisConstants.FREE_URI_KEY);
        //更新数据库
        Integer a = freeUriMapper.delByUriId(uriId);
        return Result.ok("更新了"+a+"条记录");
    }

    @Override
    @Transactional
    public Result delByResourceId(Long resourceId) {
        //删除缓存
        redisTemplate.delete(RedisConstants.FREE_URI_KEY);
        //更新数据库
        Integer a = freeUriMapper.delByResourceId(resourceId);
        return Result.ok("更新了"+a+"条记录");
    }

    @Override
    public Result delIdList(List<FreeUri> list) {
        if(list.isEmpty()){
            return Result.fail("未选择任何选项");
        }
        List<Integer> idList = list.stream().map(uri -> uri.getId()).collect(Collectors.toList());
        Boolean aBoolean = freeUriMapper.delFreeUriList(idList);
        if(aBoolean){
            return Result.ok();
        }
        return Result.fail("删除失败");
    }

    @Override
    public Result AddFreeUri(String uri) {
        Boolean aBoolean = freeUriMapper.addFreeUri(uri);
        if(aBoolean){
            return Result.ok();
        }
        return Result.fail("添加失败");
    }

    @Override
    public Result updateFreeUri(FreeUri freeUri) {
        String uri = freeUri.getUri();
        Integer id = freeUri.getId();
        freeUriMapper.updateFreeUri(id,uri);
        return Result.ok();
    }

}
