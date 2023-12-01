package com.abc.warehouse;

import com.abc.warehouse.mapper.LogMapper;
import com.abc.warehouse.pojo.Material;
import com.abc.warehouse.service.MaterialService;
import com.abc.warehouse.utils.GenerateID;
import com.abc.warehouse.utils.RedisIdWorker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
class WareHouseApplicationTests {

    @Autowired
    private MaterialService materialService;

    @Autowired
    private LogMapper logMapper;
    @Test
    void contextLoads() {
    }

    @Resource
    private GenerateID generateID;
    private ExecutorService es = Executors.newFixedThreadPool(500);

    @Test
    void testIdWorker() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(300);
        Runnable task = () -> {
            for (int i = 0; i < 10; i++) {
                long id = generateID.getId("1", "order");
                System.out.println("id = " + id);
            }
            latch.countDown();
        };
        long begin = System.currentTimeMillis();
        for (int i = 0; i < 300; i++){
            es.submit(task);
        }
        latch.await();
        long end = System.currentTimeMillis();
        System.out.println("time = " + (end - begin));
    }

    @Test
    public void testID(){
        String str = "1";

        long number = Long.parseLong(str);
        System.out.println(number);
    }

    @Test
    public void test(){
        logMapper.add(1L,"查询了");
        Material u = materialService.getById(1);
        System.out.println(u);

    }
}
