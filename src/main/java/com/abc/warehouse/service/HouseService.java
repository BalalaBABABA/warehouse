package com.abc.warehouse.service;

import com.abc.warehouse.dto.Result;
import com.abc.warehouse.pojo.House;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author 吧啦
* @description 针对表【house_208201302】的数据库操作Service
* @createDate 2023-09-23 16:58:22
*/
public interface HouseService extends IService<House> {
    List<String> searchHouseId();
}
