package com.abc.warehouse.service;

import com.abc.warehouse.dto.Result;
import com.abc.warehouse.pojo.Store;
import com.baomidou.mybatisplus.extension.service.IService;
import java.text.ParseException;


/**
* @author 吧啦
* @description 针对表【store_208201302】的数据库操作Service
* @createDate 2023-12-02 16:59:21
*/
public interface StoreService extends IService<Store> {
    Result storePage(Integer page);

    Result del_store(Long id);

    Result update_store(Store store);

    Result findNameBetweenDates(String startTime, String endTime) throws ParseException;

    Result findCountByNameBetweenDates(String startTime, String endTime) throws ParseException;

}
