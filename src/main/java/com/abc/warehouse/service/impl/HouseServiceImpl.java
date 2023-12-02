package com.abc.warehouse.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.abc.warehouse.pojo.House;
import com.abc.warehouse.service.HouseService;
import com.abc.warehouse.mapper.HouseMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
* @author 吧啦
* @description 针对表【house_208201302】的数据库操作Service实现
* @createDate 2023-09-23 16:58:22
*/
@Service
public class HouseServiceImpl extends ServiceImpl<HouseMapper, House>
    implements HouseService{

    @Override
    public List<String> searchHouseId() {
        List<String> houseIds = list().stream()
                .map(house -> String.valueOf(house.getHouseId()))
                .collect(Collectors.toList());
        return houseIds;
    }

    @Override
    public List<String> searchHouseName() {
        List<String> houseName = list().stream()
                .map(house -> house.getHouseName())
                .collect(Collectors.toList());
        return houseName;
    }
}




