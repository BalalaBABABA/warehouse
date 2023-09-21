package com.abc.warehouse;

import com.abc.warehouse.mapper.LogMapper;
import com.abc.warehouse.pojo.Material;
import com.abc.warehouse.service.MaterialService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class WareHouseApplicationTests {

    @Autowired
    private MaterialService materialService;

    @Autowired
    private LogMapper logMapper;
    @Test
    void contextLoads() {
    }

    @Test
    public void test(){
        logMapper.add(1L,"查询了");
        Material u = materialService.getById(1);
        System.out.println(u);

    }
}
