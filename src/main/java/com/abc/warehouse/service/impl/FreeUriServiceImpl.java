package com.abc.warehouse.service.impl;

import cn.hutool.json.JSONUtil;
import com.abc.warehouse.dto.constants.RedisConstants;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.abc.warehouse.pojo.FreeUri;
import com.abc.warehouse.service.FreeUriService;
import com.abc.warehouse.mapper.FreeUriMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
* @author 吧啦
* @description 针对表【free_uri_208201302】的数据库操作Service实现
* @createDate 2023-12-21 13:32:59
*/
@Service
public class FreeUriServiceImpl extends ServiceImpl<FreeUriMapper, FreeUri>
    implements FreeUriService{

    @Autowired
    private FreeUriMapper freeUriMapper;
    @Autowired
    private StringRedisTemplate redisTemplate;


    @Override
    @Transactional
    public List<String> getFreeUriList() {
        //查询缓存
        String jsonStr = redisTemplate.opsForValue().get(RedisConstants.FREE_URI_KEY);
        List<String> freeUriList = JSONUtil.toList(jsonStr,String.class);
        if(StringUtils.isBlank(jsonStr)){
            //查询数据库
            freeUriList = freeUriMapper.getFreeUriList();
            //重建缓存
            redisTemplate.opsForValue().set(RedisConstants.FREE_URI_KEY,JSONUtil.toJsonStr(freeUriList));
        }
        return freeUriList;
    }

    @Override
    @Transactional
    public boolean deleteFromFreeUri(Long permissionId) {
        //删除缓存
        redisTemplate.delete(RedisConstants.FREE_URI_KEY);
        //更新数据库
        boolean b = freeUriMapper.deleteFromFreeUri(permissionId);
        return b;
    }

    @Override
    @Transactional
    public boolean AddUriToFreeUri(Long permissionId) {
        //删除缓存
        redisTemplate.delete(RedisConstants.FREE_URI_KEY);
        //更新数据库
        boolean b = freeUriMapper.AddUriToFreeUri(permissionId);
        return b;
    }
}




