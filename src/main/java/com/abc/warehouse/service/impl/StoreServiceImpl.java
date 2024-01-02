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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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
    @Autowired
    private StoreMapper storeMapper;


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

    public Result findNameBetweenDates(Date startTime, Date endTime) throws ParseException {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        Date start = null, end = null;
//
//        if(startTime != null && !startTime.isEmpty()){
//            start = sdf.parse(startTime);
//        }
//        if(endTime != null && !endTime.isEmpty()){
//            end = sdf.parse(endTime);
//
//            Calendar calendar = Calendar.getInstance();
//            calendar.setTime(end);
//            calendar.add(Calendar.DATE, 1);
//            end = calendar.getTime();
//        }
        List<String> nameList = new ArrayList<>();
        List<Map<String, Object>> resultMapList = storeMapper.findCountByNameBetweenDates(startTime,endTime);
        // 遍历列表并获取每个Map的键和值
        for (Map<String, Object> resultMap : resultMapList) {
            nameList.add(resultMap.get("materialName").toString());
        }
        return EncryotResult.ok(nameList);
    }

    public Result findCountByNameBetweenDates(Date startTime, Date endTime) throws ParseException {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        Date start = null, end = null;
//
//        if(startTime != null && !startTime.isEmpty()){
//            start = sdf.parse(startTime);
//        }
//        if(endTime != null && !endTime.isEmpty()){
//            end = sdf.parse(endTime);
//
//            Calendar calendar = Calendar.getInstance();
//            calendar.setTime(end);
//            calendar.add(Calendar.DATE, 1);
//            end = calendar.getTime();
//        }
        List<Long>countList = new ArrayList<>();
        List<Map<String, Object>> resultMapList = storeMapper.findCountByNameBetweenDates(startTime,endTime);
        // 遍历列表并获取每个Map的键和值
        for (Map<String, Object> resultMap : resultMapList) {
            Long totalstoreCount = (Long) resultMap.get("totalstoreCount");
            countList.add(totalstoreCount);
        }
        return EncryotResult.ok(countList);

    }
}




