package com.abc.warehouse.service.impl;

import com.abc.warehouse.dto.EncryotResult;
import com.abc.warehouse.dto.Result;
import com.abc.warehouse.dto.constants.PageConstants;
import com.abc.warehouse.utils.GenerateID;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.abc.warehouse.pojo.Store;
import com.abc.warehouse.service.StoreService;
import com.abc.warehouse.mapper.StoreMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
* @author 吧啦
* @description 针对表【store_208201302】的数据库操作Service实现
* @createDate 2023-12-02 16:59:21
*/
@Service
public class StoreServiceImpl extends ServiceImpl<StoreMapper, Store>
    implements StoreService{

    @Resource
    private  GenerateID generateID;

    @Override
    public Result storePage(Integer curPage) {
        LambdaQueryWrapper<Store> wrapper = new LambdaQueryWrapper<>();  //查询条件构造器
        wrapper.orderByDesc(Store::getStoreTime);
        wrapper.orderByDesc(Store::getStoreNo);
        IPage<Store> pageQuery = new Page<>(curPage, PageConstants.STORE_SEARCH_PAGE_SIZE);
        IPage<Store> page = baseMapper.selectPage(pageQuery, wrapper);
        return Result.ok(page.getRecords(), page.getPages());
    }

    @Override
    public Result del_store(Long id) {
        removeById(id);
        return Result.ok();
    }

    @Override
    public Result update_store(Store store) {
        updateById(store);
        return EncryotResult.ok();
    }
}




