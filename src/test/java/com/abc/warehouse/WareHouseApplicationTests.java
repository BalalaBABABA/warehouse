package com.abc.warehouse;

import com.abc.warehouse.service.MaterialService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

@SpringBootTest
class WareHouseApplicationTests {

    @Autowired
    private MaterialService materialService;

    @Test
    void contextLoads() {
    }

    @Test
    public void test(){
        Map<String, Object> map = materialService.getMap(null);
        map.forEach(System.out::printf);
    }
}
